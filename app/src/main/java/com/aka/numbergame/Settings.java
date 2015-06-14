package com.aka.numbergame;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class Settings extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_acitvity);

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		finish();
	}

}
