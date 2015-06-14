package com.aka.numbergame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GamePlayStartTimer extends Activity {

	Context context;
	String type = "ggg";
	int mode = 0;
	TextView type_name, mode_name, question_limit, timer_view;
	SoundPool sp;
	int timer_sound;
	CountDownTimer my_timer;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game_play_start_timer);
		context = this;

		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		timer_sound = sp.load(this, R.raw.button_tap_1, 1);

		type_name = (TextView) findViewById(R.id.tv_starter_type_name);
		mode_name = (TextView) findViewById(R.id.tv_starter_mode);
		question_limit = (TextView) findViewById(R.id.tv_starter_question_limit);
		timer_view = (TextView) findViewById(R.id.tv_starter_timer);
		TextView starter_tips = (TextView) findViewById(R.id.tv_starter_tips);

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

		type_name.setTextSize(hint_height);
		mode_name.setTextSize(hint_height);
		question_limit.setTextSize(text_height);
		timer_view.setTextSize(text_height * 2);
		starter_tips.setTextSize(hint_height - 3);

		type = getIntent().getExtras().getString("my_type");
		mode = getIntent().getExtras().getInt("mode");

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

		type_name.setText(type);
		mode_name.setText(mode_value);

		my_timer = new CountDownTimer(5000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

				int time = (int) (millisUntilFinished / 1000);

				switch (time) {
				case 4:

					timer_view.setTextColor(Color.RED);
					timer_view.setText("3");
					break;

				case 3:

					timer_view.setTextColor(Color.YELLOW);
					timer_view.setText("2");
					break;

				case 2:

					timer_view.setTextColor(Color.GREEN);
					timer_view.setText("1");
					break;

				case 1:

					timer_view.setTextColor(Color.BLUE);
					timer_view.setText("GO !");
					break;

				}

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						GamePlay.class);
				intent.putExtra("my_type", type);
				intent.putExtra("mode", mode);
				startActivity(intent);
			}
		};
		my_timer.start();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		my_timer.cancel();
		finish();
	}

	@Override
	protected void onPause() {

		super.onPause();
		finish();
	}

}
