package com.cs646.photosharing;

import android.app.Application;

public class PhotoSharingApplication extends Application {
	    private PhotoSQLiteHelper mDb;

	    @Override
	    public void onCreate() {
			// create the SQLite database to store users and photo ids
	        mDb = new PhotoSQLiteHelper(getApplicationContext());
	    }
	    
	    public PhotoSQLiteHelper getDatabase() {
	    	return mDb;
	    }
	
}
