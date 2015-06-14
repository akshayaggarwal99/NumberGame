package com.aka.numbergame;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class ThinkTank extends Activity {

	private Handler mHandler = new Handler();
	Random rand;
	TextView tv_text, tv_int, tv_progress, tv_current_question;
	Button next;
	int result_num, total_question_limit = 10, current_question = 1;
	Context context;
	ProgressBar wait_time;
	CountDownTimer my_timer;
	SoundPool sp;
	int wrong_press, loose_game, win_game, right_press;

	String answer_string = "";

	private AdView adView;
	public static final String AD_UNIT_ID = "ca-app-pub-7137206256481745/7918156514";
	public static final String Interstitial_AD_UNIT_ID = "ca-app-pub-7137206256481745/1871622912";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.think_tank);

		context = this;

		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		wrong_press = sp.load(this, R.raw.blast_1, 1);
		loose_game = sp.load(this, R.raw.loses, 1);
		win_game = sp.load(this, R.raw.wins, 1);
		right_press = sp.load(this, R.raw.button_tap_1, 1);

		/* admob ads */
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_adview);
		layout.setVisibility(View.GONE);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("B80542AC486A951BA4E0E4E6D9469287").build();
		adView.loadAd(adRequest);
		// ///////////////////////////////

		tv_text = (TextView) findViewById(R.id.tv_think_tank_text);
		tv_text.setTextSize(30);
		tv_int = (TextView) findViewById(R.id.tv_think_tank_digit);
		tv_int.setTextSize(45);
		next = (Button) findViewById(R.id.b_think_tank_next);
		wait_time = (ProgressBar) findViewById(R.id.pb_think_tank_wait_time);
		// tv_progress = (TextView) findViewById(R.id.tv_progress_circle);
		tv_current_question = (TextView) findViewById(R.id.tv_game_play_question);
		wait_time.setMax(600);
		// tv_progress.setText("10");

		rand = new Random();
		result_num = rand.nextInt(10) + 2;
		tv_int.setText(String.valueOf(result_num));

		answer_string = "Start With <br>" + String.valueOf(result_num)
				+ " = <b>" + String.valueOf(result_num) + "</b><br><br>"; // add
																			// to
																			// answer
																			// string

		tv_current_question.setText(String.valueOf(current_question) + " / "
				+ String.valueOf(total_question_limit));

		// //////////////////////
		/*
		 * Toast.makeText(context, "Final ans is " + result_num,
		 * Toast.LENGTH_SHORT).show();
		 */
		// //////////////////////////////

		my_timer = new CountDownTimer(6 * 1000, 10) {
			@Override
			public void onTick(long millisUntilFinished) {
				int time = (int) (millisUntilFinished / 10);
				// tv_progress.setText(String.valueOf(time));

				// long seconds = millisUntilFinished / 1000;
				int barVal = (600) - time;
				wait_time.setProgress(barVal);
				// tv_progress.setText(String.valueOf(time));

			}

			@Override
			public void onFinish() {
				wait_time.setProgress(0);
				// tv_progress.setText("0");
				update_question();
				my_timer.start();
			}
		};
		my_timer.start();

		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				my_timer.cancel();
				update_question();
				my_timer.start();
			}
		});

	}

	@Override
	protected void onPause() {
		if (adView != null) {
			adView.pause();
		}
		my_timer.cancel();
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();
		my_timer.start();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	public void onBackPressed() {
		my_timer.cancel();
		finish();
		super.onBackPressed();

	}

	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	public void update_question() { // function to generate question

		if (current_question < total_question_limit)

		{

			if (right_press != 0) {
				// sp.play(right_press, 1, 1, 0, 0, 1);
			}

			Vibrator v = null;
			v = (Vibrator) this.context
					.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(20);

			current_question++;
			tv_text.setText("");
			tv_int.setText("");
			mHandler.postDelayed(new Runnable() {
				public void run() {
					int question = rand.nextInt(4);

					switch (question) {
					case 0:
						addition();
						break;

					case 1:
						subtraction();
						break;

					case 2:
						multiplication();
						break;

					case 3:
						division();
						break;

					default:
						subtraction();
						break;
					}

					tv_current_question.setText(String
							.valueOf(current_question)
							+ " / "
							+ String.valueOf(total_question_limit));

					// ////////////////////////
					/*
					 * Toast.makeText(context, "Final ans is " + result_num,
					 * Toast.LENGTH_SHORT).show();
					 */
					// ////////////////
				}
			}, 250);

		} else {

			finish();
			Intent intent = new Intent(getApplicationContext(),
					ThinkTankInput.class);
			intent.putExtra("correct_ans", result_num);
			intent.putExtra("correct_ans_string", answer_string);
			startActivity(intent);
		}

	}

	// Function for addition
	public void addition() {
		int new_int = rand.nextInt(10) + 2;
		int old_result = result_num;
		result_num = result_num + new_int;
		tv_text.setText("Add");
		tv_int.setText(String.valueOf(new_int));
		answer_string = answer_string + "Add " + String.valueOf(new_int)
				+ "<br> " + String.valueOf(old_result) + " + "
				+ String.valueOf(new_int) + " = <b>"
				+ String.valueOf(result_num) + "</b><br><br>";
	}

	// Function for subtraction
	public void subtraction() {
		int new_int = rand.nextInt(result_num);
		int old_result = result_num;
		result_num = result_num - new_int;
		tv_text.setText("Subtract");
		tv_int.setText(String.valueOf(new_int));

		answer_string = answer_string + "Subtract " + String.valueOf(new_int)
				+ "<br> " + String.valueOf(old_result) + " - "
				+ String.valueOf(new_int) + " = <b>"
				+ String.valueOf(result_num) + "</b><br><br>";
	}

	// Function for multiplication
	public void multiplication() {
		int new_int = rand.nextInt(10) + 2;
		int old_result = result_num;
		if (result_num < 20) {
			result_num = result_num * new_int;
			tv_text.setText("Multiply by");
			tv_int.setText(String.valueOf(new_int));

			answer_string = answer_string + "Multiply by "
					+ String.valueOf(new_int) + "<br> "
					+ String.valueOf(old_result) + " x "
					+ String.valueOf(new_int) + " = <b>"
					+ String.valueOf(result_num) + "</b><br><br>";
		} else
			division();
	}

	// Function for division
	public void division() {

		if (isPrime(result_num) == true) {
			int new_int = rand.nextInt(10) + 2;
			int old_result = result_num;
			result_num = result_num + new_int;
			tv_text.setText("Add");
			tv_int.setText(String.valueOf(new_int));

			answer_string = answer_string + "Add " + String.valueOf(new_int)
					+ "<br> " + String.valueOf(old_result) + " + "
					+ String.valueOf(new_int) + " = <b>"
					+ String.valueOf(result_num) + "</b><br><br>";
		} else {

			int new_int = devisor(result_num);
			int old_result = result_num;
			result_num = result_num / new_int;
			tv_text.setText("Divide by");
			tv_int.setText(String.valueOf(new_int));

			answer_string = answer_string + "Divide by "
					+ String.valueOf(new_int) + "<br> "
					+ String.valueOf(old_result) + " / "
					+ String.valueOf(new_int) + " = <b>"
					+ String.valueOf(result_num) + "</b><br><br>";
		}

		// result_num = current_num / new_int;

	}

	public Boolean isPrime(int number) { // function to check prime number
		boolean isdivisible = false;

		for (int i = 2; i <= (int) Math.sqrt(number); i++) {
			if (number % i == 0)
				isdivisible = true;
		}

		if (isdivisible == true)
			return false;

		else
			return true;

	}

	public int devisor(int number) { // function to check devisor

		int devisor = 1;

		for (int i = 2; i <= (int) Math.sqrt(number); i++) {
			if (number % i == 0)
				devisor = i;
		}

		return devisor;

	}

}
