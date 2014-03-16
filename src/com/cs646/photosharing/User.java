package com.cs646.photosharing;

public class User {
	private int mUserId;
	private String mUserName;
	
	public User (String n, int i) {
		super();
		this.mUserName = n;
		this.mUserId = i;
	}
	
	public User() {
	}

	public String getUserName() {
		return mUserName;
	}
	public void setUserName(String name) {
		mUserName = name;
	}
	public int getUserId() {
		return mUserId;
	}
	public void setUserId(int id) {
		mUserId = id;
	}
	
    @Override
    public String toString() {
        return "Photo [userId=" + mUserId + ", userName=" + mUserName
                + "]";
    }
}
