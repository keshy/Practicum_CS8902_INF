/*********************************************************************

    File          : MarkerOverlay.java
    Author(s)     : enck
    Description   : description

    Copyright (c) 2008 The Pennsylvania State University
    Systems and Internet Infrastructure Security Laboratory

**********************************************************************/

package org.siislab.tutorial.friendviewer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Contacts;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/**
 * @author enck
 * 
 * Help from Om's TestLBS for this
 */
public class MarkerOverlay extends ItemizedOverlay<OverlayItem> {

	private Context mContext;
	private List<OverlayItem> mItemList = new ArrayList<OverlayItem>();
	private List<Long> mIdList = new ArrayList<Long>();
	private Drawable mMarker;
	
	private Integer mLastIndex;
	private Long mLastTap;
	
	/**
	 * @param defaultMarker
	 */
	public MarkerOverlay(Context context, Drawable marker) {
		super(marker);
		mContext = context;
		mMarker = marker;
	}
	
	public void addMarker(GeoPoint position, String title, String snippet, Long id) {
		mItemList.add(new OverlayItem(position, title, snippet));
		mIdList.add(id);
		populate();
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected OverlayItem createItem(int i) {
		return mItemList.get(i);
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#onTap(int)
	 */
	@Override
	protected boolean onTap(int index) {
		boolean rc = super.onTap(index);

		boolean show = true;
		long curtime = SystemClock.elapsedRealtime();
		if (mLastIndex != null) {
			long diff = curtime - mLastTap;
			// TODO: double click is hardcoded as half a second, make variable
			if (mLastIndex == index && diff < 500) {
				mLastIndex = null;
				mLastTap = null;
				show = false;
				showContact(mIdList.get(index));
			} else {
				mLastIndex = index;
				mLastTap = curtime;
			}
		} else {
			mLastIndex = index;
			mLastTap = curtime;
		}
		
		if (show) {
			String nick = mItemList.get(index).getSnippet();
			Toast.makeText(mContext, nick, Toast.LENGTH_SHORT).show();
		}
		
		return rc;
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#draw(android.graphics.Canvas, com.google.android.maps.MapView, boolean)
	 */
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		
		boundCenterBottom(mMarker);
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		return mItemList.size();
	}
	
	private void showContact(Long id) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.withAppendedPath(Contacts.People.CONTENT_URI, id.toString()));
		mContext.startActivity(i);
	}

}
