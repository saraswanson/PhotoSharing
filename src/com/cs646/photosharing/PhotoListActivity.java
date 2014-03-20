package com.cs646.photosharing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class PhotoListActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photolist_fragment);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
        }
    }
    
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
