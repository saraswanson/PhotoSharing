package com.cs646.photosharing;

import org.apache.http.client.HttpClient;

import android.app.Application;
import android.net.http.AndroidHttpClient;

public class PhotoSharingApplication extends Application {
	    private PhotoSQLiteHelper mDb;
	    private HttpClient mHttpClient;

	    @Override
	    public void onCreate() {
			// create the SQLite database to store users and photo ids
	    	// creating it here will allow all activities to use it
	        mDb = new PhotoSQLiteHelper(getApplicationContext());
	        
	        // Create the HttpClient once for reuse
			String userAgent = null;
			mHttpClient = AndroidHttpClient.newInstance(userAgent);
	    }
	    
	    public PhotoSQLiteHelper getDatabase() {
	    	return mDb;
	    }
	    
	    public HttpClient getHttpClient() {
	    	return mHttpClient;
	    }
	
}
