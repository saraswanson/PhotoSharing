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

public class PhotoListFragment extends ListFragment {
	HttpClient mHttpclient;
	List<Photo> mPhotoList;
	int mUserId;
	public static final String EXTRA_PHOTO_ID = "com.cs646.android.UISampler.photo_id";
	public static final String EXTRA_PHOTO_ID_LIST = "com.cs646.android.UISampler.photo_id_list";
	public static final int USER_PHOTO_CODE = 0;
	private PhotoSQLiteHelper mDb;

	/*
	 * Create and instance of the Fragment pass the userId to the Fragment using
	 * Fragment Arguments
	 */
	public static PhotoListFragment newInstance(int userId) {
		Bundle args = new Bundle();
		args.putInt(UserListFragment.EXTRA_USER_ID, userId);

		PhotoListFragment fragment = new PhotoListFragment();
		fragment.setArguments(args);

		return fragment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle) Called
	 * when the Fragment is created Get the mUserId passed from the calling
	 * activity by using Fragment Arguments
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Save the userId chosen from the user list, later query the photos for
		// that user
		mUserId = (int) getArguments().getInt(UserListFragment.EXTRA_USER_ID);

		// Get the SQLite database to store users and photo ids
		PhotoSharingApplication myApplication = (PhotoSharingApplication) getActivity()
				.getApplication();
		mDb = myApplication.getDatabase();

		// Retain the fragment across the activity's re-creation
		setRetainInstance(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView
	 * , android.view.View, int, long) Called when a list item is clicked
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Photo p = mPhotoList.get(position);
		Log.i("", "in onListItemClick: position = " + position + "  photo = "
				+ p.getPhotoName() + "  id = " + p.getPhotoId());

		startPhotoActivity(p.getPhotoId());
	}

	/*
	 * Start the UserPhotoActivity
	 */
	protected void startPhotoActivity(int id) {
		Log.i(UserListActivity.TAG, "startPhotoActivity id = " + id);

		// Create an Intent to call the List Activity
		Intent i = new Intent(getActivity(), PhotoPagerActivity.class);

		// Pass data to the PhotoListActivity
		i.putExtra(PhotoListFragment.EXTRA_PHOTO_ID, id);

		// Swiping requires an array of all photo ids
		int[] idList = new int[mPhotoList.size()];
		for (int j = 0; j < mPhotoList.size(); j++) {
			idList[j] = mPhotoList.get(j).getPhotoId();
		}
		i.putExtra(PhotoListFragment.EXTRA_PHOTO_ID_LIST, idList);

		startActivity(i);
	}

	@Override
	public void onResume() {
		super.onResume();
		String userAgent = null;
		mHttpclient = AndroidHttpClient.newInstance(userAgent);
		FetchPhotoListTask task = new FetchPhotoListTask();
		String url = "http://bismarck.sdsu.edu/photoserver/userphotos/"
				+ mUserId;
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
	class FetchPhotoListTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			HttpGet getMethod = new HttpGet(urls[0]);
			try {
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = mHttpclient.execute(getMethod,
						responseHandler);
				Log.i(UserListActivity.TAG, responseBody);
				return responseBody;
			} catch (Throwable t) {
				Log.i(UserListActivity.TAG, "PhotoList request failed", t);
				// TODO if the connection fails then look up data in the db
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
					JSONArray pList = new JSONArray(result);
					mPhotoList = new LinkedList<Photo>();
					for (int i = 0; i < pList.length(); i++) {
						JSONObject p = (JSONObject) pList.get(i);
						// Save the users and ids
						Photo photo = new Photo(p.getString("name"),
								p.getInt("id"));
						mPhotoList.add(photo);
						// Create a list of user names for the list adapter to
						// use in the display
						adapterList.add(p.getString("name"));
						// Save the data in the db for offline use
						mDb.addPhoto(new Photo(photo.getPhotoName(), photo
								.getPhotoId()));
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.e(UserListActivity.TAG, "Error getting JSON response",
							e);
				}
			} else {
				// TODO The request failed so try to get the data out of the
				// database
				mPhotoList = mDb.getAllPhotos();
				for (Iterator<Photo> photoIter = mPhotoList.iterator(); photoIter
						.hasNext();) {
					adapterList.add(photoIter.next().getPhotoName());
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
