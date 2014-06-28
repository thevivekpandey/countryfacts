package com.vivekpandey.countryfactsandquiz;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CountryDetailView extends View {
    private Paint paint = new Paint();
    Context context;
    String country, capital;
    double population, area, gdp;

	public CountryDetailView(Context context, String country, String capital, 
							String population, String area, String gdp) {
		super(context);
		this.context = context;
		this.country = country;
		this.capital = capital;
		this.population = Double.parseDouble(population);
		this.area = Double.parseDouble(area);
		this.gdp = Double.parseDouble(gdp.replace(",", ""));
		init();
	}
    private void init(){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setTextSize(40);
        paint.setTextAlign(Align.CENTER);
    } 
    @Override
    protected void onMeasure(int x, int y) {
    	DisplayMetrics dm = context.getResources().getDisplayMetrics();
    	int screenWidth = dm.widthPixels;
    	int screenHeight = dm.heightPixels;
    	setMeasuredDimension(screenWidth, 1600);
    }
    @Override
    protected void onDraw(Canvas canvas) {
    	DisplayMetrics dm = context.getResources().getDisplayMetrics();
    	int screenWidth = dm.widthPixels;
    	int screenHeight = dm.heightPixels;
    	int gap = 180;

    	writeCountryName(canvas);
    	writeCapital(canvas);
    	int imgHeight = drawLocation(canvas);
    	String populationStr = canonicalize(population);
    	int populationAngle = (int) (180.0 / Math.log(1340000000.0 * Math.E / population));
    	drawGauge(canvas, "population", populationStr, "", 150, screenWidth/2 - gap, imgHeight + 550, populationAngle);

    	String areaString = canonicalize(area);
    	int areaAngle = (int) (180.0 / Math.log(18000000.0 * Math.E / area));
    	drawGauge(canvas, "area", Double.toString(area) + "km", "2", 150, screenWidth/2 + gap, imgHeight + 550, areaAngle);
    	writeGdp(canvas, gdp, 17000L, screenWidth / 2 - gap, imgHeight + 800);
    	writePerCapitaGdp(canvas, gdp, population, 100000L, screenWidth /2 + gap, imgHeight + 800);
    }
    private void writeGdp(Canvas canvas, double gdp, long max, int locX, int locY) {
    	String gdpToWrite = "$" + Double.toString(gdp) + " billion";
    	if (gdp < 0.001) {
    		gdp = 0.001;
    	}
    	int angle = (int)(180.0 / Math.log(17000.0 * Math.E/ gdp)); 
    	drawGauge(canvas, "GDP", gdpToWrite, "", 150, locX, locY, angle);
    }
    private void writePerCapitaGdp(Canvas canvas, double gdp, double population, long max, int locX, int locY) {
    	Double perCapitaGdpDouble = gdp * 1000000000 / population;
    	String perCapitaGdp = "$" + canonicalize(perCapitaGdpDouble);
    	int perCapitaGdpAngle = (int) (180.0 / Math.log(250000.0 * Math.E / perCapitaGdpDouble));
    	drawGauge(canvas, "per capita GDP", perCapitaGdp, "", 150, locX, locY, perCapitaGdpAngle);    	
    }
    private void writeCountryName(Canvas canvas) {
    	TextPaint textPaint = new TextPaint();
    	textPaint.setTextSize(40);
    	
    	StaticLayout textLayout = new StaticLayout(country, textPaint, canvas.getWidth(), 
    			                                  Alignment.ALIGN_CENTER, 1.0f, 1.0f, false);
    	canvas.save();
    	canvas.translate(0, 50);
    	textLayout.draw(canvas);
    	canvas.restore();
    }
    private void writeCapital(Canvas canvas) {
    	TextPaint textPaint = new TextPaint();
    	textPaint.setTextSize(40);
    	
    	StaticLayout capitalLayout = new StaticLayout("Capital: " + capital, textPaint, canvas.getWidth(), 
    											Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
    	canvas.save();
    	canvas.translate(30, 200);
    	capitalLayout.draw(canvas);
    	canvas.restore();    	

    }
    private int drawLocation(Canvas canvas) {
		int location_pos_y = 350;

    	DisplayMetrics dm = context.getResources().getDisplayMetrics();
    	int screenWidth = dm.widthPixels;
    	int screenHeight = dm.heightPixels;
    
    	TextPaint textPaint = new TextPaint();
    	textPaint.setTextSize(10);
    	textPaint.setColor(Color.RED);
    	
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(10);

		String normalizedCountryName = country.toLowerCase().replace("-", "").replace(" ", "");
    	int resid = getResources().getIdentifier(normalizedCountryName + "_location", "drawable", context.getPackageName());

    	Drawable drawable = getResources().getDrawable(resid);
    	Bitmap b = ((BitmapDrawable)drawable).getBitmap();
		int width = b.getWidth();
		int height = b.getHeight();
		
		int displayImageWidth = screenWidth > width ? width: screenWidth;
		int displayImageHeight = (int)((float)height * (float)displayImageWidth / (float)width);
		Bitmap bResized = Bitmap.createScaledBitmap(b, displayImageWidth, displayImageHeight, false);  	
		canvas.drawBitmap(bResized, screenWidth / 2 - displayImageWidth / 2, location_pos_y, textPaint);
		
		int left = screenWidth / 2 - displayImageWidth / 2;
		int top = location_pos_y;
		int right = screenWidth / 2 + displayImageWidth / 2;
		int bottom = location_pos_y + displayImageHeight;
		canvas.drawRoundRect(new RectF(left, top, right, bottom), 10, 10, paint);
		return displayImageHeight;
    }
    
    private void drawGauge(Canvas canvas, String field, String value, String superscript,
    				int arc_radius, int arc_center_x, int arc_center_y, int first_part) {
    	TextPaint textPaint = new TextPaint();
    	textPaint.setTextSize(40);

		textPaint.setStyle(Paint.Style.STROKE);
		textPaint.setStrokeWidth(40);
		textPaint.setColor(Color.CYAN);
		int left = arc_center_x - arc_radius;
		int right = arc_center_x + arc_radius;
		int top = arc_center_y - arc_radius;
		int bottom = arc_center_y + arc_radius;

		int second_part = 180 - first_part;
		RectF rect = new RectF(left, top, right, bottom);
		canvas.drawArc(rect, 180, first_part, false, textPaint);

		textPaint.setColor(Color.GRAY);
		canvas.drawArc(rect, 180 + first_part, second_part, false, textPaint);

		textPaint.setColor(Color.BLACK);
		textPaint.setStrokeWidth(1);
		textPaint.setTextAlign(Align.CENTER);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(30);

        LinearLayout layout = new LinearLayout(context);
        TextView textView = new TextView(context);
        textView.setText(Html.fromHtml(value + "<sup>" + superscript + "</sup>"));
        textView.setVisibility(View.VISIBLE);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.addView(textView);

        layout.measure(canvas.getWidth(), canvas.getHeight());
        layout.layout(0, 0, canvas.getWidth(), canvas.getHeight());

        canvas.translate(arc_center_x - textView.getWidth() / 2, arc_center_y - 40);
        layout.draw(canvas);
        canvas.translate(-arc_center_x + textView.getWidth() / 2, -arc_center_y + 40); 		
        
        textPaint.setTextSize(40);
		canvas.drawText(field, arc_center_x, arc_center_y + 40, textPaint);
    }
    String canonicalize(double v) {
    	String vStr = Double.toString(v);
    	DecimalFormat formatter = new DecimalFormat("#,###,###");
    	return formatter.format(Double.parseDouble(vStr));
    }
}
