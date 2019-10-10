/*
 * Copyright (C) 2009 University of Washington
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

package org.sdrc.dgacg.collect.android.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.adapters.FormDownloadListAdapter;
import org.sdrc.dgacg.collect.android.application.Collect;
import org.sdrc.dgacg.collect.android.dao.FormsDao;
import org.sdrc.dgacg.collect.android.listeners.DownloadFormsTaskListener;
import org.sdrc.dgacg.collect.android.listeners.FormListDownloaderListener;
import org.sdrc.dgacg.collect.android.logic.FormDetails;
import org.sdrc.dgacg.collect.android.preferences.PreferencesActivity;
import org.sdrc.dgacg.collect.android.tasks.DownloadFormListTask;
import org.sdrc.dgacg.collect.android.tasks.DownloadFormsTask;
import org.sdrc.dgacg.collect.android.utilities.AuthDialogUtility;
import org.sdrc.dgacg.collect.android.utilities.ToastUtils;
import org.sdrc.dgacg.collect.android.utilities.WebUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import timber.log.Timber;

/**
 * Responsible for displaying, adding and deleting all the valid forms in the forms directory. One
 * caveat. If the server requires authentication, a dialog will pop up asking when you request the
 * form list. If somehow you manage to wait long enough and then try to download selected forms and
 * your authorization has timed out, it won't again ask for authentication, it will just throw a
 * 401
 * and you'll have to hit 'refresh' where it will ask for credentials again. Technically a server
 * could point at other servers requiring authentication to download the forms, but the current
 * implementation in Collect doesn't allow for that. Mostly this is just because it's a pain in the
 * butt to keep track of which forms we've downloaded and where we're needing to authenticate. I
 * think we do something similar in the instanceuploader task/activity, so should change the
 * implementation eventually.
 *
 * @author Carl Hartung (carlhartung@gmail.com)
 */
