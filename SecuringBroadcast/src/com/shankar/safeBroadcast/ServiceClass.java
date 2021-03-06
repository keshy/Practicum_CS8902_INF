package com.shankar.safeBroadcast;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ServiceClass extends Service {

	public static String TAG = "Service Class : Media Player";
	MediaPlayer player; 
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate(){
		int elapsedTime = 0;
		
		Toast.makeText(this, "Service started: Medial player", Toast.LENGTH_LONG).show();
		Log.v(TAG,"In the service class for medial player");
		
		player = MediaPlayer.create(this, R.raw.brainychild);
		elapsedTime = player.getCurrentPosition();
		
		/*
		 * Send broadcast : Prepare Intent 
		 */
		if(elapsedTime%10 == 0){
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction("ACTION_EDIT");
			broadcastIntent.setData(Uri.parse("content://contacts/people/1"));
			broadcastIntent.putExtra("timer", elapsedTime);
			sendBroadcast(broadcastIntent);
			
			Log.v(TAG,"Broadcast sent");
			
			
			
		}
		
		player.setLooping(false);
		
	}
	
	public void onDestroy(){
		
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
		player.stop();
	}
	
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
		player.start();
	}
	
}
