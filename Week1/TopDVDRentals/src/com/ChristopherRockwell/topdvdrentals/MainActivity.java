/*
 * Project: TopDVDRentals
 * Package: com.ChristopherRockwell.topdvdrentals
 * @author	Christopher Rockwell
 * Date: 	Feb 4, 2014
 */
package com.ChristopherRockwell.topdvdrentals;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity implements OnClickListener {
	Button getRentalsBtn;
	String response = null;
	private static final String TAG = "MyActivity";
	
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getRentalsBtn = (Button) findViewById(R.id.button1);
        getRentalsBtn.setOnClickListener(this);
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
						Log.i(TAG, response.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.e("Error: ", e.getMessage().toString());
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
