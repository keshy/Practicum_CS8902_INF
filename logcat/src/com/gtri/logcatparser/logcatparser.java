package com.gtri.logcatparser;

import android.app.Activity;
import android.app.NotificationManager;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;

public class logcatparser extends Activity implements OnClickListener{
	public logcatService mService;
	public static String TAG = "Service Demo Tag";
	public Button onStart,onStop;
	private NotificationManager mNotificationManager; 
	public void onCreate(Bundle icicles){
		super.onCreate(icicles);
		setContentView(R.layout.main);
		
		onStart = (Button) findViewById(R.id.buttonStart);
		onStop = (Button) findViewById(R.id.buttonStop);
			
		onStart.setOnClickListener(this);
		onStop.setOnClickListener(this);
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);	
		mService = new logcatService(this.getApplicationContext(),mNotificationManager);
		
		
	}
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.buttonStart:
				Log.v(TAG,"OnClick: starting the service now...");
				
				Thread t = new Thread(){
				
					
				
					public void run(){
						mService.start();
						
					}
				};
				t.start();
				break;
			case R.id.buttonStop:
				Log.v(TAG,"Onclick: stopping the service now...");
				mService.stop();
				break;
			}
		}
	}


