package com.bullock.skimapp;


import java.util.ArrayList;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

public class SkiRun {

	private ArrayList<LatLng> 	latLongList;
	private String 				name;
	private SkiRunType          runType;
	private String              description;
	
	
	public SkiRun() {
		latLongList = new  ArrayList<LatLng>();
		runType     = SkiRunType.OTHER;
		description = "";
	}
	
	
	
	// Setters
	public void setName (String name){
		this.name = name;
	}
	
	public void addLatLong (double lat, double lon){
		latLongList.add(new LatLng (lat, lon));
	}
	public void setRunType(SkiRunType t){
		runType=t;
	}
	public void setDescription(String s){
		description = s;
	}
	
	
	
	// Getters
	public String getName(){
		return name;
	}
	
	public ArrayList<LatLng> getAllLatLong(){
		return latLongList;
	}
	public 	void dumpLatLong(){
		//TODO
		return;
	}
	public boolean hasMarker(){
		if (latLongList.size()!=0)
			return true;
		else
			return false;
	}
	public LatLng getMarkerPosition(){
		return latLongList.get(0);
	}
	
	
	// This is used for the colour of the polylines
	public int getColour() {  
		switch (runType){
			case     GREEN:
				return Color.GREEN;
			case	RED:
				return Color.RED;
			case 	BLUE:
				return Color.BLUE;
			case 	BLACK:
				return Color.BLACK;
		    default:
		    	return Color.YELLOW;
		}
	}
	
	

	
	// This returns the icons at the top/bottom of each run/lift
	// need a bespoke icon for black cos its not in the HSV colour spectrum
	// you beauty! http://mapicons.nicolasmollet.com/markers/sports/winter-sports/snowboarding/?custom_color=2f61de 
	public BitmapDescriptor getIcon() {
		switch (runType){
			case     GREEN:
				return BitmapDescriptorFactory.fromResource(R.drawable.snowboarding_green);
				
			case	RED:
				return BitmapDescriptorFactory.fromResource(R.drawable.snowboarding_red);
				
			case 	BLUE:
				return BitmapDescriptorFactory.fromResource(R.drawable.snowboarding_blue);
				
			case 	BLACK:
				return BitmapDescriptorFactory.fromResource(R.drawable.snowboarding_black);
				
		    default:
				return BitmapDescriptorFactory.fromResource(R.drawable.skilifting);

			}
	}
	
	public String getDescription() {  
		switch (runType){
		case     GREEN:
			return "Green - easy";
		case	RED:
			return "Red - fun!";
		case 	BLUE:
			return "Blue - smooth";
		case 	BLACK:
			return "Black - more fun!";
	    default:
	    	return description; // Only non-runs have this populated
		}
	}

	
	
	
	
}
