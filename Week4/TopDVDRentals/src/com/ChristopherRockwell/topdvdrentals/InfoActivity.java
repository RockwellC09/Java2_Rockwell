package com.ChristopherRockwell.topdvdrentals;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends Activity {

	static TextView titleView;
	static TextView infoView;
	static Button textButton;
	static Button posterButton;
	Context context;
	public String title;
	public String posterURL;
	Bundle data;
	String myData;
	JSONObject obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.info_fragment);
		context = this;
		
		//Log.i("Color: ", MainActivity.eColor);

		titleView = (TextView) this.findViewById(R.id.titleView);
		infoView = (TextView) this.findViewById(R.id.infoView);
		textButton = (Button) this.findViewById(R.id.textButton);
		posterButton = (Button) this.findViewById(R.id.posterBtn);
		infoView.setMovementMethod(new ScrollingMovementMethod());
		
		
		// check for preference color
		if (MainActivity.eColor == "default" || MainActivity.eColor == null) { 
			titleView.setTextColor(getResources().getColor(R.color.btn_color));
			infoView.setTextColor(getResources().getColor(R.color.btn_color));
			textButton.setTextColor(getResources().getColor(R.color.btn_color));
			posterButton.setTextColor(getResources().getColor(R.color.btn_color));
		} else if (MainActivity.eColor == "purple") {
			titleView.setTextColor(getResources().getColor(R.color.btn_color2));
			infoView.setTextColor(getResources().getColor(R.color.btn_color2));
			textButton.setTextColor(getResources().getColor(R.color.btn_color2));
			posterButton.setTextColor(getResources().getColor(R.color.btn_color2));
		} else if (MainActivity.eColor == "blue") {
			titleView.setTextColor(getResources().getColor(R.color.btn_color3));
			infoView.setTextColor(getResources().getColor(R.color.btn_color3));
			textButton.setTextColor(getResources().getColor(R.color.btn_color3));
			posterButton.setTextColor(getResources().getColor(R.color.btn_color3));
		}

		// set button widths equal
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		textButton.setWidth(size.x/2);
		posterButton.setWidth(size.x/2);

		// custom typefaces 
		Typeface customFont = Typeface.createFromAsset(this.getAssets(), "Exo2-Bold.ttf");
		Typeface customFont2 = Typeface.createFromAsset(this.getAssets(), "Exo2-Medium.ttf");

		// set custom fonts
		titleView.setTypeface(customFont);
		infoView.setTypeface(customFont2);
		textButton.setTypeface(customFont);
		posterButton.setTypeface(customFont);

		if (savedInstanceState != null) {
			// get intent data from savedInstance
			data = savedInstanceState.getParcelable("bundleData");
		} else {
			// get intent data set in MainActivity
			data = getIntent().getExtras();
		}
		myData = data.getString(MainActivity.MOVIE_KEY);
		Log.i("Movie Result: ", myData);

		// parse JSON data
		try {
			obj = new JSONObject(myData);
			JSONObject imgCast = obj.getJSONObject("posters");
			title = obj.getString("title");
			posterURL = imgCast.getString("original");
			String synopsis = obj.getString("synopsis");

			titleView.setText(title);
			infoView.setText("Movie Info: " + synopsis);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("Error: ", e.getMessage().toString());
			e.printStackTrace();
		} 

		// text message intent
		textButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, you should checkout the movie " + title);
				sendIntent.setType("text/plain");
				try {
					startActivity(sendIntent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("Error: ", e.getMessage().toString());
					e.printStackTrace();
				}
			}
		});

		// URL intent
		posterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent urlIntent = new Intent(Intent.ACTION_VIEW);
				urlIntent.setData(Uri.parse(posterURL));
				startActivity(urlIntent);
			}
		});

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Intent firstActivity = new Intent(getApplicationContext(),MainActivity.class);
			startActivityForResult(firstActivity,0);
		} else {
			Toast.makeText(this, "Second Activity", Toast.LENGTH_LONG).show();
		}

	}

	// pass movie title back to MainActivity
	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("srcMovie", title);
		MainActivity.dataString = obj.toString();
		Log.i("Ran", "Finish");
		setResult(RESULT_OK, data);
		super.finish();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// saved bundle data
		savedInstanceState.putParcelable("bundleData", data);
		Log.i("Saved: ", "Instance data saved!");
		finish();
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);
		Log.i("Restored: ", "Instance data restored!");
	}
}
