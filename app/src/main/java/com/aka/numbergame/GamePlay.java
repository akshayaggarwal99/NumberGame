package com.aka.numbergame;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
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

public class GamePlay extends Activity {
	private Handler mHandler = new Handler();
	Button button_1, button_2, button_3, button_4, button_5, button_6,
			button_7, button_8, button_9, button_clear, button_0;
	TextView game_question, game_input, tv_current_question, tv_mode;;

	int correct_ans = 0, current_question_number = 0, total_question_limt = 15,
			wrong_press_number = 0;
	Context context;
	SoundPool sp;
	int wrong_press, loose_game, win_game, right_press;

	long time_start, time_end;
	private String type;
	Vibrator v = null;
	int mode;
	SharedPreferences sharepreference;

	private AdView adView;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game_play);
		context = this;
		time_start = System.currentTimeMillis();

		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		wrong_press = sp.load(this, R.raw.blast_1, 1);
		loose_game = sp.load(this, R.raw.loses, 1);
		win_game = sp.load(this, R.raw.wins, 1);
		right_press = sp.load(this, R.raw.button_tap_1, 1);
		v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

		/* admob ads */
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(getResources().getString(R.string.AD_UNIT_ID));
		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_adview);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("B80542AC486A951BA4E0E4E6D9469287").build();
		adView.loadAd(adRequest);

		game_question = (TextView) findViewById(R.id.tv_game_question);
		game_input = (TextView) findViewById(R.id.tv_game_result_input);
		tv_current_question = (TextView) findViewById(R.id.tv_game_play_question);
		tv_mode = (TextView) findViewById(R.id.tv_game_play_mode);
		game_input.setTextColor(Color.BLACK);

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

		/* set the text size in each view */
		// int width = 0;
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

		/*
		 * Log.d("text height", String.valueOf(text_height));
		 * 
		 * Toast.makeText( GamePlay.this, "Text height : " +
		 * String.valueOf(text_height) + "\n Hint height :" +
		 * String.valueOf(hint_height), Toast.LENGTH_SHORT).show();
		 */

		game_input.setTextSize(text_height);
		game_question.setTextSize(text_height);
		tv_current_question.setTextSize(hint_height);
		tv_mode.setTextSize(hint_height);
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

		type = getIntent().getExtras().getString("my_type");
		mode = getIntent().getExtras().getInt("mode");

		// call the main function
		generate_question(mode, type);

		tv_current_question.setText(String.valueOf(current_question_number)
				+ " / 15");
		// tv_current_question.setTextColor(Color.GREEN);
		/* set the mode value */
		String mode_value;

		switch (mode) {
		case 0:
			mode_value = "Easy";
			break;

		case 1:
			mode_value = "Medium";
			break;

		case 2:
			mode_value = "Hard";
			break;

		case 3:
			mode_value = "Expert";
			break;

		default:
			mode_value = "Easy";
			break;
		}
		tv_mode.setText(mode_value);
		tv_mode.setTextColor(Color.GREEN);

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
		// Destroy the AdView.
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

	public void generate_question(int mode, String game_type) {

		if (current_question_number < total_question_limt)

		{
			int number_one = 0, number_two = 0, number_three = 0, number_four = 0, number_five = 0;

			Random rand = new Random();

			if (game_type.equals("Addition")) {
				current_question_number++;

				if (mode == 0) // mode ==0 means easy mode
				{
					number_one = rand.nextInt(10);
					number_two = rand.nextInt(10);

					game_question.setText(number_one + " + " + number_two);
					correct_ans = number_one + number_two;

				}
				if (mode == 1) // mode == 1 means medium mode
				{

					number_one = rand.nextInt(90) + 10;
					number_two = rand.nextInt(8) + 2;
					game_question.setText(number_one + " + " + number_two);
					correct_ans = number_one + number_two;

				}
				if (mode == 2) // mode == 2 means hard mode
				{

					number_one = rand.nextInt(90) + 10;
					number_two = rand.nextInt(90) + 10;
					number_three = rand.nextInt(90) + 10;
					game_question.setText(number_one + " + " + number_two
							+ " + " + number_three);
					correct_ans = number_one + number_two + number_three;

				}
				if (mode == 3) // mode == 3 means expert mode
				{

					number_one = rand.nextInt(90) + 10;
					number_two = rand.nextInt(90) + 10;
					number_three = rand.nextInt(90) + 10;
					number_four = rand.nextInt(90) + 10;
					game_question.setText(number_one + " + " + number_two
							+ " + " + number_three + " + " + number_four);
					correct_ans = number_one + number_two + number_three
							+ number_four;

				}

			} else if (game_type.equals("Subtraction")) {
				current_question_number++;

				if (mode == 0) // mode ==0 means easy mode
				{

					number_two = rand.nextInt(10);
					number_one = rand.nextInt(10 - number_two) + (number_two);
					game_question.setText(number_one + " - " + number_two);
					correct_ans = number_one - number_two;
				}
				if (mode == 1) // mode == 1 means medium mode
				{

					number_two = rand.nextInt(8) + 2;
					number_one = rand.nextInt(100 - number_two) + (number_two);
					game_question.setText(number_one + " - " + number_two);
					correct_ans = number_one - number_two;
				}
				if (mode == 2) // mode == 2 means hard mode
				{

					number_two = rand.nextInt(90) + 10;
					number_one = rand.nextInt(100 - number_two) + (number_two);
					number_three = rand.nextInt(10);
					game_question.setText(number_one + " - " + number_two
							+ " + " + number_three);
					correct_ans = number_one - number_two + number_three;
				}
				if (mode == 3) // mode == 3 means expert mode
				{

					number_two = rand.nextInt(8) + 2;
					number_one = rand.nextInt(100 - number_two) + (number_two);

					number_four = rand.nextInt(8) + 2;
					number_three = rand.nextInt(100 - number_four)
							+ (number_four);

					game_question.setText(number_one + " - " + number_two
							+ " + " + number_three + " - " + number_four);
					correct_ans = number_one - number_two + number_three
							- number_four;
				}

			}

			else if (game_type.equals("Multiplication")) {
				current_question_number++;

				if (mode == 0) // mode ==0 means easy mode
				{

					number_one = rand.nextInt(10);
					number_two = rand.nextInt(10);
					game_question.setText(number_one + " x " + number_two);
					correct_ans = number_one * number_two;

				}
				if (mode == 1) // mode == 1 means medium mode
				{

					number_one = rand.nextInt(90) + 10;
					number_two = rand.nextInt(8) + 2;
					game_question.setText(number_one + " x " + number_two);
					correct_ans = number_one * number_two;
				}

				if (mode == 2) // mode == 2 means hard mode
				{

					number_one = rand.nextInt(90) + 10;
					number_two = rand.nextInt(8) + 2;
					number_three = rand.nextInt(8) + 2;
					game_question.setText(number_one + " x " + number_two
							+ " x " + number_three);
					correct_ans = number_one * number_two * number_three;

				}
				if (mode == 3) // mode == 3 means expert mode
				{

					number_one = rand.nextInt(900) + 100;
					number_two = rand.nextInt(900) + 100;
					number_three = rand.nextInt(8) + 2;
					game_question.setText(number_one + " x " + number_two
							+ " x " + number_three);
					correct_ans = number_one * number_two * number_three;

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

				game_question.setText(number_one + " / " + number_two);
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

					game_question.setText(Html.fromHtml(number_one
							+ "<sup>2</sup>"));
					correct_ans = number_one * number_one;

				}
				if (mode == 1) // mode == 1 means medium mode
				{

					number_one = rand.nextInt(90) + 10;
					game_question.setText(Html.fromHtml(number_one
							+ "<sup>2</sup>"));
					correct_ans = number_one * number_one;

				}
				if (mode == 2) // mode == 2 means hard mode
				{

					number_one = rand.nextInt(10);

					game_question.setText(Html.fromHtml(number_one
							+ "<sup>3</sup>"));
					correct_ans = number_one * number_one * number_one;

				}
				if (mode == 3) // mode == 3 means expert mode
				{

					number_one = rand.nextInt(90) + 10;

					game_question.setText(Html.fromHtml(number_one
							+ "<sup>3</sup>"));
					correct_ans = number_one * number_one * number_one;

				}

			} else if (game_type.equals("Average")) {
				current_question_number++;

				if (mode == 0) // mode ==0 means easy mode
				{
					number_one = rand.nextInt(50) * 2;
					number_two = rand.nextInt(50) * 2;

					game_question.setText(number_one + ", " + number_two);
					correct_ans = (number_one + number_two) / 2;

				}
				if (mode == 1) // mode == 1 means medium mode
				{

					number_one = rand.nextInt(50) * 2 + 1;
					number_two = rand.nextInt(50) * 2 + 1;
					number_three = rand.nextInt(50) * 2;

					game_question.setText(number_one + ", " + number_two + ", "
							+ number_three);
					correct_ans = (number_one + number_two + number_three) / 3;

				}
				if (mode == 2) // mode == 2 means hard mode
				{

					number_one = rand.nextInt(50) * 2 + 1;
					number_two = rand.nextInt(50) * 2 + 1;
					number_three = rand.nextInt(50) * 2;
					number_four = rand.nextInt(50) * 2;

					game_question.setText(number_one + ", " + number_two + ", "
							+ number_three + ", " + number_four);
					correct_ans = (number_one + number_two + number_three + number_four) / 4;

				}
				if (mode == 3) // mode == 3 means expert mode
				{

					number_one = rand.nextInt(50) * 2 + 1;
					number_two = rand.nextInt(50) * 2 + 1;
					number_three = rand.nextInt(50) * 2;
					number_four = rand.nextInt(50) * 2;
					number_five = rand.nextInt(50) * 2;

					game_question.setText(number_one + ", " + number_two + ", "
							+ number_three + ", " + number_four + ", "
							+ number_five);
					correct_ans = (number_one + number_two + number_three
							+ number_four + number_five) / 5;

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

					game_question.setText(number_two + "% of " + number_one);
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

					game_question.setText(number_two + "% of " + number_one);
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

					game_question.setText(number_two + "% of " + number_one);
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

					game_question.setText(number_two + "% of " + number_one);
					correct_ans = (number_one * number_two) / 100;

				}

			}

			else {
				System.out.println("no matches");
			}

		}

		else {

			v.vibrate(50);
			finish();
			time_end = System.currentTimeMillis();

			long tDelta = time_end - time_start;
			long final_time_ms = tDelta + wrong_press_number * 3000;
			double elapsedSeconds = final_time_ms / 1000.0;

			Intent intent = new Intent(getApplicationContext(),
					GamePlayComplete.class);
			intent.putExtra("wrong_attampt", wrong_press_number);
			intent.putExtra("your_time", elapsedSeconds);
			intent.putExtra("mode", mode);
			intent.putExtra("type", type);
			startActivity(intent);
		}
		tv_current_question.setText(String.valueOf(current_question_number)
				+ " / 15");
	}

	public void check_input() {
		game_input.setTextColor(Color.BLACK);
		String input_text = game_input.getText().toString();
		int input_text_value = Integer.parseInt(input_text);
		String correct_text = String.valueOf(correct_ans);

		if (input_text.length() == correct_text.length()) {
			if (input_text_value == correct_ans) {
                game_input.setTextColor(Color.GREEN);

				v.vibrate(20);

				mHandler.postDelayed(new Runnable() {
					public void run() {
						game_input.setText("");
						generate_question(mode, type);
					}
				}, 150);

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

			}
		}

	}

}
