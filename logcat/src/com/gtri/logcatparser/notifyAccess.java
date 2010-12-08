package com.gtri.logcatparser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class notifyAccess extends Activity implements OnClickListener{
	
	private TextView Victim = null;
	private TextView Packagename = null;
	private TextView SourceDir = null;
	private TextView UID = null;
	private SharedPreferences mShared;
	public String app_name; 
	public Button report_abuse;
	public Button delete_app;
	
	protected void onCreate(Bundle icicles){
		
		super.onCreate(icicles);
		setContentView(R.layout.notify);
		
		mShared = this.getSharedPreferences("Logcat", 0);
		
		app_name = new String();
		app_name = mShared.getString("Victim Application Name", "null");
		
		Victim = (TextView)findViewById(R.id.Victim_app);
		Victim.setText("Victim :"+app_name);
		
		Packagename = (TextView)findViewById(R.id.Package_name);
		Packagename.setText("Package name :"+mShared.getString("Victim Package", null));
		
		SourceDir = (TextView)findViewById(R.id.SourceDir);
		SourceDir.setText("Source Dir :"+mShared.getString("Source Directory" ,null));
		
		UID = (TextView)findViewById(R.id.UID);
		UID.setText("UID :"+mShared.getInt("UID" ,0));
		
		report_abuse = (Button)findViewById(R.id.Report_app);
		delete_app = (Button)findViewById(R.id.delete_app);
		
		report_abuse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Application reported to the App store", Toast.LENGTH_SHORT).show();
				/*
				 * Module to report malicious activity to some governing authority.
				 * E.g: Google Android market to be red-flagged. 
				 */
				
			}
		});
		
		delete_app.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "For app to be deleted you need root privileges. Please uninstall the Application.", Toast.LENGTH_SHORT).show();			}
			/*
			 * Module to delete the application. 
			 * A simple scenario could be starting the Settings native application and nagivate the user to the 
			 * "manage applications" activity where he could uninstall the application. 
			 */
		});
		
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
