/*
 *@author	Christopher Rockwell 
 *Description: This custom array adapter will get the title, image, critic rating, and audience rating to my ListView.
 */

package com.ChristopherRockwell.topdvdrentals;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

// This custom array adapter will populate the ListView rows with the appropriate movie data
public class MoviesArrayAdapter extends ArrayAdapter<Movie>{
	Context context;

	private SmartImageView img;
	private TextView title;
	private TextView critic;
	private TextView audience;
	static String TAG = "NETWORK DATA - MAINACTIVITY";

	private List<Movie> movies = new ArrayList<Movie>();

	public MoviesArrayAdapter(Context context, int textViewResourceId,
			List<Movie> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.movies = objects;
	}

	public int getCount() {
		return this.movies.size();
	}

	public Movie getItem(int index) {
		return this.movies.get(index);
	}

	// get the ListView row and inflate it
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			// ROW INFLATION
			Log.d("Starting: ", "XML Row Inflation ... ");
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.list_row, parent, false);
			Log.d("Success: ", "Successfully completed XML Row Inflation!");
		}

		// Get item by position
		Movie movie = getItem(position);
		img = (SmartImageView) row.findViewById(R.id.img);
		title = (TextView) row.findViewById(R.id.title);
		critic = (TextView) row.findViewById(R.id.rating1);
		audience = (TextView) row.findViewById(R.id.rating2);

		// set ListView rows text color and custom font
		title.setTextColor(context.getResources().getColor(R.color.btn_color));
		critic.setTextColor(context.getResources().getColor(R.color.btn_color));
		audience.setTextColor(context.getResources().getColor(R.color.btn_color));

		title.setTypeface(MainActivity.customFont2);
		critic.setTypeface(MainActivity.customFont2);
		audience.setTypeface(MainActivity.customFont2);

		// set list item row values
		title.setText(movie.name);
		critic.setText(movie.critic);
		audience.setText(movie.audience);
		img.setImageUrl(movie.image);


		return row;
	}

	public class MovieFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// TODO Auto-generated method stub

			constraint = constraint.toString();
			int conInt = Integer.parseInt((String) constraint);

			FilterResults newFilterResults = new FilterResults();

			if (constraint != null && constraint.length() > 0) {


				List<Movie> auxData = new ArrayList<Movie>();

				for (int i = 0; i < movies.size(); i++) {
					if (Integer.parseInt(movies.get(i).critic) >= conInt)
						auxData.add(movies.get(i));
				}

				newFilterResults.count = auxData.size();
				newFilterResults.values = auxData;
			} else {

				newFilterResults.count = movies.size();
				newFilterResults.values = movies;
			}

			return newFilterResults;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {

			List<Movie> resultData = new ArrayList<Movie>();

			resultData = (List<Movie>) results.values;

			MoviesArrayAdapter adapter = new MoviesArrayAdapter(context, R.layout.list_row, resultData);

			MainActivity.listV.setAdapter(adapter);

			//          notifyDataSetChanged();
		}

	}
	@Override
	public Filter getFilter() {
		Filter filter = null;

		if(filter == null)
			filter = new MovieFilter();
		return filter;
	}
}
