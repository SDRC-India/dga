package org.sdrc.dgacg.collect.android.tasks;

import android.os.AsyncTask;


import org.opendatakit.httpclientandroidlib.NameValuePair;
import org.opendatakit.httpclientandroidlib.message.BasicNameValuePair;
import org.sdrc.dgacg.collect.android.listeners.LoginListener;

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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author Ratikanta Pradhan (ratikanta@sdrc.co.in)
 *
 */
public class LoginTask extends AsyncTask<String, Integer, HashMap<Integer, String>> {
	
	

	


	private LoginListener loginListener;

	@Override
	protected HashMap<Integer, String> doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		HashMap<Integer, String> result = new HashMap<Integer, String>();
		String s = "";
		
		try {

			URL url = new URL(params[0] + "authenticate");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(55000);
			conn.setConnectTimeout(60000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("userString", params[1]));

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(getQuery(params1));
			writer.flush();
			writer.close();
			os.close();

			conn.connect();
			
			int responseCode = conn.getResponseCode(); 

			if ( responseCode == 200) {
				// dismissDialog(PROGRESS_DIALOG);
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				s = br.readLine();
				if (s != null) {
					result.put(1, s);					
				} else {
					s = "0";
					result.put(0, s);
				}
			} else if(responseCode == 401){
				s = "0";
				result.put(0, s);
			}  else {
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
		}catch (UnknownHostException e) {
			s = "5";
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
            if (loginListener != null) {
            	loginListener.loginOperationComplete(result);
            }
        }		
	}
	
	public void setLoginListener(LoginListener loginListener) {
		synchronized (this) {
			this.loginListener = loginListener;
        }
		
	}
	
	
	/**
	 * This method will help in login process
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 * @since v1.0.0.0
	 */
	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (NameValuePair pair : params)
	    {
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
