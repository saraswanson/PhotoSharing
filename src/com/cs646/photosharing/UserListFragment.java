package com.cs646.photosharing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
	private HttpClient mHttpclient;
	private List<User> mUserList;
	public static final String EXTRA_USER_ID = "com.cs646.android.UISampler.user_id";
	public static final String EXTRA_DATABASE = "com.cs646.android.UISampler.database";
	private PhotoSQLiteHelper mDb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the SQLite database to store users and photo ids
		// TODO mDb = new PhotoSQLiteHelper(getActivity());
		PhotoSharingApplication myApplication = (PhotoSharingApplication) getActivity()
				.getApplication();
		mDb = myApplication.getDatabase();

		// Retain the fragment across the activity's re-creation
		setRetainInstance(true);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		User u = mUserList.get(position);
		Log.i("", "in onListItemClick: position = " + position + "  user = "
				+ u.getUserName() + "  id = " + u.getUserId());

		// Start new activity to show the list of photos
		startPhotoListActivity(u.getUserId());

	}

	/*
	 * Start the UserPhotoActivity
	 */
	protected void startPhotoListActivity(int id) {
		Log.i(UserListActivity.TAG, "startPhotoListActivity id = " + id);

		// Create an Intent to call the List Activity
		Intent i = new Intent(getActivity(), PhotoListActivity.class);

		// Pass data to the PhotoListActivity
		i.putExtra(UserListFragment.EXTRA_USER_ID, id);

		startActivity(i);
	}

	@Override
	public void onResume() {
		super.onResume();
		String userAgent = null;

		// Create the HttpClient
		mHttpclient = AndroidHttpClient.newInstance(userAgent);

		// Create a background task to query the server
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
		// TODO should I get rid of onCreateView - don't need to set adapter
		// here
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
		// inflater.getContext(), android.R.layout.simple_list_item_1,
		// numbers_text);
		// setListAdapter(adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/*
	 * Void... is the varargs syntax introduced in Java 1.5 and puts the args
	 * into an array varargs must be the last parameter Params: 1 X input params
	 * X passed as args to doInBackground - returns result Z 2 Y progress Y
	 * passed as args to onProgressUpdate 3 Z results Z passed as args to
	 * onPostExecute
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
				Log.i(UserListActivity.TAG, responseBody);
				return responseBody;
			} catch (Throwable t) {
				Log.i(UserListActivity.TAG, "UserList request failed", t);
			}

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object) Executed
		 * when AsyncTask completes Executes on the Main UI thread so UI updates
		 * go here
		 */
		public void onPostExecute(String result) {
			List<String> adapterList = new LinkedList<String>();
			if (result != null) {
				Log.i(UserListActivity.TAG, result);

				// Get the JSON data from the response
				try {
					JSONArray jList = new JSONArray(result);
					mUserList = new LinkedList<User>();
					for (int i = 0; i < jList.length(); i++) {
						JSONObject u = (JSONObject) jList.get(i);
						// Save the users and ids
						User user = new User(u.getString("name"),
								u.getInt("id"));
						mUserList.add(user);
						// Create a list of user names for the list adapter to
						// use in the display
						adapterList.add(u.getString("name"));
						// Save the data in the db for offline use
						mDb.addUser(new User(user.getUserName(), user
								.getUserId()));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e(UserListActivity.TAG, "Error getting JSON response", e);
				}
			} else {
				// TODO The request failed so try to get the data out of the
				// database
				mUserList = mDb.getAllUsers();
				for (Iterator<User> userIter = mUserList.iterator(); userIter
						.hasNext();) {
					adapterList.add(userIter.next().getUserName());
				}
			}

			// Add the list of users to the UI list
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_1,
					adapterList);
			setListAdapter(adapter);

		}
	}

}
