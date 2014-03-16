package com.cs646.photosharing;

import android.support.v4.app.Fragment;

public class UserListActivity extends SingleFragmentActivity {
	public static final String TAG = "PhotoSharing";

	@Override
    public Fragment createFragment() {
        return new UserListFragment();
    }
}
