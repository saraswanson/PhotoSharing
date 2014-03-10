package com.cs646.photosharing;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserAdapter extends ArrayAdapter<User> {
	private Context mContext;
	private ArrayList<User> mUsers;
	private int mLayoutResourceId;
	
	public UserAdapter(Context context, int layoutResourceId, ArrayList<User> users) {
		super(context, layoutResourceId, users);
		this.mContext = context;
		this.mLayoutResourceId = layoutResourceId;
		this.mUsers = users;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
		}
		
		User i = mUsers.get(position);
		
// TODO commented out because there's an error.  Not sure what to do here.
//		if (i != null) {
//			TextView tt = (TextView) row.findViewById(R.id.toptext);
//			if (tt != null) {
//				tt.setText(i.getName());
//			}
//		}
		
		return row;
	}
}
