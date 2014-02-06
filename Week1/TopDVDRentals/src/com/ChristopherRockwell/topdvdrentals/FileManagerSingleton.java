/*
 *@author	Christopher Rockwell 
 *Description: This class will store and read my JSON data collected from the rotten tomatoes API
 */

package com.ChristopherRockwell.topdvdrentals;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class FileManagerSingleton {

	private static FileManagerSingleton mngr_instance;
	
	//constructor 
	private FileManagerSingleton() {
		
	}
	
	public static FileManagerSingleton getInstance() {
		if (mngr_instance == null) {
			mngr_instance = new FileManagerSingleton();
		}
		return mngr_instance;
	}
		
	// This method writes my JSON data to a file
	public boolean writeStrFile (Context context, String fileName, String content) {
		boolean result = false;
		
		FileOutputStream outStream = null;
		try {
			outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			outStream.write(content.getBytes());
			Log.i("Wrote string file", "successfully");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e("Error: ", e.getMessage().toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("Error:", e.getMessage().toString());
		}
		
		return result;
		
	}
	
	// This method reads my JSON data from the file I stored it in
	public String readStrFile (Context context, String filename) {
		String content = null;
		
		FileInputStream inStream = null;
		
		StringBuffer contentBuffer;
		try {
			inStream = context.openFileInput(filename);
			BufferedInputStream buffIn = new BufferedInputStream(inStream);
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			contentBuffer = new StringBuffer();
			while((bytesRead = buffIn.read(contentBytes)) != -1) {
				content = new String(contentBytes, 0, bytesRead);
				contentBuffer.append(content);
			}
			content = contentBuffer.toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("Error: ", e.getMessage().toString());
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e("Error: ", e.getMessage().toString());
				e.printStackTrace();
			}
		}
		
		return content;
		
	}
	
	// check to see if user have a valid internet connection
		public boolean connectionStatus(Context context) {
			boolean isConnected = false;
			ConnectivityManager ConnectMngr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = ConnectMngr.getActiveNetworkInfo();
			if (netInfo != null) {
				if (netInfo.isConnected()) {
					isConnected = true;
				}
			}

			return isConnected;
		}
	
}
