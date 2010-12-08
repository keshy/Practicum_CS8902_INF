package com.gtri.logcatparser;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class logcatService extends Service{

	private final String Filter = "Caused by: java.lang.SecurityException: Permission Denial:";
	private String mFilter = null;
	private Pattern mFilterPattern = null;
	private boolean mRunning = false;
	public logcatparser parser = null;
	public NotificationManager mManager = null;
	private BufferedReader mReader;
	private PackageManager pManager;
	private boolean mIsFilterPattern;
	private ArrayList<String> mLogCache = new ArrayList<String>();
	private boolean mPlay = true;
	private Process logcatProc;
	private SharedPreferences mShared;
	private SharedPreferences.Editor mEditor;
	private Context mContext;
	public int icon;
	public CharSequence title;
	public long when;
	public Intent notificationintent;
	public PendingIntent contentIntent;
	public Notification notification;
	public static int completed_notifications;
	public String pckgName;
	public int count;
	public int flag;
	final int Permission_ID = 1;
	/**
	 * Logcat Service: Parses logcat runtime execution to look for malicious activities. 
	 * @author Shankar;
	 * 
	 */
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * Constructor 
	 * @logcatService : Public constructor method
	 * @author Shankar
	 * @return initialized data sets and variables.
	 */
	
	logcatService(Context context, NotificationManager mNotificationManager)
	{
		mContext =  context;
		mFilterPattern = Pattern.compile(Filter,Pattern.CASE_INSENSITIVE);
		pManager = context.getPackageManager();
		
		/*
		 * Set up UI elements for notification 
		 */
		
		mManager = mNotificationManager;
		icon = R.drawable.notification_icon;
		title = "Access Violation!";
		notificationintent = new Intent(mContext,notifyAccess.class);
		contentIntent = PendingIntent.getActivity(mContext, 0, notificationintent,Intent.FLAG_ACTIVITY_NEW_TASK);
		completed_notifications = 0;
		count = 0;
		flag = 0;
		
		// Check if Manager object is null 
		if((mManager = mNotificationManager) == null)
		{
					System.out.println("The value of notification manager is set to null");
					stopSelf();
		}
		
		
	}
	
	
	/**
	 * Start Service:
	 * @author Shankar
	 * @throws NameNotFoundException 
	 */
	
	public void start() throws NameNotFoundException {
		
		mRunning = true;
		
		try {
			count = 0;
			logcatProc = Runtime.getRuntime().exec(new String[] {"logcat"});
		
			mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
			
			/*
			 * Store each line in a new string
			 */
			String line;
			
			while (mRunning && (line = mReader.readLine()) != null) 
			{	
				if (!mRunning) 
				{
					break;
				}
				
				if (line.length() == 0) 
				{
					continue;
				}
				
				if (mPlay) 
				{
					/*
					 * Store log in cache as well as load the data 
					 */
					cat(mLogCache);
					cat(line);
					
					/*
					 * Check for "Permission Denial:" Error to tap into the Exception Handling module. 
					 * @author Shankar
					 * returns new Intent if match is found. 
					 */
					if(line.contains(Filter))
					{	
						/*
						 * Perform string parsing to extract package name from which the details of the 
						 * victim pacakge can be extracted.	
						 */
						
						StringBuffer packageName = new StringBuffer();
						int detect_cmp = 0;
						int i = 0;
						detect_cmp = 0;
						
						for(i=0;i<line.length();i++){
							if(line.charAt(i)=='c'&&line.charAt(i+1)=='m'&&line.charAt(i+2)=='p')
							{
								//System.out.println("I am inside the parsing module");
								detect_cmp = 1; 
							}
							if(detect_cmp == 1){
								break;
							}
						}	
						i+=4;
						
						while(line.charAt(i)!='/'){
							//System.out.println(line.charAt(i));
							packageName.append(line.charAt(i));
							i++;
						}
						
						System.out.println("The package name = "+packageName);
					
					
						pckgName = new String(packageName.toString());
						System.out.println("The string = "+pckgName);
						
						// Create object to store victim information
						ApplicationInfo appInfo;
						
						if(pckgName == null){
							System.out.println("pckg is null");
						}
						appInfo = new ApplicationInfo(pManager.getApplicationInfo(pckgName,PackageManager.GET_META_DATA));
						
						
						mShared = mContext.getSharedPreferences("Logcat", 0);
						mEditor = mShared.edit();
						
						mEditor.putString("Victim Package", pckgName);
						mEditor.putString("Victim Application Name", appInfo.loadLabel(pManager).toString());
						mEditor.putString("Source Directory", appInfo.sourceDir);
						mEditor.putInt("UID", appInfo.uid);
						mEditor.commit();
						
						System.out.println("The innocent app's data directory = "+appInfo.dataDir);
						System.out.println("The innocent app's source directory = "+appInfo.sourceDir);
						System.out.println("The innocent app's permissions = "+appInfo.permission);
						System.out.println("The innocent app's  UID = "+appInfo.uid);
						System.out.println("The name of the application from the android:name = "+appInfo.name);
						System.out.println("The name of the application from the android:label = "+appInfo.loadLabel(pManager));
						
						when = System.currentTimeMillis();
						notification = new Notification(icon, title, when);
						notification.setLatestEventInfo(mContext, title, "Malicious activity detected! Press here to see details", contentIntent);
						
						Log.v("Shankar's tag","I am in here inside the filter!");
						mManager.notify(Permission_ID, notification);
						
					}
				}
				else 
				{	
					/*
					 * Add line to the cache. 
					 */
					mLogCache.add(line);
				}
			}
		} 
		
		catch (IOException e) 
		{
			Log.e("alogcat", "error reading log", e);
			return;
		} 
		finally 
		{
			if (logcatProc != null) 
			{
				logcatProc.destroy();
				logcatProc = null;
				clear();
			}
			
			if (mReader != null) 
			{
				try 
				{
					mReader.close();
					mReader = null;
				} 
				catch (IOException e) 
				{
					Log.e("alogcat", "error closing stream", e);
				}
			}
		}
	}

	/**
	 * Cat: check for patterns in the mCacheLog using the Patterns regex java class
	 * @author Shankar
	 * @param cache
	 */
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
	
	/**
	 * Clear Log
	 * @author Shankar
	 */

	public void clear() {
		try 
		{
			Runtime.getRuntime().exec(new String[] { "logcat", "-c" });
		} 
		catch (IOException e) 
		{
			Log.e("alogcat", "error clearing log", e);
		} 
		finally {
			
		}
	}
	
	/**
	 * Stop Service	
	 * @author Shankar
	 */

	public void stop() {
		Log.d("alogcat", "stopping ...");
		mRunning = false;
	}

	public boolean isRunning() {
		return mRunning;
	}

}
