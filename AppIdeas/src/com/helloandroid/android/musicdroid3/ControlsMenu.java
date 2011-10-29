package com.helloandroid.android.musicdroid3;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class ControlsMenu extends Activity {

	private ImageView pauseImage;
	private ImageView skipbImage;
	private ImageView skipfImage;
	private ImageView stopImage;
	private MDSInterface mpInterface;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.controls);

		pauseImage = (ImageView) findViewById(R.id.pause);
		skipbImage = (ImageView) findViewById(R.id.skipb);
		skipfImage = (ImageView) findViewById(R.id.skipf);
		stopImage = (ImageView) findViewById(R.id.stop);

		this.bindService(new Intent(this, MDService.class), null, mConnection,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		try {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				handleAnimation(pauseImage);
				mpInterface.pause();
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				handleAnimation(skipbImage);
				mpInterface.skipBack();
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				handleAnimation(skipfImage);
				mpInterface.skipForward();
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				handleAnimation(stopImage);
				mpInterface.stop();
				break;
			}

		} catch (DeadObjectException e) {
			Log.e(getString(R.string.app_name), e.getMessage());
		}

		return super.onKeyUp(keyCode, event);
	}

	public void handleAnimation(View v) {
		v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.selected));
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mpInterface = MDSInterface.Stub.asInterface((IBinder) service);
		}

		public void onServiceDisconnected(ComponentName className) {
			mpInterface = null;
		}
	};
}