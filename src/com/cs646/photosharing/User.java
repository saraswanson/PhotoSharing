package com.cs646.photosharing;

public class User {
	private String mName;
	private int mId;
	
	public User (String n, int i) {
		this.mName = n;
		this.mId = i;
	}
	
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}
	public int getId() {
		return mId;
	}
	public void setId(int id) {
		mId = id;
	}
	
}
