package com.cs646.photosharing;

import android.support.v4.app.Fragment;
import android.util.Log;

public class PhotoListActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		// Get the user id from the intent and pass it
		// to the Fragment using Fragment Arguments
		int userId = (int) getIntent().getIntExtra(
				UserListFragment.EXTRA_USER_ID, -1);
		if (userId < 0) {
			Log.e(UserListActivity.TAG, "Error, invalid user id sent to PhotoListActivity.");
		}
		return PhotoListFragment.newInstance(userId);
	}
}
