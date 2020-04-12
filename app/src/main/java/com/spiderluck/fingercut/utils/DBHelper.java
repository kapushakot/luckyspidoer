package com.spiderluck.fingercut.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "database.db";
	private static final int DB_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		//Log.d(getClass().getName(), "onCreate");
		ResultsTableHelper.createTable(db);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Log.d(getClass().getName(), "onUpgrade");
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Log.d(getClass().getName(), "onDowngrade");
	}
}
