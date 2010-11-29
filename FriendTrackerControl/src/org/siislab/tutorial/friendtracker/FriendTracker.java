/*********************************************************************

    File          : FriendTracker.java
    Author(s)     : enck
    Description   : description

    Copyright (c) 2008 The Pennsylvania State University
    Systems and Internet Infrastructure Security Laboratory

**********************************************************************/

package org.siislab.tutorial.friendtracker;

import org.siislab.tutorial.interfaces.IFriendTracker;
import org.siislab.tutorial.provider.FriendProvider.FriendContent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * @author enck
 *
 * Slightly modeled after the MailService in Android bundled Email application
 */
public class FriendTracker extends Service {
	
	public static final String TAG = "FriendTracker";
	public static final String ACTION_FRIEND_NEAR = "org.siislab.tutorial.action.FRIEND_NEAR";
	
	private static final String ACTION_START_TRACKING = "org.siislab.tutorial.action.START_TRACKING";
	private static final String ACTION_STOP_TRACKING = "org.siislab.tutorial.action.STOP_TRACKING";
	private static final String ACTION_POLL_LOCATIONS = "org.siislab.tutorial.action.POLL_LOCATIONS";
	
	private static final String PERMISSION_FRIEND_SERVICE_ADD = "org.siislab.tutorial.permission.FRIEND_SERVICE_ADD";
	
	private static final int POLL_INTERVAL = 10 * 1000; // every 10 seconds
	
    // Distance threshold for friend matches (in meters)
	private double mDistThreshold = 500.0; 
	
	// Tracking status flag
	private boolean mTracking = false;
	
	
	public static void actionStartTracking(Context context) {
		Intent i = new Intent();
		i.setClass(context, FriendTracker.class);
		i.setAction(FriendTracker.ACTION_START_TRACKING);
		context.startService(i);
	}
	
	public static void actionStopTracking(Context context) {
		Intent i = new Intent();
		i.setClass(context, FriendTracker.class);
		i.setAction(FriendTracker.ACTION_STOP_TRACKING);
		context.startService(i);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		if (ACTION_START_TRACKING.equals(intent.getAction())) {
			mTracking = true;
			updateFriendLocations();
			scheduleTracking();
			
			// Start tracking this phone
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocListener);
		}
		else if (ACTION_STOP_TRACKING.equals(intent.getAction())) {
			mTracking = false;
			cancelTracking();
			deactivateProvider();
			
			// Stop tracking this phone
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			lm.removeUpdates(mLocListener);
		
			// The service no longer needs to run
			stopSelf();
		}
		else if (ACTION_POLL_LOCATIONS.equals(intent.getAction())) {
			updateFriendLocations();
			scheduleTracking();
		}
		
	}
	
	private void scheduleTracking() {
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent();
		i.setClass(this, FriendTracker.class);
		i.setAction(ACTION_POLL_LOCATIONS);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
				SystemClock.elapsedRealtime() + POLL_INTERVAL, pi);
	}
	
	private void cancelTracking() {
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent();
		i.setClass(this, FriendTracker.class);
		i.setAction(ACTION_POLL_LOCATIONS);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		am.cancel(pi);
	}
	
	private void deactivateProvider() {
		ContentValues values = new ContentValues();
		values.put(FriendContent.Location.ACTIVE, 0);
		getContentResolver().update(FriendContent.Location.CONTENT_URI, values, null, null);
	}
	
	private void updateFriendLocations() {
		Thread thd = new FriendFinder(this);
		thd.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	private final IFriendTracker.Stub mBinder = new IFriendTracker.Stub() {
		public boolean isTracking() {
			return mTracking;
		}
		
		public boolean addNickname(String nick, int contactId) {
			if (FriendTracker.this.checkCallingPermission(PERMISSION_FRIEND_SERVICE_ADD)
					!= PackageManager.PERMISSION_GRANTED) {
				throw new SecurityException("Requires " + PERMISSION_FRIEND_SERVICE_ADD);
			}
			ContentResolver r = FriendTracker.this.getContentResolver();
			ContentValues values = new ContentValues();
			values.put(FriendContent.Location.NICK, nick);
			values.put(FriendContent.Location.CONTACTS_ID, contactId);
			Uri uri = r.insert(FriendContent.Location.CONTENT_URI, values);
			return (uri == null ? false : true);
		}
	};
	
	private void checkFriends(Location location) {
		Cursor c = getContentResolver().query(FriendContent.Location.CONTENT_URI, null, FriendContent.Location.ACTIVE+"=1", null, null);
	
		// Go through each returned result
		if (c.moveToFirst()) do {
			double lat = c.getFloat(c.getColumnIndex(FriendContent.Location.LATITUDE));
			double lon = c.getFloat(c.getColumnIndex(FriendContent.Location.LONGITUDE));
			
			Location floc = new Location(LocationManager.GPS_PROVIDER);
			floc.setLatitude(lat);
			floc.setLongitude(lon);
		
			// Notify any receivers
			if (location.distanceTo(floc) <= mDistThreshold) {
				Intent i = new Intent(ACTION_FRIEND_NEAR);
				i.putExtra(FriendContent.Location._ID, 
						c.getString(c.getColumnIndex(FriendContent.Location._ID)));
				i.putExtra(FriendContent.Location.NICK, 
						c.getString(c.getColumnIndex(FriendContent.Location.NICK)));
				i.putExtra(FriendContent.Location.CONTACTS_ID, 
						c.getString(c.getColumnIndex(FriendContent.Location.CONTACTS_ID)));
				sendBroadcast(i, "org.siislab.tutorial.permission.FRIEND_NEAR");
			}
				
		} while (c.moveToNext());
		
	}
	
	private LocationListener mLocListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			checkFriends(location);
		}

		public void onProviderDisabled(String provider) {
			
		}

		public void onProviderEnabled(String provider) {
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
	};
}
