package com.cs646.photosharing;

public class Photo {
	private int mPhotoId;
	private String mPhotoName;
	
	
	public Photo() {
		
	}
	
	public Photo (String n, int i) {
		super();
		this.mPhotoName = n;
		this.mPhotoId = i;
	}

	public String getPhotoName() {
		return mPhotoName;
	}
	public void setPhotoName(String name) {
		mPhotoName = name;
	}
	public int getPhotoId() {
		return mPhotoId;
	}
	public void setPhotoId(int id) {
		mPhotoId = id;
	}
	
    @Override
    public String toString() {
        return "Photo [photoId=" + mPhotoId + ", photoName=" + mPhotoName
                + "]";
    }
}
