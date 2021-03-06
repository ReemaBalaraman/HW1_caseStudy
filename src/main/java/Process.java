package main.java;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
			NodeList nodes = fetchXMLData("WIC_clinics.xml");
			List<String> zipCodeWIC = new ArrayList<String>();

			//Looping through each node
			for (int i = 1; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					zipCodeWIC.add(getValue("zipcode", element));
				}
			}

			Map<String,String> zipCodes = zipCodeMap();
			HashMap< String,Float> birthRate = new HashMap< String,Float>();
			HashMap< String,Float> cancer = new HashMap< String,Float>();
			HashMap< String,Float> mortalityRate = new HashMap< String,Float>();

			// 2. Fetching data from Public health data set
			NodeList nodes_second = fetchXMLData("Health_stats.xml");
			Map<String,String> community_zipCode = new HashMap<String,String>();

			//Looping through each node
			for (int i = 1; i < nodes_second.getLength(); i++) {
				Node node = nodes_second.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String communityAreaName = getValue("community_area_name", element);
					String communityAreaCode = getValue("community_area", element);
					String birthRatestr = getValue("birth_rate", element);
					String cancerStr = getValue("breast_cancer_in_females", element);
					String mortalityRateStr = getValue("infant_mortality_rate", element);

					if(zipCodes.containsKey(communityAreaCode))
					{
						community_zipCode.put(zipCodes.get(communityAreaCode),communityAreaName);
						birthRate.put(communityAreaCode ,Float.parseFloat(birthRatestr));
						cancer.put(communityAreaCode ,Float.parseFloat(cancerStr));
						mortalityRate.put(communityAreaCode , Float.parseFloat(mortalityRateStr));
					}

				}
			}

			/* Start sorting*/
			Comparator<String> comparator1 = new ValueComparator<String,Float>(birthRate);
			TreeMap<String,Float> sortedBirthRate = new TreeMap<String,Float>(comparator1);
			sortedBirthRate.putAll(birthRate);

			Comparator<String> comparator2 = new ValueComparator<String,Float>(cancer);
			TreeMap<String,Float> sortedCancer = new TreeMap<String,Float>(comparator2);
			sortedCancer.putAll(cancer);

			Comparator<String> comparator3 = new ValueComparator<String,Float>(mortalityRate);
			TreeMap<String,Float> sortedMortalityRate = new TreeMap<String,Float>(comparator3);
			sortedMortalityRate.putAll(mortalityRate);
			/* End sorting*/


			/* Start : Processing result */
			//OutPut Heading :
			System.out.println("The result shown below is basd on the analysis carried"
					+ " out for identifying areas effected with low birth rate,high breast cancer rate and high infant mortality rate and doesnt have a WIC clinic. ");
			System.out.println("RESULT : ");

			//Check for Birth Rate area
			String key = sortedBirthRate.firstKey();

			if(!(zipCodeWIC.contains(zipCodes.get(key))))
			{
				System.out.println("=> "+ community_zipCode.get(zipCodes.get(key)) + " " + "area with" + " " 
						+ "zip code"+" " + zipCodes.get(key) +" " + "has a low birth rate of" + " "+sortedBirthRate.get(key)
						+ " " + "and according to WIC data set this area doesn't have a functional WIC clinic . ");
			}

			//Check for Cancer area
			String key1 = sortedCancer.lastKey();
			if(!(zipCodeWIC.contains(zipCodes.get(key1))))
			{
				System.out.println("=> "+ community_zipCode.get(zipCodes.get(key1)) + " " + "area with" + " " 
						+ "zip code"+ " " +zipCodes.get(key1) +" " + "has a high breast cancer rate of" +" "+ sortedCancer.get(key1)
						+ " " + "and according to WIC data set this area doesn't have a functional WIC clinic. ");
			}
			//Check for Mortality Rate area
			String key2 = sortedMortalityRate.firstKey();
			if(!(zipCodeWIC.contains(zipCodes.get(key2))))
			{
				System.out.println("=> "+ community_zipCode.get(zipCodes.get(key2)) + " " + "area with" + " " 
						+ "zip code"+" "+ zipCodes.get(key2) +" " + "has a high infant mortality rate of" +" "+ sortedMortalityRate.get(key2)
						+ " " + "and according to WIC data set this area doesn't have a functional WIC clinic. ");
			}

			/* End : Processing result */
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}	
	}

	//to read and parse XML data sets
	public NodeList fetchXMLData(String xmlName)
	{
		NodeList nodes = null;
		try
		{
			File details = new File("src/main/resources/" + xmlName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(details);
			doc.getDocumentElement().normalize();

			nodes = doc.getElementsByTagName("row");

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return nodes;
	}

	//to read each row values
	public static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}



	/* Fetching data from grocery list data set 	
    to map community area zip code with WIC data set */
	public Map<String,String> zipCodeMap()
	{
		Map<String,String> zipCodes = new HashMap<String,String>(); 
		try{
			NodeList nodes = fetchXMLData("Area_zipCode.xml");
			//Looping through each node

			for (int i = 1; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String zipcode = getValue("zip_code", element);
					String communityArea = getValue("community_area", element);
					zipCodes.put(communityArea,zipcode );

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
