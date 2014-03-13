package com.cs646.photosharing;

import android.support.v4.app.Fragment;

public class PhotoActivity extends SingleFragmentActivity {

	@Override
    public Fragment createFragment() {
		// Get the photo id from the intent and pass it
		// to the Fragment using Fragment Arguments
		int photoId = (int) getIntent().getIntExtra(
				PhotoListFragment.EXTRA_PHOTO_ID, -1);
		return PhotoFragment.newInstance(photoId);
    }
}
