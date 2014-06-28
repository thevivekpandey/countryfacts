package com.vivekpandey.countryfactsandquiz;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.TextView;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

public class ContinentAdapter extends BaseAdapter {
	private Context mContext;
	ArrayList<String>countryNames = new ArrayList<String>();

	public ContinentAdapter(Context c, String continentName) {
		this(c, continentName, "");
	}
	public ContinentAdapter(Context c, String continentName, String searchText) {
		mContext = c;

		AssetManager assetManager = c.getAssets();
		try {
			InputStream is = assetManager.open("countries.xml");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			CountriesXMLHandler countriesXMLHandler = new CountriesXMLHandler();
			xr.setContentHandler(countriesXMLHandler);
			InputSource inStream = new InputSource(is);
			xr.parse(inStream);

			ArrayList<CountryInfo> countryList = countriesXMLHandler.getCountryList();
			for (CountryInfo countryInfo: countryList) {
				if (countryInfo.getContinent().equals(continentName)) {
					String countryName = countryInfo.getName();
					if (countryName.toLowerCase().contains(searchText.toLowerCase())) {
						countryNames.add(countryName);						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public int getCount() {
		return countryNames.size();
	}

	public Object getItem(int position) {
		return countryNames.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ContinentView countryView;
		if (convertView == null) {
			countryView = new ContinentView(mContext);
			countryView.setText(countryNames.get(position));
			countryView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			countryView.setTextSize(20);
			countryView.setTextColor(Color.WHITE);
			countryView.setHeight(250);
			return countryView;
		} else {
			return convertView;
		}
		
	}
}
