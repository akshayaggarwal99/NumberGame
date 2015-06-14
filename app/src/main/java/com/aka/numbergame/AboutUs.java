package com.aka.numbergame;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;

public class AboutUs extends ActionBarActivity {

	String version = "1.0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		try {
			PackageInfo pInfo;
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ImageButton email_button = (ImageButton) findViewById(R.id.send_an_email);

		email_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(
						Intent.ACTION_SEND);
				String aEmailList[] = { "madguy.et@gmail.com" };
				emailIntent.putExtra(Intent.EXTRA_EMAIL,
						aEmailList);
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						getResources().getString(R.string.app_name) + " ("
								+ version + ")");
				emailIntent.setType("plain/text");
				startActivity(emailIntent);
			}
		});

		ImageButton fb_icon = (ImageButton) findViewById(R.id.fb_icon);
		// ImageButton you_tube_icob = (ImageButton)
		// findViewById(R.id.you_tube_icon);

		fb_icon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("https://www.facebook.com/madguy.softlabs");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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

}
