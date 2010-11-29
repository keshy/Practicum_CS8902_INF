/*********************************************************************

    File          : FriendProvider.java
    Author(s)     : enck
    Description   : description

    Copyright (c) 2008 The Pennsylvania State University
    Systems and Internet Infrastructure Security Laboratory

**********************************************************************/

package org.siislab.tutorial.friendtracker;

import java.util.HashMap;

import org.siislab.tutorial.provider.FriendProvider.FriendContent;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author enck
 * 
 * Notes: derived from the NotesList sample application
 *
 */
public class FriendProvider extends ContentProvider {
	
	private static final String DATABASE_NAME = "friends.db";
	private static final int DATABASE_VERSION = 1;
	private static final String LOCATION_TABLE_NAME = "location";
	
	private static HashMap<String, String> sLocationProjectionMap;
	
	private static final int FRIENDS = 1;
	private static final int FRIEND_ID = 2;
	
	private static final UriMatcher sUriMatcher;
	
	/**
	 * helper class to open, create, and upgrade the database file
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + LOCATION_TABLE_NAME + " ("
					+ FriendContent.Location._ID + " INTEGER PRIMARY KEY,"
					+ FriendContent.Location.NICK + " TEXT,"
					+ FriendContent.Location.CONTACTS_ID + " INTEGER,"
					+ FriendContent.Location.ACTIVE + " INTEGER,"
					+ FriendContent.Location.LATITUDE + " FLOAT,"
					+ FriendContent.Location.LONGITUDE + " FLOAT"
					+ ");");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(FriendTracker.TAG, "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME);
			onCreate(db);
		}
	}
	
	private DatabaseHelper mOpenHelper;

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		switch (sUriMatcher.match(uri)) {
		case FRIENDS:
			qb.setTables(LOCATION_TABLE_NAME);
			qb.setProjectionMap(sLocationProjectionMap);
			break;
			
		case FRIEND_ID:
			qb.setTables(LOCATION_TABLE_NAME);
			qb.setProjectionMap(sLocationProjectionMap);
			qb.appendWhere(FriendContent.Location._ID + "=" + uri.getPathSegments().get(1));
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		// Use default sort order if not specified
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = FriendContent.Location.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}
	
		// Query the database
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		
		// Tell the cursor what uri to watch (for data changes)
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case FRIENDS:
			return FriendContent.Location.CONTENT_TYPE;
			
		case FRIEND_ID:
			return FriendContent.Location.CONTENT_ITEM_TYPE;
			
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (sUriMatcher.match(uri) != FRIENDS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		
		// Make sure fields are set
		if (values.containsKey(FriendContent.Location.NICK) == false) {
			values.put(FriendContent.Location.NICK, "Unknown");
		}
		
		if (values.containsKey(FriendContent.Location.CONTACTS_ID) == false) {
			values.put(FriendContent.Location.CONTACTS_ID, 0);
		}
		
		if (values.containsKey(FriendContent.Location.ACTIVE) == false) {
			values.put(FriendContent.Location.ACTIVE, 0);
		}

		if (values.containsKey(FriendContent.Location.LATITUDE) == false) {
			values.put(FriendContent.Location.LATITUDE, 0.0);
		}
		
		if (values.containsKey(FriendContent.Location.LONGITUDE) == false) {
			values.put(FriendContent.Location.LONGITUDE, 0.0);
		}
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(LOCATION_TABLE_NAME, FriendContent.Location.NICK, values);
		if (rowId > 0) {
			Uri friendUri = ContentUris.withAppendedId(FriendContent.Location.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(friendUri, null);
			return friendUri;
		}
	
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case FRIENDS:
			count = db.delete(LOCATION_TABLE_NAME, selection, selectionArgs);
			break;
			
		case FRIEND_ID: 
			String id = uri.getPathSegments().get(1);
			count = db.delete(LOCATION_TABLE_NAME, FriendContent.Location._ID
					+ "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" 
							+ selection + ")" : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
	
		int count;
		switch (sUriMatcher.match(uri)) {
		case FRIENDS:
			count = db.update(LOCATION_TABLE_NAME, values, selection, selectionArgs);
			break;
			
		case FRIEND_ID:
			String id = uri.getPathSegments().get(1);
			count = db.update(LOCATION_TABLE_NAME, values, FriendContent.Location._ID
					+ "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" 
							+ selection + ")" : ""), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(FriendContent.AUTHORITY, "location", FRIENDS);
		sUriMatcher.addURI(FriendContent.AUTHORITY, "location/#", FRIEND_ID);
		
		sLocationProjectionMap = new HashMap<String, String>();
		sLocationProjectionMap.put(FriendContent.Location._ID, FriendContent.Location._ID);
		sLocationProjectionMap.put(FriendContent.Location.NICK, FriendContent.Location.NICK);
		sLocationProjectionMap.put(FriendContent.Location.CONTACTS_ID, FriendContent.Location.CONTACTS_ID);
		sLocationProjectionMap.put(FriendContent.Location.ACTIVE, FriendContent.Location.ACTIVE);
		sLocationProjectionMap.put(FriendContent.Location.LATITUDE, FriendContent.Location.LATITUDE);
		sLocationProjectionMap.put(FriendContent.Location.LONGITUDE, FriendContent.Location.LONGITUDE);
	}

}
