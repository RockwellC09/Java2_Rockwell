/*
 * Project: TopDVDRentals
 * Package: com.ChristopherRockwell.topdvdrentals
 * @author	Christopher Rockwell
 * Date: 	Feb 4, 2014
 */
package com.ChristopherRockwell.topdvdrentals;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity implements OnClickListener {
	Button getRentalsBtn;
	String response = null;
	FileManagerSingleton file;
	Context mContext;
	String fileName = "TopRental.txt";
	boolean writeWorks;
	ListView listV;
	
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getRentalsBtn = (Button) this.findViewById(R.id.button1);
        getRentalsBtn.setOnClickListener(this);
        mContext = this;
        
        listV = (ListView) this.findViewById(R.id.listView1);
        View listHeader = this.getLayoutInflater().inflate(R.layout.list_header, null);
        listV.addHeaderView(listHeader);
        
        file = FileManagerSingleton.getInstance();
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Handler getRentalsHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				
				if (msg.arg1 == RESULT_OK && msg.obj != null) {
					try {
						response = (String)msg.obj;
						file.writeStrFile(mContext, fileName, response);
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
					String fileString = file.readStrFile(mContext, fileName);
					Toast.makeText(mContext, "Read data from file", Toast.LENGTH_SHORT).show();
					String result;
					String title, year, criticScore, audienceScore;
					ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

					try {
						// set JSONOject and cast into array the back into an object to get movie proper info
						JSONObject obj = new JSONObject(fileString);
						JSONArray movies = obj.getJSONArray("movies");
						
						for (int i = 0; i < movies.length(); i++) {
							JSONObject castObj = movies.getJSONObject(i);
							JSONObject antrCastObj = castObj.getJSONObject("ratings");
							
							title = castObj.getString("title");
							year = castObj.getString("year");
							criticScore = antrCastObj.getString("critics_score");
							audienceScore = antrCastObj.getString("audience_score");

							result = "Title: " + title + "\r\n"
									+ "Year Released: " + year + "\r\n"
									+ "Critic Score: " + criticScore + "%\r\n"
									+ "Audience Score: " + audienceScore + "%\r\n";
							Log.i("Result", result);
							HashMap<String, String> displayMap = new HashMap<String, String>();
							displayMap.put("title", title);
							displayMap.put("year", year);
							displayMap.put("critic", criticScore);
							displayMap.put("audience", audienceScore);
							
							list.add(displayMap);
						}
						
						SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, list, R.layout.list_row, 
								new String[] {"title", "year", "critic", "audience"}, 
								new int[] {R.id.title, R.id.year, R.id.rating1, R.id.rating2});

						listV.setAdapter(adapter);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.e("Error: ", e.getMessage().toString());
						Toast.makeText(mContext, "Error: Couldn't retrieve the data", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
			}
    	};
    	Messenger rentalsMessenger = new Messenger(getRentalsHandler);
        Intent startRentalsIntent = new Intent(this, TopRentalsService.class);
        startRentalsIntent.putExtra(TopRentalsService.MSGR_KEY, rentalsMessenger);
        startRentalsIntent.putExtra(TopRentalsService.URL_STR, "http://api.rottentomatoes.com/api/public/v1.0/lists/dvds/top_rentals.json?apikey=bf72tfc2zjfbdscenpwx2e2r");
        
        startService(startRentalsIntent);
	}   
}
