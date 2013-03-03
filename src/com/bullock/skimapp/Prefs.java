package com.bullock.skimapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;



public class Prefs extends PreferenceActivity{
	  private static final String FILE = "Prefs";

	    SharedPreferences 				 prefs;
	    OnSharedPreferenceChangeListener listener;

		@Override	    
	    @SuppressWarnings("deprecation")
		protected void onCreate(Bundle savedInstanceState) {
			  super.onCreate(savedInstanceState);
			  addPreferencesFromResource(R.xml.settings);
    	}
	   
    
    
	    //
	    // GPS 
	    //
	    public static boolean enable_gps (Context context) {
	    	boolean val = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("enable_gps", false);
	   		return val;
	    }
	    public static Integer gps_minGpsDistance(Context context) {
	    	Integer val = PreferenceManager.getDefaultSharedPreferences(context).getInt("gps_minGpsDistance",1);
	   		return val;
	    }
	    public static Integer gps_minGpsTime(Context context) {
 	   		Integer val = PreferenceManager.getDefaultSharedPreferences(context).getInt("gps_minGpsTime",400);
	   		return val;
	    }
	   

	   
	    public static boolean enable_gps_debug (Context context) {
	   		boolean val = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("enable_gps_debug", false);
	   		return val;
	    }

	    //
	    // Compass
	    //
	    public static boolean enable_compass (Context context) {
	   		boolean val = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("enable_compass", false);
	   		return val;
	    }
	    public static Integer gps_dampingValue(Context context) {
 	   		Integer val = PreferenceManager.getDefaultSharedPreferences(context).getInt("compass_damping",500);
	   		return val;
	    }
	    
	    
	    //
	    // Map type
	    //
	    public static boolean maptype_terrain (Context context) {
	   		boolean val = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("maptype_terrain", false);
	   		return val;
	    }
	    
	    public static float lineWidth (Context context) {
	   		String s_width = PreferenceManager.getDefaultSharedPreferences(context).getString("lineWidth", context.getString(R.string.default_linewidth));
	   		Float f_width = Float.valueOf(s_width); 
	   		return f_width;
	    }
	    
		   
	    
	    
}
