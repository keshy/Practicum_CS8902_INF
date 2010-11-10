package example.geocoding;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity 
{
	private LocationManager locationManager;
	private Location currentLocation;
	private TextView txtLatitude;
	private TextView txtLongitude;
	private Button btnReverseGeocode;
	public Intent data;
	public static String location;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Get handles to the elements on our android activity page.
        this.txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        this.txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        
        this.btnReverseGeocode = (Button) findViewById(R.id.btnReverseGeocode);
        currentLocation = new Location(LOCATION_SERVICE);
        
        // Subscribe to our button's click event
        this.btnReverseGeocode.setOnClickListener(
            	new OnClickListener() {
            		public void onClick(View v)
            		{
            			handleReverseGeocodeClick();
            			
            		}
            	}
        );
        location = new String();
        
        data = new Intent();
        currentLocation.setLongitude(77.659612);
        currentLocation.setLatitude(12.977554);
        txtLatitude.setText(""+currentLocation.getLatitude());
        txtLongitude.setText(""+currentLocation.getLongitude());
    }
    
    private void handleReverseGeocodeClick()
    {
    	if (this.currentLocation != null)
    	{
    		// Kickoff an asynchronous task to fire the reverse geocoding
    		// request off to google
    		ReverseGeocodeLookupTask task = new ReverseGeocodeLookupTask();
    		task.applicationContext = this;
    		task.execute();
    	}
    	else
    	{
    		// If we don't know our location yet, we can't do reverse
    		// geocoding - display a please wait message
    		showToast("Please wait until we have a location fix from the gps");
    	}
    }
    
	public void showToast(CharSequence message)
    {
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getApplicationContext(), message, duration);
		toast.show();
    }
	
	public class ReverseGeocodeLookupTask extends AsyncTask <Void, Void, String>
    {
    	private ProgressDialog dialog;
    	protected Context applicationContext;
    	
    	@Override
    	protected void onPreExecute()
    	{
    		this.dialog = ProgressDialog.show(applicationContext, "Please wait...contacting the tubes.", 
                    "Requesting reverse geocode lookup", true);
    	}
    	
		@Override
		protected String doInBackground(Void... params) 
		{
			String localityName = "";
			
			if (currentLocation != null)
			{
				localityName = Geocoder.reverseGeocode(currentLocation);
			}
			
			return localityName;
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			
			Utilities.showToast("Your Locality is: " + result, applicationContext);
			Intent resultIntent = new Intent();
			resultIntent.putExtra("loc", result);
			setResult(RESULT_OK, resultIntent);
			finish();
		}
    }
}