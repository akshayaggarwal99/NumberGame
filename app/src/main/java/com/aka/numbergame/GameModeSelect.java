package com.aka.numbergame;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.aka.numbergame.helper.CustomAdapterModeSelect;

public class GameModeSelect extends Activity {
	private String type_new = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_mode_select);

		type_new = getIntent().getExtras().getString("type");
		final String player = getIntent().getExtras().getString("player");

		GridView gridView = (GridView) findViewById(R.id.grid_view);
		CustomAdapterModeSelect adapter = new CustomAdapterModeSelect(this,
				getData());
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("ShowToast")
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				if (player.equals("multi")) {

					Intent intent = new Intent(getApplicationContext(),
							MultiGamePlay.class);
					intent.putExtra("my_type", type_new);
					intent.putExtra("mode", position);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getApplicationContext(),
							GamePlayStartTimer.class);
					intent.putExtra("my_type", type_new);
					intent.putExtra("mode", position);
					startActivity(intent);
				}

			}
		});

	}

	public ArrayList<Integer> getData() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(R.drawable.easy);
		list.add(R.drawable.medium);
		list.add(R.drawable.hard);
		list.add(R.drawable.expert);

		return list;
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
