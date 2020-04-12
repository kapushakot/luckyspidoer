package com.spiderluck.fingercut.game.gameplay;



import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.spiderluck.fingercut.game.model.Spider;
import com.spiderluck.fingercut.utils.VectoringMyGraf;

public class GameSpiderScreen {
	private Paint paint;
	private float imageScale;
	private float legs[];
	private float jaws[];
	private RectF body;
	private RectF head;

	public GameSpiderScreen() {
		paint = new Paint();
		paint.setColor(Color.rgb(255, 0, 0));
		paint.setStrokeWidth(2.0f);

		legs = new float[]{
				0, 0, 8, -8,
				8, -8, 6, -19,

				0, 0, 12, -4,
				12, -4, 16, -16,

				0, 0, 8, 8,
				8, 8, 6, 19,

				0, 0, 12, 4,
				12, 4, 16, 16,

				0, 0, -8, -8,
				-8, -8, -6, -19,

				0, 0, -12, -4,
				-12, -4, -16, -16,

				0, 0, -8, 8,
				-8, 8, -6, 19,

				0, 0, -12, 4,
				-12, 4, -16, 16,
		};

		jaws = new float[]{
				0, -4, -2, -10,
				0, -4, 2, -10,
		};

		body = new RectF(-4.0f, 12.0f, 4.0f, 0.0f);
		head = new RectF(-3.0f, 0.0f, 3.0f, -8.0f);

		imageScale = 0.003f;
	}

	public void draw(Spider spider, Canvas canvas, float scale) {
		VectoringMyGraf position = spider.getPosition();
		float x = position.X * scale;
		float y = position.Y * scale;

		canvas.save();
		canvas.translate(x, y);
		canvas.rotate(-spider.getRotation(), 0, 0);
		float s = imageScale * scale;
		canvas.scale(s, s);

		//canvas.drawCircle(0, 0, 4.0f, paint);

		canvas.drawOval(body, paint);
		canvas.drawOval(head, paint);

		canvas.drawLines(jaws, 0, jaws.length, paint);

		canvas.drawLines(legs, 0, legs.length, paint);

		canvas.restore();
	}
}
