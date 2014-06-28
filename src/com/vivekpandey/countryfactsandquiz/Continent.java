package com.vivekpandey.countryfactsandquiz;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView.OnQueryTextListener;
import android.content.Intent;
public class Continent extends TiledActivity {
	public final static String COUNTRY_NAME = "com.example.countryfactsandquiz.COUNTRY_NAME";

	final OnQueryTextListener searchQueryListener = new OnQueryTextListener() {
	        @Override
	        public boolean onQueryTextSubmit(String query) {
	    		Intent intent = getIntent();
	    		String continentName = intent.getStringExtra(MainActivity.CONTINENT_NAME);

	        	setAdapter(new ContinentAdapter(getApplicationContext(), continentName, query));
	        	populateDisplay();
	            return true;
	        }
	        @Override
	        public boolean onQueryTextChange(String newText) {
	    		Intent intent = getIntent();
	    		String continentName = intent.getStringExtra(MainActivity.CONTINENT_NAME);
	        	setAdapter(new ContinentAdapter(getApplicationContext(), continentName, newText));
	        	populateDisplay();
	        	return true;
	        }
	    };	    

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		String continentName = intent.getStringExtra(MainActivity.CONTINENT_NAME);

		ContinentAdapter continentAdapter = new ContinentAdapter(this, continentName);
		super.setChildIntentText(COUNTRY_NAME);
		super.setOnQueryTextListener(searchQueryListener);
		super.setTitle(continentName);
		super.setAdapter(continentAdapter);
		super.setConstantSize(-1);
		super.setChildClass(Country.class);
		super.onCreate(savedInstanceState);
	}
}

