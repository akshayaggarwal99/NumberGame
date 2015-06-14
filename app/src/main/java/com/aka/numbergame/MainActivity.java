package com.aka.numbergame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aka.numbergame.helper.AppRater;
import com.aka.numbergame.helper.MainPageCustomAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

public class MainActivity extends ActionBarActivity {
	//private InterstitialAd interstitial;
	int mPosition = -1;
	String mTitle = "";
	String[] mCountries;
	// Array of integers points to images stored in /res/drawable-ldpi/
	int[] mFlags = new int[] { R.drawable.d_home_icon,

			R.drawable.brain, R.drawable.d_setting_icon, R.drawable.d_about_icon,
			R.drawable.d_feedback_icon, R.drawable.d_share,
			R.drawable.d_rate_icon };

	// Array of strings to initial counts
	String[] mCount = new String[] { "", "", "", "", "", "", "" };

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private LinearLayout mDrawer;
	private List<HashMap<String, String>> mList;
	private SimpleAdapter mAdapter;
	final private String COUNTRY = "country";
	final private String FLAG = "flag";
	final private String COUNT = "count";

	Boolean fullscreenloaded = false;
	Boolean linkloaded = false;

	Boolean startappdisplayed = false;
	Context c;

	SharedPreferences sharepreference;

	private ListView listView;
	MainPageCustomAdapter list_adapter;
	HashMap<String, String> map = null;
	ArrayList<HashMap<String, String>> topic_data = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		ImageView splash=(ImageView)findViewById(R.id.splash_think_tank);
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
		splash.startAnimation(hyperspaceJumpAnimation);



		AppRater.app_launched(this);

		// ////////////////////////////

		// Getting an array of country names
		mCountries = getResources().getStringArray(R.array.menu_items);
		// Title of the activity
		mTitle = (String)getSupportActionBar().getTitle();
		// Getting a reference to the drawer listview
		mDrawerList = (ListView) findViewById(R.id.drawer_list);
		// Getting a reference to the sidebar drawer ( Title + ListView )
		mDrawer = (LinearLayout) findViewById(R.id.drawer);
		// Each row in the list stores country name, count and flag
		mList = new ArrayList<HashMap<String, String>>();

		TextView d_user_name = (TextView) findViewById(R.id.tv_d_user_name);
		TextView d_user_location = (TextView) findViewById(R.id.tv_d_user_location);

		sharepreference = PreferenceManager.getDefaultSharedPreferences(this);
		d_user_name.setText(sharepreference.getString("username", "MadGuy"));
		d_user_location.setText(sharepreference.getString("userlocation",
				"India"));

		for (int i = 0; i < 7; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put(COUNTRY, mCountries[i]);
			hm.put(COUNT, mCount[i]);
			hm.put(FLAG, Integer.toString(mFlags[i]));
			mList.add(hm);
		}

		// Keys used in Hashmap
		String[] from = { FLAG, COUNTRY, COUNT };

		// Ids of views in listview_layout
		int[] to = { R.id.flag, R.id.country, R.id.count };

