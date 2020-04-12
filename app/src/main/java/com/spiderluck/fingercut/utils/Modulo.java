package com.spiderluck.fingercut.utils;



public class Modulo {

	public static float angleDifference(float a, float b) {
		a = toRange(a, 360.0f);
		b = toRange(b, 360.0f);

		if (shortestDistanceDirection(a, b, 360.0f)) {
			return shortestDistance(a, b, 360.0f);
		} else {
			return -shortestDistance(a, b, 360.0f);
		}
	}

	public static float toRange(float a, float modulo) {
		a = a % modulo;

		if (a < 0.0f) {
			a = a + modulo;
		}

		return a;
	}

	public static boolean shortestDistanceDirection(float a, float b, float modulo) {
		if (a < b) {
			float diff = b - a;
			return diff < (modulo / 2);
		} else {
			float diff = a - b;
			return diff > (modulo / 2);
		}
	}

	public static float shortestDistance(float a, float b, float modulo) {
		if (a < b) {
			float t = a;
			a = b;
			b = t;
		}

		float diff1 = a - b;
		float diff2 = b - a + modulo;
		return Math.min(diff1, diff2);
	}
}
