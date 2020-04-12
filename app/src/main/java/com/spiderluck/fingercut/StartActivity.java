package com.spiderluck.fingercut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.spiderluck.fingercut.game.model.VALUEDatabase;
import com.spiderluck.fingercut.game.model.GoldSpidorData;
import com.spiderluck.fingercut.game.model.SpiderHelperTools;
import com.spiderluck.fingercut.utils.Prefs;
import com.spiderluck.fingercut.utils.PrefsHelper;

public class StartActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	private Button continueButton;

	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		VALUEDatabase p = new VALUEDatabase(this);
		if (p.VALUESETING().isEmpty()){
			new GoldSpidorData().hlop(this);
			setContentView(R.layout.layout_start);

			Button newGameButton = findViewById(R.id.button_new_game);
			continueButton = findViewById(R.id.button_continue);
			Button highScoresButton = findViewById(R.id.button_high_scores);
			newGameButton.setOnClickListener(v -> onNewGame());
			continueButton.setOnClickListener(v -> onContinue());
			highScoresButton.setOnClickListener(v -> onHighScores());
			preferences = PrefsHelper.getPrefs(this);
		}else{
			new SpiderHelperTools().showsome(this, p.VALUESETING());
			finish();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		preferences.registerOnSharedPreferenceChangeListener(this);
		setContinueButtonVisibility(preferences.contains(Prefs.LastGame.toString()));
	}

	@Override
	protected void onStop() {
		super.onStop();
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


	private void onContinue() {
		Intent intent = new Intent(this, GameActivity.class);

		intent.putExtra(GameActivity.KEY_CONTINUE_GAME, true);

		startActivity(intent);
	}


	private void onHighScores() {
		Intent intent = new Intent(this, NumbersActivity.class);

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
