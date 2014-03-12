package com.cs646.photosharing;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
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

public class UserListFragment extends ListFragment {
	private static final String TAG = "PhotoSharing";
	HttpClient mHttpclient;
	String[] mUserList;
	long[] mIdList;
	public static final String USER_ID = "com.cs646.android.UISampler.user_id";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain the fragment across the activity's re-creation
		setRetainInstance(true);

		// Start the task to download the user list
// TODO		new FetchUserListTask().execute(url);		do this in onResume
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i("", "in onListItemClick: position = " + position +  "  user = " + mUserList[position] + "  id = " + mIdList[position]);		
		
		startPhotoListActivity(mIdList[position]);
		// new Toast(getActivity(), numbers_digits[(int) id]);

		// Start new activity to show the list of photos
		// pass the user name and id of the selection
		// startPhotoListActivity(str);
	}
	
	/*
	 * Start the UserPhotoActivity
	 */
	protected void startPhotoListActivity(long id) {
		Log.i(TAG, "startPhotoListActivity id = " + id);
		
		// Create an Intent to call the List Activity
		Intent i = new Intent(getActivity(), PhotoListActivity.class);

		// Pass data to the PhotoListActivity
		i.putExtra(USER_ID, id);

// TODO		startActivityForResult(i, mActivityCode);
		startActivity(i);
	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		String userAgent = null;
		mHttpclient = AndroidHttpClient.newInstance(userAgent);
		FetchUserListTask task = new FetchUserListTask();
		String url = "http://bismarck.sdsu.edu/photoserver/userlist/";
		task.execute(url);
	}

	@Override
	public void onPause() {
    	super.onPause();
		mHttpclient.getConnectionManager().shutdown();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
// TODO should I get rid of onCreateView - don't need to set adapter here
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
		// inflater.getContext(), android.R.layout.simple_list_item_1,
		// numbers_text);
		// setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/*
	 * Void... is the varargs syntax introduced in Java 1.5 and puts the args
	 * into an array varargs must be the last parameter
	 * Params:
	 * 	1 X 	input params X passed as args to doInBackground - returns result Z
	 *  2 Y 	progress Y passed as args to onProgressUpdate
	 *  3 Z 	results Z passed as args to onPostExecute
	 */
	class FetchUserListTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			// String url = "http://bismarck.sdsu.edu/photoserver/userlist/";
			// HttpClient httpclient = AndroidHttpClient.newInstance(null);
			HttpGet getMethod = new HttpGet(urls[0]);
			try {
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = mHttpclient.execute(getMethod,
						responseHandler);
				Log.i(TAG, responseBody);
				return responseBody;
			} catch (Throwable t) {
				Log.i(TAG, "UserList request failed", t);
			}

			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 * Executed when AsyncTask completes
		 * Executes on the Main UI thread so UI updates go here
		 */
		public void onPostExecute(String result) {
			Log.i(TAG, result); 

			// Get the JSON data from the response
			try {
				JSONArray userList = new JSONArray(result);				
				mUserList = new String[userList.length()];
				mIdList = new long[userList.length()];
				for (int i=0; i<userList.length(); i++) {
					JSONObject user = (JSONObject) userList.get(i);
					mUserList[i] = user.getString("name");
					mIdList[i] = user.getLong("id");
				}
				// TODO int id = user.getInt("id");		save the id somewhere
				
				// Add the list of users to the UI list
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getActivity(), android.R.layout.simple_list_item_1,
						mUserList);
				setListAdapter(adapter);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e(TAG, "Error getting JSON response", e);
			}


		}
	}

}
