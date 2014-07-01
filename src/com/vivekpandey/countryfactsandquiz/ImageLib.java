package com.vivekpandey.countryfactsandquiz;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.widget.GridLayout;

public class ImageLib {
	public static Bitmap normalize(Bitmap bitmap, GridLayout.LayoutParams lp) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Log.v("VIVEK", "Size is " + width + ", " + height);
		float aspectRatio = (float)height / (float)width;
		float desiredAspectRatio = (float)lp.height / (float) lp.width;
		Bitmap bRounded;
		if (desiredAspectRatio > aspectRatio) {
			float newWidth = (float)lp.width * (float)height / (float)lp.height;
			float newHeight = (float)height;
			Bitmap bCropped = Bitmap.createBitmap(bitmap, (int)(width - newWidth) / 2, 0, (int)newWidth, (int)newHeight);
			Bitmap bResized = Bitmap.createScaledBitmap(bCropped, lp.width, lp.height, false);
			//bRounded = getRoundedCornerBitmap(bResized);
			bRounded = bResized;
		} else {
			float newHeight = (float)lp.height * (float)width / (float)lp.width;
			float newWidth = (float)width;
			Bitmap bCropped = Bitmap.createBitmap(bitmap, 0, (int)(height - newHeight) / 2, (int)newWidth, (int)newHeight);
			Bitmap bResized = Bitmap.createScaledBitmap(bCropped, lp.width, lp.height, false);
			//bRounded = getRoundedCornerBitmap(bResized);
			bRounded = bResized;
		}
		return bRounded;
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

}
