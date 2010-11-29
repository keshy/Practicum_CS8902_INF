/*********************************************************************

    File          : FriendMap.java
    Author(s)     : enck
    Description   : description

    Copyright (c) 2008 The Pennsylvania State University
    Systems and Internet Infrastructure Security Laboratory

**********************************************************************/

package org.siislab.tutorial.friendviewer;

import org.siislab.tutorial.provider.FriendProvider.FriendContent;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

/**
 * @author enck
 *
 */
public class FriendMap extends MapActivity {
	
	private Long mFriendId;
	private MapView mMap;
	private MapController mMapCtl;
	
	//private TextView mLocLabel;
	//private TextView mLocValue;
	private LinearLayout mZoomCtl;
	private MarkerOverlay mMarkers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	
		// Link to GUI
		//mLocLabel = (TextView) findViewById(R.id.location_label);
		//mLocValue = (TextView) findViewById(R.id.location_value);
		setupMap();
		
		Intent i = getIntent();
		Bundle extras = (i == null) ? null : i.getExtras();
		mFriendId = (extras == null) ? null : extras.getLong("friend_id");
	
		if (mFriendId != null) {
			Cursor c = managedQuery(FriendContent.Location.CONTENT_URI, null, 
					FriendContent.Location._ID+"="+mFriendId, null, null);
			if (c.moveToFirst()) {
				displayFriend(c);
			}
		} else {
			Cursor c = managedQuery(FriendContent.Location.CONTENT_URI, null, 
					null, null, null);
			if (c.moveToFirst()) do {
				displayFriend(c);
			} while (c.moveToNext());
		}
	}
	
	private void setupMap() {
		mMap = (MapView) findViewById(R.id.map_view);
		mZoomCtl = (LinearLayout) findViewById(R.id.map_zoom);
		
		mMap.displayZoomControls(true);
		mMap.setClickable(true);
		mMap.setEnabled(true);
		mMapCtl = mMap.getController();
		View zoomView = mMap.getZoomControls();
		mZoomCtl.addView(zoomView,
				new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, 
						LayoutParams.WRAP_CONTENT));
	
		// Setup the markers
		Drawable marker = getResources().getDrawable(R.drawable.bubble);
		marker.setBounds(0,0, (int)(marker.getIntrinsicWidth()),
				(int)(marker.getIntrinsicHeight()));
		
		mMarkers = new MarkerOverlay(this, marker);
		mMap.getOverlays().add(mMarkers);
	}
	
	private void displayFriend(Cursor c) {
		Long contact_id = getContactId(c);
		String nick = getNick(c);
		Location loc = getLocation(c);
		
		int lat = (int)(loc.getLatitude()*1000000);
		int lon = (int)(loc.getLongitude()*1000000);
		GeoPoint center = new GeoPoint(lat,lon);

		mMapCtl.setCenter(center);
		mMapCtl.setZoom(15);
	
		mMarkers.addMarker(center, nick, nick, contact_id);
	}

	private Long getContactId(Cursor c) {
		return c.getLong(c.getColumnIndex(FriendContent.Location.CONTACTS_ID));
	}
	private String getNick(Cursor c) {
		return c.getString(c.getColumnIndex(FriendContent.Location.NICK));
	}
	
	private Location getLocation(Cursor c) {
		double lat = c.getDouble(c.getColumnIndex(FriendContent.Location.LATITUDE));
		double lon = c.getDouble(c.getColumnIndex(FriendContent.Location.LONGITUDE));
		
		Location floc = new Location(LocationManager.GPS_PROVIDER);
		floc.setLatitude(lat);
		floc.setLongitude(lon);
		
		return floc;
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#isLocationDisplayed()
	 */
	@Override
	protected boolean isLocationDisplayed() {
		// TODO Auto-generated method stub
		return super.isLocationDisplayed();
		//return true;
	}

}
