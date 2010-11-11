package com.shankar.hello;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class hellpo extends Activity {
	
	public TextView tv = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tv = (TextView) findViewById(R.id.text);
        tv.setText(""+"Hello world");
        
        
        
    }
}