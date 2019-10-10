/*
 * Copyright (C) 2017 Nyoman Ribeka
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.sdrc.dgacg.collect.android.tasks;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.application.Collect;
import org.sdrc.dgacg.collect.android.dao.InstancesDao;
import org.sdrc.dgacg.collect.android.listeners.DiskSyncListener;
import org.sdrc.dgacg.collect.android.preferences.PreferenceKeys;
import org.sdrc.dgacg.collect.android.provider.FormsProviderAPI.FormsColumns;
import org.sdrc.dgacg.collect.android.provider.InstanceProviderAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import timber.log.Timber;

import static org.sdrc.dgacg.collect.android.provider.InstanceProviderAPI.InstanceColumns;

/**
 * Background task for syncing form instances from the instances folder to the instances table.
 * Returns immediately if it detects an error.
 */
public class InstanceSyncTask extends AsyncTask<Void, String, String> {


    private static int counter = 0;

    private String currentStatus = "";
    private DiskSyncListener diskSyncListener;

    public String getStatusMessage() {
        return currentStatus;
    }

    public void setDiskSyncListener(DiskSyncListener diskSyncListener) {
        this.diskSyncListener = diskSyncListener;
    }

    @Override
    protected String doInBackground(Void... params) {
        int instance = ++counter;
        Timber.i("[%d] doInBackground begins!", instance);

        try {
            List<String> candidateInstances = new LinkedList<String>();
            File instancesPath = new File(Collect.INSTANCES_PATH);
            if (instancesPath.exists() && instancesPath.isDirectory()) {
                File[] instanceFolders = instancesPath.listFiles();
                if (instanceFolders.length == 0) {
                    Timber.i("[%d] Empty instance folder. Stopping scan process.", instance);
                    Timber.d(Collect.getInstance().getString(R.string.instance_scan_completed));
                    return currentStatus;
                }

                // Build the list of potential path that we need to add to the content provider
                for (File instanceDir : instanceFolders) {
                    File instanceFile = new File(instanceDir, instanceDir.getName() + ".xml");
                    if (instanceFile.exists() && instanceFile.canRead()) {
                        candidateInstances.add(instanceFile.getAbsolutePath());
                    } else {
                        Timber.i("[%d] Ignoring: %s", instance, instanceDir.getAbsolutePath());
                    }
                }
                Collections.sort(candidateInstances);

                List<String> filesToRemove = new ArrayList<>();

                // Remove all the path that's already in the content provider
                Cursor instanceCursor = null;
                try {
                    String sortOrder = InstanceColumns.INSTANCE_FILE_PATH + " ASC ";
                    instanceCursor = Collect.getInstance().getContentResolver()
                            .query(InstanceColumns.CONTENT_URI, null, null, null, sortOrder);
                    if (instanceCursor == null) {
                        Timber.e("[%d] Instance content provider returned null", instance);
                        return currentStatus;
                    }

                    instanceCursor.moveToPosition(-1);

                    while (instanceCursor.moveToNext()) {
                        String instanceFilename = instanceCursor.getString(
                                instanceCursor.getColumnIndex(InstanceColumns.INSTANCE_FILE_PATH));
                        String instanceStatus = instanceCursor.getString(
                                instanceCursor.getColumnIndex(InstanceColumns.STATUS));
                        if (candidateInstances.contains(instanceFilename) || instanceStatus.equals(InstanceProviderAPI.STATUS_SUBMITTED)) {
                            candidateInstances.remove(instanceFilename);
                        } else {
                            filesToRemove.add(instanceFilename);
                        }
                    }

                } finally {
                    if (instanceCursor != null) {
                        instanceCursor.close();
                    }
                }

                InstancesDao instancesDao = new InstancesDao();
                instancesDao.deleteInstancesFromIDs(filesToRemove);

                final boolean instanceSyncFlag = PreferenceManager.getDefaultSharedPreferences(
                        Collect.getInstance().getApplicationContext()).getBoolean(
                        PreferenceKeys.KEY_INSTANCE_SYNC, true);

                int counter = 0;
                // Begin parsing and add them to the content provider
                for (String candidateInstance : candidateInstances) {
                    String instanceFormId = getFormIdFromInstance(candidateInstance);
                    // only process if we can find the id from the instance file
                    if (instanceFormId != null) {
                        Cursor formCursor = null;
                        try {
                            String selection = FormsColumns.JR_FORM_ID + " = ? ";
                            String[] selectionArgs = new String[]{instanceFormId};
                            // retrieve the form definition
                            formCursor = Collect.getInstance().getContentResolver()
                                    .query(FormsColumns.CONTENT_URI, null, selection, selectionArgs, null);
                            // TODO: optimize this by caching the previously found form definition
                            // TODO: optimize this by caching unavailable form definition to skip
                            if (formCursor != null && formCursor.moveToFirst()) {
                                String submissionUri = null;
                                if (!formCursor.isNull(formCursor.getColumnIndex(FormsColumns.SUBMISSION_URI))) {
                                    submissionUri = formCursor.getString(formCursor.getColumnIndex(FormsColumns.SUBMISSION_URI));
                                }
                                String jrFormId = formCursor.getString(formCursor.getColumnIndex(FormsColumns.JR_FORM_ID));
                                String jrVersion = formCursor.getString(formCursor.getColumnIndex(FormsColumns.JR_VERSION));
                                String formName = formCursor.getString(formCursor.getColumnIndex(FormsColumns.DISPLAY_NAME));

                                // add missing fields into content values
                                ContentValues values = new ContentValues();
                                values.put(InstanceColumns.INSTANCE_FILE_PATH, candidateInstance);
                                values.put(InstanceColumns.SUBMISSION_URI, submissionUri);
                                values.put(InstanceColumns.DISPLAY_NAME, formName);
                                values.put(InstanceColumns.JR_FORM_ID, jrFormId);
                                values.put(InstanceColumns.JR_VERSION, jrVersion);
                                values.put(InstanceColumns.STATUS, instanceSyncFlag
                                        ? InstanceProviderAPI.STATUS_COMPLETE : InstanceProviderAPI.STATUS_INCOMPLETE);
                                values.put(InstanceColumns.CAN_EDIT_WHEN_COMPLETE, Boolean.toString(true));
                                // save the new instance object
                                Collect.getInstance().getContentResolver()
                                        .insert(InstanceColumns.CONTENT_URI, values);
                                counter++;
                            }
                        } finally {
                            if (formCursor != null) {
                                formCursor.close();
                            }
                        }
                    }
                }
                if (counter > 0) {
                    currentStatus += String.format(
                            Collect.getInstance().getString(R.string.instance_scan_count),
                            counter);
                }
            }
        } finally {
            Timber.i("[%d] doInBackground ends!", instance);
        }
        return currentStatus;
    }

    private String getFormIdFromInstance(final String instancePath) {
        String instanceFormId = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(instancePath));
            Element element = document.getDocumentElement();
            instanceFormId = element.getAttribute("id");
        } catch (Exception e) {
            Timber.w("Unable to read form id from %s", instancePath);
        }
        return instanceFormId;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (diskSyncListener != null) {
            diskSyncListener.syncComplete(result);
        }
    }
}
