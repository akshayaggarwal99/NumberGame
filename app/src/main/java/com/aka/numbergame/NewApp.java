package com.aka.numbergame;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v7.app.ActionBarActivity;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class NewApp extends ActionBarActivity {

	Context c;

	SharedPreferences sharepreference;

	private String name, package_name, tag_line, icon_url, full_url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_app_page);
		getSupportActionBar().setTitle("We have launched a new app");

		c = this;

		sharepreference = PreferenceManager.getDefaultSharedPreferences(this);

		name = sharepreference.getString("name", "None");
		// int id = sharepreference.getInt("id", 0);
		package_name = sharepreference.getString("package_name", "None");
		tag_line = sharepreference.getString("tag_line", "None");
		sharepreference.getString("launch_date", "None");
		icon_url = sharepreference.getString("icon", "None");
		full_url = sharepreference.getString("full_url", null);

		TextView new_app_name = (TextView) findViewById(R.id.tv_new_app_name);
		TextView new_app_details = (TextView) findViewById(R.id.tv_new_app_details);
		ImageButton new_app_icon = (ImageButton) findViewById(R.id.ib_new_app_icon);
		Button get_for_free = (Button) findViewById(R.id.b_get_for_free);

		new ImageLoadTask(icon_url, new_app_icon).execute(null, null);

		new_app_name.setText(name);
		new_app_icon.setImageResource(R.drawable.new_apps);
		new_app_details.setText(tag_line);

		new_app_icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sharepreference.edit();
				editor.putString("diaplay_ads", "no");
				editor.putInt("numberofnewappdisplayed", 6);
				editor.commit();

				if (full_url.length() > 0) {
					Uri uri = Uri.parse(full_url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				} else {
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse("market://details?id=" + package_name)));
					} catch (android.content.ActivityNotFoundException anfe) {
						startActivity(new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("http://play.google.com/store/apps/details?id="
										+ package_name)));
					}
				}

			}
		});

		get_for_free.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sharepreference.edit();
				editor.putString("diaplay_ads", "no");
				editor.putInt("numberofnewappdisplayed", 6);
				editor.commit();

				if (full_url.length() > 0) {
					Uri uri = Uri.parse(full_url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				} else {
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse("market://details?id=" + package_name)));
					} catch (android.content.ActivityNotFoundException anfe) {
						startActivity(new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("http://play.google.com/store/apps/details?id="
										+ package_name)));
					}
				}

			}
		});
	}

	public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

		private String url;
		private ImageButton imageView;

		public ImageLoadTask(String url, ImageButton imageView) {
			this.url = url;
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			try {
				URL urlConnection = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) urlConnection
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			if (result != null) {
				imageView.setImageBitmap(result);
			}
		}

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		finish();
	}

	@Override
	public void onPause() {

		super.onPause();

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

}
