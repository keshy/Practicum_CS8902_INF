package com.gtri.logcatparser;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.text.Format;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


public class logcatService extends Service{
	private static final String SEPARATOR = System.getProperty("line.separator");
	private final String Filter = "Permission Denial:";
	private Level mLevel = null;
	private String mFilter = null;
	private Pattern mFilterPattern = null;
	private boolean mRunning = false;
	public logcatparser parser = null;
	public NotificationManager mNotificationManager = null;
	private BufferedReader mReader;
	private Format mFormat;
	private boolean mIsFilterPattern;
	private ArrayList<String> mLogCache = new ArrayList<String>();
	private boolean mPlay = true;
	private Handler mHandler;
	private Buffer mBuffer;
	private Process logcatProc;
	private Context mContext;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
/*
	public Logcat(Context context, Handler handler) {
		mContext = context;
		mHandler = handler;

		Prefs prefs = new Prefs(mContext);

		mLevel = prefs.getLevel();
		mIsFilterPattern = prefs.isFilterPattern();
		mFilter = prefs.getFilter();
		mFilterPattern = prefs.getFilterPattern();
		mFormat = prefs.getFormat();
		mBuffer = prefs.getBuffer();
	}
*/
	logcatService(Context context){
		mContext =  context;
		mFilterPattern = Pattern.compile(Filter,Pattern.CASE_INSENSITIVE);
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);		
	}
	public void start() {
		mRunning = true;
			
		int icon = R.drawable.notification_icon;
		CharSequence title = "Access Violation!";
		long when = System.currentTimeMillis();
		final int Permission_ID = 1;
		
		
		try {
			
			
			
			logcatProc = Runtime.getRuntime().exec(
					new String[] {"logcat"});
			Log.v("Shankar's tag","I am in here!");
			//Toast.makeText(mContext,"I am here ",Toast.LENGTH_LONG).show();

			mReader = new BufferedReader(new InputStreamReader(
					logcatProc.getInputStream()), 1024);

			String line;
			while (mRunning && (line = mReader.readLine()) != null) {
				if (!mRunning) {
					break;
				}
				if (line.length() == 0) {
			
					continue;
				}
				if (mPlay) {
					cat(mLogCache);
					cat(line);
					
					if(line.contains(Filter)){
						
						Log.v("Shankar's tag","I am in here inside the filter!");
						Notification notification = new Notification(icon, title, when);
						Intent notificationintent = new Intent(this,notifyAccess.class);
						notificationintent.putExtra("Error", "Permission violation");
						
						PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationintent, 0);
						
						
						notification.setLatestEventInfo(getApplicationContext(), title, "Access permission violation!", contentIntent);
						mNotificationManager.notify(Permission_ID, notification);
										
										
						//Toast.makeText(this, "Got error on permission",Toast.LENGTH_LONG).show();
					}
				} else {
					mLogCache.add(line);
				}
			}
		} catch (IOException e) {
			Log.e("alogcat", "error reading log", e);
			return;
		} finally {
			if (logcatProc != null) {
				logcatProc.destroy();
				logcatProc = null;
			}
			if (mReader != null) {
				try {
					mReader.close();
					mReader = null;
				} catch (IOException e) {
					Log.e("alogcat", "error closing stream", e);
				}
			}
		}
	}

	private void cat(ArrayList<String> cache) {
		for (int i = 0; i < cache.size(); i++) {
			cat(cache.get(i));
		}
		cache.clear();
	}

	private void cat(String line) {
		if (mIsFilterPattern) {
			if (mFilterPattern != null && !mFilterPattern.matcher(line).find()) {
				return;
			}
		} else {
			if (mFilter != null && !line.toLowerCase().contains(mFilter)) {
				return;
			}
		}

	}

	public void clear() {
		try {
			Runtime.getRuntime().exec(new String[] { "logcat", "-c" });
		} catch (IOException e) {
			Log.e("alogcat", "error clearing log", e);
		} finally {
		}
	}

	public void stop() {
		Log.d("alogcat", "stopping ...");
		mRunning = false;
	}

	public boolean isRunning() {
		return mRunning;
	}

	public boolean isPlay() {
		return mPlay;
	}

	public void setPlay(boolean play) {
		mPlay = play;
	}
}
