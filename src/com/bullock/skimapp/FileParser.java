package com.bullock.skimapp;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

import com.bullock.skimapp.R;


// TODO - parameterise filename

public class FileParser {

	private static final String FILE = "FileReader";
	private SkiMapp parent;
	
	public FileParser (SkiMapp m) {
		parent = m;
	}
	
    public void readKmlFile(LoadKmlFileTask caller) {
//    	Log.d (FILE, ">>>>> readKmlFile()");
    	
    	InputStream kml = parent.getResources().openRawResource(R.raw.portes_du_soleil);
    	
    	if (kml == null) {
    		parent.showErrorToast ("Error - cannot open resource R.raw.portes_du_soleil.kml");
    		return;
    	}
    	
        
        //
        // XPATH Setup
        //
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
 //       domFactory.setNamespaceAware(true); // never forget this! Owen - errrr?
        DocumentBuilder builder;
        Document doc = null;
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr;
    
        try {
			
			builder = domFactory.newDocumentBuilder();
	        doc = builder.parse(kml);
	        
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
        
        // Now extract data
        
        // all the name nodes
        try {
        	Node sibling;



        	String name;
        	
        	// All Placemark/names
			expr = xpath.compile("//Placemark/name");
	        Object result = expr.evaluate(doc, XPathConstants.NODESET);
	        NodeList nodes = (NodeList) result;
	        
//			Log.d(FILE, ">>>>>>>>>>>>> nodes.getLength() = " + (nodes.getLength())); 
			// update the UI
			caller.publish("found " +nodes.getLength()+ "nodes");

			// loop for each <name>
	        for (int i = 0; i < nodes.getLength(); i++) {
	        	SkiRun skiRun = new SkiRun();
	        	name = nodes.item(i).getTextContent();

	        	skiRun.setName(name);
	        	parent.allSkiRuns.add(skiRun);
				
//	        	//debug
//				if (!name.equals("Essert")){
//					continue;
//				}
				
//				Log.d(FILE, "loading run:  " + name ); 
				// update the UI
				caller.publish("loading run: " + name );


				// parse siblings. Look for LineString/Coordinates
		        sibling = nodes.item(i);
				while (sibling != null){
					processSibling(sibling, skiRun);
					sibling = sibling.getNextSibling();
				}
	        }
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    
    private void processSibling(Node sibling, SkiRun skiRun){
    	
		String type = sibling.getNodeName();
    	String data = sibling.getTextContent();
    	NodeList childNodes;
    	NodeList grandchildNodes;
    	Node     grandchild;

		if (type.equals("LineString")){
			
			childNodes = sibling.getChildNodes();
			extractCoordinates(childNodes, skiRun);

		} else if (type.equals("styleUrl")){

			String styleUrl = sibling.getTextContent(); 
//	    	Log.d(FILE, "        styleUrl =" +styleUrl);
	    	
	    	if (styleUrl.contains("msn_black")){
	    		skiRun.setRunType(SkiRunType.BLACK);
	    	}
	    	if (styleUrl.contains("msn_green")){
	    		skiRun.setRunType(SkiRunType.GREEN);
	    	}
	    	if (styleUrl.contains("msn_blue")){
	    		skiRun.setRunType(SkiRunType.BLUE);
	    	}
	    	if (styleUrl.contains("msn_red")){
	    		skiRun.setRunType(SkiRunType.RED);
	    	}
	    	// (SkiRun defaults all other run types to Yellow)
	    	
	    	// Lifts are also defined here. We set the description in that case
	    	if (styleUrl.contains("msn_cablecar")){
	    		skiRun.setDescription("CableCar");
	    	}
			if (styleUrl.contains("msn_chair")){
	    		skiRun.setDescription("Chair lift");
			}
	    	if (styleUrl.contains("msn_tow")){
	    		skiRun.setDescription("Drag lift");
	    	}

	    	
	    	
	    	
	    	
		} else if (type.equals("MultiGeometry")){
			
			// go down another level, and recurse
			grandchildNodes = sibling.getChildNodes();
			
	        grandchild = grandchildNodes.item(0);
			while (grandchild != null){
				processSibling(grandchild, skiRun);
				grandchild = grandchild.getNextSibling();
			}
			
		}else {
				//Log.d(FILE, "       ---  " + type + ":" + data ); 
		}
    }
    
    private void extractCoordinates (NodeList childNodes, SkiRun skiRun){
    	String coords;
    	String[] pairs;
    	
	    for (int f = 0; f < childNodes.getLength(); f++) {
	    	if (childNodes.item(f).getNodeName().equals("coordinates")){
	    			
	    		coords = childNodes.item(f).getTextContent(); 
	    		pairs = coords.trim().split(" ");
	    		for (int loop = 0; loop < pairs.length; loop++){
	    			String[] lngLat = pairs[loop].split(","); 
	    			// add to bean
	    		    skiRun.addLatLong(new Double(lngLat[1]), new Double(lngLat[0]));
	    		}
	    	}
	    }
	    return;
    }
    
}
