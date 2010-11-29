package org.siislab.tutorial.friendtracker;

import org.siislab.tutorial.interfaces.IFriendTracker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FriendTrackerControl extends Activity {
	private Button mStartButton;
	private Button mStopButton;
	private TextView mStatusText;
	
	private IFriendTracker mService = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mStartButton = (Button) findViewById(R.id.button_start);
        mStopButton = (Button) findViewById(R.id.button_stop);
        mStatusText = (TextView) findViewById(R.id.status);
        
        mStartButton.setOnClickListener(startListener);
        mStopButton.setOnClickListener(stopListener);
        
        if (mService == null) {
        	bindService(new Intent(this, FriendTracker.class), mConnection, Context.BIND_AUTO_CREATE);
        }
        
        updateStatus();
    }
    
    private void updateStatus() {
        
        if (mService != null) {
        	try {
				if (mService.isTracking()) {
					mStatusText.setText("on");
					mStartButton.setEnabled(false);
					mStopButton.setEnabled(true);
				} else {
					mStatusText.setText("off");
					mStartButton.setEnabled(true);
					mStopButton.setEnabled(false);
				}
			} catch (RemoteException e) {
				mStatusText.setText("Remote Connection Error");
				mStartButton.setEnabled(false);
				mStopButton.setEnabled(false);
			}
        } else {
        	mStatusText.setText("Unable to bind");
        	mStartButton.setEnabled(false);
        	mStopButton.setEnabled(false);
        }
    }
    
    private OnClickListener startListener = new OnClickListener() {
    	public void onClick(View view) {
    		FriendTracker.actionStartTracking(FriendTrackerControl.this);
    		updateStatus();
    	}
    };
    
    private OnClickListener stopListener = new OnClickListener() {
    	public void onClick(View view) {
    		FriendTracker.actionStopTracking(FriendTrackerControl.this);
    		updateStatus();
    	}
    };
    
    //-- the service connection
    private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IFriendTracker.Stub.asInterface(service);
			updateStatus();
		}

		public void onServiceDisconnected(ComponentName name) {
			mService = null;
			updateStatus();
		}
    };
}