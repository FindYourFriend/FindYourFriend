package com.example.findyourfriend;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TouchListener implements OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.d("my", v.toString() );
		return false;
	}

}
