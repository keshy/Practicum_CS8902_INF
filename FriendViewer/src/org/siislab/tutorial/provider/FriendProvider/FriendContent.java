/*********************************************************************

    File          : FriendContent.java
    Author(s)     : enck
    Description   : description

    Copyright (c) 2008 The Pennsylvania State University
    Systems and Internet Infrastructure Security Laboratory

**********************************************************************/

package org.siislab.tutorial.provider.FriendProvider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author enck
 * 
 * Convenience class for the FriendProvider
 */
public class FriendContent {
	
	public static final String AUTHORITY = "friends";

	public static final class Location implements BaseColumns {
		
		/**
		 * Content URI for location data
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY+"/location");
		
		/**
         * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.siislab.tutorial.friend";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.siislab.tutorial.friend";

	
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
	
		/**
		 * Internal location service nickname
		 */
		public static final String NICK = "nick";
	
		/**
		 * Cross referenced ID in the Contacts Provider for more information
		 */
		public static final String CONTACTS_ID = "contacts_id";
	
		/**
		 * Indicates if the friend is active, i.e., location data is valid
		 */
		public static final String ACTIVE = "active";
	
		/**
		 * latitude coordinate
		 */
		public static final String LATITUDE = "latitude";
	
		/**
		 * longitude coordinate
		 */
		public static final String LONGITUDE = "longitude";
	}
}
