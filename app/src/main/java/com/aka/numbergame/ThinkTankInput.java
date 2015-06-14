package com.aka.numbergame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class ThinkTankInput extends Activity {
	private Handler mHandler = new Handler();
	Button button_1, button_2, button_3, button_4, button_5, button_6,
			button_7, button_8, button_9, button_clear, button_0;

	TextView game_question, game_input;;

	int correct_ans = 0, current_question_number = 0, wrong_press_number = 0,
			max_wrong_press = 1;
	Context context;
	SoundPool sp;

	int mode;
	Vibrator v = null;
	SharedPreferences sharepreference;
	String correct_ans_sring;
	private AdView adView;
	public static final String AD_UNIT_ID = "ca-app-pub-7137206256481745/7918156514";
	public static final String Interstitial_AD_UNIT_ID = "ca-app-pub-7137206256481745/1871622912";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.think_tank_input);
		context = this;

		v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

		correct_ans_sring = getIntent().getExtras().getString(
				"correct_ans_string");

		/* admob ads */
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_adview);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("B80542AC486A951BA4E0E4E6D9469287").build();
		adView.loadAd(adRequest);
		// ///////////////////////////////

		game_question = (TextView) findViewById(R.id.tv_game_question);
		game_input = (TextView) findViewById(R.id.tv_game_result_input);

		button_clear = (Button) findViewById(R.id.button_clear);
		button_0 = (Button) findViewById(R.id.button_0);
		button_1 = (Button) findViewById(R.id.button_1);
		button_2 = (Button) findViewById(R.id.button_2);
		button_3 = (Button) findViewById(R.id.button_3);
		button_4 = (Button) findViewById(R.id.button_4);
		button_5 = (Button) findViewById(R.id.button_5);
		button_6 = (Button) findViewById(R.id.button_6);
		button_7 = (Button) findViewById(R.id.button_7);
		button_8 = (Button) findViewById(R.id.button_8);
		button_9 = (Button) findViewById(R.id.button_9);

		int height = 0;
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
			Display display = getWindowManager().getDefaultDisplay();
			// width = display.getWidth(); // deprecated
			height = display.getHeight(); // deprecated
		} else {
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			// width = size.x;
			height = size.y;
		}

		int text_height = height / 28;
		if (text_height < 30)
			text_height = 30;
		if (text_height > 40)
			text_height = 40;

		int hint_height = height / 40;
		if (hint_height < 20)
			hint_height = 20;
		if (hint_height > 26)
			hint_height = 26;

		game_question.setTextSize(hint_height);
		game_input.setTextSize(text_height);

		button_clear.setTextSize(text_height);
		button_0.setTextSize(text_height);
		button_1.setTextSize(text_height);
		button_2.setTextSize(text_height);
		button_3.setTextSize(text_height);
		button_4.setTextSize(text_height);
		button_5.setTextSize(text_height);
		button_6.setTextSize(text_height);
		button_7.setTextSize(text_height);
		button_8.setTextSize(text_height);
		button_9.setTextSize(text_height);

		correct_ans = getIntent().getExtras().getInt("correct_ans");

		button_0.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game_input.setText(game_input.getText().toString() + "0");
				check_input();
			}
		});

		button_1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game_input.setText(game_input.getText().toString() + "1");
				check_input();
			}
		});

		button_2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game_input.setText(game_input.getText().toString() + "2");
				check_input();
			}
		});

		button_3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game_input.setText(game_input.getText().toString() + "3");
				check_input();
			}
		});

		button_4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game_input.setText(game_input.getText().toString() + "4");
				check_input();
			}
		});

		button_5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game_input.setText(game_input.getText().toString() + "5");
				check_input();
			}
		});

		button_6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game_input.setText(game_input.getText().toString() + "6");
				check_input();
			}
		});

		button_7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game_input.setText(game_input.getText().toString() + "7");
				check_input();
			}
		});

		button_8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game_input.setText(game_input.getText().toString() + "8");
				check_input();
			}
		});

		button_9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game_input.setText(game_input.getText().toString() + "9");
				check_input();
			}
		});

		button_clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				game_input.setText("");
				// check_input();
			}
		});

		TextView tv_think_help = (TextView) findViewById(R.id.tv_think_help);
		tv_think_help.setTextSize(hint_height);
		tv_think_help.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Builder builder = new Builder(ThinkTankInput.this);
				builder.setTitle("Review !!");
				builder.setMessage(Html.fromHtml(correct_ans_sring
						+ "<br><br></hr>  Final Answer =  <b>" + correct_ans
						+ "</b> <br><hr>"));
				builder.setCancelable(true);
				builder.setPositiveButton("Got it",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();

			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();

	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	public void check_input() {
		game_input.setTextColor(Color.WHITE);
		String input_text = game_input.getText().toString();
		int input_text_value = Integer.parseInt(input_text);
		String correct_text = String.valueOf(correct_ans);

		if (input_text.length() == correct_text.length()) {
			if (input_text_value == correct_ans) {

				// play sound
				/*
				 * if (win_game != 0) sp.play(win_game, 1, 1, 0, 0, 1);
				 */
				v.vibrate(50);
				finish();
				Intent intent = new Intent(getApplicationContext(),
						ThinkTankResult.class);
				intent.putExtra("wrong_ans", wrong_press_number);

				intent.putExtra("correct_ans", correct_ans);
				intent.putExtra("correct_ans_string", correct_ans_sring);
				startActivity(intent);

			} else {
				// play sound
				/*
				 * if (wrong_press != 0) sp.play(wrong_press, 1, 1, 0, 0, 1);
				 */

				v.vibrate(100);
				wrong_press_number++;
				game_input.setTextColor(Color.RED);
				mHandler.postDelayed(new Runnable() {
					public void run() {
						game_input.setText("");
					}
				}, 150);

				if (wrong_press_number >= max_wrong_press) {
					finish();
					v.vibrate(150);
					Intent intent = new Intent(getApplicationContext(),
							ThinkTankResult.class);
					intent.putExtra("wrong_ans", wrong_press_number);
					String correct_ans_sring = getIntent().getExtras()
							.getString("correct_ans_string");
					intent.putExtra("correct_ans", correct_ans);
					intent.putExtra("correct_ans_string", correct_ans_sring);
					startActivity(intent);

				}
			}
		}

	}

}
