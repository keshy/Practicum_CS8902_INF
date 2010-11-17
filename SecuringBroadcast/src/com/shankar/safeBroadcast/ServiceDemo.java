package com.shankar.safeBroadcast;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ServiceDemo extends Activity implements OnClickListener{
	
	public static String TAG = "Service Demo Tag";
	public Button onStart,onStop;
	
	public void onCreate(Bundle icicles){
		super.onCreate(icicles);
		setContentView(R.layout.main);
		
		onStart = (Button) findViewById(R.id.buttonStart);
		onStop = (Button) findViewById(R.id.buttonStop);
			
		onStart.setOnClickListener(this);
		onStop.setOnClickListener(this);
			
		
	}
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.buttonStart:
				Log.v(TAG,"OnClick: starting the service now...");
				Intent svc = new Intent(this,ServiceClass.class);
				startService(svc);
				break;
			case R.id.buttonStop:
				Log.v(TAG,"Onclick: stopping the service now...");
				stopService(new Intent(this,ServiceClass.class));
				break;
			}
		}
	}


