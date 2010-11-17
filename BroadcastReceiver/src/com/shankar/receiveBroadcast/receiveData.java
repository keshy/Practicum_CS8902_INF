package com.shankar.receiveBroadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class receiveData extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        IntentFilter intentReceiver = new IntentFilter();
        intentReceiver.addDataScheme("content");
        
        
        
      
        
		registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				Log.v("In receiver process ","*****************");
				System.out.println("I am getting the data as "+intent);
			}
		}, intentReceiver);
		
		Toast.makeText(this, "Received data", Toast.LENGTH_LONG).show();
    }
}