package com.cs646.photosharing;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PhotoFragment extends Fragment {
	private static final String TAG = "PhotoSharing";
	HttpClient mHttpclient;
	long mPhotoId;
	ImageView mPhotoView;

	/*
	 * Create and instance of the Fragment pass the userId to the Fragment using
	 * Fragment Arguments
	 */
	public static PhotoFragment newInstance(long photoId) {
		Bundle args = new Bundle();
		args.putLong(PhotoListFragment.EXTRA_PHOTO_ID, photoId);

		PhotoFragment fragment = new PhotoFragment();
		fragment.setArguments(args);

		return fragment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle) Called
	 * when the Fragment is created Get the mPhotoId passed from the calling
	 * activity by using Fragment Arguments
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPhotoId = (long) getArguments().getLong(
				PhotoListFragment.EXTRA_PHOTO_ID);

		// Retain the fragment across the activity's re-creation
		setRetainInstance(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume() Called right before UI is
	 * displayed Fetch the photo
	 */
	@Override
	public void onResume() {
		super.onResume();
		String userAgent = null;
		mHttpclient = AndroidHttpClient.newInstance(userAgent);
		FetchPhotoTask task = new FetchPhotoTask();
		String url = "http://bismarck.sdsu.edu/photoserver/photo/" + mPhotoId;
		task.execute(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onPause() Shutdown the http
	 * connection when the Fragment is paused
	 */
	@Override
	public void onPause() {
		super.onPause();
		mHttpclient.getConnectionManager().shutdown();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle) Create the ImageView to
	 * display the photo
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.photo_fragment, container, false);
		mPhotoView = (ImageView)v.findViewById(R.id.photo2View);
				
		return v;
	}

	/*
	 * Void... is the varargs syntax introduced in Java 1.5 and puts the args
	 * into an array. varargs must be the last parameter Params: 1 X input
	 * params X passed as args to doInBackground - returns result Z 2 Y progress
	 * Y passed as args to onProgressUpdate 3 Z results Z passed as args to
	 * onPostExecute
	 */
	class FetchPhotoTask extends AsyncTask<String, Void, Bitmap> {
		protected Bitmap doInBackground(String... urls) {
			// HttpGet getMethod = new HttpGet(urls[0]);
			// try {
			// ResponseHandler<String> responseHandler = new
			// BasicResponseHandler();
			// String responseBody = mHttpclient.execute(getMethod,
			// responseHandler);
			// Log.i(TAG, responseBody);
			// return responseBody;
			// } catch (Throwable t) {
			// Log.i(TAG, "Photo request failed", t);
			// }

			try {
				Bitmap downloadBitmap = downloadBitmap(urls[0]);
				// Updates the user interface
				return downloadBitmap;
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		// Utiliy method to download image from the internet
		private Bitmap downloadBitmap(String url) throws IOException {
			HttpGet request = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				byte[] bytes = EntityUtils.toByteArray(entity);

				Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
						bytes.length);
				return bitmap;
			} else {
				throw new IOException("Download failed, HTTP response code "
						+ statusCode + " - " + statusLine.getReasonPhrase());
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object) Executed
		 * when AsyncTask completes Executes on the Main UI thread so UI updates
		 * go here
		 */
		public void onPostExecute(Bitmap result) {
			Log.i(TAG, "In PhotoFragment:onPostExecute");

			// result is the image/jpeg
			mPhotoView.setImageBitmap(result);
			
			// close the connection
			mHttpclient.getConnectionManager().shutdown();
		}
	}

}
