package com.vivekpandey.countryfactsandquiz;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.widget.BaseAdapter;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

public class MainAdapter extends BaseAdapter {
	private Context mContext;
	ArrayList<String>continentNames = new ArrayList<String>();
	
	public MainAdapter(Context c) {
		this(c, "");
	}
	public MainAdapter(Context c, String searchText) {
		mContext = c;

		AssetManager assetManager = c.getAssets();
		try {
			InputStream is = assetManager.open("continents.xml");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			ContinentsXMLHandler continentsXMLHandler = new ContinentsXMLHandler();
			xr.setContentHandler(continentsXMLHandler);
			InputSource inStream = new InputSource(is);
			xr.parse(inStream);

			ArrayList<ContinentInfo> continentList = continentsXMLHandler.getContinentList();
			for (ContinentInfo continentInfo: continentList) {
				String continentName = continentInfo.getName();
				if (continentName.toLowerCase().contains(searchText.toLowerCase())) {
					continentNames.add(continentInfo.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public int getCount() {
		return continentNames.size();
	}

	public Object getItem(int position) {
		return continentNames.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ContinentView continentView;
		if (convertView == null) {
			continentView = new ContinentView(mContext);
			continentView.setText(continentNames.get(position));
			continentView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			continentView.setTextSize(20);
			continentView.setTextColor(Color.WHITE);
			continentView.setHeight(250);
			return continentView;
		} else {
			return convertView;
		}
		
	}
}
