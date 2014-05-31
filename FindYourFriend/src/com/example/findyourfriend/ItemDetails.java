package com.example.findyourfriend;

import android.graphics.Bitmap;

public class ItemDetails {

	private String name;
	private boolean hasApp;
	private String phoneNumber;
	private String uri;
	private Bitmap bit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean hasApp() {
		return hasApp;
	}

	public void setHavingApp(boolean hasApp) {
			this.hasApp = hasApp;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	
	public String getImageNumber() {
		return uri;
	}

	public void setImageNumber(String imageURI) {
		uri = imageURI;
	}

	public Bitmap getBitmap() {
		return bit;
	}

	public void setBitmap(Bitmap bitmap) {
		bit = bitmap;
	}

}
