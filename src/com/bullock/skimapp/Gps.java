package com.bullock.skimapp;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Gps implements LocationListener {

	private static final String FILE = "Gps";
	
	private LocationManager locationManager;
    private String provider;
    private int gpsUpdated = 1;
	private static final int DEFAULTDISTANCE = 1;
	private static final int DEFAULTINTERVAL = 400;
	
	private SkiMapp parent;
	
	
	//--------------------------------------------------
	public Gps (SkiMapp m){
		parent = m;
	}

    public void removeUpdates() {
	    locationManager.removeUpdates(this);
    }
    
    
    // actually request updates. Only place we set the parameters.  
    // (Also called after returning from Prefs screen)
    public void register() {
        if (Prefs.enable_gps(parent.getBaseContext())){
            int minGpsDistance = 1;   // metres 
            int minGpsTime     = 400; // seconds 
            
//            String s_gps_minGpsDistance = Prefs.gps_minGpsDistance(parent.getBaseContext());
//            String s_gps_minGpsTime     = Prefs.gps_minGpsTime(parent.getBaseContext());
//
//            minGpsDistance = safeFloatInt (s_gps_minGpsDistance, "Min Distance", DEFAULTDISTANCE);
//            minGpsTime     = safeFloatInt (s_gps_minGpsTime,     "Min Interval", DEFAULTINTERVAL);
            minGpsDistance = Prefs.gps_minGpsDistance(parent.getBaseContext());
            minGpsTime     = Prefs.gps_minGpsTime(parent.getBaseContext());

        	
            locationManager.requestLocationUpdates(provider, minGpsTime, minGpsDistance, this);
        }
    }
    
    
	 
    public void initService(){
//    	Log.d (FILE, ">>>>>  initLocationService() ");

    	
        // Get the location manager
        locationManager = (LocationManager) parent.getSystemService(parent.LOCATION_SERVICE);
        
    	// Maybe go no further if GPS == false
        boolean enable_gps = Prefs.enable_gps(parent.getBaseContext());
        
	    // switch off icon from map, maybe
	    if (parent.mMap != null) {
	    	parent.mMap.setMyLocationEnabled(enable_gps);
	    }
        
	    // go no further, if we dont need to
        if (!enable_gps){
        	// disable if need be
    	    locationManager.removeUpdates(this);
        	return;
        }
        
        
        // Check if GPS enabled and if not alert user  
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
        	parent.showErrorToast("Yerk, no GPS! - please enable it");
        } 
        
        // Define the criteria how to select the location provider ->  demand GPS
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        provider = locationManager.getBestProvider(criteria, true);
//    	Log.d (FILE, ">>>>>  initLocationService() - provider is "+provider);
    	
    	// ask for last known. Could be friggin miles away
        Location location = locationManager.getLastKnownLocation(provider);

        
        // Initialise the location fields
        if (location != null) {	
//        	Log.d (FILE, "Provider " + provider + " has been selected.");
        	onLocationChanged(location);
        } else {
//        	Log.d (FILE, "!!! No Provider selected.");
        }    
    }


    

	//--------------------------------------------------
	


    @Override
    public void onLocationChanged(Location location) {
//    	Log.d (FILE, ">>>>>  onLocationChanged() ");
	
	    int lat = (int) (location.getLatitude());
	    int lng = (int) (location.getLongitude());
	 
	    //
	    // todo - work out best battery life
	    //
        if (Prefs.enable_gps_debug(parent.getBaseContext())){
	        Toast.makeText(parent, "GPS update #" + gpsUpdated + "\n  lat: " + lat + " lon: "+ lng,
		    		 		Toast.LENGTH_SHORT).show();
	        gpsUpdated++;
        }
	}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//    	Log.d (FILE, ">>>>>  onStatusChanged() ");
    }

    @Override
    public void onProviderEnabled(String provider) {
      Toast.makeText(parent, "Enabled new locn provider " + provider,
          Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
      Toast.makeText(parent, "Disabled locn provider " + provider,
          Toast.LENGTH_SHORT).show();
    }
    

	//--------------------------------------------------
	

    
    private int safeFloatInt(String stringVal, String err, int deflt){
		int retval;
		
    	try
		{
    		retval = Integer.parseInt(stringVal);
		}
		catch (NumberFormatException nfe)
		{
		   // bad data - set to default
			parent.showErrorToast("Invalid "+err+" - check Prefs. \n Defaulting to "+deflt);
			retval = 	 deflt;
		}  
    	
    	return retval;
    }
    
    
}
