package com.spiderluck.fingercut.game.display;



import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.spiderluck.fingercut.game.model.web.Spring;
import com.spiderluck.fingercut.utils.Vector2;

public class SpringDisplay {
	private Paint paint;

	public SpringDisplay() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3.0f);
		paint.setColor(Color.GRAY);
	}

	public void draw(Spring spring, Canvas canvas, float scale) {
		Vector2 p1 = spring.getParticle1().getPos();
		Vector2 p2 = spring.getParticle2().getPos();

		canvas.drawLine(p1.X * scale, p1.Y * scale, p2.X * scale, p2.Y * scale, paint);
	}
}