public class FormDownloadList extends FormListActivity implements FormListDownloaderListener,
        DownloadFormsTaskListener, AuthDialogUtility.AuthDialogUtilityResultListener, AdapterView.OnItemClickListener {
    private static final String FORM_DOWNLOAD_LIST_SORTING_ORDER = "formDownloadListSortingOrder";

    private static final int PROGRESS_DIALOG = 1;
    private static final int AUTH_DIALOG = 2;
    private static final int CANCELLATION_DIALOG = 3;

    public static final String DISPLAY_ONLY_UPDATED_FORMS = "displayOnlyUpdatedForms";
    private static final String BUNDLE_SELECTED_COUNT = "selectedcount";
    private static final String BUNDLE_FORM_MAP = "formmap";
    private static final String DIALOG_TITLE = "dialogtitle";
    private static final String DIALOG_MSG = "dialogmsg";
    private static final String DIALOG_SHOWING = "dialogshowing";
    private static final String FORMLIST = "formlist";
    private static final String SELECTED_FORMS = "selectedForms";

    public static final String FORMNAME = "formname";
    private static final String FORMDETAIL_KEY = "formdetailkey";
    public static final String FORMID_DISPLAY = "formiddisplay";

    public static final String FORM_ID_KEY = "formid";
    private static final String FORM_VERSION_KEY = "formversion";

    private String alertMsg;
    private boolean alertShowing = false;
    private String alertTitle;

    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    private ProgressDialog cancelDialog;
    private Button downloadButton;

    private DownloadFormListTask downloadFormListTask;
    private DownloadFormsTask downloadFormsTask;
    private Button toggleButton;

    private HashMap<String, FormDetails> formNamesAndURLs = new HashMap<String, FormDetails>();
    private ArrayList<HashMap<String, String>> filteredFormList = new ArrayList<>();
    private LinkedHashSet<String> selectedForms = new LinkedHashSet<>();

    private static final boolean EXIT = true;
    private static final boolean DO_NOT_EXIT = false;
    private boolean shouldExit;
    private static final String SHOULD_EXIT = "shouldexit";
    private boolean displayOnlyUpdatedForms;
    private static final String t = "RemoveFileManageList";
    private static final int MENU_PREFERENCES = Menu.FIRST;
    public static final String LIST_URL = "listurl";


    private HashMap<String, FormDetails> mFormNamesAndURLs = new HashMap<String,FormDetails>();
    private SimpleAdapter mFormListAdapter;
    private ArrayList<HashMap<String, String>> mFormList;
    private boolean mToggled = false;
    private int mSelectedCount = 0;
    //Ratikanta
    SQLiteDatabase db = null;
    private int numberOfTimeFormDownloaded = 0;
    private int numberOfURLPresent = 0;
    public String url;
    private String username;
    private String password;
    List<String> serverURLs;
    private HashMap<String, FormDetails> totalResult = new HashMap<String, FormDetails>();
    private ArrayList<String> xFormIdsTemp = new ArrayList<String>();
    private LinkedHashMap<String, FormDetails> forms;

    //Amit
    String dialogMessage="";
    String dialogTitle;
    int noOfTimeCredentialAdded = 0;
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_file_manage_list);
        setTitle(getString(R.string.get_forms));

        if (getIntent().getExtras() != null) {
            displayOnlyUpdatedForms = (boolean) getIntent().getExtras().get(DISPLAY_ONLY_UPDATED_FORMS);
        }
        //Ratikanta
        db = openOrCreateDatabase("SDRCCollectDB",MODE_PRIVATE,null);

        Cursor resultSet1 = db.rawQuery("Select * from "+ getString(R.string.xform_table_name),null);
        int rows1 =
                resultSet1.getCount();
        forms = new LinkedHashMap<String, FormDetails>();
        if(rows1 > 0){
            while(resultSet1.moveToNext()){
                String formId = resultSet1.getString(0);
                String formURL = resultSet1.getString(1);
                String formUsername = resultSet1.getString(2);
                String formPassword = resultSet1.getString(3);
                FormDetails fd = new FormDetails(null, formURL, null, formId, null, formUsername, formPassword);

                forms.put(formId, fd);
            }
        }
        //Ratikanta end

        alertMsg = getString(R.string.please_wait);
        setServerURLs();
        downloadButton = findViewById(R.id.add_button);
        downloadButton.setEnabled(listView.getCheckedItemCount() > 0);
        downloadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // this is called in downloadSelectedFiles():
                //    Collect.getInstance().getActivityLogger().logAction(this,
                // "downloadSelectedFiles", ...);
                downloadSelectedFiles();
            }
        });

        toggleButton = findViewById(R.id.toggle_button);
       // toggleButton.setEnabled(false);
        toggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadButton.setEnabled(toggleChecked(listView));
                toggleButtonLabel(toggleButton, listView);
                selectedForms.clear();
                if (listView.getCheckedItemCount() == listView.getCount()) {
                    for (HashMap<String, String> map : mFormList) {
                        selectedForms.add(map.get(FORMDETAIL_KEY));
                    }
                }
            }
        });

