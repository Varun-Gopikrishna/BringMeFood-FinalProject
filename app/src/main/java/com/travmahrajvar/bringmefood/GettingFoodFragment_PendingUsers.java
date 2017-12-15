package com.travmahrajvar.bringmefood;

import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.travmahrajvar.bringmefood.utils.PendingAdapter;
import com.travmahrajvar.bringmefood.utils.Wanter;
import com.travmahrajvar.bringmefood.utils.WanterAdapter;

import java.util.ArrayList;


/**
 * (Automatically generated by Android Studio)
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GettingFoodFragment_PendingUsers.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GettingFoodFragment_PendingUsers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GettingFoodFragment_PendingUsers extends Fragment {
	
	public GettingFoodFragment_PendingUsers() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment GettingFoodFragment_PendingUsers.
	 */
	// TODO: Rename and change types and number of parameters
	public static GettingFoodFragment_PendingUsers newInstance() {
		GettingFoodFragment_PendingUsers fragment = new GettingFoodFragment_PendingUsers();
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_getting_food_pendingusers, container, false);
		
		ListView approvedList = root.findViewById(R.id.listPendingUsers);
		
		//TODO get make an actual list of users from Firebase
		ArrayList<Wanter> wanters = new ArrayList<Wanter>();
		
		ArrayList<String> w1List = new ArrayList<>();
		w1List.add("Aaa");
		w1List.add("Bbb");
		
		ArrayList<String> w2List = new ArrayList<>();
		w2List.add("Ccc");
		
		wanters.add(new Wanter("Joe", "", w1List));
		wanters.add(new Wanter("Jen", "", w2List));
		
		approvedList.setAdapter(new PendingAdapter(container.getContext(), wanters));
		
		return root;
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (!(context instanceof OnFragmentInteractionListener)) {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
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
