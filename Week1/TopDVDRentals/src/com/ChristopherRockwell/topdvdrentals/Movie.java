/*
 *@author	Christopher Rockwell 
 *Description: This class is a constructor for the movie information.
 */

package com.ChristopherRockwell.topdvdrentals;

// create movie constructor for storing the movie info into an array
public class Movie {

	public String name;
	public String image;
	public String critic;
	public String audience;

	public Movie() {
		// TODO Auto-generated constructor stub
	}

	public Movie(String name, String myImg, String rate1,
			String rate2) {
		this.name = name;
		this.image = myImg;
		this.critic = rate1;
		this.audience = rate2;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
