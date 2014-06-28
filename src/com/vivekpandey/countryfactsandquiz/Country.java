package com.vivekpandey.countryfactsandquiz;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.os.Build;

public class Country extends Activity {
	private ListView countryView;
	private ArrayAdapter<String>listAdapter;
	String countryName = "", continent = "", area = "", population = "", capital = "", gdp = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ArrayList<String>countryDisplayInfo = new ArrayList<String>();

		Intent intent = getIntent();
		countryName = intent.getStringExtra(Continent.COUNTRY_NAME);

		AssetManager assetManager = getBaseContext().getAssets();
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
				if (countryInfo.getName().equals(countryName)) {
					continent = countryInfo.getContinent();
					area = countryInfo.getArea();
					population = countryInfo.getPopulation();
					gdp = countryInfo.getGdp();
					capital = countryInfo.getCapital();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ScrollView scrollView = new ScrollView(this);
		scrollView.addView(new CountryDetailView(this, countryName, capital, population, area, gdp));
		setContentView(scrollView);
	}
}