		// Instantiating an adapter to store each items
		// R.layout.drawer_layout defines the layout of each item
		mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, from,
				to);

		// Getting reference to DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Creating a ToggleButton for NavigationDrawer with drawer event
		// listener
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				highlightSelectedCountry();
				supportInvalidateOptionsMenu();
			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle("Exam GK");
				supportInvalidateOptionsMenu();
			}
		};

		// Setting event listener for the drawer
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// ItemClick event handler for the drawer items
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {

				mDrawerLayout.closeDrawer(mDrawer);
				// onclick_listner(position);
				onclick_listner(position);

			}
		});

		// Enabling Up navigation
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setDisplayShowHomeEnabled(true);

		// Setting the adapter to the listView
		mDrawerList.setAdapter(mAdapter);

		// /////////////////////////////

		// /////////////////////////////

		// ////////////////////////////

		c = this;

		/* get old shared preferences */
		sharepreference = PreferenceManager.getDefaultSharedPreferences(this);

		map = new HashMap<String, String>();
		map.put("category", "Addition");
		map.put("context", "main_page");
		map.put("row_id", "1");
		topic_data.add(map);

		map = new HashMap<String, String>();
		map.put("category", "Subtraction");
		map.put("context", "main_page");
		map.put("row_id", "2");
		topic_data.add(map);

		map = new HashMap<String, String>();
		map.put("category", "Multiplication");
		map.put("context", "main_page");
		map.put("row_id", "3");
		topic_data.add(map);

		map = new HashMap<String, String>();
		map.put("category", "Division");
		map.put("context", "main_page");
		map.put("row_id", "4");
		topic_data.add(map);

		map = new HashMap<String, String>();
		map.put("category", "Mixed");
		map.put("context", "main_page");
		map.put("row_id", "5");
		topic_data.add(map);

		map = new HashMap<String, String>();
		map.put("category", "Power");
		map.put("context", "main_page");
		map.put("row_id", "6");
		topic_data.add(map);

		map = new HashMap<String, String>();
		map.put("category", "Average");
		map.put("context", "main_page");
		map.put("row_id", "10");
		topic_data.add(map);

		map = new HashMap<String, String>();
		map.put("category", "Fractions");
		map.put("context", "main_page");
		map.put("row_id", "9");
		topic_data.add(map);

		/*
		 * map = new HashMap<String, String>(); map.put("category",
		 * "GCD & LCM"); map.put("context", "main_page"); map.put("row_id",
		 * "8"); topic_data.add(map);
		 *
		 * map = new HashMap<String, String>(); map.put("category", "Equation");
		 * map.put("context", "main_page"); map.put("row_id", "11");
		 * topic_data.add(map);
		 *
		 * map = new HashMap<String, String>(); map.put("category",
		 * "Statistics"); map.put("context", "main_page"); map.put("row_id",
		 * "7"); topic_data.add(map);
		 */

		map = new HashMap<String, String>();
		map.put("category", "Rate this app");
		map.put("context", "main_page");
		map.put("row_id", "12");
		topic_data.add(map);

		map = new HashMap<String, String>();
		map.put("category", "Tell a friend");
		map.put("context", "main_page");
		map.put("row_id", "13");
		topic_data.add(map);

		map = new HashMap<String, String>();
		map.put("category", "Like on Facebook");
		map.put("context", "main_page");
		map.put("row_id", "14");
		topic_data.add(map);

		listView = (ListView) findViewById(R.id.list);
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.attachToListView(listView);

		list_adapter = new MainPageCustomAdapter(this, topic_data);
		listView.setAdapter(list_adapter);

		final String player = getIntent().getExtras().getString("player");
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
									int position, long id) {

				int row_id = Integer.parseInt(topic_data.get(position).get(
						"row_id"));
				String category = topic_data.get(position).get("category");

				if (row_id <= 11) {
					Intent intent = new Intent(getApplicationContext(),
							GameModeSelect.class);
					intent.putExtra("type", category);
					intent.putExtra("player", player);
					startActivity(intent);

				} else {

					switch (row_id) {

						case 12:

							Editor editor = sharepreference.edit();
							editor.putBoolean("dontshowagain", true);
							editor.putInt("is_app_rated", 1);
							editor.commit();
							try {
								startActivity(new Intent(
										Intent.ACTION_VIEW,
										Uri.parse("market://details?id=com.madguy.numbergame")));
							} catch (android.content.ActivityNotFoundException anfe) {
								startActivity(new Intent(
										Intent.ACTION_VIEW,
										Uri.parse("http://play.google.com/store/apps/details?id=com.madguy.numbergame")));
							}
							break;
						case 13:

							Intent sharing_intent = new Intent(
									android.content.Intent.ACTION_SEND);
							sharing_intent.setType("text/plain");
							sharing_intent
									.putExtra(
											Intent.EXTRA_TEXT,
											"Hey please check out "
													+ getResources().getString(
													R.string.app_name)
													+ " App https://play.google.com/store/apps/details?id=com.madguy.numbergame");
							sharing_intent.putExtra(
									Intent.EXTRA_SUBJECT,
									"Check out '"
											+ getResources().getString(
											R.string.app_name) + "' App");
							startActivity(Intent.createChooser(sharing_intent,
									"How do you want to share?"));
							break;

						case 14:
							Uri uri = Uri
									.parse("https://www.facebook.com/madguy.softlabs");
							Intent intent101 = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent101);

							break;

					}

				}
			}
		});

	}

	@Override
	public void onResume() {

		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		sharepreference = PreferenceManager.getDefaultSharedPreferences(this);
		String isdisplay_ads = sharepreference.getString("diaplay_ads", "no");
		int numberOfNewAppDislay = sharepreference.getInt(
				"numberofnewappdisplayed", 0);
		System.out.println(numberOfNewAppDislay);
		System.out.println(isdisplay_ads);
		if (isdisplay_ads.equals("yes")) {
			// update the shared preferences
			Editor editor = sharepreference.edit();
			editor.putInt("numberofnewappdisplayed", numberOfNewAppDislay + 1);
			editor.commit();
			if (numberOfNewAppDislay < 3) {
				Intent intent = new Intent(getApplicationContext(),
						NewApp.class);
				startActivity(intent);
			}
		} else {

			// displayInterstitial();
		}
		finish();
	}

