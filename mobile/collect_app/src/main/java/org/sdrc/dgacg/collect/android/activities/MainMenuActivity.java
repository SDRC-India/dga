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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.application.Collect;
import org.sdrc.dgacg.collect.android.dao.FormsDao;
import org.sdrc.dgacg.collect.android.dao.InstancesDao;
import org.sdrc.dgacg.collect.android.listeners.UpdateListener;
import org.sdrc.dgacg.collect.android.preferences.AdminKeys;
import org.sdrc.dgacg.collect.android.preferences.AdminPreferencesActivity;
import org.sdrc.dgacg.collect.android.preferences.AdminSharedPreferences;
import org.sdrc.dgacg.collect.android.preferences.AutoSendPreferenceMigrator;
import org.sdrc.dgacg.collect.android.preferences.GeneralSharedPreferences;
import org.sdrc.dgacg.collect.android.preferences.PreferenceKeys;
import org.sdrc.dgacg.collect.android.preferences.PreferencesActivity;
import org.sdrc.dgacg.collect.android.provider.FormsProviderAPI;
import org.sdrc.dgacg.collect.android.provider.InstanceProviderAPI;
import org.sdrc.dgacg.collect.android.provider.InstanceProviderAPI.InstanceColumns;
import org.sdrc.dgacg.collect.android.tasks.UpdateTask;
import org.sdrc.dgacg.collect.android.utilities.ApplicationConstants;
import org.sdrc.dgacg.collect.android.utilities.AuthDialogUtility;
import org.sdrc.dgacg.collect.android.utilities.PlayServicesUtil;
import org.sdrc.dgacg.collect.android.utilities.SharedPreferencesUtils;
import org.sdrc.dgacg.collect.android.utilities.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import timber.log.Timber;

/**
 * Responsible for displaying buttons to launch the major activities. Launches
 * some activities based on returns of others.
 *
 * @author Carl Hartung (carlhartung@gmail.com)
 * @author Yaw Anokwa (yanokwa@gmail.com)
 */
public class MainMenuActivity extends CollectAbstractActivity implements UpdateListener {


    private static final boolean EXIT = true;
    // buttons
    private Button enterDataButton;
    private Button manageFilesButton;
    private Button sendDataButton;
    private Button viewSentFormsButton;
    private Button reviewDataButton;
    private Button getFormsButton;
    private View reviewSpacer;
    private View getFormsSpacer;
    private AlertDialog alertDialog;
    private SharedPreferences adminPreferences;
    private int completedCount;
    private int savedCount;
    private int viewSentCount;
    private Cursor finalizedCursor;
    private Cursor savedCursor;
    private Cursor viewSentCursor;
    private IncomingHandler handler = new IncomingHandler(this);
    private MyContentObserver contentObserver = new MyContentObserver();

