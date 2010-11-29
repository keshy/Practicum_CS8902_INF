/*********************************************************************

    File          : FriendFinder.java
    Author(s)     : enck
    Description   : description

    Copyright (c) 2008 The Pennsylvania State University
    Systems and Internet Infrastructure Security Laboratory

**********************************************************************/

package org.siislab.tutorial.friendtracker;

import java.util.Random;

import org.siislab.tutorial.provider.FriendProvider.FriendContent;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

/**
 * @author enck
 *
 * This is just a mock friend finder. A real one would contact an external web service
 */
public class FriendFinder extends Thread {
	
	private Context mContext;
	private ContentResolver mResolver;

	private static final int LATITUDE = 0;
	private static final int LONGITUDE= 1;

	// Enter the following in the URL bar when in google maps to find coordinates
	// -> javascript:void(prompt('',gApplication.getMap().getCenter()));
	static double[][] mInitLocations = {
			{38.889462878011365, -77.03518867492676}, // Washington monument
			{38.88922905292896, -77.04984426498413}, // Lincoln Memorial
			{38.881729540325594, -77.0365834236145}, // Jefferson Memorial
			{38.88962989545612, -77.02298998832703}, // Middle of The National Mall
	};
	
	static String[] mNickLookup = { "george", "john", "thomas", "andrew" };
	
	FriendFinder(Context ctx) {
		mContext = ctx;
		mResolver = mContext.getContentResolver();
	}
	
	public void run() {
		int count = mNickLookup.length;
	
		ContentValues values = new ContentValues();
		for (int i=0; i<count; i++) {
			values.clear();
			
			double lat = fuzz(mInitLocations[i][LATITUDE]);
			double lon = fuzz(mInitLocations[i][LONGITUDE]);
			
			values.put(FriendContent.Location.ACTIVE, 1);
			values.put(FriendContent.Location.LATITUDE, lat);
			values.put(FriendContent.Location.LONGITUDE, lon);
	
			String where = FriendContent.Location.NICK + "=?";
			mResolver.update(FriendContent.Location.CONTENT_URI, values, 
					where, new String[] {mNickLookup[i]});
		}
		//Log.i(FriendTracker.TAG, "Friend locations updated");
	}
	
	private double fuzz(double coord) {
		Random r = new Random();
		double fz = r.nextInt(100) / 100000.0;
		
		return coord + fz;
	}
}
