package com.spiderluck.fingercut.utils;

import android.graphics.Canvas;
import android.graphics.RectF;

public class CanvasHelper {

	private int height;
	private int width;
	private float scale;

	private RectF area;

	public CanvasHelper() {
		width = height = 1;
		scale = 1.0f;
		area = new RectF(-1.0f, -1.0f, 1.0f, 1.0f);
	}

	public void setSize(int w, int h) {
		width = w;
		height = h;

		// calculate scale and game area
		int min = Math.min(width, height);
		scale = min * 0.5f;

		float x = (float) width / (float) min;
		float y = (float) height / (float) min;
		area = new RectF(-x, -y, x, y);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getScale() {
		return scale;
	}

	public RectF getArea() {
		return area;
	}

	public VectoringMyGraf transform(float x, float y) {
		VectoringMyGraf ret = new VectoringMyGraf(x - width / 2, y - height / 2);
		ret.scale(1.0f / scale);
		return ret;
	}

	public void translate(Canvas canvas) {
		canvas.translate(width / 2, height / 2);
	}
}
