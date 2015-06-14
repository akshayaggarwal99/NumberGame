package com.aka.numbergame.helper;

import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.aka.numbergame.R;

@SuppressLint("InflateParams")
public class MainPageCustomAdapter extends BaseAdapter {

	LayoutInflater inflater;
	ImageView thumb_image;
	List<HashMap<String, String>> weatherDataCollection;
	ViewHolder holder;

	public MainPageCustomAdapter() {
		// TODO Auto-generated constructor stub
	}

	public MainPageCustomAdapter(Activity act, List<HashMap<String, String>> map) {

		this.weatherDataCollection = map;

		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		// return idlist.size();
		return weatherDataCollection.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		if (convertView == null) {

			vi = inflater.inflate(R.layout.main_page_custom_topic_list_row,
					null);
			holder = new ViewHolder();

			holder.tvCity = (TextView) vi.findViewById(R.id.tv_topic);

			holder.tvWeatherImage = (ImageView) vi
					.findViewById(R.id.list_image); // thumb image

			vi.setTag(holder);
		} else {

			holder = (ViewHolder) vi.getTag();
		}

		// Setting all values in listview

		String context = weatherDataCollection.get(position).get("context");
		holder.tvCity.setText(weatherDataCollection.get(position).get(
				"category"));

		int row_id = Integer.parseInt(weatherDataCollection.get(position).get(
				"row_id"));

		if (context.equals("main_page")) {

			switch (row_id) {
			case 0:
				holder.tvWeatherImage
						.setImageResource(R.drawable.d_current_affairs_icon);
				break;

			case 1:
				holder.tvWeatherImage.setImageResource(R.drawable.addition);
				break;

			case 2:
				holder.tvWeatherImage.setImageResource(R.drawable.subtraction);
				break;

			case 3:
				holder.tvWeatherImage.setImageResource(R.drawable.multiplication);
				break;
			case 4:
				holder.tvWeatherImage
						.setImageResource(R.drawable.division);
				break;

			case 5:
				holder.tvWeatherImage
						.setImageResource(R.drawable.ic_launcher);
				break;

			case 6:
				holder.tvWeatherImage.setImageResource(R.drawable.power);
				break;

			case 7:
				holder.tvWeatherImage.setImageResource(R.drawable.statistics);
				break;

			case 8:
				holder.tvWeatherImage.setImageResource(R.drawable.lcm);
				break;

			case 9:
				holder.tvWeatherImage.setImageResource(R.drawable.fractions);
				break;

			case 10:
				holder.tvWeatherImage.setImageResource(R.drawable.average);
				break;

			case 11:
				holder.tvWeatherImage
						.setImageResource(R.drawable.equations);
				break;

			case 12:
				holder.tvWeatherImage.setImageResource(R.drawable.d_rate_icon);
				break;
			case 13:
				holder.tvWeatherImage
						.setImageResource(R.drawable.d_share);
				break;

			case 14:
				holder.tvWeatherImage.setImageResource(R.drawable.fb_icon);
				break;

			case 15:
				holder.tvWeatherImage.setImageResource(R.drawable.d_flash_card);
				break;

			default:
				holder.tvWeatherImage.setImageResource(R.drawable.d_about_icon);
				break;
			}

		}

		return vi;
	}

	static class ViewHolder {

		TextView tvCity;

		ImageView tvWeatherImage;

	}

}
