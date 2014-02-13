package com.ChristopherRockwell.topdvdrentals;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

public class InfoActivity extends Activity {

	TextView titleView;
	TextView infoView;
	Button textButton;
	Button posterButton;
	Context context;
	public String title;
	public String posterURL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_activity);
		context = this;
		
		titleView = (TextView) this.findViewById(R.id.titleView);
		infoView = (TextView) this.findViewById(R.id.infoView);
		textButton = (Button) this.findViewById(R.id.emailBtn);
		posterButton = (Button) this.findViewById(R.id.posterBtn);
		
		// custom typefaces 
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), "Exo2-Bold.ttf");
        Typeface customFont2 = Typeface.createFromAsset(this.getAssets(), "Exo2-Medium.ttf");
        
        titleView.setTypeface(customFont);
        infoView.setTypeface(customFont2);
        textButton.setTypeface(customFont);
        posterButton.setTypeface(customFont);
        
        // get intent data set in MainActivity
        Bundle data = getIntent().getExtras();
        String myData = data.getString(MainActivity.MOVIE_KEY);
	    Log.i("Movie Result: ", myData);
	    
	    Toast.makeText(this, "Second Activity", Toast.LENGTH_LONG).show();
	    
	    JSONObject obj;
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
	}
	
	@Override
	public void finish() {
	    Intent data = new Intent();
	    data.putExtra("srcMovie", title);
	    setResult(RESULT_OK, data);
	    super.finish();
	}
}
