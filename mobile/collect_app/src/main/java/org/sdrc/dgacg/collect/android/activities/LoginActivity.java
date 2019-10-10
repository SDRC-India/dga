package org.sdrc.dgacg.collect.android.activities;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sdrc.dgacg.collect.android.R;
import org.sdrc.dgacg.collect.android.application.Collect;
import org.sdrc.dgacg.collect.android.listeners.LoginListener;
import org.sdrc.dgacg.collect.android.preferences.PreferencesActivity;
import org.sdrc.dgacg.collect.android.provider.FormsProviderAPI;
import org.sdrc.dgacg.collect.android.provider.InstanceProviderAPI;
import org.sdrc.dgacg.collect.android.tasks.LoginTask;
import org.sdrc.dgacg.collect.android.utilities.UrlUtils;


import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * This is the login activity. Once we logged in to the system until logged out, we won't see this screen.
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @since v1.0.0.0
 */

@SuppressLint("InlinedApi")
public class LoginActivity extends Activity implements LoginListener {

    private static final String t = "LoginActivity";

    private final static int PROGRESS_DIALOG = 1;
    private final static int SERVER_URL_CHANGE = 2;
    private final static int  WARNING_DIALOG = 3;

    /**
     * This variable will help us to access the database.
     */
    SQLiteDatabase db = null;

    /**
     * This variable will keep the database user name after fetching from database
     */
    String db_username = "";
    String username = "";
    String password = "";


    /**
     * This variable will keep the database password after fetching from database
     */
    String db_password = "";

    /**
     * This variable will help us check and persist, whether the user has logged in or not. After we know it we can render the login activity or main activity.
     * If the value will be 0 then the user is not logged in and if the value is 1 it is logged in user
     */
    int isLoggedIn = 0;

    /**
     * This will help in whether the back button is tapped or not, "<b style="color: red;">The tap again to exit thing</b>".
     */
    private boolean doubleBackToExitPressedOnce = false;

    /**
     * This is the SDRC Server URL. Which is our middle layer. We will use this here to authenticate and fetch data.
     */
    private String webServerURL;
    private ProgressDialog mProgressDialog;
    private LoginTask loginTask;

    HashMap<Integer, String> resultGlobal = null;

    EditText editTextForUsername, editTextForPassword;
    private ImageView showHide;
    Boolean showHideFlag = false;
    private long megAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        db = openOrCreateDatabase("SDRCCollectDB",MODE_PRIVATE,null);

        //Creating tables what we need in further process
        createTables();

        //setting up the webServerURL in the shared preference
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        webServerURL = settings.getString(PreferencesActivity.KEY_SUBMISSION_SERVER_URL,"");

