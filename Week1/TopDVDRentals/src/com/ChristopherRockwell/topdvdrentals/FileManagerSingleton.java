/*
 *@author	Christopher Rockwell 
 *Description: This class will store and read my JSON data collected from the rotten tomatoes API
 */

package com.ChristopherRockwell.topdvdrentals;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
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
}
