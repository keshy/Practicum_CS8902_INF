package org.siislab.tutorial.friendviewer;

import org.siislab.tutorial.provider.FriendProvider.FriendContent;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class FriendViewer extends ListActivity {
	public static final String TAG = "FriendViewer";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);
        
        populate();
    }
    
    private void populate() {
    	String[] projection = new String[] {
    			FriendContent.Location._ID,
    			FriendContent.Location.NICK,
    			FriendContent.Location.CONTACTS_ID,
    			FriendContent.Location.ACTIVE,
    			FriendContent.Location.LATITUDE,
    			FriendContent.Location.LONGITUDE
    	};
    	Cursor c = managedQuery(FriendContent.Location.CONTENT_URI, projection, null, null, null);
    	
    	ListAdapter adapter = new SimpleCursorAdapter(this,
    			R.layout.friend_row, c, 
    			new String[] {FriendContent.Location.NICK,
    						  FriendContent.Location.ACTIVE,
    						  FriendContent.Location.LATITUDE,
    						  FriendContent.Location.LONGITUDE},
    			new int[] { R.id.friend_nick, R.id.friend_active, 
    					    R.id.friend_latitude, R.id.friend_longitude});
    	setListAdapter(adapter);
    	
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	
		Intent i = new Intent(this, FriendMap.class);
		i.putExtra("friend_id", id);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean rc = super.onCreateOptionsMenu(menu);
	
		menu.add(0, Menu.FIRST, 0, "View All");
		
		return rc;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent i;
		switch(item.getItemId()) {
		case Menu.FIRST:
			i = new Intent(this, FriendMap.class);
			startActivity(i);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

}