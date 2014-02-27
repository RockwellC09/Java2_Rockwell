/*
 *@author	Christopher Rockwell 
 *Description: This class is the fragment class launched when the application is in landscape mode
 */

package com.ChristopherRockwell.topdvdrentals;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class InfoFragment extends Fragment {
	static TextView titleView;
	static TextView infoView;
	static Button textButton;
	static Button posterButton;
	Context context;
	public String title;
	public String posterURL;
	Bundle data;
	public String myData;
	JSONObject obj;

	public interface infoInterface {

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.info_activity, container);
		context = getActivity();

		titleView = (TextView) view.findViewById(R.id.titleView);
		infoView = (TextView) view.findViewById(R.id.infoView);
		textButton = (Button) view.findViewById(R.id.textButton);
		posterButton = (Button) view.findViewById(R.id.posterBtn);
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


		// custom typefaces 
		Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "Exo2-Bold.ttf");
		Typeface customFont2 = Typeface.createFromAsset(getActivity().getAssets(), "Exo2-Medium.ttf");

		// set custom fonts
		titleView.setTypeface(customFont);
		infoView.setTypeface(customFont2);
		textButton.setTypeface(customFont);
		posterButton.setTypeface(customFont);

		myData = "";
		Log.i("Movie Result: ", myData);

		return view;
	}

	public void DisplayMovie() {
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
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (obj != null) {
			MainActivity.dataString = obj.toString();
		}
	}

}
