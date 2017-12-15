package com.travmahrajvar.bringmefood.utils;

import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Travis on 07/11/17.
 */

public class FirebaseHandler {
	
	/** The app's connection to the Firebase authentication methods */
	private static FirebaseAuth fbAuthenticator;

	/** The app's connection to the Firebase database */
	private static FirebaseDatabase fbDatabase;
	private static DatabaseReference fbDatabaseReference;
    static final ArrayList<String> mGamesPlaying = new ArrayList<String>();
	/**
	 * Starts this app's connection to Firebase.
	 */
	public static void initialize(){
		fbDatabase = FirebaseDatabase.getInstance();
		fbDatabaseReference = fbDatabase.getReference();
		fbAuthenticator = FirebaseAuth.getInstance();
		//fbAuthenticator.signOut();
	}
	
	//region Authentication
	
	/**
	 * Gets the currently authenticated user, if signed in.
	 * @return The currently authenticated user, null otherwise
	 */
	public static FirebaseUser getCurrentUser(){
		return fbAuthenticator.getCurrentUser();
	}
	
	/**
	 * Signs in a user to their appropriate account using their OAuth2 token.
	 * @param credential The OAuth2 token received from by the sign-in providers
	 * @return A Task that will perform the sign in to Firebase
	 */
	public static Task<AuthResult> signInWithCredentials(@NonNull AuthCredential credential){
		return fbAuthenticator.signInWithCredential(credential);
	}
	
	/**
	 * Creates a new user in the Firebase authentication database.
	 * @param email The email address to use for this account
	 * @param password The password to use for this account
	 * @return A Task that will perform the account creation and sign in to Firebase
	 */
	public static Task<AuthResult> createUserWithEmailAndPassword(@NonNull String email, @NonNull String password){
		return fbAuthenticator.createUserWithEmailAndPassword(email, password);
	}
	
	/**
	 * Signs in a user using their email and password, if they've made an account already.
	 * @param email The email address associated with this account
	 * @param password The password associated with this account
	 * @return A Task that will perform the sign in to Firebase
	 */
	public static Task<AuthResult> signInWithEmailAndPassword(@NonNull String email, @NonNull String password){
		return fbAuthenticator.signInWithEmailAndPassword(email, password);
	}
	
	/**
	 * Signs out the currently authenticated user.
	 */
	public static void signOutCurrentUser(){
		if(getCurrentUser() != null){
			fbAuthenticator.signOut();
		}
	}
	
	//endregion
	
	//region Database
	
	/**
	 * Puts the newly created user's name and email into the public listing of users.
	 *
	 * @param fbUser The Firebase user object (should only be called when a user profile is created)
	 */
	public static void putPublicUserInfoInDatabase(FirebaseUser fbUser){
		UserInfo user = UserInfo.createFromFirebaseUser(fbUser);
		fbDatabaseReference.child("users").child(fbUser.getUid()).setValue(user);
		updateUserDeviceToken(getUserDeviceToken());
	}

	/**
	 * Add food items to user
	 */
	public static void addRequiredFoodList(ArrayList<String> foodList, String deliveryLoc){
		//goto current user id to put the foodList
		fbDatabaseReference.child("users").child(getCurrentUser().getUid()).child("foodlist").setValue(foodList);
		fbDatabaseReference.child("users").child(getCurrentUser().getUid()).child("deliverylocation").setValue(deliveryLoc);
	}

	/**
	 * Update approved list array
	 */
	public static void updateApprovedList(ArrayList<String> approvedList, String currentSession){
		fbDatabaseReference.child("getting").child(currentSession).child("approved").setValue(approvedList);
	}

	/**
	 * Add food items to user
	 */
	public static void updateFriendList(ArrayList<String> friend){

		//goto current user id to put the foodList
		fbDatabaseReference.child("users").child(getCurrentUser().getUid()).child("friendlist").setValue(friend);
	}

	public static void updatePrice(int price){
        fbDatabaseReference.child("users").child(getCurrentUser().getUid()).child("foodlist").child("price").setValue(price);
    }


