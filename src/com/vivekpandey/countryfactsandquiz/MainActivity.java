package com.vivekpandey.countryfactsandquiz;


import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView.OnQueryTextListener;

public class MainActivity extends TiledActivity {
	public final static String CONTINENT_NAME = "com.example.countryfactsandquiz.CONTINENT_NAME";

	final OnQueryTextListener searchQueryListener = new OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
        	setAdapter(new MainAdapter(getApplicationContext(), query));
        	populateDisplay();
            return true;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
        	setAdapter(new MainAdapter(getApplicationContext(), newText));
        	populateDisplay();
        	return true;
        }
    };	    

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MainAdapter mainAdapter = new MainAdapter(this, "");
		super.setChildIntentText(CONTINENT_NAME);
		super.setOnQueryTextListener(searchQueryListener);
		super.setTitle("Continents");
		super.setAdapter(mainAdapter);
		super.setConstantSize(0);
		super.setChildClass(Continent.class);
		super.onCreate(savedInstanceState);
	}
}
