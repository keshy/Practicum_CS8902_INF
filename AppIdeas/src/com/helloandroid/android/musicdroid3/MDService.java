package com.helloandroid.android.musicdroid3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.util.Log;

public class MDService extends Service {

	private MediaPlayer mp = new MediaPlayer();
	private List<String> songs = new ArrayList<String>();
	private int currentPosition;
	private boolean isPaused = false;
	
	private NotificationManager nm;
	private static final int NOTIFY_ID = R.layout.songlist;

	@Override
	protected void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
	}

	@Override
	protected void onDestroy() {
		mp.stop();
		mp.release();
		nm.cancel(NOTIFY_ID);
	}

	@Override
	public IBinder getBinder() {
		return mBinder;
	}

	private void playSong(String file) {
		try {

			nm.cancel(NOTIFY_ID);
			Notification notification = new Notification(
					R.drawable.playbackstart, file, null, file, null);
			nm.notify(NOTIFY_ID, notification);

			mp.reset();
			mp.setDataSource(file);
			mp.prepare();
			mp.start();
			isPaused = false;
			
			mp.setOnCompletionListener(new OnCompletionListener() {

				public void onCompletion(MediaPlayer arg0) {
					nextSong();
				}
			});

		} catch (IOException e) {
			Log.e(getString(R.string.app_name), e.getMessage());
		}
	}

	private void nextSong() {
		// Check if last song or not
		if (++currentPosition >= songs.size()) {
			currentPosition = 0;
			nm.cancel(NOTIFY_ID);
		} else {
			playSong(MusicDroid.MEDIA_PATH + songs.get(currentPosition));
		}
	}

	private void prevSong() {
		if (mp.getCurrentPosition() < 3000 && currentPosition >= 1) {
			playSong(MusicDroid.MEDIA_PATH + songs.get(--currentPosition));
		} else {
			playSong(MusicDroid.MEDIA_PATH + songs.get(currentPosition));
		}
	}

	private final MDSInterface.Stub mBinder = new MDSInterface.Stub() {

		public void playFile(int position) throws DeadObjectException {
			try {
				currentPosition = position;
				playSong(MusicDroid.MEDIA_PATH + songs.get(position));

			} catch (IndexOutOfBoundsException e) {
				Log.e(getString(R.string.app_name), e.getMessage());
			}
		}

		public void addSongPlaylist(String song) throws DeadObjectException {
			songs.add(song);
		}

		public void clearPlaylist() throws DeadObjectException {
			songs.clear();
		}

		public void skipBack() throws DeadObjectException {
			prevSong();

		}

		public void skipForward() throws DeadObjectException {
			nextSong();
		}

		public void pause() throws DeadObjectException {
			if (isPaused) {
				Notification notification = new Notification(
						R.drawable.playbackstart, null, null, null, null);
				nm.notify(NOTIFY_ID, notification);
				mp.start();
			} else {
				Notification notification = new Notification(
						R.drawable.playbackpause, null, null, null, null);
				nm.notify(NOTIFY_ID, notification);
				mp.pause();
			}
			isPaused = !isPaused;
		}

		public void stop() throws DeadObjectException {
			nm.cancel(NOTIFY_ID);
			mp.stop();
		}

	};
}