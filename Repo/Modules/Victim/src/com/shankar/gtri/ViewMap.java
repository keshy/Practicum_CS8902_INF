package com.shankar.gtri;
 
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

//import com.google.android.maps.mapView;

import android.os.Bundle;
 
public class ViewMap extends MapActivity 
{    
    /** Called when the activity is first created. */
   
    MapView mapView;
    MapController mc;
    GeoPoint p;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.maps);
    }
    @Override
    protected boolean isRouteDisplayed() {
    return false;
    }
}