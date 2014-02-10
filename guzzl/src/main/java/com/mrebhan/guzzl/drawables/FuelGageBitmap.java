package com.mrebhan.guzzl.drawables;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * This class defines complex shape needed to represent a fuel gauge
 * 
 * @author Mark
 * 
 */
public class FuelGageBitmap extends Drawable {

	private Paint paint;
	private Path path;
	public FuelGageBitmap() {
		super();
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		path = new Path();
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);

	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawPath(path, paint);

		// calculate all the points
		int point1x = canvas.getWidth() * 1 / 14;
		int point1y = canvas.getHeight();

		int point2x = canvas.getWidth() - point1x - 40;
		int point2y = point1y;

		int tanLength = (int) (point1x / Math.cos(Math.toRadians(60)));

		int point3x = canvas.getWidth() - 40;
		int point3y = canvas.getHeight()
				- (int) (tanLength * Math.sin(Math.toRadians(60)));

		int point4x = 0;
		int point4y = point3y;

		final RectF ovalOuter = new RectF(point4x,
				point3y - 120, point3x,
				point3y + 120 );

		final RectF ovalInner = new RectF(point1x, canvas.getHeight() - 120,
				point2x, canvas.getHeight() + 120);
		// Arc starts drawing from midpoint of edge
		canvas.drawLine(point1x, point1y, point4x, point4y, paint);
		canvas.drawLine(point2x, point2y, point3x, point3y, paint);
		canvas.drawArc(ovalInner, 0, -180, false, paint);
		canvas.drawArc(ovalOuter, 0, -180, false, paint);

		// move to first point
		path.moveTo(point2x, point2y);

		// draw arc to point 2 defined by ovalInner
		path.arcTo(ovalInner, 0, 180f);
		// draw line to point 3
		path.lineTo(point3x, point3y);

		// move back to first point and draw line to point 4
		path.moveTo(point1x, point1y);
		path.lineTo(point4x, point4y);

		// draw arc from point 4 to 3
		// path.arcTo(ovalOuter, 0, 180);

		// canvas.drawPath(path, paint);

	}

	@Override
	public int getOpacity() {
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		paint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		paint.setColorFilter(cf);
	}

}
