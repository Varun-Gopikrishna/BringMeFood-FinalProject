package com.travmahrajvar.bringmefood;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.travmahrajvar.bringmefood.utils.Wanter;
import com.travmahrajvar.bringmefood.utils.WanterAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * (Automatically generated by Android Studio)
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GettingFoodFragment_ApprovedUsers.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GettingFoodFragment_ApprovedUsers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GettingFoodFragment_ApprovedUsers extends Fragment {
	//Argument names for the incoming intents
	private static final String ARG_SESSION_KEY = "key";
	
	//The arguments themselves
	private String sessionKey;
	
	WanterAdapter wanterAdapter;
	ArrayList<Wanter> wanters;
	private DatabaseReference refApprovedUsers;
	
	public GettingFoodFragment_ApprovedUsers() {
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment GettingFoodFragment_ApprovedUsers.
	 */
	// TODO: Rename and change types and number of parameters
	public static GettingFoodFragment_ApprovedUsers newInstance(String sessionKey) {
		GettingFoodFragment_ApprovedUsers fragment = new GettingFoodFragment_ApprovedUsers();
		
		
		//Get the intent extras and put them into arguments
		Bundle args = new Bundle();
		args.putString(ARG_SESSION_KEY, sessionKey);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) { //Get and set the arguments
			sessionKey = getArguments().getString(ARG_SESSION_KEY);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_getting_food_approvedusers, container, false);
		ListView approvedList = root.findViewById(R.id.listApprovedUsers);
		
		wanters = new ArrayList<Wanter>();
		wanters.clear();
		wanterAdapter = new WanterAdapter(getContext(), wanters);
		
		
		//Get the arguments from the parent activity
		Bundle args = getArguments();
		sessionKey = args.getString(ARG_SESSION_KEY);
		refApprovedUsers = FirebaseDatabase.getInstance().getReference().child("getting").child(sessionKey).child("approved");
		
		if(refApprovedUsers != null){
			refApprovedUsers.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					wanters.clear();
					if(dataSnapshot.getValue() != null){
						Log.i("dataSnapshot", dataSnapshot.getValue().toString());
						Log.i("dataSnapshot class", dataSnapshot.getValue().getClass().toString());
						if(dataSnapshot.getValue() instanceof ArrayList)
							updateWanterAdapter((ArrayList<String>) dataSnapshot.getValue());
						else if(dataSnapshot.getValue() instanceof Map){
							ArrayList<String> users = new ArrayList<>();
							Map<String, Object> m = (Map<String, Object>)dataSnapshot.getValue();
							for(Object o : m.values())
								users.add(o.toString());
							updateWanterAdapter(users);
						}
					}
				}
				
				@Override
				public void onCancelled(DatabaseError databaseError) { }
			});
		}
		
		approvedList.setAdapter(wanterAdapter);
		
		return root;
	}
	
	private void updateWanterAdapter(ArrayList<String> newWantersList){
		wanters.clear();
		for(String entry : newWantersList){
			try {
				if(entry != null) {
					FirebaseDatabase.getInstance().getReference().child("users").child(entry)
							.addListenerForSingleValueEvent(new ValueEventListener() {
								@Override
								public void onDataChange(DataSnapshot dataSnapshot) {
									if (dataSnapshot.getValue() instanceof Map) {
										Map<String, Object> wanterInfo = (Map<String, Object>) dataSnapshot.getValue();
										Log.i("wanterInfo", wanterInfo.toString());
										Wanter w = new Wanter(wanterInfo.get("name").toString(), wanterInfo.get("uid").toString());
										ArrayList<String> foodList = (ArrayList<String>) wanterInfo.get("foodlist");
										w.setOrderList(foodList);
										
										wanterAdapter.addWanter(w);
										wanterAdapter.notifyDataSetChanged();
									}
								}
								
								@Override
								public void onCancelled(DatabaseError databaseError) {
								}
							});
				}
			} catch(Exception e){
				Crashlytics.logException(e);
			}
		}
		
		//wanterAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (!(context instanceof OnFragmentInteractionListener)) {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	void requestPayment(View view){
		System.out.println("HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
}
