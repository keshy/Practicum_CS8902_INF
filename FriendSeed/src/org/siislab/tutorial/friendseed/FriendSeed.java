package org.siislab.tutorial.friendseed;

import org.siislab.tutorial.provider.FriendProvider.FriendContent;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;

public class FriendSeed extends Activity {
	private static final String TAG = "FriendSeed";

	private static final String[] mNames = {
		"George Washington",
		"Thomas Jefferson",
		"John Adams",
		"Andrew Jackson"
	};
	
	private static final String[] mNicks = {
		"george",
		"thomas",
		"john",
		"andrew"
	};
	
	private static final String[] mPhone = {
		"555-555-0001",
		"555-555-0002",
		"555-555-0003",
		"555-555-0004"
	};
	
	private String[] mId = { null, null, null, null};
	
	private ContentResolver mResolver;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mResolver = getContentResolver();
        seedContacts();
        seedFriends();
    }
    
    private void seedContacts() {
    	int count = mNames.length;
    	
    	for (int i=0; i<count; i++) {
    		Log.i(TAG, "Starting index " + i);
	    	ContentValues values = new ContentValues();
    		values.put(Contacts.People.NAME, mNames[i]);
    		
    		Uri person = mResolver.insert(Contacts.People.CONTENT_URI, values);
    		if (person != null) {
	    		mId[i] = person.getLastPathSegment();
	    		Log.i(TAG, "Successfully added index " + i + " as ID " + mId[i]);
	    		
	    		values.clear();
	    		values.put(Contacts.Phones.PERSON_ID, mId[i]);
	    		values.put(Contacts.Phones.NUMBER, mPhone[i]);
	    		values.put(Contacts.Phones.TYPE, Contacts.Phones.TYPE_MOBILE);
    		
	    		Uri phone = mResolver.insert(Contacts.Phones.CONTENT_URI, values);
	    		if (phone == null) {
	    			Log.e(TAG, "Failed to insert phone number for index " + i);
	    		}
    		} else {
    			Log.e(TAG, "Failed to insert person for index " + i);
    		}
    	}
    }
    
    private void seedFriends() {
    	int count = mNames.length;
    	
    	for (int i=0; i<count; i++) {
    		ContentValues values = new ContentValues();
    		
    		values.put(FriendContent.Location.NICK, mNicks[i]);
    		values.put(FriendContent.Location.CONTACTS_ID, mId[i]);
    		
    		Uri uri = mResolver.insert(FriendContent.Location.CONTENT_URI, values);
    		if (uri == null) {
    			Log.e(TAG, "Error adding location tracker for index " + i);
    		}
    	}
    }
   
}