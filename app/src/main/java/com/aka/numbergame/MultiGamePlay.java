package com.aka.numbergame;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MultiGamePlay extends Activity {

	TextView bottom_question, bottom_timer, bottom_correct_questions,
			b_option_a, b_option_b, b_option_c;

	TextView top_question, top_timer, top_correct_questions, t_option_a,
			t_option_b, t_option_c;

	int correct_ans = 0, current_question_number = 0, total_question_limt = 15;

	int total_t_correct_answer = 0, total_b_correct_answer = 0,
			total_t_wrong_answer = 0, total_b_wrong_answer = 0;

	Context context;
	Vibrator vibrate = null;
	int text_height = 0;
	String correct_option = "";
	long time_start, time_end;
	private String type;
	int mode;
	SharedPreferences sharepreference;
	int height = 0;
	private int a_option;

	private int b_option;

	private int c_option;

	private CountDownTimer game_timer;
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
		setContentView(R.layout.multi_game_play);
		context = this;
		time_start = System.currentTimeMillis();

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

		vibrate = (Vibrator) this.context
				.getSystemService(Context.VIBRATOR_SERVICE);
		bottom_question = (TextView) findViewById(R.id.bottom_question);
		b_option_a = (TextView) findViewById(R.id.b_option_a);
		b_option_b = (TextView) findViewById(R.id.b_option_b);
		b_option_c = (TextView) findViewById(R.id.b_option_c);

		top_question = (TextView) findViewById(R.id.top_question);
		t_option_a = (TextView) findViewById(R.id.t_option_a);
		t_option_b = (TextView) findViewById(R.id.t_option_b);
		t_option_c = (TextView) findViewById(R.id.t_option_c);

		bottom_correct_questions = (TextView) findViewById(R.id.bottom_correct_questions);
		bottom_correct_questions.setText("0");
		top_correct_questions = (TextView) findViewById(R.id.top_correct_questions);
		top_correct_questions.setText("0");
		bottom_timer = (TextView) findViewById(R.id.bottom_timer);
		top_timer = (TextView) findViewById(R.id.top_timer);

		/* set the text size in each view */

		int width = 0;

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

		// game_input.setTextSize(height / size);

		text_height = height / 42;
		if (text_height < 26)
			text_height = 26;
		if (text_height > 32)
			text_height = 32;

		int hint_height = height / 40;
		if (hint_height < 20)
			hint_height = 20;
		if (hint_height > 26)
			hint_height = 26;

		/*
		 * Log.d("text height", String.valueOf(text_height));
		 * 
		 * Toast.makeText( MultiGamePlay.this, "Text height : " +
		 * String.valueOf(text_height) + "\n Hint height :" +
		 * String.valueOf(hint_height), Toast.LENGTH_SHORT).show();
		 */

		bottom_question.setTextSize(text_height);
		b_option_a.setTextSize(text_height);
		b_option_b.setTextSize(text_height);
		b_option_c.setTextSize(text_height);

		top_question.setTextSize(text_height);
		t_option_a.setTextSize(text_height);
		t_option_b.setTextSize(text_height);
		t_option_c.setTextSize(text_height);

		type = getIntent().getExtras().getString("my_type");
		mode = getIntent().getExtras().getInt("mode");

		// call the main function
		generate_question(mode, type);

		switch (mode) {
		case 0:
			break;

		case 1:
			break;

		case 2:
			break;

		case 3:
			break;

		default:
			break;
		}

		LayoutParams params = bottom_question.getLayoutParams();
		params.height = height / 9;
		params.width = width * 75 / 100;
		bottom_question.setLayoutParams(params);

		LayoutParams params_answer_a = b_option_a.getLayoutParams();
		params_answer_a.height = height / 9;
		params_answer_a.width = width * 25 / 100;

		b_option_a.setLayoutParams(params_answer_a);

		LayoutParams params_answer_b = b_option_b.getLayoutParams();
		params_answer_b.height = height / 9;
		params_answer_b.width = width * 25 / 100;
		b_option_b.setLayoutParams(params_answer_b);

		LayoutParams params_answer_c = b_option_c.getLayoutParams();
		params_answer_c.height = height / 9;
		params_answer_c.width = width * 25 / 100;
		b_option_c.setLayoutParams(params_answer_c);

		LayoutParams top_params = top_question.getLayoutParams();
		top_params.height = height / 9;
		top_params.width = width * 75 / 100;
		top_question.setLayoutParams(top_params);

		LayoutParams top_answer_a = t_option_a.getLayoutParams();
		top_answer_a.height = height / 9;
		top_answer_a.width = width * 25 / 100;
		t_option_a.setLayoutParams(top_answer_a);

		LayoutParams top_answer_b = t_option_b.getLayoutParams();
		top_answer_b.height = height / 9;
		top_answer_b.width = width * 25 / 100;
		t_option_b.setLayoutParams(top_answer_b);

		LayoutParams top_answer_c = t_option_c.getLayoutParams();
		top_answer_c.height = height / 9;
		top_answer_c.width = width * 25 / 100;
		t_option_c.setLayoutParams(top_answer_c);

		/*
		 * RelativeLayout multi_top_layout = (RelativeLayout)
		 * findViewById(R.id.multi_top_layout); RelativeLayout.LayoutParams lp =
		 * new RelativeLayout.LayoutParams(
		 * RelativeLayout.LayoutParams.MATCH_PARENT, height / 2);
		 * multi_top_layout.setLayoutParams(lp);
		 * 
		 * RelativeLayout multi_bottom_layout = (RelativeLayout)
		 * findViewById(R.id.multi_bottom_layout);
		 * multi_bottom_layout.setLayoutParams(lp);
		 */

		if (currentapiVersion >= android.os.Build.VERSION_CODES.KITKAT) {

			RelativeLayout bottom_options = (RelativeLayout) findViewById(R.id.bottom_options);
			RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
					new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT));
			relativeParams.setMargins(width * 7 / 100, height / 5, 0, 0);

			bottom_options.setLayoutParams(relativeParams);
			bottom_options.requestLayout();

			RelativeLayout top_options = (RelativeLayout) findViewById(R.id.top_options);

			RelativeLayout.LayoutParams relativeParams1 = new RelativeLayout.LayoutParams(
					new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT));
			relativeParams1.setMargins(width * 7 / 100, height / 5, 0, 0);
			top_options.setLayoutParams(relativeParams1);
			top_options.requestLayout();

		}

		final RelativeLayout multi_top_layout = (RelativeLayout) findViewById(R.id.multi_top_layout);
		final RelativeLayout multi_bottom_layout = (RelativeLayout) findViewById(R.id.multi_bottom_layout);

		b_option_a.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (correct_option.equals("A")) {
					total_b_correct_answer++;
					bottom_correct_questions.setText(String
							.valueOf(total_b_correct_answer));
					next_question();
				} else {
					vibrate.vibrate(100);
					total_b_wrong_answer++;
				}
			}
		});

		b_option_b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (correct_option.equals("B")) {
					total_b_correct_answer++;
					bottom_correct_questions.setText(String
							.valueOf(total_b_correct_answer));
					next_question();
				} else {
					vibrate.vibrate(100);
					total_b_wrong_answer++;
				}
			}
		});

		b_option_c.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (correct_option.equals("C")) {
					total_b_correct_answer++;
					bottom_correct_questions.setText(String
							.valueOf(total_b_correct_answer));
					next_question();
				} else {
					vibrate.vibrate(100);
					total_b_wrong_answer++;
				}
			}
		});

		t_option_a.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (correct_option.equals("A")) {
					total_t_correct_answer++;
					top_correct_questions.setText(String
							.valueOf(total_t_correct_answer));
					next_question();
				} else {
					vibrate.vibrate(100);
					total_t_wrong_answer++;
				}
			}
		});

		t_option_b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (correct_option.equals("B")) {
					total_t_correct_answer++;
					top_correct_questions.setText(String
							.valueOf(total_t_correct_answer));
					next_question();
				} else {
					vibrate.vibrate(100);
					total_t_wrong_answer++;
				}
			}
		});

		t_option_c.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (correct_option.equals("C")) {
					total_t_correct_answer++;
					top_correct_questions.setText(String
							.valueOf(total_t_correct_answer));
					next_question();
				} else {
					vibrate.vibrate(100);
					total_t_wrong_answer++;
				}
			}
		});

		game_timer = new CountDownTimer(30 * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				int time = (int) (millisUntilFinished / 1000);
				top_timer.setText(String.valueOf(time));
				bottom_timer.setText(String.valueOf(time));

			}

			@Override
			public void onFinish() {
				bottom_question.setVisibility(View.GONE);
				b_option_a.setVisibility(View.GONE);
				b_option_b.setVisibility(View.GONE);
				b_option_c.setVisibility(View.GONE);

				top_question.setVisibility(View.GONE);
				t_option_a.setVisibility(View.GONE);
				t_option_b.setVisibility(View.GONE);
				t_option_c.setVisibility(View.GONE);

				RelativeLayout rl_top_timer = (RelativeLayout) findViewById(R.id.rl_top_timer);
				RelativeLayout rl_bottom_timer = (RelativeLayout) findViewById(R.id.rl_bottom_timer);

				rl_top_timer.setVisibility(View.GONE);
				rl_bottom_timer.setVisibility(View.GONE);

				if (total_t_correct_answer == total_b_correct_answer) {
					ImageView iv1 = new ImageView(MultiGamePlay.this);
					iv1.setImageResource(R.drawable.mathc_draw);
					iv1.setPadding(10, height / 6, 10, height / 6);

					multi_bottom_layout.addView(iv1);

					ImageView iv11 = new ImageView(MultiGamePlay.this);
					iv11.setImageResource(R.drawable.mathc_draw);
					iv11.setPadding(10, height / 6, 10, height / 6);

					multi_top_layout.addView(iv11);

				}

				else if (total_t_correct_answer > total_b_correct_answer) {
					ImageView iv1 = new ImageView(MultiGamePlay.this);
					iv1.setImageResource(R.drawable.you_lost);
					iv1.setPadding(10, height / 6, 10, height / 6);

					multi_bottom_layout.addView(iv1);

					ImageView iv11 = new ImageView(MultiGamePlay.this);
					iv11.setImageResource(R.drawable.you_won);
					iv11.setPadding(10, height / 6, 10, height / 6);

					multi_top_layout.addView(iv11);

				} else {
					ImageView iv1 = new ImageView(MultiGamePlay.this);
					iv1.setImageResource(R.drawable.you_won);
					iv1.setPadding(10, height / 6, 10, height / 6);

					multi_bottom_layout.addView(iv1);

					ImageView iv11 = new ImageView(MultiGamePlay.this);
					iv11.setImageResource(R.drawable.you_lost);
					iv11.setPadding(10, height / 6, 10, height / 6);

					multi_top_layout.addView(iv11);
				}
			}
		};
		game_timer.start();

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

	}

	@Override
	public void onDestroy() {
		// Destroy the AdView.

		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	public void displayInterstitial() {
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}

	public void generate_question(int mode, String game_type) {

		/*
		 * if (current_question_number < total_question_limt)
		 * 
		 * {
		 */
		int number_one = 0, number_two = 0, number_three = 0, number_four = 0, number_five = 0;

		Random rand = new Random();

		if (game_type.equals("Addition")) {
			current_question_number++;

			if (mode == 0) // mode ==0 means easy mode
			{
				number_one = rand.nextInt(10);
				number_two = rand.nextInt(10);

				top_question.setText(number_one + " + " + number_two);
				bottom_question.setText(number_one + " + " + number_two);
				correct_ans = number_one + number_two;

			}
			if (mode == 1) // mode == 1 means medium mode
			{

				number_one = rand.nextInt(90) + 10;
				number_two = rand.nextInt(8) + 2;
				top_question.setText(number_one + " + " + number_two);
				bottom_question.setText(number_one + " + " + number_two);
				correct_ans = number_one + number_two;

			}
			if (mode == 2) // mode == 2 means hard mode
			{

				number_one = rand.nextInt(90) + 10;
				number_two = rand.nextInt(90) + 10;
				number_three = rand.nextInt(90) + 10;
				top_question.setText(number_one + " + " + number_two + " + "
						+ number_three);
				bottom_question.setText(number_one + " + " + number_two + " + "
						+ number_three);
				correct_ans = number_one + number_two + number_three;

				bottom_question.setTextSize(text_height - 2);
				top_question.setTextSize(text_height - 2);

			}
			if (mode == 3) // mode == 3 means expert mode
			{

				number_one = rand.nextInt(90) + 10;
				number_two = rand.nextInt(90) + 10;
				number_three = rand.nextInt(90) + 10;
				number_four = rand.nextInt(90) + 10;
				top_question.setText(number_one + " + " + number_two + " + "
						+ number_three + " + " + number_four);
				bottom_question.setText(number_one + " + " + number_two + " + "
						+ number_three + " + " + number_four);
				correct_ans = number_one + number_two + number_three
						+ number_four;
				bottom_question.setTextSize(text_height - 3);
				top_question.setTextSize(text_height - 3);

			}

		} else if (game_type.equals("Subtraction")) {
			current_question_number++;

			if (mode == 0) // mode ==0 means easy mode
			{

				number_two = rand.nextInt(10);
				number_one = rand.nextInt(10 - number_two) + (number_two);
				top_question.setText(number_one + " - " + number_two);
				bottom_question.setText(number_one + " - " + number_two);
				correct_ans = number_one - number_two;
			}
			if (mode == 1) // mode == 1 means medium mode
			{

				number_two = rand.nextInt(8) + 2;
				number_one = rand.nextInt(100 - number_two) + (number_two);
				top_question.setText(number_one + " - " + number_two);
				bottom_question.setText(number_one + " - " + number_two);
				correct_ans = number_one - number_two;
			}
			if (mode == 2) // mode == 2 means hard mode
			{

				number_two = rand.nextInt(90) + 10;
				number_one = rand.nextInt(100 - number_two) + (number_two);
				number_three = rand.nextInt(10);
				top_question.setText(number_one + " - " + number_two + " + "
						+ number_three);

				bottom_question.setText(number_one + " - " + number_two + " + "
						+ number_three);
				correct_ans = number_one - number_two + number_three;
				bottom_question.setTextSize(text_height - 2);
				top_question.setTextSize(text_height - 2);
			}
			if (mode == 3) // mode == 3 means expert mode
			{

				number_two = rand.nextInt(8) + 2;
				number_one = rand.nextInt(100 - number_two) + (number_two);

				number_four = rand.nextInt(8) + 2;
				number_three = rand.nextInt(100 - number_four) + (number_four);

				top_question.setText(number_one + " - " + number_two + " + "
						+ number_three + " - " + number_four);
				bottom_question.setText(number_one + " - " + number_two + " + "
						+ number_three + " - " + number_four);
				correct_ans = number_one - number_two + number_three
						- number_four;
				bottom_question.setTextSize(text_height - 3);
				top_question.setTextSize(text_height - 3);
			}

		}

		else if (game_type.equals("Multiplication")) {
			current_question_number++;

			if (mode == 0) // mode ==0 means easy mode
			{

				number_one = rand.nextInt(10);
				number_two = rand.nextInt(10);
				top_question.setText(number_one + " x " + number_two);
				bottom_question.setText(number_one + " x " + number_two);
				correct_ans = number_one * number_two;

			}
			if (mode == 1) // mode == 1 means medium mode
			{

				number_one = rand.nextInt(90) + 10;
				number_two = rand.nextInt(8) + 2;
				top_question.setText(number_one + " x " + number_two);
				bottom_question.setText(number_one + " x " + number_two);
				correct_ans = number_one * number_two;
			}

			if (mode == 2) // mode == 2 means hard mode
			{

				number_one = rand.nextInt(90) + 10;
				number_two = rand.nextInt(8) + 2;
				number_three = rand.nextInt(8) + 2;
				top_question.setText(number_one + " x " + number_two + " x "
						+ number_three);
				bottom_question.setText(number_one + " x " + number_two + " x "
						+ number_three);
				correct_ans = number_one * number_two * number_three;
				bottom_question.setTextSize(text_height - 2);
				top_question.setTextSize(text_height - 2);

			}
			if (mode == 3) // mode == 3 means expert mode
			{

				number_one = rand.nextInt(900) + 100;
				number_two = rand.nextInt(900) + 100;
				number_three = rand.nextInt(8) + 2;
				top_question.setText(number_one + " x " + number_two + " x "
						+ number_three);
				bottom_question.setText(number_one + " x " + number_two + " x "
						+ number_three);
				correct_ans = number_one * number_two * number_three;
				bottom_question.setTextSize(text_height - 2);
				top_question.setTextSize(text_height - 2);

			}

		} else if (game_type.equals("Division")) {
			current_question_number++;

			if (mode == 0) // mode ==0 means easy mode
			{

				number_two = rand.nextInt(10);
				if (number_two == 0)
					number_two = 2;

				number_one = (rand.nextInt(10 - number_two) + (number_two))
						* number_two;

			}
			if (mode == 1) // mode == 1 means medium mode
			{

				number_two = rand.nextInt(8) + 2;
				if (number_two == 0)
					number_two = 2;
				number_one = (rand.nextInt(100 - number_two) + (number_two))
						* number_two;

			}
			if (mode == 2) // mode == 2 means hard mode
			{

				number_two = rand.nextInt(90) + 10;
				if (number_two == 0)
					number_two = 2;
				number_one = (rand.nextInt(100 - number_two) + (number_two))
						* number_two;

			}
			if (mode == 3) // mode == 3 means expert mode
			{

				number_two = rand.nextInt(900) + 100;
				if (number_two == 0)
					number_two = 2;
				number_one = (rand.nextInt(1000 - number_two) + (number_two))
						* number_two;

			}

			top_question.setText(number_one + " / " + number_two);
			bottom_question.setText(number_one + " / " + number_two);
			correct_ans = number_one / number_two;

		}

		else if (game_type.equals("Mixed")) {

			int new_rand = rand.nextInt(3);
			String my_type = "Addition";
			switch (new_rand) {
			case 1:
				my_type = "Subtraction";
				break;
			case 2:
				my_type = "Multiplication";
				break;
			case 3:
				my_type = "Division";
				break;

			}

			generate_question(mode, my_type);

		} else if (game_type.equals("Power")) {
			current_question_number++;

			if (mode == 0) // mode ==0 means easy mode
			{
				number_one = rand.nextInt(10);

				top_question
						.setText(Html.fromHtml(number_one + "<sup>2</sup>"));
				bottom_question.setText(Html.fromHtml(number_one
						+ "<sup>2</sup>"));
				correct_ans = number_one * number_one;

			}
			if (mode == 1) // mode == 1 means medium mode
			{

				number_one = rand.nextInt(90) + 10;
				top_question
						.setText(Html.fromHtml(number_one + "<sup>2</sup>"));
				bottom_question.setText(Html.fromHtml(number_one
						+ "<sup>2</sup>"));
				correct_ans = number_one * number_one;

			}
			if (mode == 2) // mode == 2 means hard mode
			{

				number_one = rand.nextInt(10);

				top_question
						.setText(Html.fromHtml(number_one + "<sup>3</sup>"));
				bottom_question.setText(Html.fromHtml(number_one
						+ "<sup>3</sup>"));
				correct_ans = number_one * number_one * number_one;

			}
			if (mode == 3) // mode == 3 means expert mode
			{

				number_one = rand.nextInt(90) + 10;

				top_question
						.setText(Html.fromHtml(number_one + "<sup>3</sup>"));
				bottom_question.setText(Html.fromHtml(number_one
						+ "<sup>3</sup>"));
				correct_ans = number_one * number_one * number_one;

			}
			bottom_question.setTextSize(text_height - 2);
			top_question.setTextSize(text_height - 2);

		} else if (game_type.equals("Average")) {
			current_question_number++;

			if (mode == 0) // mode ==0 means easy mode
			{
				number_one = rand.nextInt(50) * 2;
				number_two = rand.nextInt(50) * 2;

				top_question.setText(number_one + ", " + number_two);
				bottom_question.setText(number_one + ", " + number_two);
				correct_ans = (number_one + number_two) / 2;

			}
			if (mode == 1) // mode == 1 means medium mode
			{

				number_one = rand.nextInt(50) * 2 + 1;
				number_two = rand.nextInt(50) * 2 + 1;
				number_three = rand.nextInt(50) * 2;

				top_question.setText(number_one + ", " + number_two + ", "
						+ number_three);
				bottom_question.setText(number_one + ", " + number_two + ", "
						+ number_three);
				correct_ans = (number_one + number_two + number_three) / 3;
				bottom_question.setTextSize(text_height - 2);
				top_question.setTextSize(text_height - 2);

			}
			if (mode == 2) // mode == 2 means hard mode
			{

				number_one = rand.nextInt(50) * 2 + 1;
				number_two = rand.nextInt(50) * 2 + 1;
				number_three = rand.nextInt(50) * 2;
				number_four = rand.nextInt(50) * 2;

				top_question.setText(number_one + ", " + number_two + ", "
						+ number_three + ", " + number_four);
				bottom_question.setText(number_one + ", " + number_two + ", "
						+ number_three + ", " + number_four);
				correct_ans = (number_one + number_two + number_three + number_four) / 4;
				bottom_question.setTextSize(text_height - 3);
				top_question.setTextSize(text_height - 3);

			}
			if (mode == 3) // mode == 3 means expert mode
			{

				number_one = rand.nextInt(50) * 2 + 1;
				number_two = rand.nextInt(50) * 2 + 1;
				number_three = rand.nextInt(50) * 2;
				number_four = rand.nextInt(50) * 2;
				number_five = rand.nextInt(50) * 2;

				top_question.setText(number_one + ", " + number_two + ", "
						+ number_three + ", " + number_four + ", "
						+ number_five);
				bottom_question.setText(number_one + ", " + number_two + ", "
						+ number_three + ", " + number_four + ", "
						+ number_five);
				correct_ans = (number_one + number_two + number_three
						+ number_four + number_five) / 5;
				bottom_question.setTextSize(text_height - 4);
				top_question.setTextSize(text_height - 4);

			}

		} else if (game_type.equals("Fractions")) {
			current_question_number++;

			if (mode == 0) // mode ==0 means easy mode
			{
				int new_rand = rand.nextInt(2);
				if (new_rand == 0) {
					// 50 %
					number_one = (rand.nextInt(9) + 1) * 2;
					number_two = 50;
				} else {
					// 25 %
					number_one = (rand.nextInt(9) + 1) * 4;
					number_two = 25;
				}

				top_question.setText(number_two + "% of " + number_one);
				bottom_question.setText(number_two + "% of " + number_one);
				correct_ans = (number_one * number_two) / 100;

			}
			if (mode == 1) // mode == 1 means medium mode
			{

				int new_rand = rand.nextInt(2);
				if (new_rand == 0) {
					// 20 %
					number_one = (rand.nextInt(50) + 1) * 100 / 20;
					number_two = 20;
				} else {
					// 10 %
					number_one = (rand.nextInt(25) + 1) * 100 / 10;
					number_two = 10;
				}

				top_question.setText(number_two + "% of " + number_one);
				bottom_question.setText(number_two + "% of " + number_one);
				correct_ans = (number_one * number_two) / 100;

			}
			if (mode == 2) // mode == 2 means hard mode
			{

				int new_rand = rand.nextInt(2);
				if (new_rand == 0) {
					// 4 %
					number_one = (rand.nextInt(20) + 1) * 100 / 4;
					number_two = 4;
				} else {
					// 5 %
					number_one = (rand.nextInt(30) + 1) * 100 / 5;
					number_two = 5;
				}

				top_question.setText(number_two + "% of " + number_one);
				bottom_question.setText(number_two + "% of " + number_one);
				correct_ans = (number_one * number_two) / 100;

			}
			if (mode == 3) // mode == 3 means expert mode
			{

				int new_rand = rand.nextInt(3);
				if (new_rand == 0) {
					// 2 %
					number_one = (rand.nextInt(9) + 1) * 100 / 2;
					number_two = 2;
				} else if (new_rand == 1) {
					// 4 %
					number_one = (rand.nextInt(20) + 1) * 100 / 4;
					number_two = 4;
				} else {
					// 5 %
					number_one = (rand.nextInt(30) + 1) * 100 / 5;
					number_two = 5;
				}

				top_question.setText(number_two + "% of " + number_one);
				bottom_question.setText(number_two + "% of " + number_one);
				correct_ans = (number_one * number_two) / 100;

			}

		}

		else {
			System.out.println("no matches");
		}
		a_option = correct_ans;
		b_option = correct_ans + rand.nextInt(10) + 1;
		c_option = correct_ans - rand.nextInt(10) - 1;
		if (a_option <= 15 && a_option > 2)
			c_option = a_option - rand.nextInt((int) a_option) - 1;
		if (a_option < 2)
			c_option = correct_ans + rand.nextInt(10) + 21;

		int rand_option = rand.nextInt(3);

		switch (rand_option) {
		case 0:
			b_option_a.setText(String.valueOf(a_option));
			b_option_b.setText(String.valueOf(b_option));
			b_option_c.setText(String.valueOf(c_option));

			t_option_a.setText(String.valueOf(a_option));
			t_option_b.setText(String.valueOf(b_option));
			t_option_c.setText(String.valueOf(c_option));

			correct_option = "A";
			break;

		case 1:
			b_option_a.setText(String.valueOf(b_option));
			b_option_b.setText(String.valueOf(a_option));
			b_option_c.setText(String.valueOf(c_option));

			t_option_a.setText(String.valueOf(b_option));
			t_option_b.setText(String.valueOf(a_option));
			t_option_c.setText(String.valueOf(c_option));

			correct_option = "B";
			break;

		case 2:
			b_option_a.setText(String.valueOf(b_option));
			b_option_b.setText(String.valueOf(c_option));
			b_option_c.setText(String.valueOf(a_option));

			t_option_a.setText(String.valueOf(b_option));
			t_option_b.setText(String.valueOf(c_option));
			t_option_c.setText(String.valueOf(a_option));

			correct_option = "C";
			break;

		default:
			b_option_a.setText(String.valueOf(a_option));
			b_option_b.setText(String.valueOf(b_option));
			b_option_c.setText(String.valueOf(c_option));

			t_option_a.setText(String.valueOf(a_option));
			t_option_b.setText(String.valueOf(b_option));
			t_option_c.setText(String.valueOf(c_option));

			correct_option = "A";
			break;
		}

	}

	private void next_question() {
		final RelativeLayout top_layout = (RelativeLayout) findViewById(R.id.multi_top_layout);
		final TranslateAnimation moveRight = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f);

		moveRight.setDuration(500);

		moveRight.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				top_layout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				top_layout.setVisibility(View.VISIBLE);
			}
		});
		top_layout.startAnimation(moveRight);

		final RelativeLayout bottom_layout = (RelativeLayout) findViewById(R.id.multi_bottom_layout);
		final TranslateAnimation moveRight1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f);

		moveRight1.setDuration(500);

		moveRight1.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				bottom_layout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				generate_question(mode, type);
				bottom_layout.setVisibility(View.VISIBLE);
			}
		});
		bottom_layout.startAnimation(moveRight1);
	}

}
