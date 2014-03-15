package com.cs646.photosharing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class PhotoPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);

		// Get the list of Photos passed in from the PhotoListFragment
		final int[] photoList = (int[]) getIntent().getIntArrayExtra(PhotoListFragment.EXTRA_PHOTO_ID_LIST);
		
		// Create the ViewPager Adapter
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
			@Override
			public int getCount() {
				return photoList.length;
			}
			
			// show the photo at the position scrolled to
			@Override
			public Fragment getItem(int pos) {
                int photoId =  photoList[pos];
				return PhotoFragment.newInstance(photoId);
			}
		});
		
		// Get the photo id the user chose from the Intent
		int photoId = (int) getIntent().getIntExtra(
				PhotoListFragment.EXTRA_PHOTO_ID, -1);
		
		// Set the current Activity to view to the photo the
		// user chose in PhotoListFragment
        for (int i = 0; i < photoList.length; i++) {
            if (photoList[i] == photoId) {
                mViewPager.setCurrentItem(i);
                break;
            } 
        }
		
		
	}
}
