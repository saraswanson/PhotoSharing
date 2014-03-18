package com.cs646.photosharing;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PhotoSQLiteHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "PhotoDB";
	private static final String TABLE_USERS = "users"; // users table name
	private static final String KEY_USERID = "userid"; // users column userid
	private static final String KEY_USERNAME = "username"; // users column name
	private static final String[] USERS_COLUMNS = { KEY_USERID, KEY_USERNAME };
	private static final String TABLE_PHOTOLIST = "photolist"; // photolist
														
	private static final String KEY_PHOTOID = "photoid"; // photolist column
	private static final String KEY_PHOTONAME = "photoname"; // photolist column name
	//private static final String[] PHOTOLIST_COLUMNS = { KEY_PHOTOID, KEY_PHOTONAME };
	SQLiteDatabase mDb;

	public PhotoSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mDb = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create user table
		String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users ( "
				+ "userid INTEGER PRIMARY KEY, " + "username TEXT )";

		// create users table
		db.execSQL(CREATE_USERS_TABLE);

		// SQL statement to create user table
		String CREATE_PHOTOLIST_TABLE = "CREATE TABLE photolist ( "
				+ "photoid INTEGER PRIMARY KEY, " +  "photoname TEXT )";

		// create users table
		db.execSQL(CREATE_PHOTOLIST_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older users table if existed
		db.execSQL("DROP TABLE IF EXISTS users");
		db.execSQL("DROP TABLE IF EXISTS photolist");

		// create fresh users table
		this.onCreate(db);
	}

	public void addUser(User user) {
		// for logging
		Log.d("addUser", user.toString());

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_USERID, user.getUserId()); // get userid
		values.put(KEY_USERNAME, user.getUserName()); // get username
		// 3. insert
		db.replace(TABLE_USERS, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
							// values
		// 4. close
		db.close();
	}

	public void addPhoto(Photo photo) {
		// for logging
		Log.d("addUser", photo.toString());

		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_PHOTOID, photo.getPhotoId()); // get photoid
		values.put(KEY_PHOTONAME, photo.getPhotoName()); // get photoname
		// 3. insert
		db.replace(TABLE_PHOTOLIST, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
							// values
		// 4. close
		db.close();
	}
	
    public User getUser(int id){
    	 
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
 
        // 2. build query
        Cursor cursor = 
                db.query(TABLE_USERS, // a. table
                USERS_COLUMNS, // b. column names
                " id = ?", // c. selections 
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
 
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
 
        // 4. build user object
        User user = new User();
        user.setUserId(Integer.parseInt(cursor.getString(0)));
        user.setUserName(cursor.getString(1));
 
        Log.d("getUser("+id+")", user.toString());
 
        // 5. return user
        return user;
    }
 
    // Get All Users
    public List<User> getAllUsers() {
        List<User> users = new LinkedList<User>();
 
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_USERS;
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build user and add it to list
        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setUserId(Integer.parseInt(cursor.getString(0)));
                user.setUserName(cursor.getString(1));
 
                // Add user to users
                users.add(user);
            } while (cursor.moveToNext());
        }
 
        Log.d("getAllUsers()", users.toString());
 
        // return users
        return users;
    }
    
    // Get All Users
    public List<Photo> getAllPhotos() {
        List<Photo> photos = new LinkedList<Photo>();
 
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_PHOTOLIST;
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build user and add it to list
        Photo photo = null;
        if (cursor.moveToFirst()) {
            do {
                photo = new Photo();
                photo.setPhotoId(Integer.parseInt(cursor.getString(0)));
                photo.setPhotoName(cursor.getString(1));
 
                // Add user to users
                photos.add(photo);
            } while (cursor.moveToNext());
        }
 
        Log.d("getAllUsers()", photos.toString());
 
        // return users
        return photos;
    }
}
