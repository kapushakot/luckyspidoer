package com.spiderluck.fingercut.utils;



import android.graphics.RectF;

import org.json.JSONException;
import org.json.JSONObject;

public class VectoringMyGraf {
	public float X;
	public float Y;

	public VectoringMyGraf() {
		X = 0.0f;
		Y = 0.0f;
	}

	public VectoringMyGraf(VectoringMyGraf v) {
		X = v.X;
		Y = v.Y;
	}

	public VectoringMyGraf(float x, float y) {
		X = x;
		Y = y;
	}

	public VectoringMyGraf(JSONObject json) throws JSONException {
		X = (float) json.getDouble("X");
		Y = (float) json.getDouble("Y");
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject state = new JSONObject();

		state.put("X", (double) X);
		state.put("Y", (double) Y);

		return state;
	}

	public void set(float x, float y) {
		X = x;
		Y = y;
	}

	public void set(VectoringMyGraf v) {
		X = v.X;
		Y = v.Y;
	}

	public void add(float x, float y) {
		X += x;
		Y += y;
	}

	public void add(VectoringMyGraf v) {
		X += v.X;
		Y += v.Y;
	}

	public static VectoringMyGraf add(VectoringMyGraf v1, VectoringMyGraf v2) {
		VectoringMyGraf ret = new VectoringMyGraf(v1);
		ret.add(v2);
		return ret;
	}

	public void sub(VectoringMyGraf v) {
		X -= v.X;
		Y -= v.Y;
	}

	public static VectoringMyGraf sub(VectoringMyGraf v1, VectoringMyGraf v2) {
		VectoringMyGraf ret = new VectoringMyGraf(v1);
		ret.sub(v2);
		return ret;
	}

	public void scale(float a) {
		X *= a;
		Y *= a;
	}

	public static VectoringMyGraf scale(VectoringMyGraf v, float a) {
		return new VectoringMyGraf(v.X * a, v.Y * a);
	}

	public void reverse() {
		X = -X;
		Y = -Y;
	}

	public static VectoringMyGraf reverse(VectoringMyGraf v) {
		return new VectoringMyGraf(-v.X, -v.Y);
	}

	public float dotProduct(VectoringMyGraf v2) {
		return X * v2.X + Y * v2.Y;
	}

	public static float dotProduct(VectoringMyGraf v1, VectoringMyGraf v2) {
		return v1.X * v2.X + v1.Y * v2.Y;
	}

	public float angle(VectoringMyGraf v2) {
		double a = Math.atan2(v2.Y, v2.X) - Math.atan2(Y, X);
		if (a < 0) {
			a = a + 2 * Math.PI;
		}
		return (float) (a * 180 / Math.PI);
	}

	public static float angle(VectoringMyGraf v1, VectoringMyGraf v2) {
		double a = Math.atan2(v2.Y, v2.X) - Math.atan2(v1.Y, v1.X);
		if (a < 0) {
			a = a + 2 * Math.PI;
		}
		return (float) (a * 180 / Math.PI);
	}

	public float length() {
		return (float) Math.sqrt((double) (X * X + Y * Y));
	}

	public static float length(VectoringMyGraf v) {
		return (float) Math.sqrt((double) (v.X * v.X + v.Y * v.Y));
	}

	public static float length(float x, float y) {
		return (float) Math.sqrt((double) (x * x + y * y));
	}

	public void normalize() {
		float l = length();
		if (l != 0.0f) {
			X /= l;
			Y /= l;
		}
	}

	public static VectoringMyGraf lerp(VectoringMyGraf v1, VectoringMyGraf v2, float a) {
		return new VectoringMyGraf(v1.X + (v2.X - v1.X) * a, v1.Y + (v2.Y - v1.Y) * a);
	}

	public boolean isInBounds(RectF bounds) {
		return (X >= bounds.left && X <= bounds.right && Y >= bounds.top && Y <= bounds.bottom);
	}
}