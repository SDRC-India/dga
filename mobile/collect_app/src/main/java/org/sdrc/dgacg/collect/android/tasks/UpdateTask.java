package org.sdrc.dgacg.collect.android.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONObject;
import org.opendatakit.httpclientandroidlib.NameValuePair;
import org.sdrc.dgacg.collect.android.dao.FormsDao;
import org.sdrc.dgacg.collect.android.listeners.UpdateListener;
import org.sdrc.dgacg.collect.android.models.FormsToDownloadMediafiles;
import org.sdrc.dgacg.collect.android.provider.FormsProviderAPI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 * @author Amit Kumar Sahoo (amit@sdrc.co.in)
 */

public class UpdateTask extends AsyncTask<String, Integer, HashMap<Integer, String>> {
    Context context;

    public UpdateTask(Context context) {
        this.context = context;
    }

    private UpdateListener updateListener;

    public void setUpdateListener(UpdateListener updateListener) {
        synchronized (this) {
            this.updateListener = updateListener;
        }
    }

    @Override
    protected HashMap<Integer, String> doInBackground(String... params) {
        // TODO Auto-generated method stub

        HashMap<Integer, String> result = new HashMap<Integer, String>();
        String s = "";

        try {

            //qfind the list of all downloaded forms with their media updated date
            List<FormsToDownloadMediafiles> mediafiles = new ArrayList<>();

            //get the cursor of all forms
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            Cursor cursor = new FormsDao().getFormsCursor();
            cursor.move(-1);
            while (cursor.moveToNext()) {
                FormsToDownloadMediafiles toDownloadMediafiles = new FormsToDownloadMediafiles();
                toDownloadMediafiles.setFormId(cursor.getString(cursor.getColumnIndex(FormsProviderAPI.FormsColumns.JR_FORM_ID)));
                toDownloadMediafiles.setDownloadOrUpdateDate(settings.getString(cursor.getString(cursor.getColumnIndex(FormsProviderAPI.FormsColumns.JR_FORM_ID)), null));
                mediafiles.add(toDownloadMediafiles);
            }

            String jsonArray = new Gson().toJson(mediafiles);

            JSONArray array = new JSONArray(jsonArray);
            JSONObject obj = new JSONObject();
            obj.put("formsToDownloadMediafiles", array);
            obj.put("userString", params[1]);

            URL url = new URL(params[0] + "update");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(10000);
            //conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.addRequestProperty("Accept", "application/json");
            conn.addRequestProperty("Content-Type", "application/json");


//List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//nameValuePairs.add(new BasicNameValuePair("userString", params[1]));

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
//writer.write(getQuery(nameValuePairs));
            writer.write(obj.toString());
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

            int responseCode = conn.getResponseCode();
            Log.i("responsecode", responseCode + "");
            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                s = br.readLine();
                if (s != null) {
                    result.put(1, s);
                } else {
                    s = "0";
                    result.put(0, s);
                }

            } else if (responseCode == 401) {
                s = "0";
                result.put(0, s);
            } else {
                s = "1";
                result.put(0, s);
            }
        } catch (ConnectException e) {
            if (e.getMessage().contains("failed to connect")) {
                s = "2";
                result.put(0, s);
            } else {
                s = "4";
                result.put(0, s);
            }
        } catch (SocketTimeoutException e) {
            s = "3";
            result.put(0, s);
        } catch (Exception e) {
            s = "4";
            result.put(0, s);
        }

        return result;
    }

    @Override
    protected void onPostExecute(HashMap<Integer, String> result) {
        // TODO Auto-generated method stub
        synchronized (this) {
            if (updateListener != null) {
                updateListener.updateOperationComplete(result);
            }
        }
    }

    /**
     * This method will help in login process
     *
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     * @since v1.0.0.0
     */
    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