    public static void transaction(final int p,final String uid) {
       // fbDatabaseReference.child("users").child(getCurrentUser().getUid()).child("balance").setValue(getBalance()+p);

	    if (uid.equals("null")){
            fbDatabaseReference.child("users").child(getCurrentUser().getUid()).child("balance").setValue((getBalance()+p));
        }else{
            fbDatabaseReference.child("users").child(getCurrentUser().getUid()).child("balance").setValue((getBalance()+p));
            fbDatabaseReference.child("users").child(uid).child("balance").setValue((getBalance()-p));
        }

    }


    public static int getBalance(){
        final int[] prices = new int[1];

        //public ArrayList<String> mGamesPlaying(final String uid, final Callable refresh) {

        //}

        fbDatabaseReference.child("users").child(getCurrentUser().getUid()).child("balance").addValueEventListener(new ValueEventListener() {

            String balance;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGamesPlaying.clear();
                showbalance(dataSnapshot);

                if (dataSnapshot.exists()) {
                    balance = dataSnapshot.getValue().toString();
                    System.out.println("parsed" + Integer.parseInt(balance));
                   // mGamesPlaying.add(balance);
                    //orderprice.notifyAll();
                    prices[0] = Integer.parseInt(balance);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //System.out.println("price"+mGamesPlaying.get(0).toString());
        return prices[0];
    }

    private static void showbalance(DataSnapshot dataSnapshot) {

    }


    /**
	 *
	 */
	public static void updateAgentWantList(ArrayList<String> wanterlist, String sessionID){
		//goto current agent id to put the wantlist
		fbDatabaseReference.child("getting").child(sessionID).child("wanterlist").setValue(wanterlist);
	}
	

	/**
	 * Creates a new food-getting session for the currently authenticated user.
	 *
	 * @param restaurant The restaurant the user is going to
	 * @param location The location of the restaurant the user is going to
	 * @return The UID of the new food-getting session
	 */
	public static String createGettingFoodSession(String restaurant, String location, String getterName){
		DatabaseReference dbr = fbDatabaseReference.child("getting");
		String key = dbr.push().getKey();
		removePreviousGettingFoodSessions(key);

		Map<String, Object> newGetFoodSession = new HashMap<>();
		Map<String, Object> newGetFoodSession_children = new HashMap<>();
		
		newGetFoodSession_children.put("getter", getCurrentUser().getUid());
		newGetFoodSession_children.put("restaurant", restaurant);
		newGetFoodSession_children.put("location", location);
		newGetFoodSession_children.put("deviceTok", getUserDeviceToken());
		newGetFoodSession_children.put("gettername", getterName);
		
		newGetFoodSession.put("/" + key + "/", newGetFoodSession_children);
		
		dbr.updateChildren(newGetFoodSession);
		
		return key;
	}
	
	public static String getUserDeviceToken(){
		return FirebaseInstanceId.getInstance().getToken();
	}
	
	public static void updateUserDeviceToken(String updatedToken){
		if(FirebaseHandler.getCurrentUser() != null) {
			DatabaseReference dbr = fbDatabaseReference.child("users").child(FirebaseHandler.getCurrentUser().getUid());
			Map<String, Object> map = new HashMap<>();
			map.put("/deviceToken", updatedToken);
			dbr.updateChildren(map);
		}
	}
	
	/**
	 * Removes any possible previous session codes the current user may have,
	 *  so that when friends query if this user is getting food,
	 *  no leftover sessions come up instead of the current one.
	 */
	private static void removePreviousGettingFoodSessions(final String generatedKey){
		fbDatabaseReference.child("getting").orderByChild("getter").equalTo(getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for(DataSnapshot snapshot : dataSnapshot.getChildren()){
					if(!snapshot.getKey().equals(generatedKey))
						snapshot.getRef().removeValue();
				}
			}
			
			@Override
			public void onCancelled(DatabaseError databaseError) { }
		});
	}
	
	
	/**
	 * Removes all previous session codes that the current user has.
	 * Should be called when the food getting session is complete
	 *  and everyone is happily eating the food they were brought.
	 */
	public static void removeAllGettingFoodSessions(){
		fbDatabaseReference.child("getting").orderByChild("getter").equalTo(getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
					snapshot.getRef().removeValue();
				}
			}
			
			@Override
			public void onCancelled(DatabaseError databaseError) { }
		});
	}
	
	
	//endregion
	
}
