package com.aka.numbergame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.aka.numbergame.helper.ExternalDbOpenHelper;

public class GamePlayComplete extends Activity {

	TextView wrong_attempt_text, wrong_attampt_value, time_penalty_text,
			time_penalty_value, your_time_text, your_time_value;

	private SQLiteDatabase database;
	Context context;
	String type;
	int mode;
	SharedPreferences sharepreference;
	Double local_min_time, local_max_time, local_avg_time, local_play_times;

	@SuppressWarnings("deprecation")
	@SuppressLint({ "SimpleDateFormat", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_play_complete);
		context = this;

		wrong_attempt_text = (TextView) findViewById(R.id.tv_text_wrong_attampt);
		wrong_attampt_value = (TextView) findViewById(R.id.tv_value_wrong_attampt);
		time_penalty_text = (TextView) findViewById(R.id.tv_text_time_penalty);
		time_penalty_value = (TextView) findViewById(R.id.tv_value_time_penalty);
		your_time_text = (TextView) findViewById(R.id.tv_text_your_time);
		your_time_value = (TextView) findViewById(R.id.tv_value_your_time);

		sharepreference = PreferenceManager.getDefaultSharedPreferences(this);

		/* set the text size in each view */
		//int width = 0;
		int height = 0;
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
			Display display = getWindowManager().getDefaultDisplay();
		//	width = display.getWidth(); // deprecated
			height = display.getHeight(); // deprecated
		} else {
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
		//	width = size.x;
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

		wrong_attempt_text.setTextSize(hint_height);
		wrong_attampt_value.setTextSize(text_height);
		time_penalty_text.setTextSize(hint_height);
		time_penalty_value.setTextSize(text_height);
		your_time_text.setTextSize(hint_height);
		your_time_value.setTextSize(text_height);

		int wrong_press_number = getIntent().getExtras()
				.getInt("wrong_attampt");
		double time_elapsed = getIntent().getExtras().getDouble("your_time");
		type = getIntent().getExtras().getString("type");
		mode = getIntent().getExtras().getInt("mode");

		String time_penalty = "None";

		if (wrong_press_number > 0) {
			time_penalty_value.setTextColor(Color.RED);
			time_penalty = String.valueOf(wrong_press_number) + " x " + "3 s";
		}
		wrong_attampt_value.setText(String.valueOf(wrong_press_number));
		time_penalty_value.setText(time_penalty);
		your_time_value.setText(String.valueOf(time_elapsed) + " s");
		your_time_value.setTextColor(Color.GREEN);

		/* now insert whole data in database */

		ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(this,
				getResources().getString(R.string.database));
		database = dbOpenHelper.openDataBase();

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String current_time = dateFormat.format(cal.getTime());
		int final_time = (int) time_elapsed;

		String query2 = "INSERT INTO myscores ( type, mode, time_spent, date ) VALUES ('"
				+ type
				+ "', "
				+ mode
				+ ", '"
				+ String.valueOf(final_time)
				+ "', '" + current_time + "' )";
		System.out.println(query2);

		database.execSQL(query2);

		/* alert the dialog box for the first time */

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

		/* query to find the max time of user */
		String query = "Select time_spent FROM myscores WHERE type = '" + type
				+ "' AND mode = " + mode + " ORDER BY  _id  DESC  LIMIT 20";

		ArrayList my_score = new ArrayList();
		int total_record = 0;

		Cursor friendCursor = database.rawQuery(query, null);
		friendCursor.moveToFirst();
		if (!friendCursor.isAfterLast()) {
			do {
				my_score.add(friendCursor.getString(0));
				total_record++;

			} while (friendCursor.moveToNext());
		}

		friendCursor.close();

		if (total_record > 1) {
			/* set the graph view */

			GraphViewData[] data_array = new GraphViewData[my_score.size()];
			int j = data_array.length - 1;
			for (int i = 1; i <= data_array.length; i++) {
				data_array[i - 1] = new GraphViewData(i,
						Double.parseDouble(my_score.get(j).toString()));
				j = j - 1;
			}

			GraphViewSeries exampleSeries = new GraphViewSeries(data_array);

			GraphView graphView = new LineGraphView(this, type + " "
					+ mode_value + " mode last " + data_array.length
					+ " records");
			graphView.addSeries(exampleSeries); // data
			graphView.getGraphViewStyle().setGridColor(Color.WHITE);

			graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.GREEN);

			graphView.getGraphViewStyle().setVerticalLabelsColor(Color.YELLOW);
			graphView.getGraphViewStyle().setTextSize(hint_height);

			LinearLayout layout = (LinearLayout) findViewById(R.id.ll_graph_view);
			layout.addView(graphView);
		} else {
			LinearLayout layout = (LinearLayout) findViewById(R.id.ll_graph_view);
			TextView tv = new TextView(GamePlayComplete.this);
			tv.setText("Play at least 2 times to see graph !!");

			tv.setTextColor(0xFFFFFFFF);
			tv.setTextSize(20);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);

			tv.setPadding(20, 20, 20, 10);
			layout.addView(tv);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

}
