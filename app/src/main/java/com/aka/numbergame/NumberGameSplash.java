package com.aka.numbergame;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class NumberGameSplash extends Activity {
	private InterstitialAd interstitial;
	protected boolean admob_fullscreen_loaded;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_number_game_splash);

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

		ImageView math_expert = (ImageView) findViewById(R.id.splash_math_expert);
		ImageView single_player = (ImageView) findViewById(R.id.splash_single_player);
		ImageView multi_player = (ImageView) findViewById(R.id.splash_multi_player);
		ImageView math_tricks = (ImageView) findViewById(R.id.splash_math_tricks);
		ImageView think_tank = (ImageView) findViewById(R.id.splash_think_tank);

		int width = 0;
		int height = 0;
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
			Display display = getWindowManager().getDefaultDisplay();
			width = display.getWidth(); // deprecated
			height = display.getHeight(); // deprecated
		} else {
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
		}

		// Log.d("width", String.valueOf(width)); 540
		// Log.d("height", String.valueOf(height)); 888
		/*
		 * Toast.makeText(NumberGameSplash.this, "Width =" + width +
		 * " Height = " + height, Toast.LENGTH_LONG) .show();
		 */

		Bitmap b_MathExert = BitmapFactory.decodeResource(getResources(),
				R.drawable.math_expert);
		Bitmap bMapScaled = Bitmap.createScaledBitmap(b_MathExert,
				width * 8 / 10, height / 11, true);
		math_expert.setImageBitmap(bMapScaled);

		Bitmap b_SinglePlayer = BitmapFactory.decodeResource(getResources(),
				R.drawable.single_player);
		Bitmap bMapScaled1 = Bitmap.createScaledBitmap(b_SinglePlayer,
				width * 8 / 10, height / 11, true);
		single_player.setImageBitmap(bMapScaled1);

		Bitmap b_MultiPlayer = BitmapFactory.decodeResource(getResources(),
				R.drawable.multi_player);
		Bitmap bMapScaled2 = Bitmap.createScaledBitmap(b_MultiPlayer,
				width * 8 / 10, height / 11, true);
		multi_player.setImageBitmap(bMapScaled2);

		Bitmap b_MathtTricks = BitmapFactory.decodeResource(getResources(),
				R.drawable.math_tricks);
		Bitmap bMapScaled3 = Bitmap.createScaledBitmap(b_MathtTricks,
				width * 8 / 10, height / 11, true);
		math_tricks.setImageBitmap(bMapScaled3);

		// /// Comment it when needed
		math_tricks.setVisibility(View.GONE);

		Bitmap b_think_tank = BitmapFactory.decodeResource(getResources(),
				R.drawable.think_tank_main);
		Bitmap bMapScaled4 = Bitmap.createScaledBitmap(b_think_tank,
				width * 8 / 10, height / 11, true);
		think_tank.setImageBitmap(bMapScaled4);

		single_player.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra("player", "single");
				startActivity(intent);

			}
		});

		multi_player.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				intent.putExtra("player", "multi");
				startActivity(intent);

			}
		});

		think_tank.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ThinkTank.class);
				startActivity(intent);

			}
		});

		ImageView splah_rate = (ImageView) findViewById(R.id.splash_rate);
		ImageView splash_share = (ImageView) findViewById(R.id.splash_share);

		splah_rate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("market://details?id=com.madguy.numbergame")));
				} catch (android.content.ActivityNotFoundException anfe) {
					startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id=com.madguy.numbergame")));
				}

			}
		});

		splash_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sharing_intent = new Intent(
						Intent.ACTION_SEND);
				sharing_intent.setType("text/plain");
				sharing_intent
						.putExtra(
								Intent.EXTRA_TEXT,
								"Hey please check out "
										+ getResources().getString(
												R.string.app_name)
										+ " App https://play.google.com/store/apps/details?id=com.madguy.numbergame");
				sharing_intent
						.putExtra(Intent.EXTRA_SUBJECT, "Check out '"
								+ getResources().getString(R.string.app_name)
								+ "' App");
				startActivity(Intent.createChooser(sharing_intent,
						"How do you want to share?"));
			}
		});

	}

	public void displayInterstitial() {
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		if (admob_fullscreen_loaded == true)
			displayInterstitial();
		finish();
	}

	@Override
	protected void onPause() {

		super.onPause();
		// finish();
	}

}
