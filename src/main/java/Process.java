package main.java;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




public class Process {

	/*function to read XML file */
	public void readXML ()
	{

		try{
			// 1. Fetching data from WIC health clinic data set
			File details = new File("src/main/resources/WIC_clinics.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(details);
			doc.getDocumentElement().normalize();

			NodeList nodes = doc.getElementsByTagName("row");
			//Looping through each node
			for (int i = 1; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					System.out.println("Site name: " + getValue("site_name", element));
				}
			}

 Map zipCodes = zipCodeMap();
			
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}	
	}

	//to read each row values
	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}

	/* Fetching data from grocery list data set 	
    to map community area zip codewith WIC data set */
	private Map zipCodeMap()
 {
		Map<String,String> zipCodes = new HashMap<String,String>(); 
	 try{
	 File details = new File("src/main/resources/Area_zipCode.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(details);
	doc.getDocumentElement().normalize();
 
	NodeList nodes = doc.getElementsByTagName("row");
	//Looping through each node
	for (int i = 1; i < nodes.getLength(); i++) {
		Node node = nodes.item(i);
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			System.out.println("zipcode: " + getValue("zip_code", element));
			String zipcode = getValue("zip_code", element);
			String communityArea = getValue("community_area", element);
			zipCodes.put(zipcode, communityArea);
		}
	}
	 }
	 catch (Exception ex) {
			ex.printStackTrace();
		} 
	 return zipCodes;
 }
	public static void main(String [ ] args)
	{
		Process p = new Process ();
		p.readXML();	
	}
}
