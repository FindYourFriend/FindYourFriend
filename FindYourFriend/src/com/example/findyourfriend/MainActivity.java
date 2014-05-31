package com.example.findyourfriend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	Button btnFriendsActivity;
	Button btnFriends;
	Button btnLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnFriendsActivity=(Button)findViewById(R.id.btnFriendsActivity);
		btnFriendsActivity.setOnClickListener(this);
		btnFriends = (Button)findViewById(R.id.btnFriends);
		btnFriends.setOnClickListener(this);
		btnLocation = (Button)findViewById(R.id.btnLocation);
		btnLocation.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()){
		case R.id.btnFriendsActivity:
			intent=new Intent(this, ContactsActivity.class);
			startActivity(intent);
			break;
		case R.id.btnFriends:
			intent = new Intent(this,FriendsActivity.class);
			startActivity(intent);
			break;
		case R.id.btnLocation:
			intent = new Intent(this,LocationActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}

}
