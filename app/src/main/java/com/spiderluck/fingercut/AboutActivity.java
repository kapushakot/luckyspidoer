package com.spiderluck.fingercut;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.installreferrer.BuildConfig;

public class AboutActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_about);

		Toolbar toolbar = findViewById(R.id.toolbar_about);
		setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		TextView appVersion = findViewById(R.id.app_version);
		int versionCode = BuildConfig.VERSION_CODE;
		String versionName = BuildConfig.VERSION_NAME;
		String version = String.format("v. %s (%d)", versionName, versionCode);
		appVersion.setText(version);

		Button licenseButton = findViewById(R.id.button_license);
		licenseButton.setOnClickListener(v -> showLicense());
	}

	private void showLicense() {
		String url = "https://www.gnu.org/licenses/gpl.html";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Log.e(getClass().getName(), "cannot open browser");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				onBackPressed();
				break;
		}

		return super.onOptionsItemSelected(item);
	}
}
