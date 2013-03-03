package com.bullock.skimapp;

import java.util.ArrayList;

import android.app.Activity;
import android.widget.TextView;

public class MessageWindow {
	
//	int allMsgsMax;
//	String[] allMsgs = new String[allMsgsMax];
//	String[] allMsgsDisplay = new String[allMsgsMax];
//	int allMsgsCount;
//	
//	private Activity parentActivity;
//
//	public MessageWindow(Activity act, int allMsgsMax, int allMsgsCount) {
//		this.parentActivity = act;
//		this.allMsgsMax = allMsgsMax;
//		this.allMsgsCount = allMsgsCount;
//		
//		allMsgs        = new String[allMsgsMax] ;
//		allMsgsDisplay = new String[allMsgsMax];
//		
//		for (int i = 0; i < allMsgsMax; i++){
//			allMsgs[i]= "";
//			allMsgsDisplay[i]= "";
//		}
//	}
//	
//	//
//	//
//	//
//	public void put (String msg){
//	
//		if (allMsgsCount == allMsgsMax-1)
//		for (int i = 1; i < allMsgsMax; i++){
//			if (!allMsgs[i].equals("")) {
//			
//				allMsgsDisplay[i-1]=allMsgs[i];
//			}
//		}
//		
//		allMsgsDisplay[allMsgsCount++] = msg;
//		if (allMsgsCount > allMsgsMax-1) {  allMsgsCount = allMsgsMax-1; };
//
//
//		for ( int i = 0 ; i < allMsgsMax; i++ ){
//			allMsgs[i] = allMsgsDisplay[i];
//		}
//		
//		TextView t2v =  (TextView) parentActivity.findViewById(R.id.messages);
//		t2v.setText(allMsgsDisplay[0]+ "\n" + 
//					allMsgsDisplay[1]+ "\n" +
//					allMsgsDisplay[2]+ "\n");
//	}
//	
	
	
	
	private  ArrayList <String>  msgs;  
	private  Activity parentActivity;

	public MessageWindow(Activity act, int allMsgsMax) {
		this.parentActivity = act;
		
		msgs = new ArrayList <String> ();
		
		for (int i = 0; i < allMsgsMax; i++){
			msgs.add("");
		}
	}
	
	//
	//
	//
	public void put (String msg){
 
		msgs.remove(0);
		msgs.add(msg);
		
		TextView t2v =  (TextView) parentActivity.findViewById(R.id.messages);
		t2v.setText(msgs.get(0) + "\n" + 
				    msgs.get(1) + "\n" +
			     	msgs.get(2) );
	}
}

 

