package com.vivekpandey.countryfactsandquiz;

import android.widget.TextView;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.RectF;

public class ContinentView extends TextView {
    private Paint paint = new Paint();
    public static final int PADDING = 8;
    public static final int RADIUS = 10;
    public static final int BORDER_WIDTH = 2;
    int x, y, width;
    RectF rect;

    public ContinentView(Context context, int x, int y, int width) {
        super(context);
        init();
        this.x = x;
        this.y = y;
        this.width = width;
    }
    public ContinentView(Context context) {
    	super(context);
    	init();
    	this.x = 0;
    	this.width = width;
    }
    private void init(){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(BORDER_WIDTH);
    }
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	rect = new RectF(0, 0, getWidth(), getHeight());
    	//rect = new RectF(left, top, getWidth() - right, getHeight() - bottom);
    	canvas.drawRoundRect(rect, RADIUS, RADIUS, paint);
    }
}