//	public void displayInterstitial() {
//		if (interstitial.isLoaded()) {
//			interstitial.show();
//		}
//	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Highlight the selected country : 0 to 4
	public void highlightSelectedCountry() {
		int selectedItem = mDrawerList.getCheckedItemPosition();

		if (selectedItem > 4)
			mDrawerList.setItemChecked(mPosition, true);
		else
			mPosition = selectedItem;

		if (mPosition != -1)
			getSupportActionBar().setTitle(mCountries[mPosition]);
	}

	public void onclick_listner(int position) {

		switch (position) {
			case 0:

				break;

			case 1:
				Intent intent1 = new Intent(getApplicationContext(),
						ThinkTank.class);

				startActivity(intent1);

				break;

			case 2:

				Intent intent2 = new Intent(getApplicationContext(), Settings.class);

				startActivity(intent2);

				break;
			case 3:
				Intent intent3 = new Intent(getApplicationContext(), AboutUs.class);

				startActivity(intent3);

				break;

			case 4:
				String version = "1.0";
				try {
					PackageInfo pInfo;
					pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
					version = pInfo.versionName;
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				String aEmailList[] = { "madguy.et@gmail.com" };
				emailIntent
						.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"GK in Hindi for UPSC/IBPS/RRB  (" + version + ")");
				emailIntent.setType("plain/text");
				startActivity(emailIntent);

				break;

			case 5:
				Intent sharing_intent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharing_intent.setType("text/plain");
				sharing_intent
						.putExtra(
								Intent.EXTRA_TEXT,
								"Hey please check out "
										+ getResources().getString(
										R.string.app_name)
										+ " https://play.google.com/store/apps/details?id=com.madguy.numbergame");
				sharing_intent.putExtra(Intent.EXTRA_SUBJECT, "Check out ' "
						+ getResources().getString(R.string.app_name));
				startActivity(Intent.createChooser(sharing_intent,
						"How do you want to share?"));
				break;

			case 6:
				Editor editor = sharepreference.edit();
				editor.putInt("is_app_rated", 1);
				editor.commit();

				try {
					startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("market://details?id=com.madguy.numbergame")));
				} catch (android.content.ActivityNotFoundException anfe) {
					startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id=com.madguy.numbergame")));
				}

				break;

			default:

				break;
		}
	}




}
