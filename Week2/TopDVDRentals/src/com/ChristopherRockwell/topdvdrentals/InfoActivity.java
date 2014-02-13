package com.ChristopherRockwell.topdvdrentals;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;

public class InfoActivity extends Activity {

	TextView titleView;
	TextView infoView;
	Button emailButton;
	Button posterButton;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_activity);
		context = this;
		
		titleView = (TextView) this.findViewById(R.id.titleView);
		infoView = (TextView) this.findViewById(R.id.infoView);
		emailButton = (Button) this.findViewById(R.id.emailBtn);
		posterButton = (Button) this.findViewById(R.id.posterBtn);
		
		// custom typefaces 
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), "Exo2-Bold.ttf");
        Typeface customFont2 = Typeface.createFromAsset(this.getAssets(), "Exo2-Medium.ttf");
        
        titleView.setTypeface(customFont);
        infoView.setTypeface(customFont2);
        emailButton.setTypeface(customFont);
        posterButton.setTypeface(customFont);
        
        // get intent data set in MainActivity
        Bundle data = getIntent().getExtras();
        String myData = data.getString(MainActivity.MOVIE_KEY);
	    Log.i("Movie Result: ", myData);
	    
	    JSONObject obj;
		try {
			obj = new JSONObject(myData);
			String title = obj.getString("title");
			String synopsis = obj.getString("synopsis");
			
			titleView.setText(title);
			infoView.setText("Movie Info: " + synopsis);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("Error: ", e.getMessage().toString());
			e.printStackTrace();
		} 
	}
}
