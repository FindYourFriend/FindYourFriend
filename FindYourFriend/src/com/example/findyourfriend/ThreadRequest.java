package com.example.findyourfriend;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ThreadRequest extends Thread {
	private String URL;
	private String text;
	
	public void setURL(String url){ 
		this.URL = url;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public void run() {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(URL);

		try {
		     HttpResponse response = httpclient.execute(httpget);
		     HttpEntity httpEntity =response.getEntity();
		     String line = EntityUtils.toString(httpEntity, "UTF-8");
		     text = line;
		}
		 catch (ClientProtocolException e) {
		} 
		catch (IOException e) {
		// TODO Auto-generated catch block
		}
		
		synchronized (this) {
            notifyAll();
        }
	}
}
