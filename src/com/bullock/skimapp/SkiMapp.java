/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bullock.skimapp;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;




/**
 * This shows how to create a simple activity with a map and a marker on the map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is not
 * installed/enabled/updated on a user's device.
 */
public class SkiMapp extends android.support.v4.app.FragmentActivity implements OnSharedPreferenceChangeListener  
																				//,SensorEventListener
																				{
    /**
     * Note that this may be null if the Google Play services APK is not available.
     */
    protected GoogleMap mMap;
    private static final String FILE = "SkiMapp";

    
    protected ArrayList<SkiRun> allSkiRuns = null;
    private ArrayList<Polyline> allPolylines = null;

    // this is the scrolling text box on the splash screen
    protected MessageWindow messageWindow;
    
    // GPS 
    private Gps gps;

    // Latitude
//    private LatitudeHelper latitude;
//    private LatLng owenLatLng;
    
    // Compass
    private Compass compass;
    
    // Prefs updates
    private SharedPreferences	prefs;
    
    

//
//	/ disable screen saver - TODO
//	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//    	Log.d (FILE, ">>>>>  onCreate() - <<<<<<<<<<<<<<");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        // init vars
        	// (not sure if i need to test it for null?) TODO - test for persistence
        if (allSkiRuns == null){
        	allSkiRuns = new ArrayList<SkiRun>();
        }
        if (allPolylines == null){
        	allPolylines = new ArrayList<Polyline>();
        }
        
        // setup splash screen
        messageWindow = new MessageWindow(this, 4);
        
        // load kml file in async thread
        new LoadKmlFileTask(this).execute ("","progress", null);
        
        // setup gps
        gps = new Gps(this);
        gps.initService();
        
        // when Prefs changed
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);

        //setup compass
        compass = new Compass(this);
        compass.initService();
    }

 
    
    @Override
    protected void onStop() {
        compass.unregister(); 
        super.onStop();
    }

    @Override
    protected void onPause() {
	    super.onPause();
	    gps.removeUpdates();
    }
    
    @Override
    protected void onResume() {
//    	Log.d (FILE, ">>>>>  onResume() - <<<<<<<<<<<<<<");
    	super.onResume();

//        setUpMapIfNeeded();  //TODO see google comment below. Need to replace this at some point
        
        // GPS 
    	gps.register();
    	
        // compass
        compass.register();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
//            this.showErrorToast("LANDSCAPE");
//        }else{
//            this.showErrorToast("PORTRAIT");
//        }
    }
    
    //
    // Menu-key: settings and help 
    //
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

	     if (key.equals("enable_gps")){
	    	 gps.initService();
	     }
	     if (key.equals("enable_compass")){
	         if (Prefs.enable_compass(getBaseContext())){
		    	 showToastLong("Compass tracking enabled - make sure your compass is calibrated (Hold device flat and slowly rotate 2 times)");
		    	 compass.initService();
	         } else {
	        	 compass.unregister();
	         }
	     }
	     if (key.equals("compass_damping")){
	    	 compass.setDamping(Prefs.gps_dampingValue(getBaseContext()));
	     }
	     
	     if (key.equals("maptype_terrain") && (mMap != null)){
	    	 
	    	 if (Prefs.maptype_terrain(getBaseContext())){
	          	mMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN);
	         } else {
	          	mMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL);
	         }
	     }

	     if (key.equals("lineWidth") && (mMap != null)) {
	    	// redo each line
	    	for (Polyline p : allPolylines){
	    		p.setWidth(Prefs.lineWidth(this));
	    	}
	     }
	     

    };
 	
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {

       		case R.id.settings:
       			startActivity(new Intent(this, Prefs.class));
       			return true;
	          
       		case R.id.layers:
       			return true;
		      
       		case R.id.help:
      			Intent i = new Intent (this, Info.class);
	            startActivity(i);
	            return true;
	   }
	    
       return false;
    }   
    	

    
    
    
    
    

    
    
    /**
     * Map Functions
     */
    protected void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
            	
            	// put the little location icon in the top right.  
            	// ... actually this switches on the flashy icon too
                if (Prefs.enable_gps(getBaseContext())){
                	mMap.setMyLocationEnabled(true);
                }
                
                // Set map type
                if (Prefs.maptype_terrain(getBaseContext())){
                	mMap.setMapType(MAP_TYPE_TERRAIN);
                }
               
                // draw our custom overlay
                setUpMap();
                		        
            }
        }
    }

    
    
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
    	// draw
    	drawSkiRunsOverlays();
    }
    
    protected void setMapOrientation(float bearing, boolean animate){
    	
    	CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(mMap.getCameraPosition().target)
			.zoom(mMap.getCameraPosition().zoom)
			.bearing(bearing)
			.build();    
    	
    	if (animate)
    		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    	else
    		mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    
	//				     
    // Draw all runs
    //
    private void drawSkiRunsOverlays(){
    	Polyline line;
    	
	    for (SkiRun r : allSkiRuns){
	    	//Log.d (FILE, ">>>>>  drawSkiRuns() - " + r.getName());

	    	// Line for each run
	        line = mMap.addPolyline((new PolylineOptions())
	        					.addAll(r.getAllLatLong())
	        			        .color(r.getColour())
	        			        .width(Prefs.lineWidth(this))
	        
	        );
	        
	        allPolylines.add(line);

	        // Marker at top of each run. 
	        //
	        // known bug - if marker set to invisible, its still visible
	        // bugtrak:    http://code.google.com/p/gmaps-api-issues/issues/detail?id=4677  
	        //
	        if (r.hasMarker()){
		        mMap.addMarker(new MarkerOptions()
							.position(r.getMarkerPosition())
							.title(r.getName())
							.snippet(r.getDescription())
		        			.icon(r.getIcon())
		        			.visible(true)   // known bug with false: http://code.google.com/p/gmaps-api-issues/issues/detail?id=4677  
		        );
		    }
	    }
    }
        

    
	protected void showErrorToast (String err) {
		Toast toast = Toast.makeText(this, err, Toast.LENGTH_SHORT);
		TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
	//	v.setTextColor(Color.GREEN);
		toast.show(); 
//		Log.d(FILE, err);
	}
	
	protected void showToastLong (String err) {
		Toast toast = Toast.makeText(this, err, Toast.LENGTH_LONG);
		TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
		toast.show(); 
	}
	
}
