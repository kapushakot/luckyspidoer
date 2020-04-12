package com.spiderluck.fingercut.game.gameplay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.spiderluck.fingercut.game.model.Finger;
import com.spiderluck.fingercut.utils.VectoringMyGraf;

public class CutDisplay {

	private Paint rightColor;
	private Paint badColor;

	CutDisplay() {
		rightColor = new Paint();
		rightColor.setStyle(Paint.Style.STROKE);
		rightColor.setStrokeWidth(3.0f);
		rightColor.setColor(Color.YELLOW);

		badColor = new Paint();
		badColor.setStyle(Paint.Style.STROKE);
		badColor.setStrokeWidth(3.0f);
		badColor.setColor(Color.RED);
	}

	public void colorAndDraw(Finger finger, Canvas canvas, float scale) {
		VectoringMyGraf position = finger.getPosition();
		if (position != null) {
			Paint paint;
			if (finger.isBitten()) {
				paint = badColor;
			} else {
				paint = rightColor;
			}
			canvas.drawCircle(position.X * scale, position.Y * scale, finger.getRadius() * scale, paint);
		}
	}
}
