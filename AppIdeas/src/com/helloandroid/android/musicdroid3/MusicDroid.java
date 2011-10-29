package com.helloandroid.android.musicdroid3;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

class Mp3Filter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		return (name.endsWith(".mp3"));
	}
}

public class MusicDroid extends ListActivity {

	public static final String MEDIA_PATH = new String("/sdcard/");
	private List<String> songs = new ArrayList<String>();
	private MDSInterface mpInterface;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.songlist);
		this.bindService(new Intent(MusicDroid.this,MDService.class), 
				null, mConnection, Context.BIND_AUTO_CREATE);
	}

	public void updateSongList() {
		try {
			File home = new File(MEDIA_PATH);
			File fileList[] = home.listFiles(new Mp3Filter());
			mpInterface.clearPlaylist();
			if (fileList != null) {
				for (File file : fileList ) {
					songs.add(file.getName());
					mpInterface.addSongPlaylist(file.getName());
				}
		
				ArrayAdapter<String> songList = new ArrayAdapter<String>(this,
						R.layout.song_item, songs);
				setListAdapter(songList);
			}
		} catch(DeadObjectException e) {
			Log.e(getString(R.string.app_name), e.getMessage());
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		try {
			Intent intent = new Intent(this, ControlsMenu.class);
			startSubActivity(intent,0);
			mpInterface.playFile(position);
		} catch(DeadObjectException e) {
			Log.e(getString(R.string.app_name), e.getMessage());
		}
	}
	
	
	private ServiceConnection mConnection = new ServiceConnection()
    {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mpInterface = MDSInterface.Stub.asInterface((IBinder)service);
			updateSongList();
		}

		public void onServiceDisconnected(ComponentName className) {
			mpInterface = null;
		}
    };

}