    // private static boolean DO_NOT_EXIT = false;
    /**
     * Will help to access the database
     */
    SQLiteDatabase db = null;
    private String db_username;
    private String db_password;
    private UpdateTask updateTask;
    private static final int PASSWORD_DIALOG = 1;
    private static final int WARNING_DIALOG = 2;
    private final static int PROGRESS_DIALOG = 3;
    public static void startActivityAndCloseAllOthers(Activity activity) {
        activity.startActivity(new Intent(activity, MainMenuActivity.class));
        activity.overridePendingTransition(0, 0);
        activity.finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        initToolbar();

        // enter data button. expects a result.
        enterDataButton = findViewById(R.id.enter_data);
        enterDataButton.setText(getString(R.string.enter_data_button));
        enterDataButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Collect.allowClick()) {
                    Collect.getInstance().getActivityLogger()
                            .logAction(this, "fillBlankForm", "click");
                    Intent i = new Intent(getApplicationContext(),
                            FormChooserList.class);
                    startActivity(i);
                }
            }
        });

        // review data button. expects a result.
        reviewDataButton = findViewById(R.id.review_data);
        reviewDataButton.setText(getString(R.string.review_data_button));
        reviewDataButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Collect.allowClick()) {
                    Collect.getInstance().getActivityLogger()
                            .logAction(this, ApplicationConstants.FormModes.EDIT_SAVED, "click");
                    Intent i = new Intent(getApplicationContext(), InstanceChooserList.class);
                    i.putExtra(ApplicationConstants.BundleKeys.FORM_MODE,
                            ApplicationConstants.FormModes.EDIT_SAVED);
                    startActivity(i);
                }
            }
        });

        // send data button. expects a result.
        sendDataButton = findViewById(R.id.send_data);
        sendDataButton.setText(getString(R.string.send_data_button));
        sendDataButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Collect.allowClick()) {
                    Collect.getInstance().getActivityLogger()
                            .logAction(this, "uploadForms", "click");
                    Intent i = new Intent(getApplicationContext(),
                            InstanceUploaderList.class);
                    startActivity(i);
                }
            }
        });

        //View sent forms
        viewSentFormsButton = findViewById(R.id.view_sent_forms);
        viewSentFormsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Collect.allowClick()) {
                    Collect.getInstance().getActivityLogger().logAction(this,
                            ApplicationConstants.FormModes.VIEW_SENT, "click");
                    Intent i = new Intent(getApplicationContext(), InstanceChooserList.class);
                    i.putExtra(ApplicationConstants.BundleKeys.FORM_MODE,
                            ApplicationConstants.FormModes.VIEW_SENT);
                    startActivity(i);
                }
            }
        });

        // manage forms button. no result expected.
        getFormsButton = findViewById(R.id.get_forms);
        getFormsButton.setText(getString(R.string.get_forms));
        getFormsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Collect.allowClick()) {
                    Collect.getInstance().getActivityLogger()
                            .logAction(this, "downloadBlankForms", "click");
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(MainMenuActivity.this);
                    String protocol = sharedPreferences.getString(
                            PreferenceKeys.KEY_PROTOCOL, getString(R.string.protocol_odk_default));
                    Intent i = null;
                    if (protocol.equalsIgnoreCase(getString(R.string.protocol_google_sheets))) {
                        if (PlayServicesUtil.isGooglePlayServicesAvailable(MainMenuActivity.this)) {
                            i = new Intent(getApplicationContext(),
                                    GoogleDriveActivity.class);
                        } else {
                            PlayServicesUtil.showGooglePlayServicesAvailabilityErrorDialog(MainMenuActivity.this);
                            return;
                        }
                    } else {
                        i = new Intent(getApplicationContext(),
                                ProgramsActivity.class);
                    }
                    startActivity(i);
                }
            }
        });

        // manage forms button. no result expected.
        manageFilesButton = findViewById(R.id.manage_forms);
        manageFilesButton.setText(getString(R.string.manage_files));
        manageFilesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Collect.allowClick()) {
                    Collect.getInstance().getActivityLogger()
                            .logAction(this, "deleteSavedForms", "click");
                    Intent i = new Intent(getApplicationContext(),
                            FileManagerTabs.class);
                    startActivity(i);
                }
            }
        });

        db = openOrCreateDatabase("SDRCCollectDB", MODE_PRIVATE, null);
        Cursor resultSet = db.rawQuery("Select * from " + getString(R.string.user_table_name), null);
        int rows = resultSet.getCount();
        if (rows > 0) {
            resultSet.moveToFirst();
            db_username = resultSet.getString(0);
            db_password = resultSet.getString(1);
        }




        // must be at the beginning of any activity that can be called from an
        // external intent
        Timber.i("Starting up, creating directories");
        try {
            Collect.createODKDirs();
        } catch (RuntimeException e) {
            createErrorDialog(e.getMessage(), EXIT);
            return;
        }

        {
            // dynamically construct the "ODK Collect vA.B" string
            TextView mainMenuMessageLabel = findViewById(R.id.main_menu_header);
            mainMenuMessageLabel.setText(Collect.getInstance()
                    .getVersionedAppName());
        }

        File f = new File(Collect.ODK_ROOT + "/collect.settings");
        File j = new File(Collect.ODK_ROOT + "/collect.settings.json");
        // Give JSON file preference
        if (j.exists()) {
            boolean success = SharedPreferencesUtils.loadSharedPreferencesFromJSONFile(j);
            if (success) {
                ToastUtils.showLongToast(R.string.settings_successfully_loaded_file_notification);
                j.delete();

                // Delete settings file to prevent overwrite of settings from JSON file on next startup
                if (f.exists()) {
                    f.delete();
                }
            } else {
                ToastUtils.showLongToast(R.string.corrupt_settings_file_notification);
            }
        } else if (f.exists()) {
            boolean success = loadSharedPreferencesFromFile(f);
            if (success) {
                ToastUtils.showLongToast(R.string.settings_successfully_loaded_file_notification);
                f.delete();
            } else {
                ToastUtils.showLongToast(R.string.corrupt_settings_file_notification);
            }
        }

        reviewSpacer = findViewById(R.id.review_spacer);
        getFormsSpacer = findViewById(R.id.get_forms_spacer);

        adminPreferences = this.getSharedPreferences(
                AdminPreferencesActivity.ADMIN_PREFERENCES, 0);

        InstancesDao instancesDao = new InstancesDao();

        // count for finalized instances
        try {
            finalizedCursor = instancesDao.getFinalizedInstancesCursor();
        } catch (Exception e) {
            createErrorDialog(e.getMessage(), EXIT);
            return;
        }

        if (finalizedCursor != null) {
            startManagingCursor(finalizedCursor);
        }
        completedCount = finalizedCursor != null ? finalizedCursor.getCount() : 0;
        getContentResolver().registerContentObserver(InstanceColumns.CONTENT_URI, true,
                contentObserver);
        // finalizedCursor.registerContentObserver(contentObserver);

        // count for saved instances
        try {
            savedCursor = instancesDao.getUnsentInstancesCursor();
        } catch (Exception e) {
            createErrorDialog(e.getMessage(), EXIT);
            return;
        }

        if (savedCursor != null) {
            startManagingCursor(savedCursor);
        }
        savedCount = savedCursor != null ? savedCursor.getCount() : 0;

        //count for view sent form
        try {
            viewSentCursor = instancesDao.getSentInstancesCursor();
        } catch (Exception e) {
            createErrorDialog(e.getMessage(), EXIT);
            return;
        }
        if (viewSentCursor != null) {
            startManagingCursor(viewSentCursor);
        }
        viewSentCount = viewSentCursor != null ? viewSentCursor.getCount() : 0;

        updateButtons();
        setupGoogleAnalytics();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(getString(R.string.main_menu));
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                AdminPreferencesActivity.ADMIN_PREFERENCES, 0);

        boolean edit = sharedPreferences.getBoolean(
                AdminKeys.KEY_EDIT_SAVED, true);
        if (!edit) {
            if (reviewDataButton != null) {
                reviewDataButton.setVisibility(View.GONE);
            }
            if (reviewSpacer != null) {
                reviewSpacer.setVisibility(View.GONE);
            }
        } else {
            if (reviewDataButton != null) {
                reviewDataButton.setVisibility(View.VISIBLE);
            }
            if (reviewSpacer != null) {
                reviewSpacer.setVisibility(View.VISIBLE);
            }
        }

        boolean send = sharedPreferences.getBoolean(
                AdminKeys.KEY_SEND_FINALIZED, true);
        if (!send) {
            if (sendDataButton != null) {
                sendDataButton.setVisibility(View.GONE);
            }
        } else {
            if (sendDataButton != null) {
                sendDataButton.setVisibility(View.VISIBLE);
            }
        }

        boolean viewSent = sharedPreferences.getBoolean(
                AdminKeys.KEY_VIEW_SENT, true);
        if (!viewSent) {
            if (viewSentFormsButton != null) {
                viewSentFormsButton.setVisibility(View.GONE);
            }
        } else {
            if (viewSentFormsButton != null) {
                viewSentFormsButton.setVisibility(View.VISIBLE);
            }
        }

        boolean getBlank = sharedPreferences.getBoolean(
                AdminKeys.KEY_GET_BLANK, true);
        if (!getBlank) {
            if (getFormsButton != null) {
                getFormsButton.setVisibility(View.GONE);
            }
            if (getFormsSpacer != null) {
                getFormsSpacer.setVisibility(View.GONE);
            }
        } else {
            if (getFormsButton != null) {
                getFormsButton.setVisibility(View.VISIBLE);
            }
            if (getFormsSpacer != null) {
                getFormsSpacer.setVisibility(View.VISIBLE);
            }
        }

        boolean deleteSaved = sharedPreferences.getBoolean(
                AdminKeys.KEY_DELETE_SAVED, true);
        if (!deleteSaved) {
            if (manageFilesButton != null) {
                manageFilesButton.setVisibility(View.GONE);
            }
        } else {
            if (manageFilesButton != null) {
                manageFilesButton.setVisibility(View.VISIBLE);
            }
        }

