package com.example.findyourfriend;

import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
  
  final String LOG_TAG = "myLogs";

  public void onCreate() {
    super.onCreate();
    Log.d(LOG_TAG, "onCreate");
  }
  
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(LOG_TAG, "onStartCommand");
    someTask();
    return super.onStartCommand(intent, flags, startId);
  }

  public void onDestroy() {
    super.onDestroy();
    Log.d(LOG_TAG, "onDestroy");
  }

  public IBinder onBind(Intent intent) {
    Log.d(LOG_TAG, "onBind");
    return null;
  }
  
  void someTask() {
	    new Thread(new Runnable() {
	      public void run() {
	    	  while(true){
	    		  try {
	    			  new Inet().sendLocation(LocationActivity.getLatLng().latitude+" "+LocationActivity.getLatLng().longitude);
	  	            TimeUnit.SECONDS.sleep(1000*60*30);
	  	          } catch (InterruptedException e) {
	  	            e.printStackTrace();  
	  		        stopSelf();
	  	          }
	    	  }	    	
	      }
	    }).start();
	  }
}
