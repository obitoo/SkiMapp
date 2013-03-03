package com.bullock.skimapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.WindowManager;





public class Compass {

	private static final String FILE = "Compass";
	
    private boolean compass = true;
    protected SensorManager mSensorManager;
    private Sensor mSensor;
    
    private SkiMapp parent;

    private float damping = 500;//Prefs.gps_dampingValue(parent.getBaseContext()); 

    // --------------------------------------------------------------------
    
    public Compass(SkiMapp x) {
    	parent = x;
    }
    
    public void setDamping (int d) {
    	damping = d;
    }
    
    public void unregister() {
    	if (mSensorManager != null)  // overkill? 
    		mSensorManager.unregisterListener(mListener);
    }
    
    public void register() {
    	if (mSensorManager != null)  // overkill?
     	  mSensorManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
  
    @SuppressWarnings({ "deprecation", "unused" })
	public void initService(){
        if (Prefs.enable_compass(parent.getBaseContext())){
        	mSensorManager = (SensorManager)parent.getSystemService(Context.SENSOR_SERVICE);
        	mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);//TYPE_MAGNETIC_FIELD);
        }
    }
    // --------------------------------------------------------------------
    
    
    private final SensorEventListener mListener = new SensorEventListener() {

        private final float[] mScale = new float[] { 2, 2.5f, 0.5f };   // accel
        private float[] mPrev = new float[3];
        private long mLastShowTime;

        
                
        // Only worry about [0] ie x for now
        public void onSensorChanged(SensorEvent event) {
            
        	// if option not selected, do nothing
            if (!Prefs.enable_compass(parent.getBaseContext()))
            	return;
        	
        	boolean show = false;

            float diff = Math.round(event.values[0] - mPrev[0]);
            if (Math.abs(diff) > 10) {
                show = true;
            }

           long now = android.os.SystemClock.uptimeMillis();
//           Log.e(FILE, "now:"+now+", mLastGestureTime:"+mLastGestureTime);

           
           if (now - mLastShowTime < damping) 
        	   show = false;
           else 
        	   mLastShowTime = now+1;
           
       
            if (show) {
                mPrev[0] = event.values[0];

                // only shows if we think the delta is big enough, in an attempt
                // to detect "serious" moves left/right or up/down
            	String stuff =  "sensorChanged " + event.sensor.getName() +
                        " \n(" + event.values[0] + ", " + event.values[1] + ", " +
                        event.values[2] + ")";
                
//                Log.e(FILE, stuff + "diff:"+diff);
                
                // set the map
                // TODO - loop in parent? update every second based on average over last x seconds
                if (parent.mMap != null) 
                	parent.setMapOrientation(event.values[0], (damping < 100 ? false : true) );
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    
}
