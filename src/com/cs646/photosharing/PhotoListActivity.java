package com.cs646.photosharing;

import android.support.v4.app.Fragment;

public class PhotoListActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		// Get the user id from the intent and pass it
		// to the Fragment using Fragment Arguments
		long userId = (long) getIntent().getLongExtra(
				UserListFragment.EXTRA_USER_ID, -1);
		return PhotoListFragment.newInstance(userId);
	}
}
