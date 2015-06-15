package com.aka.numbergame;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ThinkTankResult extends Activity {
	String correct_ans_sring;
	TextView status;
	int wrong_answer_number = 0, correct_ans;
	Vibrator v = null;
	Context context;
	private InterstitialAd interstitial;
	protected boolean admob_fullscreen_loaded;
	private SharedPreferences sharepreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.think_tank_result);

		sharepreference = PreferenceManager.getDefaultSharedPreferences(this);

		int num_interstitial_display = sharepreference.getInt(
				"num_interstitial_display", 0);
		Editor editor = sharepreference.edit();
		editor.putInt("num_interstitial_display", num_interstitial_display + 1);
		editor.commit();

		if (num_interstitial_display % 3 == 0) {

			Log.d("Display ads", "yes");

			interstitial = new InterstitialAd(this);
			interstitial.setAdUnitId(getResources().getString(
					R.string.Interstitial_AD_UNIT_ID));

			AdRequest adRequest = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.addTestDevice("AB06E67A3BA0997B99A60C6D77E560CC")
					.addTestDevice("B80542AC486A951BA4E0E4E6D9469287").build();

			interstitial.loadAd(adRequest);

			interstitial.setAdListener(new AdListener() {
				public void onAdLoaded() {
					admob_fullscreen_loaded = true;

				}
			});
		}

		status = (TextView) findViewById(R.id.tv_think_result);
		YoYo.with(Techniques.Tada).duration(2000).playOn(status);

		wrong_answer_number = getIntent().getExtras().getInt("wrong_ans");
		context = this;
		v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

		if (wrong_answer_number == 0) {
			status.setText("Correct");
			status.setTextColor(Color.GREEN);
		} else {
			status.setText("Incorrect");
			status.setTextColor(Color.RED);
		}

		correct_ans_sring = getIntent().getExtras().getString(
				"correct_ans_string");
		correct_ans = getIntent().getExtras().getInt("correct_ans");

		TextView think_tannk_review = (TextView) findViewById(R.id.think_tannk_review);
		think_tannk_review.setText(Html.fromHtml(correct_ans_sring
				+ "<br><br></hr>  Final Answer =  <b>" + correct_ans
				+ "</b> <br><hr>"));

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (admob_fullscreen_loaded == true)
			displayInterstitial();
		finish();
	}

	public void displayInterstitial() {
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}
}
