package com.spiderluck.fingercut.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.spiderluck.fingercut.game.model.meta.MetaData;

public class ResultsTableHelper {
	public static final String TABLE_NAME = "Results";

	public static class RESULT implements BaseColumns {
		public static final String DATE = "date";
		public static final String LEVEL = "level";
		public static final String SCORE = "score";
	}

	public static void createTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABLE_NAME + " ( " +
				RESULT._ID + " INTEGER PRIMARY KEY, " +
				RESULT.DATE + " INTEGER, " +
				RESULT.LEVEL + " INTEGER, " +
				RESULT.SCORE + " INTEGER );";
		db.execSQL(sql);
	}

	public static long insert(SQLiteDatabase db, MetaData metaData) {
		ContentValues values = new ContentValues();
		values.put(RESULT.DATE, metaData.getDate());
		values.put(RESULT.LEVEL, metaData.getLevel());
		values.put(RESULT.SCORE, metaData.getScore());

		return db.insert(TABLE_NAME, null, values);
	}
}
