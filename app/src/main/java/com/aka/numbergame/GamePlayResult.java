package com.aka.numbergame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.aka.numbergame.helper.ExternalDbOpenHelper;
import com.aka.numbergame.helper.JSONParser;

public class GamePlayResult extends Activity {

	TextView wrong_attempt_text, wrong_attampt_value, time_penalty_text,
			time_penalty_value, your_time_text, your_time_value;
	// ImageButton view_score, go_home;

	private static final String DB_NAME = "myscroes.db";

	private static final String TABLE_NAME = "myscores";
	private static final String TYPE = "type";
	private static final String MODE = "mode";
	private static final String SCORE = "time_spent";
	private static final String DATE = "date";

	private SQLiteDatabase database;
	Context context;
	String type;
	int mode;
	SharedPreferences sharepreference;
	Double local_min_time, local_max_time, local_avg_time, local_play_times;

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
		// view_score = (ImageButton) findViewById(R.id.button_viewscores);
		// go_home = (ImageButton) findViewById(R.id.button_home);
		YoYo.with(Techniques.RollIn).duration(2500).playOn(wrong_attampt_value);

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
		int size = 30;

		wrong_attempt_text.setTextSize(height / size);
		wrong_attampt_value.setTextSize(height / (size + 5));
		time_penalty_text.setTextSize(height / size);
		time_penalty_value.setTextSize(height / (size + 5));
		your_time_text.setTextSize(height / size);
		your_time_value.setTextSize(height / (size + 5));

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
				DB_NAME);
		database = dbOpenHelper.openDataBase();

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String current_time = dateFormat.format(cal.getTime());

		String query2 = "INSERT INTO " + TABLE_NAME + " ( " + TYPE + ", "
				+ MODE + ", " + SCORE + ", " + DATE + " ) VALUES ('" + type
				+ "', " + mode + ", '" + String.valueOf(time_elapsed) + "', '"
				+ current_time + "' )";
		System.out.println(query2);

		database.execSQL(query2);

		/* alert the dialog box for the first time */
		sharepreference = PreferenceManager.getDefaultSharedPreferences(this);

		String display_game_complete_popup = sharepreference.getString(
				"display_game_complete_popup", "yes");
		if (display_game_complete_popup.equals("yes")) {
			Builder builder = new Builder(GamePlayResult.this);
			builder.setTitle("Welcome !!");
			builder.setMessage("Thank you for playing The Numbers Game. \n\n "
					+ "Try going through addition and subtraction easy mode first and when you get quick move on to medium and then hard mode. \n\n"
					+ "This game is best played for a few minutes each day. Monitor your daily performance, challenge your family and friends and most importantly have fun. \n");
			builder.setCancelable(true);
			builder.setPositiveButton("Got it",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();

							Editor editor = sharepreference.edit();
							editor.putString("display_game_complete_popup",
									"no");
							editor.commit();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();

		}

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
		String query = "Select " + SCORE + " FROM " + TABLE_NAME
				+ " WHERE type = '" + type + "' AND mode = " + mode
				+ " ORDER BY  _id  DESC  LIMIT 20";
		System.out.println(query);

		ArrayList my_score = new ArrayList();

		Cursor friendCursor = database.rawQuery(query, null);
		friendCursor.moveToFirst();
		if (!friendCursor.isAfterLast()) {
			do {
				my_score.add(friendCursor.getString(0));

			} while (friendCursor.moveToNext());
		}

		friendCursor.close();

		/* set the graph view */

		GraphViewData[] data_array = new GraphViewData[my_score.size()];
		int j = data_array.length - 1;
		for (int i = 1; i <= data_array.length; i++) {
			data_array[i - 1] = new GraphViewData(i,
					Double.parseDouble(my_score.get(j).toString()));
			j = j - 1;
		}

		GraphViewSeries exampleSeries = new GraphViewSeries(data_array);

		GraphView graphView = new LineGraphView(this, type + " " + mode_value
				+ " mode last " + data_array.length + " records");
		graphView.addSeries(exampleSeries); // data
		graphView.getGraphViewStyle().setGridColor(Color.WHITE);

		graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.GREEN);

		graphView.getGraphViewStyle().setVerticalLabelsColor(Color.YELLOW);
		graphView.getGraphViewStyle().setTextSize(height / (size + 5));

		LinearLayout layout = (LinearLayout) findViewById(R.id.ll_graph_view);
		layout.addView(graphView);

		load_global_data.start();

		/* post and get the global data using thread */

		// post device data
		new Thread() {
			public void run() {
				sharepreference = PreferenceManager
						.getDefaultSharedPreferences(context);

				String insert_device_details = sharepreference.getString(
						"insert_device_details", "yes");
				if (insert_device_details.equals("yes")) {
					try {
						if (postdevice_details() == true) {
							Editor editor = sharepreference.edit();
							editor.putString("insert_device_details", "no");
							editor.commit();
							System.out.println("ok");
						} else
							System.out.println("error on post device details");

					} catch (Exception e) {
						System.out.println("Post device details error");
						e.printStackTrace();
					}
				}
			}
		}.start();
		/* ///////////////// */

		post_score_details.start();
		/* ///////////////// */

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

	Thread load_global_data = new Thread() {

		private String url = "http://qurebank.com/madguy/android_apps/the_numbers_game/get_json.php?type="
				+ type + "&mode=" + mode;

		JSONArray details = null;

		public void run() {

			JSONParser jParser = new JSONParser();
			try {

				JSONObject json = jParser.getJSONFromUrl(url);
				details = json.getJSONArray("details");
				JSONObject c = details.getJSONObject(0);
				Double min_time = Double.parseDouble(c.getString("min_time"));
				Double max_time = Double.parseDouble(c.getString("max_time"));
				Double average_time = Double.parseDouble(c
						.getString("average_time"));
				Double played = Double.parseDouble(c.getString("total_play"));

				System.out.println("min time = " + min_time + " max time ="
						+ max_time + " avg time = " + average_time
						+ " played = " + played);
				System.out.println(url);

				String query2 = "INSERT INTO  global_data ( type,mode,min_time,max_time,avg_time,played  ) VALUES ('"
						+ type
						+ "', "
						+ mode
						+ ", '"
						+ String.valueOf(min_time)
						+ "', '"
						+ String.valueOf(max_time)
						+ "', '"
						+ String.valueOf(average_time)
						+ "', '"
						+ String.valueOf(played) + "')";
				System.out.println(query2);

				database.execSQL(query2);
			} catch (Exception e) {

				e.printStackTrace();
			}
			System.out.println(url);
		}

	};
	// post score data
	Thread post_score_details = new Thread() {
		public void run() {

			try {

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

					local_min_time = 0.0;
					local_max_time = 0.0;
					local_avg_time = 0.0;
					local_play_times = 0.0;

				} else {

					local_max_time = Double.valueOf(strArray[0]);

					local_min_time = Double
							.valueOf(strArray[strArray.length - 1]);

					local_play_times = (double) strArray.length;

					Double my_total = (double) 0;
					for (int j = 0; j < my_score.size(); j++) {
						my_total = my_total
								+ Double.parseDouble(my_score.get(j).toString());
					}

					double my_avarage = my_total / my_score.size();
					my_avarage = (double) Math.round(my_avarage * 1000) / 1000;

					local_avg_time = my_avarage;
				}

				// call the post score function
				postscore_details();
				System.out.println("success");
			} catch (Exception e) {
				System.out.println("Post score data error");
				e.printStackTrace();
			}
		}

	};

	/* function to post score details to server */
	public Boolean postscore_details() throws ClientProtocolException,
			IOException {

		HttpClient myClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://qurebank.com/madguy/android_apps/the_numbers_game/post_details.php");
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		String deviceId = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);

		parameters.add(new BasicNameValuePair("device_id", deviceId));
		parameters.add(new BasicNameValuePair("type", type));
		parameters.add(new BasicNameValuePair("mode", String.valueOf(mode)));
		parameters.add(new BasicNameValuePair("min_time", String
				.valueOf(local_min_time)));
		parameters.add(new BasicNameValuePair("avg_time", String
				.valueOf(local_avg_time)));
		parameters.add(new BasicNameValuePair("max_time", String
				.valueOf(local_max_time)));
		parameters.add(new BasicNameValuePair("total_play", String
				.valueOf(local_play_times)));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);
		post.setEntity(entity);

		HttpResponse response = myClient.execute(post);

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		String bufferedStringChunk = null;
		StringBuilder sb = new StringBuilder();
		while ((bufferedStringChunk = bufferedReader.readLine()) != null) {
			sb.append(bufferedStringChunk);
		}

		if (sb.toString().trim().equals("yes")) {
			return true;
		}
		return false;

	}

	// ////////////////////////////////

	/* function to post deveice details to server */
	public Boolean postdevice_details() throws ClientProtocolException,
			IOException {

		HttpClient myClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				"http://qurebank.com/madguy/android_apps/the_numbers_game/postuserdata.php");
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		String deviceId = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);

		parameters.add(new BasicNameValuePair("device_id", deviceId));
		parameters.add(new BasicNameValuePair("android_version",
				android.os.Build.VERSION.CODENAME));
		String mobileVersion = (android.os.Build.MANUFACTURER + "-" + android.os.Build.MODEL)
				.trim();
		parameters.add(new BasicNameValuePair("mobile_version", mobileVersion));
		mobileVersion = null;
		parameters.add(new BasicNameValuePair("country", context.getResources()
				.getConfiguration().locale.getCountry()));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);
		post.setEntity(entity);

		HttpResponse response = myClient.execute(post);

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		String bufferedStringChunk = null;
		StringBuilder sb = new StringBuilder();
		while ((bufferedStringChunk = bufferedReader.readLine()) != null) {
			sb.append(bufferedStringChunk);
		}

		if (sb.toString().trim().equals("yes")) {
			return true;
		}
		return false;

	}

}
