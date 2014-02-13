/*
 * Project: TopDVDRentals
 * Package: com.ChristopherRockwell.topdvdrentals
 * @author	Christopher Rockwell
 * Date: 	Feb 4, 2014
 */
package com.ChristopherRockwell.topdvdrentals;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.image.SmartImageView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity implements OnClickListener {
	Button getRentalsBtn;
	Button srcButton;
	String response = null;
	FileManagerSingleton file;
	Context mContext;
	public static final String FILE_NAME = "TopRental.txt";
	public static final String MOVIE_KEY = "movie";
	public static final String LIST_KEY = "myList";
	boolean writeWorks;
	static ListView listV;
	public static SmartImageView smrtImg;
	private List<Movie> mList;
	public static Typeface customFont2;
	File mfile;
	Intent secondActivity;
	int selected;
	MoviesArrayAdapter adapter;
	EditText srcText;
	boolean checkSrc = false;
	
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getRentalsBtn = (Button) this.findViewById(R.id.emailBtn);
        srcButton = (Button) this.findViewById(R.id.posterBtn);
        srcText = (EditText) this.findViewById(R.id.editText1);
        getRentalsBtn.setOnClickListener(this);
        mContext = this;
        mfile = mContext.getFileStreamPath(FILE_NAME);
        
        // custom typefaces 
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), "Exo2-Bold.ttf");
        customFont2 = Typeface.createFromAsset(this.getAssets(), "Exo2-Medium.ttf");
        
        getRentalsBtn.setTypeface(customFont);
        srcButton.setTypeface(customFont);

        listV = (ListView) this.findViewById(R.id.listView1);
        View listHeader = this.getLayoutInflater().inflate(R.layout.list_header, null);
        
     // reference smart ImaveView to be used when setting the movie poster image
     	smrtImg = (SmartImageView) findViewById(R.id.img);
        
        // set custom text for the list headers
        TextView tv1 = (TextView) listHeader.findViewById(R.id.title_header);
        TextView tv3 = (TextView) listHeader.findViewById(R.id.rating1_header);
        TextView tv4 = (TextView) listHeader.findViewById(R.id.rating2_header);
        tv1.setTypeface(customFont);
        tv3.setTypeface(customFont);
        tv4.setTypeface(customFont);
        
        listV.addHeaderView(listHeader);
        
        file = FileManagerSingleton.getInstance();
        
     // check for savedInstance and populate list
     		if (savedInstanceState != null) {
     			mList = (List<Movie>) savedInstanceState.getSerializable(LIST_KEY);
     			
     			if (mList != null) {
     				Log.i("List: ", "Not null");
     				adapter = new MoviesArrayAdapter(MainActivity.this, R.layout.list_row, mList);

     				listV.setAdapter(adapter);
     				// retain the search data in the list view
     				if (savedInstanceState.getBoolean("bool")) {
     					adapter.getFilter().filter(savedInstanceState.getString("eText").toString());
     					checkSrc = true;
     				}
     				
     			} else {
     				Log.i("List: ", "null");
     			}
     		}
        
        // create intent and list view click listener in preparation of moving to the second view
        secondActivity = new Intent(mContext,InfoActivity.class);
        
        listV.setOnItemClickListener(new OnItemClickListener() {
        	
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        		selected = position - 1;
        		
        		JSONObject castObj;
				try {
					// send specific movie to the info activity
					String fileString = file.readStrFile(mContext, FILE_NAME);
					JSONObject obj = new JSONObject(fileString);
					JSONArray movies = obj.getJSONArray("movies");
					castObj = movies.getJSONObject(selected);
					secondActivity.putExtra(MOVIE_KEY, castObj.toString());
					startActivityForResult(secondActivity,0);
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
      //enables filtering for the contents of the given ListView
        listV.setTextFilterEnabled(true);
        srcButton.setOnClickListener(new OnClickListener() {
        	// TODO Auto-generated method stub
			@Override
			public void onClick(View v) {
				// check to see if its a number and between 1 and 99
				if (srcText.getText().toString().matches("\\d+") && Integer.parseInt(srcText.getText().toString()) >= 0 && 
						Integer.parseInt(srcText.getText().toString()) <= 100) {
					adapter.getFilter().filter(srcText.getText().toString());
					checkSrc = true;
				} else {
					Toast.makeText(mContext, "Please enter a number between 0 and 100", Toast.LENGTH_SHORT).show();
				}
			}
		});
    }

	@Override
	public void onClick(View arg0) {
		
		// check to see if there's a valid connection
		if (file.connectionStatus(mContext)){
			// TODO Auto-generated method stub
			Handler getRentalsHandler = new Handler(new Handler.Callback() {

				@Override
				public boolean handleMessage(Message msg) {
					// TODO Auto-generated method stub
					
					
					if (msg.arg1 == RESULT_OK && msg.obj != null) {
						try {
							response = (String)msg.obj;
							TopRentalsService.writeStrFile(mContext, FILE_NAME, response);
							writeWorks = true;
							Toast.makeText(mContext, "Wrote data to file", Toast.LENGTH_SHORT).show();
							
						} catch (Exception e) {
							writeWorks = false;
							// TODO Auto-generated catch block
							Log.e("Error: ", e.getMessage().toString());
							Toast.makeText(mContext, "Error: Couldn't write data to file", Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}
					
					// if the write data works, read the the data
					if (writeWorks) {
						String fileString = file.readStrFile(mContext, FILE_NAME);
						Toast.makeText(mContext, "Read data from file", Toast.LENGTH_SHORT).show();
						String result;
						String title, img, criticScore, audienceScore;
						ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
						mList = new ArrayList<Movie>();

						try {
							// set JSONOject and cast into array the back into an object to get movie proper info
							JSONObject obj = new JSONObject(fileString);
							JSONArray movies = obj.getJSONArray("movies");
							
							for (int i = 0; i < movies.length(); i++) {
								JSONObject castObj = movies.getJSONObject(i);
								JSONObject antrCastObj = castObj.getJSONObject("ratings");
								JSONObject imgCast = castObj.getJSONObject("posters");
								
								title = castObj.getString("title");
								img = imgCast.getString("profile");
								criticScore = antrCastObj.getString("critics_score");
								audienceScore = antrCastObj.getString("audience_score");

								result = "Title: " + title + "\r\n"
										+ "Year Released: " + img + "\r\n"
										+ "Critic Score: " + criticScore + "%\r\n"
										+ "Audience Score: " + audienceScore + "%\r\n";
								Log.i("Result", result);
								Movie movie = new Movie(title, img, criticScore, audienceScore);
								mList.add(movie);
								HashMap<String, String> displayMap = new HashMap<String, String>();
								displayMap.put("title", title);
								displayMap.put("critic", criticScore);
								displayMap.put("audience", audienceScore);
								
								list.add(displayMap);
							}
							
//							SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, list, R.layout.list_row, 
//									new String[] {"title", "critic", "audience"}, 
//									new int[] {R.id.title, R.id.rating1, R.id.rating2});
							
							adapter = new MoviesArrayAdapter(MainActivity.this, R.layout.list_row, mList);

							listV.setAdapter(adapter);
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.e("Error: ", e.getMessage().toString());
							Toast.makeText(mContext, "Error: Couldn't retrieve the data", Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}
					return true;
				}
	    	});
	    	Messenger rentalsMessenger = new Messenger(getRentalsHandler);
	        Intent startRentalsIntent = new Intent(this, TopRentalsService.class);
	        startRentalsIntent.putExtra(TopRentalsService.MSGR_KEY, rentalsMessenger);
	        startRentalsIntent.putExtra(TopRentalsService.URL_STR, "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/top_rentals.json?apikey=bf72tfc2zjfbdscenpwx2e2r");
	        
	        startService(startRentalsIntent);
	        // If the user doesn't have a connection, but has the txt file then the data will still output
		} else if (mfile.exists() && file.connectionStatus(mContext) == false) {
			String fileString = file.readStrFile(mContext, FILE_NAME);
			Toast.makeText(mContext, "Read data from file", Toast.LENGTH_SHORT).show();
			String result;
			String title, img, criticScore, audienceScore;
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			mList = new ArrayList<Movie>();

			try {
				// set JSONOject and cast into array the back into an object to get movie proper info
				JSONObject obj = new JSONObject(fileString);
				JSONArray movies = obj.getJSONArray("movies");
				
				for (int i = 0; i < movies.length(); i++) {
					JSONObject castObj = movies.getJSONObject(i);
					JSONObject antrCastObj = castObj.getJSONObject("ratings");
					JSONObject imgCast = castObj.getJSONObject("posters");
					
					title = castObj.getString("title");
					img = imgCast.getString("profile");
					criticScore = antrCastObj.getString("critics_score");
					audienceScore = antrCastObj.getString("audience_score");

					result = "Title: " + title + "\r\n"
							+ "Year Released: " + img + "\r\n"
							+ "Critic Score: " + criticScore + "%\r\n"
							+ "Audience Score: " + audienceScore + "%\r\n";
					Log.i("Result", result);
					Movie movie = new Movie(title, img, criticScore, audienceScore);
					mList.add(movie);
					HashMap<String, String> displayMap = new HashMap<String, String>();
					displayMap.put("title", title);
					//displayMap.put("img", img);
					displayMap.put("critic", criticScore);
					displayMap.put("audience", audienceScore);
					
					//smrtImg.setImageUrl(img);
					
					list.add(displayMap);
				}
				
				adapter = new MoviesArrayAdapter(MainActivity.this, R.layout.list_row, mList);

				listV.setAdapter(adapter);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e("Error: ", e.getMessage().toString());
				Toast.makeText(mContext, "Error: Couldn't retrieve the data", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			// Check for no connection
		} else {
			// create alert dialog for users without a valid internet connection
			AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
			builder1.setTitle("No Connection");
			builder1.setMessage("You don't have a valid internet connection.");
			builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Check your connection and try again.", Toast.LENGTH_LONG).show();
				}
			});

			// show alert
			builder1.show();
		}
		
	}  
	public List<Movie> getList() {
		return this.mList;
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		if (mList != null && !mList.isEmpty()) {
			savedInstanceState.putSerializable(LIST_KEY, (Serializable) mList);
			savedInstanceState.putBoolean("bool", checkSrc);
			savedInstanceState.putString("eText", srcText.getText().toString());
			Log.i("Saved: ", "Instance data saved!");
		}

		super.onSaveInstanceState(savedInstanceState);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);
	}
}
