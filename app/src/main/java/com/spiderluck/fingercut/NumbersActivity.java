package com.spiderluck.fingercut;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.CursorAdapter;

import com.spiderluck.fingercut.utils.DBHelper;
import com.spiderluck.fingercut.utils.ResultsTableHelper;

public class NumbersActivity extends AppCompatActivity {

	private CursorAdapter adapter;

	private DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.i(getClass().getName(), "onCreate");

		setContentView(R.layout.layout_high_scores);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_high_scores);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		ListView listView = (ListView) findViewById(R.id.list_high_scores);
		TextView noGames = (TextView) findViewById(R.id.text_no_games);

		listView.setEmptyView(noGames);

		listView.addHeaderView(createHeader(listView));

		adapter = new HighScoresCursorAdapter(this, null, 0);

		listView.setAdapter(adapter);

		dbHelper = new DBHelper(this);
	}


	@Override
	protected void onStart() {
		super.onStart();
		//Log.i(getClass().getName(), "onStart");

		Cursor cursor = createCursor();
		adapter.changeCursor(cursor);
	}

	@Override
	protected void onStop() {
		super.onStop();
		//Log.i(getClass().getName(), "onStop");

		dbHelper.close();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the clear button
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				onBackPressed();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	private View createHeader(ViewGroup listView) {
		View header = getLayoutInflater().inflate(R.layout.layout_high_score, listView, false);
		TextView dateText = (TextView) header.findViewById(R.id.text_date);
		TextView scoreText = (TextView) header.findViewById(R.id.text_score);

		dateText.setTextColor(Color.WHITE);
		scoreText.setTextColor(Color.WHITE);

		dateText.setText(R.string.high_scores_date);
		scoreText.setText(R.string.high_scores_score);

		return header;
	}

	private Cursor createCursor() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		String table = ResultsTableHelper.TABLE_NAME;

		String[] columns = new String[]{
				ResultsTableHelper.RESULT._ID,
				ResultsTableHelper.RESULT.DATE,
				ResultsTableHelper.RESULT.SCORE
		};

		String orderBy = ResultsTableHelper.RESULT.SCORE + " DESC, " +
				ResultsTableHelper.RESULT.DATE + " DESC";

		return db.query(table, columns, null, null, null, null, orderBy, null);
	}

	private void clearHighScores() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		db.delete(ResultsTableHelper.TABLE_NAME, null, null);
	}

	private class HighScoresCursorAdapter extends CursorAdapter {
		public HighScoresCursorAdapter(Context context, Cursor cursor, int flags) {
			super(context, cursor, flags);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return LayoutInflater.from(context).inflate(R.layout.layout_high_score, parent, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView dateText = (TextView) view.findViewById(R.id.text_date);
			TextView scoreText = (TextView) view.findViewById(R.id.text_score);

			long date = cursor.getLong(cursor.getColumnIndexOrThrow(ResultsTableHelper.RESULT.DATE));
			int score = cursor.getInt(cursor.getColumnIndexOrThrow(ResultsTableHelper.RESULT.SCORE));

			long now = System.currentTimeMillis();
			String timeSpan = (String) DateUtils.getRelativeTimeSpanString(date, now, DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
			dateText.setText(timeSpan);

			scoreText.setText(String.format("%d", score));
		}
	}
}