//        ((Collect) getApplication())
//                .getDefaultTracker()
//                .enableAutoActivityTracking(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Collect.getInstance().getActivityLogger()
                .logAction(this, "onCreateOptionsMenu", "show");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                Collect.getInstance()
                        .getActivityLogger()
                        .logAction(this, "onOptionsItemSelected",
                                "MENU_ABOUT");
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.menu_general_preferences:
                Collect.getInstance()
                        .getActivityLogger()
                        .logAction(this, "onOptionsItemSelected",
                                "MENU_PREFERENCES");
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            case R.id.menu_admin_preferences:
                Collect.getInstance().getActivityLogger()
                        .logAction(this, "onOptionsItemSelected", "MENU_ADMIN");
                String pw = adminPreferences.getString(
                        AdminKeys.KEY_ADMIN_PW, "");
                if ("".equalsIgnoreCase(pw)) {
                    startActivity(new Intent(this, AdminPreferencesActivity.class));
                } else {
                    showDialog(PASSWORD_DIALOG);
                    Collect.getInstance().getActivityLogger()
                            .logAction(this, "createAdminPasswordDialog", "show");
                }
                return true;
            case R.id.menu_update:
                // update
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
               /* Editor editor = settings.edit();
                editor.putString(PreferencesActivity.ODK_SERVER_URLS, "");
                editor.apply();*/

                //     eraseLoginData();

                // hit the server again and update the database

                ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.isConnected()) {

                    settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    String webServerURL = settings.getString(PreferencesActivity.KEY_SUBMISSION_SERVER_URL, "");

                    if (!(webServerURL.charAt(webServerURL.length() - 1) == '/'))
                        webServerURL += "/";

                    showDialog(PROGRESS_DIALOG);

                    if (updateTask != null && updateTask.getStatus() != AsyncTask.Status.FINISHED) {
                        // return; // Already login in progress
                    } else if (updateTask != null) {
                        updateTask.setUpdateListener(null);
                        updateTask.cancel(true);
                        updateTask = null;
                    }

                    updateTask = new UpdateTask(getApplicationContext());
                    updateTask.setUpdateListener(this);
                    updateTask.execute(webServerURL, Base64.encodeToString((db_username + ":" + db_password).getBytes(), Base64.DEFAULT), db_password);

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.check_your_intenet_connection),
                            Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.menu_logout:
                // showDialog(WARNING_DIALOG);
                db.execSQL("UPDATE " + getString(R.string.user_table_name) + " set IsLoggedIn = 0;");
                goToLoginPage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createErrorDialog(String errorMsg, final boolean shouldExit) {
        Collect.getInstance().getActivityLogger()
                .logAction(this, "createErrorDialog", "show");
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setMessage(errorMsg);
        DialogInterface.OnClickListener errorListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Collect.getInstance()
                                .getActivityLogger()
                                .logAction(this, "createErrorDialog",
                                        shouldExit ? "exitApplication" : "OK");
                        if (shouldExit) {
                            finish();
                        }
                        break;
                }
            }
        };
        alertDialog.setCancelable(false);
        alertDialog.setButton(getString(R.string.ok), errorListener);
        alertDialog.show();
    }
    private void goToLoginPage() {

        // moving control to the login page
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case PROGRESS_DIALOG:
                Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.PROGRESS_DIALOG", "show");
                ProgressDialog mProgressDialog = new ProgressDialog(this);
                DialogInterface.OnClickListener loadingButtonListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.PROGRESS_DIALOG", "OK");
                        dialog.dismiss();
                        if (updateTask != null) {
                            updateTask.setUpdateListener(null);
                            updateTask.cancel(true);
                            updateTask = null;
                        }
                    }
                };
                mProgressDialog.setTitle(getString(R.string.downloading_data));
                mProgressDialog.setMessage(getString(R.string.update_dialog_message));
                mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setButton(getString(R.string.cancel), loadingButtonListener);
                return mProgressDialog;
            case PASSWORD_DIALOG:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog passwordDialog = builder.create();
                passwordDialog.setTitle(getString(R.string.enter_admin_password));
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialogbox_layout, null);
                passwordDialog.setView(dialogView, 20, 10, 20, 10);
                final CheckBox checkBox = dialogView.findViewById(R.id.checkBox);
                final EditText input = dialogView.findViewById(R.id.editText);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (!checkBox.isChecked()) {
                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        } else {
                            input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                    }
                });
                passwordDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                        getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                String value = input.getText().toString();
                                String pw = adminPreferences.getString(
                                        AdminKeys.KEY_ADMIN_PW, "");
                                if (pw.compareTo(value) == 0) {
                                    Intent i = new Intent(getApplicationContext(),
                                            AdminPreferencesActivity.class);
                                    startActivity(i);
                                    input.setText("");
                                    passwordDialog.dismiss();
                                } else {
                                    ToastUtils.showShortToast(R.string.admin_password_incorrect);
                                    Collect.getInstance()
                                            .getActivityLogger()
                                            .logAction(this, "adminPasswordDialog",
                                                    "PASSWORD_INCORRECT");
                                }
                            }
                        });

                passwordDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                        getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                Collect.getInstance()
                                        .getActivityLogger()
                                        .logAction(this, "adminPasswordDialog",
                                                "cancel");
                                input.setText("");
                            }
                        });

                passwordDialog.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                return passwordDialog;

        }
        return null;
    }

    // This flag must be set each time the app starts up
    private void setupGoogleAnalytics() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(Collect
                .getInstance());
        boolean isAnalyticsEnabled = settings.getBoolean(PreferenceKeys.KEY_ANALYTICS, true);
        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(getApplicationContext());
        googleAnalytics.setAppOptOut(!isAnalyticsEnabled);
    }

    private void updateButtons() {
        if (finalizedCursor != null && !finalizedCursor.isClosed()) {
            finalizedCursor.requery();
            completedCount = finalizedCursor.getCount();
            if (completedCount > 0) {
                sendDataButton.setText(
                        getString(R.string.send_data_button, String.valueOf(completedCount)));
            } else {
                sendDataButton.setText(getString(R.string.send_data));
            }
        } else {
            sendDataButton.setText(getString(R.string.send_data));
            Timber.w("Cannot update \"Send Finalized\" button label since the database is closed. "
                    + "Perhaps the app is running in the background?");
        }

        if (savedCursor != null && !savedCursor.isClosed()) {
            savedCursor.requery();
            savedCount = savedCursor.getCount();
            if (savedCount > 0) {
                reviewDataButton.setText(getString(R.string.review_data_button,
                        String.valueOf(savedCount)));
            } else {
                reviewDataButton.setText(getString(R.string.review_data));
            }
        } else {
            reviewDataButton.setText(getString(R.string.review_data));
            Timber.w("Cannot update \"Edit Form\" button label since the database is closed. "
                    + "Perhaps the app is running in the background?");
        }

        if (viewSentCursor != null && !viewSentCursor.isClosed()) {
            viewSentCursor.requery();
            viewSentCount = viewSentCursor.getCount();
            if (viewSentCount > 0) {
                viewSentFormsButton.setText(
                        getString(R.string.view_sent_forms_button, String.valueOf(viewSentCount)));
            } else {
                viewSentFormsButton.setText(getString(R.string.view_sent_forms));
            }
        } else {
            viewSentFormsButton.setText(getString(R.string.view_sent_forms));
            Timber.w("Cannot update \"View Sent\" button label since the database is closed. "
                    + "Perhaps the app is running in the background?");
        }
    }

    private boolean loadSharedPreferencesFromFile(File src) {
        // this should probably be in a thread if it ever gets big
        boolean res = false;
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new FileInputStream(src));
            GeneralSharedPreferences.getInstance().clear();

            // first object is preferences
            Map<String, ?> entries = (Map<String, ?>) input.readObject();

            AutoSendPreferenceMigrator.migrate(entries);

            for (Entry<String, ?> entry : entries.entrySet()) {
                GeneralSharedPreferences.getInstance().save(entry.getKey(), entry.getValue());
            }

            AuthDialogUtility.setWebCredentialsFromPreferences();

            AdminSharedPreferences.getInstance().clear();

            // second object is admin options
            Map<String, ?> adminEntries = (Map<String, ?>) input.readObject();
            for (Entry<String, ?> entry : adminEntries.entrySet()) {
                AdminSharedPreferences.getInstance().save(entry.getKey(), entry.getValue());
            }
            res = true;
        } catch (IOException | ClassNotFoundException e) {
            Timber.e(e, "Exception while loading preferences from file due to : %s ", e.getMessage());
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                Timber.e(ex, "Exception thrown while closing an input stream due to: %s ", ex.getMessage());
            }
        }
        return res;
    }

    @Override
    public void updateOperationComplete(HashMap<Integer, String> result) {
        // TODO Auto-generated method stub

        dismissDialog(PROGRESS_DIALOG);

        try {
            String s = result.get(1);
            if (s != null) {

                //Eithi khela haba
                JSONObject object = new JSONObject(s);
                JSONArray programXFormModelList = object.getJSONArray("programXFormModelList");
                JSONArray listOfMediaFilesToUpdate = object.getJSONArray("listOfMediaFilesToUpdate");
                saveUpdatedMediafile(listOfMediaFilesToUpdate);

                // then try to insert
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PreferencesActivity.ODK_SERVER_URLS, "");
                editor.apply();
                eraseLoginData();
                insertDataIntoTables(programXFormModelList);
                if (db_username.equals("")) {
                    // insert
                    db.execSQL("INSERT INTO " + getString(R.string.user_table_name) + " VALUES('" + db_username + "','"
                            + db_password + "', 1);");
                } else {
                    // update
                    db.execSQL("UPDATE " + getString(R.string.user_table_name) + " set username = '" + db_username
                            + "', password = '" + db_password + "', IsLoggedIn = 1;");
                }
                setFormListWhereClause();
                updateUI();
                Toast.makeText(getApplicationContext(), getString(R.string.update_successful), Toast.LENGTH_SHORT)
                        .show();

            } else {
                s = result.get(0);
                int resultNumber = Integer.parseInt(s);
                switch (resultNumber) {
                    case 0:
                        s = getString(R.string.invalid_username_password);
                      /*  code started by subha
                        this  code is doing the logout operation thing if authentication fails and make the column AuthFromServer to true i.e. 1 .
                      * */
                        db.execSQL("UPDATE " + getString(R.string.user_table_name) + " set IsLoggedIn = 0;");
                        db.execSQL("UPDATE " + getString(R.string.user_table_name) + " set AuthFromServer = 1;");
                        //code end
                        goToLoginPage();
                        break;
                    case 1:
                        s = getString(R.string.some_other_server_error);
                        break;
                    case 2:
                        s = getString(R.string.server_not_found);
                        break;
                    case 3:
                        s = getString(R.string.request_timeout);
                        break;
                    case 4:
                        s = getString(R.string.exception_while_update);
                        break;
                    default:
                        s = "Exception";
                }
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_processing_after_login_data),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void eraseLoginData() {
        // TODO Auto-generated method stub
        Collect.getInstance().getActivityLogger().logAction(this, "onOptionsItemSelected", "MENU_LOGOUT");
        db.execSQL("UPDATE " + getString(R.string.user_table_name) + " set IsLoggedIn = 0;");

        // delete all the program, xform and their mapping data, they will get
        // inserted again in next login
        // we have do it because in next login another user may come, which
        // might have assigned to different programs and xforms.
        db.execSQL("delete from " + getString(R.string.program_table_name) + ";");
        db.execSQL("delete from " + getString(R.string.xform_table_name) + ";");
        db.execSQL("delete from " + getString(R.string.program_xform_mapping_table_name) + ";");
    }
    /**
     * This method will be responsible for inserting data into program, xform
     * and their mapping table.
     *
     * @param jsonArray
     * @throws JSONException
     * @since v1.0.0.0
     */

    private void insertDataIntoTables(JSONArray jsonArray) throws JSONException, UnsupportedEncodingException {
        // TODO Auto-generated method stub
        Map<String, JSONObject> xForms = new HashMap<String, JSONObject>();
        int count = 0;
        // iterate over programs
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject programWithXFormObj = jsonArray.getJSONObject(i);
            JSONObject pogramObj = programWithXFormObj.getJSONObject("programModel");

            db.execSQL("INSERT INTO " + getString(R.string.program_table_name) + " VALUES("
                    + pogramObj.getInt("programId") + ",'" + pogramObj.getString("programName") + "', 0);");

            JSONArray xFormArray = programWithXFormObj.getJSONArray("xFormsModel");

            // iterate over xForms
            for (int j = 0; j < xFormArray.length(); j++) {
                JSONObject xFormObject = xFormArray.getJSONObject(j);
                xForms.put(xFormObject.getString("xFormId"), xFormObject);
                db.execSQL("INSERT INTO " + getString(R.string.program_xform_mapping_table_name) + " VALUES(" + ++count
                        + ", " + pogramObj.getInt("programId") + ",'" + xFormObject.getString("xFormId") + "');");
            }

        }

        // boolean countForm = true;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String odkServerURLs = settings.getString(PreferencesActivity.ODK_SERVER_URLS, "");

        // iterate over all xForms
        for (JSONObject obj : xForms.values()) {

            String encodedPassword = new String(Base64.decode(obj.getString("password").getBytes(), Base64.DEFAULT), "UTF-8");

            if (odkServerURLs.equals("")) {
                odkServerURLs += obj.getString("odkServerURL") + PreferencesActivity.KEY_SEPARATOR
                        + obj.getString("username") + PreferencesActivity.KEY_SEPARATOR + encodedPassword;
            } else {
                if (!odkServerURLs.contains(obj.getString("odkServerURL"))) {
                    odkServerURLs += PreferencesActivity.URL_SEPARATOR;
                    odkServerURLs += obj.getString("odkServerURL") + PreferencesActivity.KEY_SEPARATOR
                            + obj.getString("username") + PreferencesActivity.KEY_SEPARATOR + encodedPassword;
                }
            }

            db.execSQL("INSERT INTO " + getString(R.string.xform_table_name) + " VALUES('" + obj.getString("xFormId")
                    + "','" + obj.getString("odkServerURL") + "','" + obj.getString("username") + "','"
                    + encodedPassword + "');");
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PreferencesActivity.USERNAME, db_username);
        editor.putString(PreferencesActivity.PASSWORD, db_password);
        editor.putString(PreferencesActivity.ODK_SERVER_URLS, odkServerURLs);
        editor.apply();

    }
    private void setFormListWhereClause() {
        // TODO Auto-generated method stub

        Cursor resultSet = db.rawQuery("Select * from " + getString(R.string.xform_table_name), null);
        String whereClauseString = "jrFormId IN (";
        while (resultSet.moveToNext()) {
            whereClauseString += "'" + resultSet.getString(resultSet.getColumnIndex("id")) + "', ";
        }

        if (resultSet.getCount() > 0) {
            whereClauseString = whereClauseString.substring(0, whereClauseString.length() - 2);
            whereClauseString += ")";
            Collect.getInstance().setFormIdWhereClauseString(whereClauseString);
        } else {
            Collect.getInstance().setFormIdWhereClauseString(null);
        }

    }

    private void saveUpdatedMediafile(JSONArray jsonArray) throws JSONException {

        Map<String, JSONObject> xForms = new HashMap<String, JSONObject>();
        int count = 0;
        // iterate over programs
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject programWithXFormObj = jsonArray.getJSONObject(i);
            String formId = programWithXFormObj.getString("xFormId");
            if (!programWithXFormObj.get("mediaFile").toString().equals("null")) {
                //get the cursor for corresponding formId
                Cursor cursor = new FormsDao().getFormsCursorForFormId(formId);
                cursor.moveToFirst();
                String mediaPath = cursor.getString(cursor.getColumnIndex(FormsProviderAPI.FormsColumns.FORM_MEDIA_PATH));
                try {
                    //byte[] mediafile =programWithXFormObj.getString("mediaFile").toString().getBytes();
                    byte[] mediafile = Base64.decode(programWithXFormObj.getString("mediaFile"),Base64.DEFAULT);
                    File file = new File(mediaPath, "itemsets.csv");
                    if (file.exists()) {
                        file.delete();
                    }
                    OutputStream stream = new FileOutputStream(file);
                    stream.write(mediafile);
                    stream.close();

                    //update the shared preference for latest date to media file
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(formId, sdf.format(new Date()));
                    editor.apply();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    private void updateUI() {
        InstancesDao instancesDao = new InstancesDao();

        // count for finalized instances
        try {
            finalizedCursor = instancesDao.getFinalizedInstancesCursor();
        } catch (Exception e) {
            createErrorDialog(e.getMessage(), EXIT);
            return;
        }

        if (finalizedCursor != null) {
            startManagingCursor(finalizedCursor);
        }
        completedCount = finalizedCursor != null ? finalizedCursor.getCount() : 0;
        getContentResolver().registerContentObserver(InstanceProviderAPI.InstanceColumns.CONTENT_URI, true,
                contentObserver);
        // finalizedCursor.registerContentObserver(contentObserver);

        // count for saved instances
        try {
            savedCursor = instancesDao.getUnsentInstancesCursor();
        } catch (Exception e) {
            createErrorDialog(e.getMessage(), EXIT);
            return;
        }

        if (savedCursor != null) {
            startManagingCursor(savedCursor);
        }
        savedCount = savedCursor != null ? savedCursor.getCount() : 0;

        //count for view sent form
        try {
            viewSentCursor = instancesDao.getSentInstancesCursor();
        } catch (Exception e) {
            createErrorDialog(e.getMessage(), EXIT);
            return;
        }
        if (viewSentCursor != null) {
            startManagingCursor(viewSentCursor);
        }
        viewSentCount = viewSentCursor != null ? viewSentCursor.getCount() : 0;

        updateButtons();
    }
    /*
     * Used to prevent memory leaks
     */
    static class IncomingHandler extends Handler {
        private final WeakReference<MainMenuActivity> target;

        IncomingHandler(MainMenuActivity target) {
            this.target = new WeakReference<MainMenuActivity>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            MainMenuActivity target = this.target.get();
            if (target != null) {
                target.updateButtons();
            }
        }
    }

    /**
     * notifies us that something changed
     */
    private class MyContentObserver extends ContentObserver {

        MyContentObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            handler.sendEmptyMessage(0);
        }
    }

}