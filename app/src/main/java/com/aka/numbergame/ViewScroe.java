package com.aka.numbergame;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.aka.numbergame.helper.ExternalDbOpenHelper;

public class ViewScroe extends Activity {

	private static final String DB_NAME = "myscroes.db";

	private static final String TABLE_NAME = "myscores";
	// private static final String TYPE = "type";
	// private static final String MODE = "mode";
	private static final String SCORE = "time_spent";
	// private static final String DATE = "date";

	TextView analysis_2, analysis_3, analysis_5, analysis_6, analysis_8,
			analysis_9, analysis_11, analysis_12, analysis_14, analysis_15,
			analysis_20, analysis_21;
	private SQLiteDatabase database;
	Context context;
	String type = "add_subs", mode_value = "Easy";
	int mode = 0;
	Spinner type_select, mode_select;
	SharedPreferences sharepreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_score);
		context = this;

		analysis_2 = (TextView) findViewById(R.id.tv_analysis_2);
		analysis_3 = (TextView) findViewById(R.id.tv_analysis_3);
		analysis_5 = (TextView) findViewById(R.id.tv_analysis_5);
		analysis_6 = (TextView) findViewById(R.id.tv_analysis_6);
		analysis_8 = (TextView) findViewById(R.id.tv_analysis_8);
		analysis_9 = (TextView) findViewById(R.id.tv_analysis_9);
		analysis_11 = (TextView) findViewById(R.id.tv_analysis_11);
		analysis_12 = (TextView) findViewById(R.id.tv_analysis_12);
		analysis_14 = (TextView) findViewById(R.id.tv_analysis_14);
		analysis_15 = (TextView) findViewById(R.id.tv_analysis_15);
		analysis_20 = (TextView) findViewById(R.id.tv_analysis_20);
		analysis_21 = (TextView) findViewById(R.id.tv_analysis_21);

		ImageButton what_is_this = (ImageButton) findViewById(R.id.b_show_help);
		ImageButton go_home = (ImageButton) findViewById(R.id.b_go_home);

		type_select = (Spinner) findViewById(R.id.spinner_type_select);
		mode_select = (Spinner) findViewById(R.id.spinner_mode_select);

		/* alert the dialog box for the first time */
		sharepreference = PreferenceManager.getDefaultSharedPreferences(this);

		String display_view_score_popup = sharepreference.getString(
				"display_view_score_popup", "yes");
		if (display_view_score_popup.equals("yes")) {
			Builder builder = new Builder(ViewScroe.this);
			builder.setTitle("Welcome !!");
			builder.setMessage("Here is the Complete analysis of your whole activity on this game \n\n "
					+ "Click on top left ( White Button ) and Top right ( Green Button) to change the analysis mode. \n\n"
					+ "The Graph displays the data of your last 10 activities  and the table displays your overall best performance time and avarage time. \n\n"
					+ "Formula for average time =  ( Total Time spent in a perticular mode ) / ( Total number of play of same mode ). \n\n"
					+ "This game is best played for a few minutes each day. Challenge your family and friends and most importantly have fun. \n");
			builder.setCancelable(true);
			builder.setPositiveButton("Got it",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							Editor editor = sharepreference.edit();
							editor.putString("display_view_score_popup", "no");
							editor.commit();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
		}

		/* *********************** */

		mode = set_mode_value(mode_value);
		setgraph();

		List<String> list = new ArrayList<String>();
		list.add("add_subs");
		list.add("multiply_devide");
		list.add("geeky");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		type_select.setAdapter(dataAdapter);
		type_select.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				type = parent.getItemAtPosition(pos).toString();

				if (String.valueOf(type_select.getSelectedItem()).length() > 0)
					mode_value = String.valueOf(mode_select.getSelectedItem());
				else
					mode_value = "Easy";

				mode = set_mode_value(mode_value);
				setgraph();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		List<String> list1 = new ArrayList<String>();
		list1.add("Easy");
		list1.add("Medium");
		list1.add("Hard");
		list1.add("Expert");
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list1);
		dataAdapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mode_select.setAdapter(dataAdapter1);
		mode_select.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				mode_value = parent.getItemAtPosition(pos).toString();
				mode = set_mode_value(mode_value);
				if (String.valueOf(type_select.getSelectedItem()).length() > 0)
					type = String.valueOf(type_select.getSelectedItem());
				else
					type = "add_subs";

				setgraph();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		/* now attach onclick listner to buttons */
		what_is_this.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Builder builder = new Builder(ViewScroe.this);
				builder.setTitle("Complete analysis of your activity");
				builder.setMessage("This is the complete analysis of your whole activity \n "
						+ " # Click On the Dropdown list to select the Game Mode \n"
						+ "# Graph displays your last 10 game score \n"
						+ "# Use this app daily to see the improvements in your skills"
						+ " # Min time is the Minimum time in which you have solved a particular game (till now)");
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

		go_home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);

			}
		});

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

	private int set_mode_value(String mode_string) {
		int mode;

		if (mode_string.matches("Easy"))
			mode = 0;
		else if (mode_string.matches("Medium"))
			mode = 1;
		else if (mode_string.matches("Hard"))
			mode = 2;
		else if (mode_string.matches("Expert"))
			mode = 3;

		else
			mode = 0;

		return mode;
	}

	private void setgraph() {

		// set type
		analysis_2.setText(type);
		analysis_3.setText(type);

		// set mode
		analysis_5.setText(mode_value);
		analysis_6.setText(mode_value);
		/* now select the data from database */
		ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(this,
				DB_NAME);
		database = dbOpenHelper.openDataBase();

		/* set global data */
		try {
			String query1 = "Select * FROM  global_data WHERE type = '" + type
					+ "' AND mode = " + mode;
			System.out.println(query1);

			Cursor friendCursor2 = database.rawQuery(query1, null);
			friendCursor2.moveToFirst();
			if (!friendCursor2.isAfterLast()) {
				do {
					analysis_9.setText(friendCursor2.getString(5));
					analysis_12.setText(friendCursor2.getString(3));
					analysis_15.setText(friendCursor2.getString(4));
					analysis_21.setText(friendCursor2.getString(6) + "Times");

				} while (friendCursor2.moveToNext());
			} else {
				analysis_9.setText("0");
				analysis_12.setText("0");
				analysis_15.setText("0");
				analysis_21.setText("0" + "Times");
			}

			friendCursor2.close();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(
					context,
					"Here is some problem with the database. \n Please Reinstall this app Again \n sorry for the inconvenience",
					Toast.LENGTH_LONG).show();
		}

		/* set local data */
		String query = "Select " + SCORE + " FROM " + TABLE_NAME
				+ " WHERE type = '" + type + "' AND mode = " + mode
				+ " ORDER BY  _id  DESC";
		// System.out.println(query);

		ArrayList my_score = new ArrayList();

		Cursor friendCursor = database.rawQuery(query, null);
		friendCursor.moveToFirst();
		if (!friendCursor.isAfterLast()) {
			do {
				my_score.add(friendCursor.getString(0));

			} while (friendCursor.moveToNext());
		}

		friendCursor.close();

		String[] strArray = new String[my_score.size()];
		for (int j = 0; j < strArray.length; j++)
			strArray[j] = my_score.get(j).toString();

		for (int i = 1; i <= strArray.length; i++) {

			for (int j = 0; j < (strArray.length - 1); j++) {
				if (Double.parseDouble(strArray[j + 1]) > Double
						.parseDouble(strArray[j])) {
					String temp = strArray[j];
					strArray[j] = strArray[j + 1];
					strArray[j + 1] = temp;

				}
			}
		}

		if (strArray.length == 0) {

			analysis_14.setText("0");
			analysis_11.setText("0");
			analysis_20.setText("0 times");
			analysis_8.setText("0");

		} else {
			analysis_14.setText(strArray[0]);

			analysis_11.setText(strArray[strArray.length - 1]);

			analysis_20.setText(String.valueOf(strArray.length) + " times");

			Double my_total = (double) 0;
			for (int j = 0; j < my_score.size(); j++) {
				my_total = my_total
						+ Double.parseDouble(my_score.get(j).toString());
			}

			double my_avarage = my_total / my_score.size();
			my_avarage = (double) Math.round(my_avarage * 1000) / 1000;
			analysis_8.setText(String.valueOf(my_avarage));

		}

		/* set the graph view */

		String query11 = "Select " + SCORE + " FROM " + TABLE_NAME
				+ " WHERE type = '" + type + "' AND mode = " + mode
				+ " ORDER BY  _id  DESC  LIMIT 10";
		// System.out.println(query1);

		ArrayList my_score1 = new ArrayList();

		Cursor friendCursor1 = database.rawQuery(query11, null);
		friendCursor1.moveToFirst();
		if (!friendCursor1.isAfterLast()) {
			do {
				my_score1.add(friendCursor1.getString(0));

			} while (friendCursor1.moveToNext());
		}

		friendCursor1.close();

		GraphViewData[] data_array = new GraphViewData[my_score1.size()];
		int j = data_array.length - 1;
		for (int i = 1; i <= data_array.length; i++) {
			data_array[i - 1] = new GraphViewData(i,
					Double.parseDouble(my_score1.get(j).toString()));
			j = j - 1;
		}

		GraphViewSeries exampleSeries = new GraphViewSeries(data_array);

		GraphView graphView = new LineGraphView(this, type + " " + mode_value
				+ " mode last " + data_array.length + " records");
		graphView.addSeries(exampleSeries); // data
		graphView.getGraphViewStyle().setGridColor(Color.GREEN);
		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.YELLOW);
		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.YELLOW);
		graphView.getGraphViewStyle().setTextSize(12);

		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_graph_view);
		layout.removeAllViews();
		layout.addView(graphView);
	}

}
