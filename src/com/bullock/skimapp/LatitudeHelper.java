package com.bullock.skimapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;



//
// Latitude shizzle
//		- all a bit lame tbh
//

public class LatitudeHelper {
	
    private static final String FILE = "LatitudeHelper";

	String userId;
	
	public LatitudeHelper (){
    	Log.d (FILE, ">>>>>  Constructor  =============================  ");

		userId = "-5870584271567092397"; //Owen   -949325085883133182 // ROB 
	}

	
	public LatLng getLocation (){
    	LatLng ll = null;

        String json =  doLocationRequest(userId);
    	Log.d (FILE, "				>>>>>  readJson: " + json);

    	
    	try {

    		JSONObject jsono = new JSONObject(json);

    		JSONArray features = (JSONArray)jsono.get("features");
    		JSONObject feature = features.getJSONObject(0);

    		JSONObject geometry = (JSONObject)feature.get("geometry");
 
    		JSONArray coordinates = (JSONArray)geometry.get("coordinates");
    		Log.d(FILE, "coords:"+coordinates.getDouble(0));
    		Log.d(FILE, "coords:"+coordinates.getDouble(1));
    		
    		ll = new LatLng(coordinates.getDouble(1), coordinates.getDouble(0));
        
    		JSONObject properties = (JSONObject)feature.get("properties");
    		Log.d(FILE, "timeStamp:"+properties.getLong("timeStamp"));
    		
    		Log.d(FILE, "accuracy:"+properties.get("accuracyInMeters"));

    //		Location location = new Location(properties.getLong("timeStamp")*1000,Double.toString(coordinates.getDouble(0)),Double.toString(coordinates.getDouble(1)),properties.getInt("accuracyInMeters"));
          } catch (Exception e) {
          e.printStackTrace();
        }
    	
    	return ll;
	}





    	  
//        try {
//          JSONObject j = new JSONObject(readJson);
//      //    JSONArray jsonArray = new JSONArray(readJson);
//          
//          
////          JSONObject j = jo.getJSONObject("results");
//          JSONArray features =  j.getJSONArray("features");
//          
//          Log.d(FILE,  "Number of entries in features array " + features.length());
//
//          
//          JSONObject jsonObject = features.getJSONObject(0);
//
//          Log.d(FILE," type is "+  jsonObject.getString("type"));
//      
//        } catch (Exception e) {
//          e.printStackTrace();
//	      System.exit(1);
//        }
//      
//        return ll;
//	}
	
//
// Json
//
//	{
//	    "type": "FeatureCollection",
//	    "features": [
//	        {
//	            "type": "Feature",
//	            "geometry": {
//	                "type": "Point",
//	                "coordinates": [
//	                    0.0517229,
//	                    51.6469671
//	                ]
//	            },
//	            "properties": {
//	                "id": "-5870584271567092397",
//	                "accuracyInMeters": 29,
//	                "timeStamp": 1358804819,
//	                "reverseGeocode": "Loughton, Essex, UK",
//	                "photoUrl": "http://www.google.com/latitude/apps/badge/api?type=photo&photo=Cq0VXzwBAAA.aeBTJEp0VU4yNcfnMHGYWg.ym_xaypG7T4qIV96uOl9YQ",
//	                "photoWidth": 96,
//	                "photoHeight": 96,
//	                "placardUrl": "http://www.google.com/latitude/apps/badge/api?type=photo_placard&photo=Cq0VXzwBAAA.aeBTJEp0VU4yNcfnMHGYWg.ym_xaypG7T4qIV96uOl9YQ&moving=true&stale=true&lod=1&format=png",
//	                "placardWidth": 56,
//	                "placardHeight": 59
//	            }
//	        }
//	    ]
//	}
	
	
    private  String  doLocationRequest (String userId){
    	
    	String url="http://www.google.com/latitude/apps/badge/api?user="+userId+"&type=json" ;
    	Log.d (FILE, "Making request to: "+url);

    	
    	StringBuilder builder = new StringBuilder();
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet(url);
	    httpGet.setHeader("Accept", "application/json");        
	    httpGet.setHeader("Content-type", "application/json");  
	    
	    try {
	      HttpResponse response = client.execute(httpGet);
	      StatusLine statusLine = response.getStatusLine();
	      int statusCode = statusLine.getStatusCode();
	
//	      HttpResponse response = httpClient.execute(request);           
//	      String tekst = EntityUtils.toString( response.getEntity() );
//	      
	      if (statusCode == 200) {
	        HttpEntity entity = response.getEntity();
	        
	        InputStream content = entity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	        String line;
	        while ((line = reader.readLine()) != null) {
	          builder.append(line);
	        }
	      } else {
	        Log.e(LatitudeHelper.class.toString(), "Failed to download file");
	      }
	    } catch (ClientProtocolException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    
	    return builder.toString();
    }	
	 
    


    
    	

//	$content = file_get_contents( $url );
//
//	// We convert the JSON to an object
//	$json = json_decode( $content );
//	$coord = $json->features[0]->geometry->coordinates;
//	// this will give you the coordinates of the user
//	echo $coord ;
//
//	$timeStamp = $json->features[0]->properties->timeStamp;
//	// timestamp of the last update
//
//	echo $timestamp ;
//	// If you want to retrieve the longitude & latitude separately 
//
//	$latitude=$coord[0];
//
//	$longitude=$coord[1];
//
//	echo "Your current coordinates  Latitude :".$latitude."  Longitude".$longitude ;
//	
//	return ll;
//}
}
