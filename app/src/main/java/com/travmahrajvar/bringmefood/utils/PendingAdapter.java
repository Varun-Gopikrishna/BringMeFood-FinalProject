package com.travmahrajvar.bringmefood.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.travmahrajvar.bringmefood.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Travis on 11/12/17.
 */

public class PendingAdapter extends BaseAdapter {
	
	private Context context;
	ArrayList<Wanter> wanters;
	final String currentSession;
	public DatabaseReference mRefWanters;
	
	public PendingAdapter(@NonNull Context context, @NonNull ArrayList<Wanter> objects, String currentSession) {
		this.context = context;
		this.currentSession = currentSession;
		wanters = objects;
	}
	
	@Override
	public int getCount() {
		return wanters.size();
	}
	
	@Override
	public Object getItem(int i) {
		return wanters.get(i);
	}
	
	@Override
	public long getItemId(int i) {
		return i;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Wanter w = wanters.get(position);
		
		if(convertView == null)
			convertView = LayoutInflater.from(context).inflate(R.layout.pending_adapter_view, parent, false);
		
		TextView nameBox = convertView.findViewById(R.id.txtWanterAdapter_name);
		TextView orderListBox = convertView.findViewById(R.id.txtWanterAdapter_orderListSize);
		ImageButton acceptButton = convertView.findViewById(R.id.pendingUsers_accept);
		ImageButton denyButton = convertView.findViewById(R.id.pendingUsers_deny);
		
		nameBox.setText(w.getName());
		
		String wants = "";
		if(w.getOrderList() != null) {
			for (String item : w.getOrderList()) {
				wants += item + ";";
			}
		}
		
		//orderListBox.setText(convertView.getResources().getQuantityString(R.plurals.wanterAdapter_orderSize, w.getOrderList().size(), w.getOrderList().size()));
		orderListBox.setText(wants);
		
		final int pos = position;
		acceptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				removeWanter(pos);
				//TODO add to approved list

				getDBData();
				getCurrentPending();

			}
		});
		
		denyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				removeWanter(pos);
				//TODO send notification to wanter: Denied
			}
		});
		
		return convertView;
	}
	
	public Wanter removeWanter(int position){
		Wanter w = wanters.remove(position);
		notifyDataSetChanged();
		return w;
	}
	
	public void addWanter(Wanter w){
		wanters.add(w);
		notifyDataSetChanged();
	}


	public void getDBData() {
		mRefWanters = FirebaseDatabase.getInstance().getReference().child("getting").child(currentSession).child("wanterlist");

		if (mRefWanters != null) {

			mRefWanters.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					if (dataSnapshot.getValue() != null) {
						Log.i("getdb", "data"+dataSnapshot.getValue());
					}else{
					}
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});
		}
	}

	public void getCurrentPending() {
		mRefWanters = FirebaseDatabase.getInstance().getReference().child("getting").child(currentSession).child("approved");

		if (mRefWanters != null) {

			mRefWanters.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					if (dataSnapshot.getValue() != null) {
						Log.i("getdbapproved", "data"+dataSnapshot.getValue());
					}else{
					}
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});
		}
	}
}
