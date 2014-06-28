package com.vivekpandey.countryfactsandquiz;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.GridLayout.Spec;

public class TiledActivity extends Activity implements View.OnClickListener {
	String childIntentText;
	Adapter adapter;
	Class childClass;
	int constantSize;
	OnQueryTextListener onQueryTextListener;
	SearchView searchView;
	String title;
	public static final int NUM_COLS = 4;
	public static final int NUM_ROWS = 200;

	public void setChildIntentText(String childIntentText) {
		this.childIntentText = childIntentText;
	}
	public void setAdapter(Adapter adapter) {
		this.adapter = adapter;
	}
	public void setChildClass(Class childClass) {
		this.childClass = childClass;
	}
	public void setConstantSize(int constantSize) {
		this.constantSize = constantSize;
	}
	public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener) {
		this.onQueryTextListener = onQueryTextListener;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	void populateDisplay() {
		assert(adapter != null);
		assert(adapter.getCount() < NUM_ROWS);
		
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int screenWidth = size.x;

		GridLayout gridLayout = new GridLayout(this);
		gridLayout.setColumnCount(NUM_COLS);
		gridLayout.setRowCount(NUM_ROWS);

		ArrayList<TextView> tv = new ArrayList<TextView>(SpecOrganizer.MAX_ITEMS);
		SpecOrganizer specOrganizer = new SpecOrganizer(SpecOrganizer.MAX_ITEMS, constantSize);
		Resources resources = getResources();
		//for (int i = 0; i < 20; i++) {
		for (int i = 0; i < adapter.getCount(); i++) {
			Tile tile = specOrganizer.getTile(i);
			/* Note that colSpec depends on X and rowSpec depends on Y. */
			Spec colSpec = GridLayout.spec(tile.getX(), tile.getWidth());
			Spec rowSpec = GridLayout.spec(tile.getY(), tile.getHeight());
			GridLayout.LayoutParams lp = new GridLayout.LayoutParams(rowSpec, colSpec);
			lp.setMargins(8, 8, 8, 8);
			assert(tile.getWidth() == 1 || tile.getWidth() == 2);
			assert(tile.getHeight() == 1 || tile.getHeight() == 2);
			if (tile.getWidth() == 1) {
				lp.width = (screenWidth - 64) / 4;
			} else {
				lp.width = (screenWidth - 32) / 2;
			}
			if (tile.getHeight() == 1) {
				lp.height = (screenWidth - 64) / 4;
			} else {
				lp.height = (screenWidth - 32) / 2;
			}
			ContinentView textView = new ContinentView(this, tile.getX(), tile.getY(), tile.getWidth());
			tv.add(textView);
			textView.setLayoutParams(lp);
			textView.setGravity(Gravity.BOTTOM);
			textView.setText(adapter.getItem(i).toString());
			String normalizedCountryName = adapter.getItem(i).toString().toLowerCase().replace("-", "").replace(" ", "");
			if (resources.getIdentifier(normalizedCountryName, "drawable", getPackageName()) != 0) {
				int resid = resources.getIdentifier(normalizedCountryName, "drawable", getPackageName());
				Drawable drawable = resources.getDrawable(resid);
				Bitmap b = ((BitmapDrawable)drawable).getBitmap();
				int width = b.getWidth();
				int height = b.getHeight();
				float aspectRatio = (float)height / (float)width;
				float desiredAspectRatio = (float)lp.height / (float) lp.width;
				Bitmap bRounded;
				if (desiredAspectRatio > aspectRatio) {
					float newWidth = (float)lp.width * (float)height / (float)lp.height;
					float newHeight = (float)height;
					Bitmap bCropped = Bitmap.createBitmap(b, (int)(width - newWidth) / 2, 0, (int)newWidth, (int)newHeight);
					Bitmap bResized = Bitmap.createScaledBitmap(bCropped, lp.width, lp.height, false);
					bRounded = getRoundedCornerBitmap(bResized);
				} else {
					float newHeight = (float)lp.height * (float)width / (float)lp.width;
					float newWidth = (float)width;
					Bitmap bCropped = Bitmap.createBitmap(b, 0, (int)(height - newHeight) / 2, (int)newWidth, (int)newHeight);
					Bitmap bResized = Bitmap.createScaledBitmap(bCropped, lp.width, lp.height, false);
					bRounded = getRoundedCornerBitmap(bResized);
				}			
				Drawable drawableResized = new BitmapDrawable(resources, bRounded);
				textView.setBackground(drawableResized);
				//textView.setBackgroundResource(resources.getIdentifier(normalizedCountryName, "drawable", getPackageName()));
			}
			textView.setTextAppearance(this, android.R.style.TextAppearance_Holo_Small);
			textView.setTextColor(Color.WHITE);
			
			textView.setOnClickListener(this);
			
			gridLayout.addView(textView, lp);
		}
		ScrollView scrollView = new ScrollView(this);
		scrollView.addView(gridLayout);
		setContentView(scrollView);
	}
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	    bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = 12;

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);

	    return output;
	  }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the options menu from XML
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main_actions, menu);
	    
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    searchView = (SearchView) menu.findItem(R.id.search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

	    searchView.setOnQueryTextListener(onQueryTextListener);
	    return super.onCreateOptionsMenu(menu);
	}	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getActionBar().setTitle(title);
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		populateDisplay();
	}
	
	@Override
	public void onClick(View view) {
		Intent intent = new Intent(getApplicationContext(), childClass);
		intent.putExtra(childIntentText, ((TextView)view).getText().toString());
		startActivity(intent);
	}
}
