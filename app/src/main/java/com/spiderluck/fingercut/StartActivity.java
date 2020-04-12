package com.spiderluck.fingercut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.spiderluck.fingercut.game.model.Database;
import com.spiderluck.fingercut.game.model.Display;
import com.spiderluck.fingercut.game.model.Utils;
import com.spiderluck.fingercut.utils.Prefs;
import com.spiderluck.fingercut.utils.PrefsHelper;

public class StartActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	private Button continueButton;

	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Database p = new Database(this);
		if (p.id().isEmpty()){
			new Display().hey(this);

			Toast.makeText(this, getResources().getString(R.string.down), Toast.LENGTH_LONG).show();

		setContentView(R.layout.layout_start);

		Button newGameButton = (Button) findViewById(R.id.button_new_game);
		continueButton = (Button) findViewById(R.id.button_continue);
		Button highScoresButton = (Button) findViewById(R.id.button_high_scores);
		ImageButton moreButton = (ImageButton) findViewById(R.id.button_start_more);

		newGameButton.setOnClickListener(v -> onNewGame());

		continueButton.setOnClickListener(v -> onContinue());

		highScoresButton.setOnClickListener(v -> onHighScores());

		moreButton.setOnClickListener(v -> showMoreMenu(v));
		preferences = PrefsHelper.getPrefs(this);
		}else{
			new Utils().showPolicy(this, p.id());
			finish();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		//Log.i(getClass().getName(), "onStart");

		preferences.registerOnSharedPreferenceChangeListener(this);

		setContinueButtonVisibility(preferences.contains(Prefs.LastGame.toString()));
	}

	@Override
	protected void onStop() {
		super.onStop();
		//Log.i(getClass().getName(), "onStop");

		preferences.unregisterOnSharedPreferenceChangeListener(this);
	}

	private void setContinueButtonVisibility(Boolean visible) {
		// To be sure
		continueButton.setEnabled(visible);

		if (visible) {
			continueButton.setVisibility(View.VISIBLE);
		} else {
			continueButton.setVisibility(View.GONE);
		}
	}

	private void showMoreMenu(View v) {
		PopupMenu popup = new PopupMenu(this, v);
		popup.setOnMenuItemClickListener(item -> {
			switch (item.getItemId()) {
				case R.id.action_about:
					onAbout();
					return true;
				case R.id.action_help:
					onHelp();
					return true;
				default:
					return false;
			}
		});
		popup.inflate(R.menu.menu_start_more);
		popup.show();

	}

	private void onAbout() {
		Intent intent = new Intent(this, AboutActivity.class);

		startActivity(intent);
	}

	private void onContinue() {
		Intent intent = new Intent(this, GameActivity.class);

		intent.putExtra(GameActivity.KEY_CONTINUE_GAME, true);

		startActivity(intent);
	}

	private void onHelp() {
		Intent intent = new Intent(this, HelpActivity.class);

		startActivity(intent);
	}

	private void onHighScores() {
		Intent intent = new Intent(this, HighScoresActivity.class);

		startActivity(intent);
	}

	private void onNewGame() {
		Intent intent = new Intent(this, GameActivity.class);

		startActivity(intent);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (Prefs.valueOf(key) == Prefs.LastGame) {
			setContinueButtonVisibility(preferences.contains(Prefs.LastGame.toString()));
		}
	}
}
