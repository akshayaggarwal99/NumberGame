package com.aka.numbergame.helper;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aka.numbergame.R;

public class CustomAdapterModeSelect extends ArrayAdapter<Integer> {

	private Activity context;
	private ArrayList<Integer> objects;

	public CustomAdapterModeSelect(Activity context, ArrayList<Integer> objects) {
		super(context, R.layout.grid_item_layout, objects);
		this.context = context;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater i = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) i.inflate(R.layout.grid_item_layout, parent,
					false);
		}

		TextView t = (TextView) convertView.findViewById(R.id.label);
		ImageView i = (ImageView) convertView.findViewById(R.id.image);

		String name;
		switch (position) {
		case 0:
			name = "Easy Mode";
			break;

		case 1:
			name = "Medium Mode";
			break;

		case 2:
			name = "Hard Mode";
			break;
		case 3:
			name = "Expert Mode";
			break;

		default:
			name = "Easy Mode";
			break;
		}

		t.setText(name);
		i.setImageResource(objects.get(position));

		return convertView;
	}
}