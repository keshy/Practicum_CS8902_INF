package com.shankar.gtri;



import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class main extends Activity {
	public TextView set_text = null;
	public Button getCurrentLocation = null;
	
	public void onCreate(Bundle icicles){
		
		super.onCreate(icicles);
          setContentView(R.layout.main);
        
        set_text = (TextView)findViewById(R.id.title);
        getCurrentLocation = (Button)findViewById(R.id.getlocation);
        Log.v("Shankar's tag","I am here in the victim code hehe");
        set_text.setText(""+"Get your current location based on your lat long");
        
        getCurrentLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(main.this,ViewMap.class);
				startActivity(intent);
				
			}
		});
        
	}
		
}