//        Button refreshButton = findViewById(R.id.refresh_button);
//        refreshButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Collect.getInstance().getActivityLogger().logAction(this, "refreshForms", "");
//
//                mFormList.clear();
//                updateAdapter();
//                clearChoices();
//                downloadFormList();
//            }
//        });
//        refreshButton.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            // If the screen has rotated, the hashmap with the form ids and urls is passed here.
            if (savedInstanceState.containsKey(BUNDLE_FORM_MAP)) {
                formNamesAndURLs =
                        (HashMap<String, FormDetails>) savedInstanceState
                                .getSerializable(BUNDLE_FORM_MAP);
            }

            // how many items we've selected
            // Android should keep track of this, but broken on rotate...
            if (savedInstanceState.containsKey(BUNDLE_SELECTED_COUNT)) {
                downloadButton.setEnabled(savedInstanceState.getInt(BUNDLE_SELECTED_COUNT) > 0);
            }

            // to restore alert dialog.
            if (savedInstanceState.containsKey(DIALOG_TITLE)) {
                alertTitle = savedInstanceState.getString(DIALOG_TITLE);
            }
            if (savedInstanceState.containsKey(DIALOG_MSG)) {
                alertMsg = savedInstanceState.getString(DIALOG_MSG);
            }
            if (savedInstanceState.containsKey(DIALOG_SHOWING)) {
                alertShowing = savedInstanceState.getBoolean(DIALOG_SHOWING);
            }
            if (savedInstanceState.containsKey(SHOULD_EXIT)) {
                shouldExit = savedInstanceState.getBoolean(SHOULD_EXIT);
            }
            if (savedInstanceState.containsKey(SELECTED_FORMS)) {
                selectedForms = (LinkedHashSet<String>) savedInstanceState.getSerializable(SELECTED_FORMS);
            }
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(FORMLIST)) {
            mFormList =
                    (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable(
                            FORMLIST);
        } else {
            mFormList = new ArrayList<HashMap<String, String>>();
        }

        filteredFormList.addAll(mFormList);

        if (getLastCustomNonConfigurationInstance() instanceof DownloadFormListTask) {
            downloadFormListTask = (DownloadFormListTask) getLastCustomNonConfigurationInstance();
            if (downloadFormListTask.getStatus() == AsyncTask.Status.FINISHED) {
                try {
                    dismissDialog(PROGRESS_DIALOG);
                } catch (IllegalArgumentException e) {
                    Timber.i("Attempting to close a dialog that was not previously opened");
                }
                downloadFormsTask = null;
            }
        } else if (getLastCustomNonConfigurationInstance() instanceof DownloadFormsTask) {
            downloadFormsTask = (DownloadFormsTask) getLastCustomNonConfigurationInstance();
            if (downloadFormsTask.getStatus() == AsyncTask.Status.FINISHED) {
                try {
                    dismissDialog(PROGRESS_DIALOG);
                } catch (IllegalArgumentException e) {
                    Timber.i("Attempting to close a dialog that was not previously opened");
                }
                downloadFormsTask = null;
            }
        } else if (formNamesAndURLs.isEmpty() && getLastCustomNonConfigurationInstance() == null) {
            // first time, so get the formlist
            if(numberOfURLPresent > 0)
                setODKCredentialInSP(0);

            // first time, so get the formlist
            //Ratikanta
            //I kept the progress dialog here cause the dialog will start when the activity
            //starts and will end when all the form list get downloaded
            showDialog(PROGRESS_DIALOG);
            downloadFormList();
        }

       /* listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemsCanFocus(false);

        sortingOptions = new String[]{
                getString(R.string.sort_by_name_asc), getString(R.string.sort_by_name_desc)
        };*/
        String[] data = new String[] {
                FORMNAME, FORMID_DISPLAY, FORMDETAIL_KEY
        };
        int[] view = new int[] {
                R.id.text1, R.id.text2
        };
        if(mFormList!=null){
            mFormListAdapter =
                    new SimpleAdapter(this, mFormList, R.layout.two_item_multiple_choice, data, view);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setItemsCanFocus(false);
            listView.setAdapter(mFormListAdapter);
        }


    }
    /**
     * This will set the sharedPreference with first DGA credentials
     * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
     */
    private void setODKCredentialInSP(int index) {
        // TODO Auto-generated method stub

        noOfTimeCredentialAdded=0;
        url = serverURLs.get(index).split(PreferencesActivity.KEY_SEPARATOR)[0];
        username = serverURLs.get(index).split(PreferencesActivity.KEY_SEPARATOR)[1];
        password = serverURLs.get(index).split(PreferencesActivity.KEY_SEPARATOR)[2];
        if(url != null)
            url = url.trim();
        if(username != null)
            username = username.trim();
        if(password != null)
            password = password.trim();


        SharedPreferences subsettings = PreferenceManager.getDefaultSharedPreferences(Collect.getInstance().getBaseContext());
        SharedPreferences.Editor editor = subsettings.edit();
        editor.putString(PreferencesActivity.KEY_SERVER_URL, url);
        editor.putString(PreferencesActivity.KEY_USERNAME, username);
        editor.putString(PreferencesActivity.KEY_PASSWORD, password);
        editor.commit();

    }


    public void setServerURLs(){
        // TODO Auto-generated method stub
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(Collect.getInstance().getBaseContext());

        String urlString = settings.getString(PreferencesActivity.ODK_SERVER_URLS, "");

        String arr [] = urlString.split(PreferencesActivity.URL_SEPARATOR);

        serverURLs = new ArrayList<String>();

        for(String oneURLAuth : arr)
            serverURLs.add(oneURLAuth);

        numberOfURLPresent = serverURLs.size();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Collect.getInstance().getActivityLogger().logOnStart(this);
    }

    @Override
    protected void onStop() {
        Collect.getInstance().getActivityLogger().logOnStop(this);
        super.onStop();
    }

    private void clearChoices() {
        listView.clearChoices();
        downloadButton.setEnabled(false);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toggleButtonLabel(toggleButton, listView);
        downloadButton.setEnabled(listView.getCheckedItemCount() > 0);

        Object o = listView.getAdapter().getItem(position);
        @SuppressWarnings("unchecked")
        HashMap<String, String> item = (HashMap<String, String>) o;
        FormDetails detail = formNamesAndURLs.get(item.get(FORMDETAIL_KEY));

        if (detail != null) {
            Collect.getInstance().getActivityLogger().logAction(this, "onListItemClick",
                    detail.getDownloadUrl());
        } else {
            Collect.getInstance().getActivityLogger().logAction(this, "onListItemClick",
                    "<missing form detail>");
        }

        if (listView.isItemChecked(position)) {
            selectedForms.add(((HashMap<String, String>) listView.getAdapter().getItem(position)).get(FORMDETAIL_KEY));
        } else {
            selectedForms.remove(((HashMap<String, String>) listView.getAdapter().getItem(position)).get(FORMDETAIL_KEY));
        }
    }

    /**
     * Starts the download task and shows the progress dialog.
     */
    private void downloadFormList() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

        if (ni == null || !ni.isConnected()) {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        } else {

            mFormNamesAndURLs = new HashMap<String, FormDetails>();
            if (progressDialog != null) {
                // This is needed because onPrepareDialog() is broken in 1.6.
                progressDialog.setMessage("Server: " + url + "\n" + (numberOfTimeFormDownloaded + 1) + " url searched out of  " + numberOfURLPresent);
            }
//	            showDialog(PROGRESS_DIALOG);

            if (downloadFormListTask != null &&
                    downloadFormListTask.getStatus() != AsyncTask.Status.FINISHED) {
                return; // we are already doing the download!!!
            } else if (downloadFormListTask != null) {
                downloadFormListTask.setDownloaderListener(null);
                downloadFormListTask.cancel(true);
                downloadFormListTask = null;
            }

            downloadFormListTask = new DownloadFormListTask();
            downloadFormListTask.setDownloaderListener(this);
            downloadFormListTask.execute();
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        toggleButtonLabel(toggleButton, listView);
        updateAdapter();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_SELECTED_COUNT, listView.getCheckedItemCount());
        outState.putSerializable(BUNDLE_FORM_MAP, formNamesAndURLs);
        outState.putString(DIALOG_TITLE, alertTitle);
        outState.putString(DIALOG_MSG, alertMsg);
        outState.putBoolean(DIALOG_SHOWING, alertShowing);
        outState.putBoolean(SHOULD_EXIT, shouldExit);
        outState.putSerializable(FORMLIST, mFormList);
        outState.putSerializable(SELECTED_FORMS, selectedForms);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case PROGRESS_DIALOG:
                Collect.getInstance().getActivityLogger().logAction(this,
                        "onCreateDialog.PROGRESS_DIALOG", "show");
                progressDialog = new ProgressDialog(this);
                DialogInterface.OnClickListener loadingButtonListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Collect.getInstance().getActivityLogger().logAction(this,
                                        "onCreateDialog.PROGRESS_DIALOG", "OK");
                                // we use the same progress dialog for both
                                // so whatever isn't null is running
                                dialog.dismiss();
                                if (downloadFormListTask != null) {
                                    downloadFormListTask.setDownloaderListener(null);
                                    downloadFormListTask.cancel(true);
                                    downloadFormListTask = null;
                                }
                                if (downloadFormsTask != null) {
                                    showDialog(CANCELLATION_DIALOG);
                                    downloadFormsTask.cancel(true);
                                }
                            }
                        };
                progressDialog.setTitle(getString(R.string.downloading_data));
                progressDialog.setMessage(alertMsg);
                progressDialog.setIcon(android.R.drawable.ic_dialog_info);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setButton(getString(R.string.cancel), loadingButtonListener);
                return progressDialog;
            case AUTH_DIALOG:
                Collect.getInstance().getActivityLogger().logAction(this,
                        "onCreateDialog.AUTH_DIALOG", "show");

                alertShowing = false;

                return new AuthDialogUtility().createDialog(this, this, null);
            case CANCELLATION_DIALOG:
                cancelDialog = new ProgressDialog(this);
                cancelDialog.setTitle(getString(R.string.canceling));
                cancelDialog.setMessage(getString(R.string.please_wait));
                cancelDialog.setIcon(android.R.drawable.ic_dialog_info);
                cancelDialog.setIndeterminate(true);
                cancelDialog.setCancelable(false);
                return cancelDialog;
        }
        return null;
    }

    @Override
    protected String getSortingOrderKey() {
        return FORM_DOWNLOAD_LIST_SORTING_ORDER;
    }

    @Override
    protected void updateAdapter() {
        CharSequence charSequence = getFilterText();
        filteredFormList.clear();
        if (charSequence.length() > 0) {
            for (HashMap<String, String> form : mFormList) {
                if (form.get(FORMNAME).toLowerCase(Locale.US).contains(charSequence.toString().toLowerCase(Locale.US))) {
                    filteredFormList.add(form);
                }
            }
        } else {
            filteredFormList.addAll(mFormList);
        }
        sortList();
        if (listView.getAdapter() == null) {
            listView.setAdapter(new FormDownloadListAdapter(this, filteredFormList, formNamesAndURLs));
        } else {
            ((FormDownloadListAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
        toggleButton.setEnabled(filteredFormList.size() > 0);
        checkPreviouslyCheckedItems();
        toggleButtonLabel(toggleButton, listView);
    }

    @Override
    protected void checkPreviouslyCheckedItems() {
        listView.clearChoices();
        for (int i = 0; i < listView.getCount(); i++) {
            HashMap<String, String> item =
                    (HashMap<String, String>) listView.getAdapter().getItem(i);
            if (selectedForms.contains(item.get(FORMDETAIL_KEY))) {
                listView.setItemChecked(i, true);
            }
        }
    }

    private void sortList() {
        Collections.sort(filteredFormList, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
                if (getSortingOrder().equals(SORT_BY_NAME_ASC)) {
                    return lhs.get(FORMNAME).compareToIgnoreCase(rhs.get(FORMNAME));
                } else {
                    return rhs.get(FORMNAME).compareToIgnoreCase(lhs.get(FORMNAME));
                }
            }
        });
    }

    /**
     * starts the task to download the selected forms, also shows progress dialog
     */
    @SuppressWarnings("unchecked")
    private void downloadSelectedFiles() {
        int totalCount = 0;
        ArrayList<FormDetails> filesToDownload = new ArrayList<FormDetails>();

        SparseBooleanArray sba = listView.getCheckedItemPositions();
        for (int i = 0; i < listView.getCount(); i++) {
            if (sba.get(i, false)) {
                HashMap<String, String> item =
                        (HashMap<String, String>) listView.getAdapter().getItem(i);
                //Ratikanta
                FormDetails fd = mFormNamesAndURLs.get(item.get(FORMDETAIL_KEY));
                fd.downloadUrl = forms.get(fd.formID).downloadUrl;
                if(!(fd.downloadUrl.charAt(fd.downloadUrl.length()-1) == '/'))
                    fd.downloadUrl += "/";
                fd.downloadUrl += "formXml?formId=" + fd.formID;
                fd.username = forms.get(fd.formID).username;
                fd.password = forms.get(fd.formID).password;

                //Ratikanta end
                filesToDownload.add(fd);
            }
        }
        totalCount = filesToDownload.size();

        Collect.getInstance().getActivityLogger().logAction(this, "downloadSelectedFiles",
                Integer.toString(totalCount));

        if (totalCount > 0) {
            // show dialog box
            showDialog(PROGRESS_DIALOG);

            downloadFormsTask = new DownloadFormsTask(getApplicationContext());
            downloadFormsTask.setDownloaderListener(this);
            downloadFormsTask.execute(filesToDownload);
        } else {
            ToastUtils.showShortToast(R.string.noselect_error);
        }
    }


    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if (downloadFormsTask != null) {
            return downloadFormsTask;
        } else {
            return downloadFormListTask;
        }
    }


    @Override
    protected void onDestroy() {
        if (downloadFormListTask != null) {
            downloadFormListTask.setDownloaderListener(null);
        }
        if (downloadFormsTask != null) {
            downloadFormsTask.setDownloaderListener(null);
        }
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        if (downloadFormListTask != null) {
            downloadFormListTask.setDownloaderListener(this);
        }
        if (downloadFormsTask != null) {
            downloadFormsTask.setDownloaderListener(this);
        }
        if (alertShowing) {
            createAlertDialog(alertTitle, alertMsg, shouldExit);
        }
        super.onResume();
    }


    @Override
    protected void onPause() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        super.onPause();
    }

    public boolean isLocalFormSuperseded(String formId) {
        if (formId == null) {
            Timber.e("isLocalFormSuperseded: server is not OpenRosa-compliant. <formID> is null!");
            return true;
        }

        Cursor formCursor = null;
        try {
            formCursor = new FormsDao().getFormsCursorForFormId(formId);
            return formCursor.getCount() == 0 // form does not already exist locally
                    || formNamesAndURLs.get(formId).isNewerFormVersionAvailable() // or a newer version of this form is available
                    || formNamesAndURLs.get(formId).areNewerMediaFilesAvailable(); // or newer versions of media files are available
        } finally {
            if (formCursor != null) {
                formCursor.close();
            }
        }
    }

    /**
     * Causes any local forms that have been updated on the server to become checked in the list.
     * This is a prompt and a
     * convenience to users to download the latest version of those forms from the server.
     */
    private void selectSupersededForms() {

        ListView ls = listView;
        for (int idx = 0; idx < filteredFormList.size(); idx++) {
            HashMap<String, String> item = filteredFormList.get(idx);
            if (isLocalFormSuperseded(item.get(FORM_ID_KEY))) {
                ls.setItemChecked(idx, true);
                selectedForms.add(item.get(FORMDETAIL_KEY));
            }
        }
    }

    /**
     * Called when the form list has finished downloading. results will either contain a set of
     * <formname, formdetails> tuples, or one tuple of DL.ERROR.MSG and the associated message.
     *
     * This method will keep the results if there will be multiple DGA Servers, finally after
     * downloading all form lists it will show it on the screen.(Ratikanta)
     *
     * @param result
     * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
     */
    public void formListDownloadingComplete(HashMap<String, FormDetails> result) {

        downloadFormListTask.setDownloaderListener(null);
        downloadFormListTask = null;

        if (result == null) {
            Log.e(t, "Formlist Downloading returned null.  That shouldn't happen");
            // Just displayes "error occured" to the user, but this should never happen.
            createAlertDialog(getString(R.string.load_remote_form_error),
                    getString(R.string.error_occured), EXIT);
            return;
        }

        if (result.containsKey(DownloadFormListTask.DL_AUTH_REQUIRED)) {

            // need authorization

            if(noOfTimeCredentialAdded==1){
                numberOfTimeFormDownloaded++;
                dialogMessage += getString(R.string.authentication_faild) + " (" + url+")\n";
                dialogTitle = getString(R.string.load_remote_form_error);

                if (numberOfTimeFormDownloaded < numberOfURLPresent){
                    setODKCredentialInSP(numberOfTimeFormDownloaded);
                    //calling the downloadFormList again to download forms from another server
                    downloadFormList();
                }else{
                    numberOfTimeFormDownloaded--;
                    showFormInUI(result);
                    dismissDialog(PROGRESS_DIALOG);
//            		createAlertDialog(dialogTitle, dialogMessage, DO_NOT_EXIT);
                }

            }else if(noOfTimeCredentialAdded==0){
                Uri u = Uri.parse(url);
                noOfTimeCredentialAdded++;
                WebUtils.addCredentials(username, password, u.getHost());
                downloadFormList();
            }else{
                Log.e(t, "Bad no. of time credential added");
            }


        }
        else if (result.containsKey(DownloadFormListTask.DL_ERROR_MSG)) {
            // Download failed
            //dialogMessage =
            //    getString(R.string.list_failed_with_error,
            //        result.get(DownloadFormListTask.DL_ERROR_MSG).errorStr);

            //Ratikanta
            int errorStatus = 0;

            try{
                errorStatus = Integer.parseInt(dialogMessage);
            }catch(Exception e){
                Log.e(t, "Bad request status while downloading formlist.");
            }

            if(errorStatus == 404){
                dialogMessage += getString(R.string.server_not_found)  + " ( " + url+")\n";
            }else if(errorStatus == 408){
                dialogMessage += getString(R.string.request_timeout)+"\n";
            }else if(errorStatus == 0){
                dialogMessage += getString(R.string.server_down) + " ( " + url+")\n";
            }else{
                dialogMessage += "Error status " + errorStatus + " while downloding the form\n";
            }

            dialogTitle = getString(R.string.load_remote_form_error);
//        	createAlertDialog(dialogTitle, dialogMessage, DO_NOT_EXIT);

            //check if it is final or not
            numberOfTimeFormDownloaded++;

            if (numberOfTimeFormDownloaded < numberOfURLPresent){
                setODKCredentialInSP(numberOfTimeFormDownloaded);
                //calling the downloadFormList again to download forms from another server
                downloadFormList();
            }else{
                numberOfTimeFormDownloaded--;
                showFormInUI(result);
                dismissDialog(PROGRESS_DIALOG);
            }
        }
        else {
            showFormInUI(result);
        }
    }


    /**
     * Creates an alert dialog with the given tite and message. If shouldExit is set to true, the
     * activity will exit when the user clicks "ok".
     */
    private void createAlertDialog(String title, String message, final boolean shouldExit) {
        Collect.getInstance().getActivityLogger().logAction(this, "createAlertDialog", "show");
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        DialogInterface.OnClickListener quitListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE: // ok
                        Collect.getInstance().getActivityLogger().logAction(this,
                                "createAlertDialog", "OK");
                        // just close the dialog
                        alertShowing = false;
                        // successful download, so quit
                        if (shouldExit) {
                            //Ratikanta
                            finish();
                            Intent intent = new Intent(getApplicationContext(),
                                    MainMenuActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
            }
        };
        alertDialog.setCancelable(false);
        alertDialog.setButton(getString(R.string.ok), quitListener);
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertMsg = message;
        alertTitle = title;
        alertShowing = true;
        this.shouldExit = shouldExit;
        if(!(dialogMessage.equals("")))
            alertDialog.show();
    }


    @Override
    public void progressUpdate(String currentFile, int progress, int total) {
        alertMsg = getString(R.string.fetching_file, currentFile, String.valueOf(progress), String.valueOf(total));
        progressDialog.setMessage(alertMsg);
    }


    @Override
    public void formsDownloadingComplete(HashMap<FormDetails, String> result) {
        if (downloadFormsTask != null) {
            downloadFormsTask.setDownloaderListener(null);
        }

        if (progressDialog.isShowing()) {
            // should always be true here
            progressDialog.dismiss();
        }

        Set<FormDetails> keys = result.keySet();
        StringBuilder b = new StringBuilder();
        for (FormDetails k : keys) {
            b.append(k.formName +
                    " (" +
                    ((k.formVersion != null) ?
                            (this.getString(R.string.version) + ": " + k.formVersion + " ")
                            : "") +
                    "ID: " + k.formID + ") - " +
                    result.get(k));
            b.append("\n\n");
        }

        //Ratikanta
        dialogMessage = b.toString().trim();

        createAlertDialog(getString(R.string.download_forms_result), b.toString().trim(), EXIT);
    }

    public static String getDownloadResultMessage(HashMap<FormDetails, String> result) {
        Set<FormDetails> keys = result.keySet();
        StringBuilder b = new StringBuilder();
        for (FormDetails k : keys) {
            b.append(k.getFormName() + " ("
                    + ((k.getFormVersion() != null)
                    ? (Collect.getInstance().getString(R.string.version) + ": " + k.getFormVersion() + " ")
                    : "") + "ID: " + k.getFormID() + ") - " + result.get(k));
            b.append("\n\n");
        }

        return b.toString().trim();
    }

    @Override
    public void formsDownloadingCancelled() {
        if (downloadFormsTask != null) {
            downloadFormsTask.setDownloaderListener(null);
            downloadFormsTask = null;
        }
        if (cancelDialog.isShowing()) {
            cancelDialog.dismiss();
        }
    }

    @Override
    public void updatedCredentials() {
        downloadFormList();
    }

    @Override
    public void cancelledUpdatingCredentials() {
        finish();
    }
    private void showFormInUI(HashMap<String, FormDetails> result) {
        // TODO Auto-generated method stub
        // Everything worked. Clear the list and add the results.


        //checking for the last formList download from last URL
        if (numberOfTimeFormDownloaded == (numberOfURLPresent - 1)){

            //before showing form list show the errors during fetching the form list
            createAlertDialog(dialogTitle, dialogMessage, DO_NOT_EXIT);
            if (!(result.containsKey(DownloadFormListTask.DL_ERROR_MSG))) {

                for (String key : result.keySet()) {
                    FormDetails fd = result.get(key);

                    totalResult.put(key, fd);
                }
            }
            result.clear();



            for (String key : totalResult.keySet()) {
                FormDetails fd = totalResult.get(key);

                result.put(key, fd);
            }


            totalResult = new HashMap<String, FormDetails>();



            //remove forms which are not belong to this user.
            ArrayList<String> xFormIds = new ArrayList<String>();
            String programIds = "";
            Cursor cursor = db.rawQuery("Select id from "+ getString(R.string.program_table_name) + " where selected = 1",null);
            if(cursor.moveToFirst()){
                do{
                    programIds += String.valueOf(cursor.getInt(0)) + ", ";
                }while(cursor.moveToNext());
            }

            if(programIds.length()>0)
                programIds = programIds.substring(0, programIds.length() - 2);

            cursor = db.rawQuery("Select xFormId from "+ getString(R.string.program_xform_mapping_table_name) + " where programId IN (" + programIds + ")",null);

            if(cursor.moveToFirst()){
                do{
                    xFormIds.add(cursor.getString(0));
                }while(cursor.moveToNext());
            }
            //we need the bellow comments later so keeping it, later we will remove it
            //compare and remove from result
            HashMap<Integer, String> indexes = new HashMap<Integer, String>();
            int index = 0;
            for(String xFormId: result.keySet()){
//            	System.out.println(xFormId);
                boolean flag = true;
                for(int i = 0;i < xFormIds.size();i++){
                    if(xFormIds.get(i).trim().equals(xFormId)){
                        flag = false;
                        xFormIds.remove(i);
                        i--;
                        break;

                    }
                }
                if(flag)
                    indexes.put(index, xFormId);
                index++;
            }

            //removing the unassigned form
            for(int j :indexes.keySet())
                result.remove(indexes.get(j));
            mFormNamesAndURLs = result;

            mFormList.clear();

            ArrayList<String> ids = new ArrayList<String>(mFormNamesAndURLs.keySet());
            for (int i = 0; i < result.size(); i++) {
                String formDetailsKey = ids.get(i);
                FormDetails details = mFormNamesAndURLs.get(formDetailsKey);
                HashMap<String, String> item = new HashMap<String, String>();
                item.put(FORMNAME, details.formName);
                item.put(FORMID_DISPLAY,
                        ((details.formVersion == null) ? "" : (getString(R.string.version) + " " + details.formVersion + " ")) +



                                "ID: " + details.formID );
                item.put(FORMDETAIL_KEY, formDetailsKey);

                // Insert the new form in alphabetical order.
                if (mFormList.size() == 0) {
                    mFormList.add(item);
                } else {
                    int j;
                    for (j = 0; j < mFormList.size(); j++) {
                        HashMap<String, String> compareMe = mFormList.get(j);
                        String name = compareMe.get(FORMNAME);
                        if (name.compareTo(mFormNamesAndURLs.get(ids.get(i)).formName) > 0) {
                            break;
                        }
                    }
                    mFormList.add(j, item);
                }
            }
            mFormListAdapter.notifyDataSetChanged();

            //this is the only point where we are closing the dialog
            dismissDialog(PROGRESS_DIALOG);
        }else{
            numberOfTimeFormDownloaded++;

            for (String key : result.keySet()) {
                FormDetails fd = result.get(key);
                totalResult.put(key, fd);
            }

            setODKCredentialInSP(numberOfTimeFormDownloaded);
            //calling the downloadFormList again to download forms from another server
            downloadFormList();
        }

    }
}