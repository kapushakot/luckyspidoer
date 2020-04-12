package com.spiderluck.fingercut.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsHelper {
	private static final String PREFS_NAME = "Prefs";

	public static SharedPreferences getPrefs(Context context) {
		return context.getSharedPreferences(PREFS_NAME, 0);
	}

	public static void remove(SharedPreferences prefs, String key) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(key);
		editor.apply();
	}

	public static void putString(SharedPreferences prefs, String key, String value) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.apply();
	}
}
