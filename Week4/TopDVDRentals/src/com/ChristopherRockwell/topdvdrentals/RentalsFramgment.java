/*
 *@author	Christopher Rockwell 
 *Description: This class is the fragment class launched when the application is in landscape mode
 */

package com.ChristopherRockwell.topdvdrentals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RentalsFramgment extends Fragment {

	public interface onRentalsClick {
		public void onRentalsButtonClick();
	}

	private onRentalsClick parentActivity;
	public static final String FILE_NAME = "TopRental.txt";
	public static final String MOVIE_KEY = "movie";
	FileManagerSingleton file;
	static ListView listV;
	String srcResult;
	JSONObject castObj;
	String itemText;
	String objText;
	Context mContext;
	Button getRentalsBtn;
	Button srcButton;
	boolean haveResults = false;
	boolean checkSrc = false;
	MoviesArrayAdapter adapter;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		if (activity instanceof onRentalsClick) {
			parentActivity = (onRentalsClick) activity;
		} else {
			throw new ClassCastException(activity.toString() + "must implement onRentalsClick");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_main, container);
		getRentalsBtn = (Button) view.findViewById(R.id.rentalsBtn);
		srcButton = (Button) view.findViewById(R.id.srcBtn);
		getRentalsBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parentActivity.onRentalsButtonClick();
			}

		});

		srcButton.setOnClickListener(new View.OnClickListener() {
			// TODO Auto-generated method stub
			public void onClick(View v) {
				// setup alert dialog that allows users to search the listView
				AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
				alert.setTitle("Search by Minimum Critic Rating");
				alert.setMessage("Input Minimum Rating ");
				// Set an EditText view to get user input 
				final EditText input = new EditText(mContext);
				input.setText(srcResult);
				alert.setView(input);
				alert.setPositiveButton("Search", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						srcResult = input.getText().toString();
						// check to see if get items has been clicked and the results are in the listView
						if (haveResults) {
							// check to see if its a number and between 0 and 100
							if (srcResult.matches("\\d+") && Integer.parseInt(srcResult) >= 0 && 
									Integer.parseInt(srcResult) <= 100) {
								adapter.getFilter().filter(srcResult);
								checkSrc = true;
							} else {
								Toast.makeText(mContext, "Please enter a number between 0 and 100", Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(mContext, "You must get rental results before searching.", Toast.LENGTH_LONG).show();
						}
					}
				});
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});
				// clear search query
				alert.setNeutralButton("Clear Search", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						adapter.getFilter().filter("0");
						checkSrc = false;
						srcResult = "";
					}
				});
				alert.show();
			}
		});
		mContext = getActivity();
		listV = (ListView) view.findViewById(R.id.listView1);
		listV.setOnItemClickListener(new OnItemClickListener() {

			Intent secondActivity = new Intent(mContext,InfoActivity.class);
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Log.i("listV click: ", "works!");
				//selected = position - 1;
				itemText = listV.getItemAtPosition(position).toString();

				try {
					// send specific movie to the info activity
					String fileString = file.readStrFile(mContext, FILE_NAME);
					JSONObject obj = new JSONObject(fileString);
					JSONArray movies = obj.getJSONArray("movies");
					for (int i = 0; i < movies.length(); i++) {
						castObj = movies.getJSONObject(i);
						objText = castObj.getString("title");
						if (itemText.contains(objText)) {
							//secondActivity.putExtra(MOVIE_KEY, castObj.toString());
							//startActivityForResult(secondActivity,0);
							InfoFragment fragment = (InfoFragment) getFragmentManager().findFragmentById(R.id.myinfo_fragment);
							fragment.myData = castObj.toString();
							fragment.DisplayMovie();
						}
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					Log.e("Error: ", e.getMessage().toString());
					startActivityForResult(secondActivity,1);
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("Error: ", e.getMessage().toString());
					startActivityForResult(secondActivity,1);
					e.printStackTrace();
				}

			}
		});
		return view;
	}

}
