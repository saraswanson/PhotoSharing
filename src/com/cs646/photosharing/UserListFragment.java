package com.cs646.photosharing;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UserListFragment extends ListFragment {
	String[] numbers_text = new String[] { "one", "two", "three", "four",
			"five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve",
			"thirteen", "fourteen", "fifteen" };
	String[] numbers_digits = new String[] { "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "10", "11", "12", "13", "14", "15" };

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// new Toast(getActivity(), numbers_digits[(int) id]);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				inflater.getContext(), android.R.layout.simple_list_item_1,
				numbers_text);
		setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	/*
	 * Void... is the varargs syntax introduced in Java 1.5 and puts the args
	 * into an array varargs must be the last parameter
	 */
	class HttpClientTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... arg0) {
			String url = "http://bismarck.sdsu.edu/photoserver/userlist/";
			HttpClient httpclient = AndroidHttpClient.newInstance(null);
			HttpGet getMethod = new HttpGet(url);
			try {
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpclient.execute(getMethod,
						responseHandler);
				Log.i("sara", responseBody);
			} catch (Throwable t) {
				Log.i("sara", "UserList request failed", t);
			}
			httpclient.getConnectionManager().shutdown();
			return null;
		}
		
		public void onPostExecute(String result) {
			Log.i("sara", result); // here you could put contents into UI element
		}
	}

//	class HttpClientTask extends AsyncTask<String, Void, String> {
//		protected String doInBackground(String... urls) {
//			try {
//				ResponseHandler<String> responseHandler = new BasicResponseHandler();
//				HttpGet getMethod = new HttpGet(urls[0]);
//				String responseBody = httpclient.execute(getMethod,
//						responseHandler);
//				return responseBody;
//			} catch (Throwable t) {
//				Log.i("rew", "did not work", t);
//			}
//			return null;
//		}
//
//		public void onPostExecute(String result) {
//			Log.i("rew", result); // here you could put contents into UI element
//		}
//	}

}
