/*********************************************************************

    File          : FriendReceiver.java
    Author(s)     : enck
    Description   : description

    Copyright (c) 2008 The Pennsylvania State University
    Systems and Internet Infrastructure Security Laboratory

**********************************************************************/

package org.siislab.tutorial.friendviewer;

import org.siislab.tutorial.provider.FriendProvider.FriendContent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author enck
 *
 */
public class FriendReceiver extends BroadcastReceiver {
	
	private static final String ACTION_FRIEND_NEAR = "org.siislab.tutorial.action.FRIEND_NEAR";

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (ACTION_FRIEND_NEAR.equals(intent.getAction())) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				String nick = extras.getString(FriendContent.Location.NICK);
				if (nick != null) {
					Toast.makeText(context, "Near " + nick, Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

}
