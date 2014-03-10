package com.cs646.photosharing;

import android.support.v4.app.Fragment;

public class PhotoListActivity extends SingleFragmentActivity {

	@Override
    public Fragment createFragment() {
        return new PhotoListFragment();
    }
}
