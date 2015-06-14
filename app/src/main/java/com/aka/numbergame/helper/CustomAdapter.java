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

public class CustomAdapter extends ArrayAdapter<Integer> {

	private Activity context;
	private ArrayList<Integer> objects;

	public CustomAdapter(Activity context, ArrayList<Integer> objects) {
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
			name = "Addition & Subtraction";
			break;

		case 1:
			name = "Multiplication & Division";
			break;

		case 2:
			name = "I'm feeling Geeky";
			break;

		case 3:
			name = "The Think Tank";
			break;

		case 4:
			name = "Result Analysis";
			break;

		case 5:
			name = "About This App";
			break;
		case 6:
			name = "Our Other Apps";
			break;

		case 7:
			name = "Rate this App";
			break;

		case 8:
			name = "Share app with friends";
			break;

		default:
			name = "Addition & Subtraction";
			break;
		}

		t.setText(name);
		i.setImageResource(objects.get(position));

		return convertView;
	}
}