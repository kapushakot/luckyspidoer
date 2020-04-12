package com.spiderluck.fingercut.game.gameplay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

import com.spiderluck.fingercut.R;
import com.spiderluck.fingercut.game.model.Game;
import com.spiderluck.fingercut.game.model.web.WebType;
import com.spiderluck.fingercut.utils.CanvasHelper;

public class GamelyModeDisplay {

	private CanvasHelper canvasHelper;

	private Bitmap myBitmap;

	private SpiderWebDisplay spiderWebDisplay;
	private SpiderSetDisplay spiderSetDisplay;
	private CutDisplay cutDisplay;

	private int backgroundcolor;
	private Paint backgroundPaint;
	private Paint borderPaint;
	private Paint gameAreaPaint;

	public GamelyModeDisplay(Context context) {
		canvasHelper = new CanvasHelper();
		spiderWebDisplay = new SpiderWebDisplay();
		spiderSetDisplay = new SpiderSetDisplay();
		cutDisplay = new CutDisplay();

		myBitmap = null;

		// read app background image
		Bitmap appBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.webing);

		backgroundcolor = context.getResources().getColor(R.color.colorBackground);
		int borderColor = Color.BLACK;

		backgroundPaint = new Paint();
		backgroundPaint.setStyle(Paint.Style.FILL);
		if (appBackground != null) {
			Shader shader = new BitmapShader(appBackground, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			backgroundPaint.setShader(shader);
		} else {
			backgroundPaint.setColor(Color.TRANSPARENT);
		}

		borderPaint = new Paint();
		borderPaint.setColor(borderColor);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(3.0f);
		borderPaint.setAntiAlias(true);

		gameAreaPaint = new Paint();
		gameAreaPaint.setColor(backgroundcolor);
		gameAreaPaint.setStyle(Paint.Style.FILL);
	}

	public void draw(Game game, Canvas canvas, float scale) {
		canvas.save();

		canvas.drawBitmap(myBitmap, 0, 0, null);

		canvasHelper.translate(canvas);

		spiderWebDisplay.draw(game.getWeb(), canvas, scale);

		spiderSetDisplay.draw(game.getSpiderSet(), canvas, scale);

		cutDisplay.colorAndDraw(game.getFinger(), canvas, scale);

		canvas.restore();
	}

	public CanvasHelper getCanvasHelper() {
		return canvasHelper;
	}

	public void setupBackground(WebType webType) {
		if (myBitmap != null && !myBitmap.isRecycled()) {
			myBitmap.recycle();
		}

		myBitmap = Bitmap.createBitmap(canvasHelper.getWidth(), canvasHelper.getHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(myBitmap);
		canvasHelper.translate(canvas);

		float scale = canvasHelper.getScale();

		canvas.drawColor(backgroundcolor);

		canvas.drawRect(canvas.getClipBounds(), backgroundPaint);

		switch (webType) {
			case Round5x6:
			case Round4x8:
			case Round3x10:
			case Spiral35x6:
			case Spiral25x8:
				canvas.drawCircle(0.0f, 0.0f, 0.9f * scale, gameAreaPaint);
				canvas.drawCircle(0.0f, 0.0f, 0.9f * scale, borderPaint);
				break;
			case Rect5x5:
			case Rect6x6:
			case Rect7x7:
				canvas.drawRect(-0.9f * scale, -0.9f * scale, 0.9f * scale, 0.9f * scale, gameAreaPaint);
				canvas.drawRect(-0.9f * scale, -0.9f * scale, 0.9f * scale, 0.9f * scale, borderPaint);
				break;
			default:
				break;
		}
	}
}
