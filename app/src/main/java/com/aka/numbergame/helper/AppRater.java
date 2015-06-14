package com.aka.numbergame.helper;

import android.R.color;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aka.numbergame.R;

public class AppRater {

	private final static String APP_TITLE = "Math Expert - Multiplayer";
	private final static String APP_PNAME = "com.aka.numbergame";
	private final static int DAYS_UNTIL_PROMPT = 0;
	private final static int LAUNCHES_UNTIL_PROMPT = 4;

	public static void app_launched(Context mContext) {
		SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
		if (prefs.getBoolean("dontshowagain", false)) {
			return;
		}

		SharedPreferences.Editor editor = prefs.edit();

		// Increment launch counter
		long launch_count = prefs.getLong("launch_count", 0) + 1;
		editor.putLong("launch_count", launch_count);

		// Get date of first launch
		Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong("date_firstlaunch", date_firstLaunch);
		}

		// Wait at least n days before opening
		if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
			if (System.currentTimeMillis() >= date_firstLaunch
					+ (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
				showRateDialog(mContext, editor);
			}
		}

		editor.commit();
	}

	public static void showRateDialog(final Context mContext,
			final SharedPreferences.Editor editor) {
		final Dialog dialog = new Dialog(mContext);
		// dialog.setTitle("Love this app");
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LinearLayout ll = new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundColor(0xFFF7F9FC);

		TextView tv = new TextView(mContext);
		tv.setText("Love " + APP_TITLE);

		tv.setTextColor(0xFF0167CB);
		tv.setTextSize(28);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);

		tv.setPadding(20, 20, 20, 10);
		ll.addView(tv);

		TextView tv1 = new TextView(mContext);
		tv1.setText("Wow, you have read few topics. \n Does it deserve 5 star ??");

		tv1.setTextColor(0xFF858587);
		tv1.setTextSize(20);
		tv1.setGravity(Gravity.CENTER_HORIZONTAL);
		tv1.setPadding(10, 10, 10, 10);
		ll.addView(tv1);

		ImageView iv1 = new ImageView(mContext);
		iv1.setImageResource(R.drawable.five_star);
		iv1.setPadding(10, 10, 10, 10);
		ll.addView(iv1);

		Button b1 = new Button(mContext);
		b1.setText("Rate Now");
		b1.setBackgroundColor(0xFFFFFFFF);
		b1.setTextColor(0xFF7DC223);
		b1.setTextSize(25);
		b1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.putInt("is_app_rated", 1);

					editor.commit();
				}

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("market://details?id=" + APP_PNAME));
				mContext.startActivity(intent);
				dialog.dismiss();
			}
		});
		ll.addView(b1);

		Button b2 = new Button(mContext);
		b2.setText("Remind me later");
		//b2.setBackgroundColor(color.white);
		b2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		ll.addView(b2);

		Button b3 = new Button(mContext);
		b3.setText("No, thanks");
		//b3.setBackgroundColor((color.white);
		b3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);

					editor.commit();
				}
				dialog.dismiss();
			}
		});
		ll.addView(b3);

		dialog.setContentView(ll);
		dialog.show();
	}
}
// see
// http://androidsnippets.com/prompt-engaged-users-to-rate-your-app-in-the-android-market-appirater
