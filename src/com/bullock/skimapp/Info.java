package com.bullock.skimapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Info extends Activity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.info);
     
      
      String string1 = 
				"Double tap to zoom in. \n"
				+"Two finger tap to zoom out \n "
				+"\n"
				+"Markers show the top of runs,  and the bottom of lifts. \n"
				+"\n"
				+"To work without a data connection,  make sure you pre-cache the map. "
				+"This is done by running the App when you do have a data connection (WiFi"
                +" or mobile) and simply viewing the areas you want- they'll then be cached for offline use."
				+"\n "
//                +"\n Feedback:  mailto:owen.skimapp@gmail.com \n"
                ;
     
      
	 TextView infotext = (TextView)findViewById(R.id.info_content);
	 infotext.append("\n"+string1);
      
      
   }
}