        if(webServerURL.equals("")){
            webServerURL = getString(R.string.default_web_server_url);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PreferencesActivity.KEY_SUBMISSION_SERVER_URL, getString(R.string.default_web_server_url));
            editor.apply();
        }

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
        megAvailable = bytesAvailable / (1024 * 1024);
        Log.e("","Available MB : "+megAvailable);

        Cursor resultSet = db.rawQuery("Select * from "+ getString(R.string.user_table_name),null);
        int rows = resultSet.getCount();
        if(rows > 0){
            resultSet.moveToFirst();
            db_username = resultSet.getString(0);
            db_password = resultSet.getString(1);
            isLoggedIn = resultSet.getInt(2);

            if(isLoggedIn == 1){
                //set assigned form list
                Collect.getInstance().setCurrentUser(db_username);
                setFormListWhereClause();
                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        }

        editTextForPassword = (EditText)findViewById(R.id.editTextForPassword);
        showHide = (ImageView) findViewById(R.id.show_hide);
        showHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showHideFlag) {
                    editTextForPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showHide.setImageResource(R.drawable.show);
                    showHideFlag = false;
                } else {
                    editTextForPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showHide.setImageResource(R.drawable.hide);
                    showHideFlag = true;
                }
            }
        });

    }

    private void deleteBlankAndSavedForms() {
        // TODO Auto-generated method stub
        getContentResolver().delete(InstanceProviderAPI.InstanceColumns.CONTENT_URI,null, null);
        deleteBlankForms();
        deleteSavedForms();
    }

    private void deleteSavedForms() {
        // TODO Auto-generated method stub
        Cursor c = managedQuery(InstanceProviderAPI.InstanceColumns.CONTENT_URI, null, null, null,null);

        for(int i = 1 ; i <= c.getCount();i++){
            Long l = (long) i;
            try {
                Uri deleteForm =
                        Uri.withAppendedPath(InstanceProviderAPI.InstanceColumns.CONTENT_URI, l.toString());

                int wasDeleted = Collect.getInstance().getContentResolver().delete(deleteForm, null, null);

                if(wasDeleted == 0)
                    break;
            } catch ( Exception ex ) {
                Log.e(t,"Exception during delete of: " + l.toString() + " exception: "  + ex.toString());
            }
        }
    }

    private void deleteBlankForms() {
        // TODO Auto-generated method stub
        Cursor c = managedQuery(FormsProviderAPI.FormsColumns.CONTENT_URI, null, null, null, null);

        for(int i = 1 ; i <= c.getCount();i++){
            Long l = (long) i;

            try {
                Uri deleteForm =
                        Uri.withAppendedPath(FormsProviderAPI.FormsColumns.CONTENT_URI, l.toString());

                Collect.getInstance().getContentResolver().delete(deleteForm, null, null);

            } catch ( Exception ex ) {
                Log.e(t,"Exception during delete of: " + l.toString() + " exception: "  + ex.toString());
            }
        }
    }

    /**
     * This method will create all the tables which we need in user form assignment process.
     * @since v1.0.0.0
     */

    private void createTables() {

        //user table
		/* author:subhadarshani
		new column added auth_from_server in user table*/
        db.execSQL("CREATE TABLE IF NOT EXISTS " + getString(R.string.user_table_name) + "(Username VARCHAR,Password VARCHAR, IsLoggedIn INTEGER,AuthFromServer INTEGER);");

        //program table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + getString(R.string.program_table_name) + " (id INTEGER, name VARCHAR, selected INTEGER);");

        //xForm table
        db.execSQL("CREATE TABLE IF NOT EXISTS  " + getString(R.string.xform_table_name) + " (id VARCHAR, odkServerURL VARCHAR, username VARCHAR, password VARCHAR);");


        //xForm program mapping table
        db.execSQL("CREATE TABLE IF NOT EXISTS  " + getString(R.string.program_xform_mapping_table_name) + " (ID INTEGER PRIMARY KEY   AUTOINCREMENT, programId INTEGER, xFormId VARCHAR);");

    }

    /**
     * The folowing method will take user to home screen if authentication successful from server. Once the authentication will be successful, next time login will not
     * Hit server. Credentilas will be validated from local database. When user will do forgot/change password in web then this method will hit server.
     * @param view
     * @since v1.0.0.0
     * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
     */
    public void login(View view){

        EditText editTextForUsername = (EditText)findViewById(R.id.editTextForUsername);
        EditText editTextForPassword = (EditText)findViewById(R.id.editTextForPassword);

        username = editTextForUsername.getText().toString();
        password = editTextForPassword.getText().toString();

        if(username!= null && password != null && !username.trim().equals("") && !password.trim().equals("")){


            //checking whether credentials matching with local database
            Cursor resultSet = db.rawQuery("Select * from "+ getString(R.string.user_table_name),null);
            int rows = resultSet.getCount();
            if(rows > 0){

                //Local user record found. Will verify the credentials
                resultSet.moveToFirst();
                int auth_from_server = resultSet.getInt(3);

                //Checking whether we need to hit server for updating password or not
                if(auth_from_server == 0){
                    db_username = resultSet.getString(0);
                    db_password = resultSet.getString(1);

                    if(db_username.equals(username) && db_password.equals(password)){

                        //Setting IsLoggedIn = 1
                        db.execSQL("UPDATE " +getString(R.string.user_table_name)+ " set IsLoggedIn = 1;");
                        //set assigned form list
                        Collect.getInstance().setCurrentUser(db_username);
                        setFormListWhereClause();
                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        //Username or password incorrect. Trying to do fresh login by hitting server
                        freshLogin();
                    }
                }else{
                    //auth_from_server value would be 1. Cause, in web application password of user would be change.
                    //In this case we have to hit server for login
                    freshLogin();
                }


            }else{
                //There is no local user record, have to hit server
                freshLogin();
            }
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.invalid_username_password), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * This following method will hit server to verify credential
     * @since v1.0.0
     * @author Ratikanta Pradhan
     */
    private void freshLogin(){



        ConnectivityManager cm = (ConnectivityManager)getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PreferencesActivity.ODK_SERVER_URLS, "");
            editor.apply();


            settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            webServerURL = settings.getString(PreferencesActivity.KEY_SUBMISSION_SERVER_URL,"");

            if(!(webServerURL.charAt(webServerURL.length()-1) == '/'))
                webServerURL += "/";



            showDialog(PROGRESS_DIALOG);

            if (loginTask != null && loginTask.getStatus() != AsyncTask.Status.FINISHED) {
                return; // Already login in progress
            } else if (loginTask != null) {
                loginTask.setLoginListener(null);
                loginTask.cancel(true);
                loginTask = null;
            }

            loginTask = new LoginTask();
            loginTask.setLoginListener(this);
            loginTask.execute(webServerURL, Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT));

        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.check_your_intenet_connection), Toast.LENGTH_SHORT).show();
        }
    }



    private void setUpDatabaseForNewUser() {
        // TODO Auto-generated method stub
        db.execSQL("UPDATE " +getString(R.string.user_table_name)+ " set IsLoggedIn = 0;");

        //delete all the program, xform and their mapping data, they will get inserted again in next login
        //we have do it because in next login another user may come, which might have assigned to different programs and xforms.
        db.execSQL("delete from " +getString(R.string.program_table_name)+ ";");
        db.execSQL("delete from " +getString(R.string.xform_table_name)+ ";");
        db.execSQL("delete from " +getString(R.string.program_xform_mapping_table_name)+ ";");
    }


    /**
     * This method will be responsible for inserting data into program, xform and their mapping table.
     * @param jsonArray
     * @throws JSONException
     * @since v1.0.0.0
     */

    private void insertDataIntoTables(JSONArray jsonArray) throws Exception {
        // TODO Auto-generated method stub
        LinkedHashMap<String, JSONObject> xForms = new LinkedHashMap<String, JSONObject>();
        int count = 0;
        //iterate over programs
        for(int i = 0; i < jsonArray.length();i++){
            JSONObject programWithXFormObj = jsonArray.getJSONObject(i);
            JSONObject pogramObj = programWithXFormObj.getJSONObject("programModel");

            db.execSQL("INSERT INTO " + getString(R.string.program_table_name) + " VALUES(" + pogramObj.getInt("programId") + ",'"+ pogramObj.getString("programName") +"', 0);");

            JSONArray xFormArray = programWithXFormObj.getJSONArray("xFormsModel");


            //iterate over xForms
            for(int j = 0; j < xFormArray.length();j++){
                JSONObject xFormObject = xFormArray.getJSONObject(j);
                xForms.put(xFormObject.getString("xFormId"), xFormObject);
                db.execSQL("INSERT INTO " + getString(R.string.program_xform_mapping_table_name) + " VALUES("+ ++count +", " + pogramObj.getInt("programId") + ",'"+ xFormObject.getString("xFormId") +"');");
            }

        }

//		boolean countForm = true;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String odkServerURLs = settings.getString(PreferencesActivity.ODK_SERVER_URLS, "");


        //iterate over all xForms
        for(JSONObject obj: xForms.values()){

            String encodedPassword = new String(Base64.decode(obj.getString("password").getBytes(), Base64.DEFAULT), "UTF-8");

            if(odkServerURLs.equals("")){
                odkServerURLs += obj.getString("odkServerURL") + PreferencesActivity.KEY_SEPARATOR + obj.getString("username") + PreferencesActivity.KEY_SEPARATOR + encodedPassword;
            }else{
                if(!odkServerURLs.contains(obj.getString("odkServerURL"))){
                    odkServerURLs += PreferencesActivity.URL_SEPARATOR;
                    odkServerURLs += obj.getString("odkServerURL") + PreferencesActivity.KEY_SEPARATOR + obj.getString("username") + PreferencesActivity.KEY_SEPARATOR + encodedPassword;
                }
            }

            db.execSQL("INSERT INTO " + getString(R.string.xform_table_name) + " VALUES('" + obj.getString("xFormId") + "','"+ obj.getString("odkServerURL") +"','"+ obj.getString("username") +"','"+ encodedPassword +"');");
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PreferencesActivity.USERNAME, username);
        editor.putString(PreferencesActivity.PASSWORD, password);
        editor.putString(PreferencesActivity.ODK_SERVER_URLS, odkServerURLs);
        editor.apply();


    }


    /**
     * Back button is prevented because we don't want the user to go back to login screen
     * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
     */
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected Dialog onCreateDialog(int id){

        switch(id){
            case PROGRESS_DIALOG:
                Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.PROGRESS_DIALOG", "show");
                mProgressDialog = new ProgressDialog(this);
                DialogInterface.OnClickListener loadingButtonListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.PROGRESS_DIALOG", "OK");
                                dialog.dismiss();
                                if (loginTask != null) {
                                    loginTask.setLoginListener(null);
                                    loginTask.cancel(true);
                                    loginTask = null;
                                }
                            }
                        };
                mProgressDialog.setTitle(getString(R.string.downloading_data));
                mProgressDialog.setMessage(getString(R.string.login_dialog_message));
                mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setButton(getString(R.string.cancel), loadingButtonListener);
                return mProgressDialog;

            case WARNING_DIALOG:
                Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.WARNING_DIALOG", "show");
                AlertDialog.Builder b1 = new AlertDialog.Builder(this);


                b1.setTitle(getString(R.string.confirm_title));
                b1.setMessage(getString(R.string.logout_warning));
                b1.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteBlankAndSavedForms();
                        postLoginWork();

                    }


                });
                b1.setNegativeButton(getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.WARNING_DIALOG", "Cancel");
                                dismissDialog(WARNING_DIALOG);
                            }
                        });

                b1.setCancelable(false);
                return b1.create();

            case SERVER_URL_CHANGE:

                Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.AUTH_DIALOG", "show");
                AlertDialog.Builder b = new AlertDialog.Builder(this);

                LayoutInflater factory = LayoutInflater.from(this);
                final View dialogView = factory.inflate(R.layout.web_server_url, null);

                // Get the server, username, and password from the settings
                final SharedPreferences settings =
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext());


                EditText web_server_url_edit = (EditText) dialogView.findViewById(R.id.web_server_url_edit);
                String stored_web_server_url_edit = settings.getString(PreferencesActivity.KEY_SUBMISSION_SERVER_URL, null);
                web_server_url_edit.setText(stored_web_server_url_edit);



                b.setTitle(getString(R.string.web_server_url));
