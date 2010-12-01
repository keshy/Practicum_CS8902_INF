package com.gtri.logcatparser;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class notifyAccess extends Activity{
	
	private TextView notify = null;
	
	protected void onCreate(Bundle icicles){
		
		super.onCreate(icicles);
		setContentView(R.layout.notify);
		notify = (TextView)findViewById(R.id.text);
		
		notify.setText("Permission violation");
		
		
		
		
	}

}
