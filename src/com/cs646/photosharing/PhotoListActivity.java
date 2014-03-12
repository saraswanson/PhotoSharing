package com.cs646.photosharing;

import java.util.UUID;

import android.support.v4.app.Fragment;

public class PhotoListActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		long userId = (long) getIntent().getLongExtra(
				PhotoListFragment.EXTRA_USER_ID, -1);
		return PhotoListFragment.newInstance(userId);
	}
}
