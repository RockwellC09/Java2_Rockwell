package com.ChristopherRockwell.topdvdrentals;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Typeface;

public class InfoActivity extends Activity {

	TextView titleView;
	TextView infoView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_activity);
		
		titleView = (TextView) findViewById(R.id.titleView);
		
		// custom typefaces 
        Typeface customFont = Typeface.createFromAsset(this.getAssets(), "Exo2-Bold.ttf");
        Typeface customFont2 = Typeface.createFromAsset(this.getAssets(), "Exo2-Medium.ttf");
        
        titleView.setTypeface(customFont);
        infoView.setTypeface(customFont2);
	}

}
