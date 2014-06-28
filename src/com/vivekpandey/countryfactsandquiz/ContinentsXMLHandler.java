package com.vivekpandey.countryfactsandquiz;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class ContinentsXMLHandler extends DefaultHandler {
	boolean currentElement = false;
	String currentValue = "";
	ContinentInfo continentInfo;
	ArrayList<ContinentInfo> continentList;
	
	public ArrayList<ContinentInfo> getContinentList() {
		return continentList;
	}
	public void startElement(String uri, String localName, 
	String qName, Attributes attributes) throws SAXException {
		currentElement = true;
		if (qName.equals("continents")) {
			continentList = new ArrayList<ContinentInfo>();
		} else if (qName.equals("continent")) {
			continentInfo = new ContinentInfo();
		}
	}
	public void endElement(String uri, String localName,
	String qName) throws SAXException {
		currentElement = false;
		if (qName.equals("name")) {
			continentInfo.setName(currentValue.trim());
		} else if (qName.equals("continent")) {
			continentList.add(continentInfo);
		}
		currentValue = "";
	}
	public void characters(char[] ch, int start, int length) 
	throws SAXException {
		if (currentElement) {
			currentValue += new String(ch, start, length);
		}
	}
}