//            b.setMessage(getString(R.string.server_auth_credentials, url));
                b.setView(dialogView);
                b.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.AUTH_DIALOG", "OK");

                        EditText web_server_url_edit = (EditText) dialogView.findViewById(R.id.web_server_url_edit);


                        if (UrlUtils.isValidUrl(web_server_url_edit.getText().toString())) {

                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(PreferencesActivity.KEY_SUBMISSION_SERVER_URL, web_server_url_edit.getText().toString());
                            editor.apply();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    R.string.url_error, Toast.LENGTH_SHORT)
                                    .show();
                        }


                    }
                });
                b.setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Collect.getInstance().getActivityLogger().logAction(this, "onCreateDialog.AUTH_DIALOG", "Cancel");
                                dismissDialog(SERVER_URL_CHANGE);
                            }
                        });

                b.setCancelable(false);
                return b.create();
        }

        return null;
    }


    @Override
    public void loginOperationComplete(HashMap<Integer, String> result) {
        // TODO Auto-generated method stub

        resultGlobal = result;
        dismissDialog(PROGRESS_DIALOG);

        //for the new user delete blank form and saved forms
        if(!db_username.equals(username) && !db_username.equals("")){

            //New user, because database username is not matching with entered username
            String s = resultGlobal.get(1);
            if(s != null || !(resultGlobal.containsKey(0))){
                showDialog(WARNING_DIALOG);
            }else{
                postLoginWork();
            }
        }else{

            //Username same at last logged in username. The same user
            postLoginWork();
        }

    }

    /*
    @author:subhadarshani
    this method is for updating the password if it is changed in web
    */
    private void updatePassword() {

        Cursor resultSet = db.rawQuery("Select * from "+ getString(R.string.user_table_name),null);
        int rows = resultSet.getCount();
        if(rows > 0) {
            resultSet.moveToFirst();
            int auth_from_server =  resultSet.getInt(3);
            //if the column auth_from_server ===1 that means the the user is not authenticated
            if(auth_from_server == 1){

                db.execSQL("UPDATE " + getString(R.string.user_table_name) + " set password = '" + password + "', AuthFromServer = 0;");
            }

        }
    }

    private void setFormListWhereClause() {
        // TODO Auto-generated method stub

        Cursor resultSet = db.rawQuery("Select * from "+ getString(R.string.xform_table_name),null);
        String whereClauseString = "jrFormId IN (";
        while(resultSet.moveToNext()){
            whereClauseString += "'" + resultSet.getString(resultSet.getColumnIndex("id")) + "', ";
        }

        if(resultSet.getCount() > 0){
            whereClauseString = whereClauseString.substring(0, whereClauseString.length() - 2);
            whereClauseString += ")";
            Collect.getInstance().setFormIdWhereClauseString(whereClauseString);
        }else{
            Collect.getInstance().setFormIdWhereClauseString(null);
        }

    }

    private void postLoginWork() {
        // TODO Auto-generated method stub

        try{
            String s = resultGlobal.get(1);
            if(s != null || !(resultGlobal.containsKey(0))){
                //Login successful
                setUpDatabaseForNewUser();
                updatePassword();

                insertDataIntoTables(new JSONArray(s));
                if(db_username.equals("")){
                    //insert
                    db.execSQL("INSERT INTO " + getString(R.string.user_table_name) + " VALUES('" + username + "','"+password+"', 1,0);");
                }else{
                    //update
                    db.execSQL("UPDATE " + getString(R.string.user_table_name) + " set username = '" + username + "', password = '"+password+"', IsLoggedIn = 1;");
                }

                setFormListWhereClause();

                Collect.getInstance().setCurrentUser(username);
                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }else{
                s = resultGlobal.get(0);
                int resultNumber = Integer.parseInt(s);
                switch(resultNumber){
                    case 0:
                        s = getString(R.string.invalid_username_password);
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
                        s = getString(R.string.exception_while_login);
                        break;
                    case 5:
                        s = getString(R.string.check_your_intenet_connection);
                        break;
                    default :
                        s = "Exception";
                }
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), getString(R.string.error_processing_after_login_data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
        megAvailable = bytesAvailable / (1024 * 1024);
        Log.e("","Available MB : "+megAvailable);
    }

}
