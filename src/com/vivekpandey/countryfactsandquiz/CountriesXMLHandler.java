package com.vivekpandey.countryfactsandquiz;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class CountriesXMLHandler extends DefaultHandler {
	boolean currentElement = false;
	String currentValue = "";
	CountryInfo countryInfo;
	ArrayList<CountryInfo> countryList;
		
	public ArrayList<CountryInfo> getCountryList() {
		return countryList;
	}
	public void startElement(String uri, String localName, 
	String qName, Attributes attributes) throws SAXException {
		currentElement = true;
		if (qName.equals("countries")) {
			countryList = new ArrayList<CountryInfo>();
		} else if (qName.equals("country")) {
			countryInfo = new CountryInfo();
		}
	}
	public void endElement(String uri, String localName,
	String qName) throws SAXException {
		currentElement = false;
		if (qName.equals("name")) {
			countryInfo.setName(currentValue.trim());
		} else if (qName.equals("continent")) {
			countryInfo.setContinent(currentValue.trim());
		} else if (qName.equals("area")) {
			countryInfo.setArea(currentValue.trim());
		} else if (qName.equals("population")) {
			countryInfo.setPopulation(currentValue.trim());
		} else if (qName.equals("capita")) {
			countryInfo.setGdp(currentValue.trim());
		} else if (qName.equals("capital")) {
			countryInfo.setCapital(currentValue.trim());
		} else if (qName.equals("gdp")) {
			countryInfo.setGdp(currentValue.trim());
		} else if (qName.equals("country")) {
			countryList.add(countryInfo);
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
