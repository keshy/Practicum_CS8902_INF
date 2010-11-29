/*********************************************************************

    File          : BootReceiver.java
    Author(s)     : enck
    Description   : description

    Copyright (c) 2008 The Pennsylvania State University
    Systems and Internet Infrastructure Security Laboratory

**********************************************************************/

package org.siislab.tutorial.friendtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author enck
 *
 * Starts FriendTracker service on boot
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			//FriendTracker.actionStartTracking(context);
		}
	}
}
