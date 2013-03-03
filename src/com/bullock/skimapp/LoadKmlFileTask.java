package com.bullock.skimapp;

import org.apache.http.client.ClientProtocolException;

import android.os.AsyncTask;
import android.util.Log;
import android.view.InflateException;

    //
    // Background thread - read the kml file
    //



public class LoadKmlFileTask extends AsyncTask<String, String, Integer> {

	private static final String FILE = "LoadKmlFileTask";

	private SkiMapp parent;
    private FileParser fileParser;
	
    //---------------------------------------------------
	public LoadKmlFileTask(SkiMapp m) {
		parent = m;
		fileParser = new FileParser(parent);
	}

	public void publish (String str){
		publishProgress(str);
		return;
	}
	
	//---------------------------------------------------
	
    protected Integer doInBackground(String... filename) {
//    	Log.d (FILE, ">>>>>  doInBackground() - start");

    	fileParser.readKmlFile(this);
    	
//    	Log.d (FILE, ">>>>>  doInBackground() - end");
        return 0;
    }

    protected void onProgressUpdate(String... progress) {
	   parent.messageWindow.put(progress[0]); 
    }

    @Override
    protected void onPostExecute(Integer result) {
//    	Log.d (FILE, ">>>>>  onPostExecute() - <<<<<<<<<<<<<<");
    	parent.messageWindow.put("done, loading map"); 
    
        // Now show the map
    	try{
    		parent.setContentView(R.layout.basic_demo);
    		parent.setUpMapIfNeeded();
    	}  catch (InflateException e) {
  	      // ignore and carry on
          parent.showErrorToast("Ooops, error inflating map view");
  	      //e.printStackTrace();    
  	    }
    }
}
