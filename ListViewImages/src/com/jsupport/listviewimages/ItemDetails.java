package com.jsupport.listviewimages;

import android.graphics.Bitmap;

public class ItemDetails {

	private String name;
	private String online;
	private String uri;
	private Bitmap bit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String isOnline() {
		return online;
	}

	public void setState(boolean isOnline) {
		if (isOnline)
			online = "Online";
		else
			online = "Offline";
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
