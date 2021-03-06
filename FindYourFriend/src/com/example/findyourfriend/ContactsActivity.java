package com.example.findyourfriend;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactsActivity extends Activity implements OnClickListener {

	private LinearLayout slidingPanel;
	private boolean isExpanded;
	private DisplayMetrics metrics;
	private ListView listView;
	private RelativeLayout headerPanel;
	private RelativeLayout menuPanel;
	private int panelWidth;
	private ImageView menuViewButton;
	Button menu1;
	Button menu2;
	Button menu3;
	Button menu4;
	TextView txtpays;
	String[] listeStrings = { "Tunisie", "Libye", "Egypte", "Yemen", "Syrie" };
	String[] listeStrings2 = { "Mauritanie", "Maroc", "Algerie",
			"Arabie saoudite", "jordanie", "Pays du golf" };
	FrameLayout.LayoutParams menuPanelParameters;
	FrameLayout.LayoutParams slidingPanelParameters;
	LinearLayout.LayoutParams headerPanelParameters;
	LinearLayout.LayoutParams listViewParameters;

	ArrayList<String> phone_numbers = new ArrayList<String>();

	ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);

		ArrayList<ItemDetails> image_details = GetSearchResults();

		final ListView List_For_Store_Contact_Info = (ListView) findViewById(R.id.listV_main);

		List_For_Store_Contact_Info.setAdapter(new ItemListBaseAdapter(this,
				image_details));
		List_For_Store_Contact_Info
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Object o = List_For_Store_Contact_Info
								.getItemAtPosition(position);
						ItemDetails obj_itemDetails = (ItemDetails) o;
						Log.d("my", "You have chosen : " + " "
								+ obj_itemDetails.getName());
						if (!obj_itemDetails.hasApp())
							sendSMS(obj_itemDetails.getPhoneNumber(),
									"Hi! Install this cool app to spy on me ;)");
					}

				});

		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		panelWidth = (int) ((metrics.widthPixels) * 0.75);

		headerPanel = (RelativeLayout) findViewById(R.id.header);
		headerPanelParameters = (LinearLayout.LayoutParams) headerPanel
				.getLayoutParams();
		headerPanelParameters.width = metrics.widthPixels;
		headerPanel.setLayoutParams(headerPanelParameters);

		menuPanel = (RelativeLayout) findViewById(R.id.menuPanel);
		menuPanelParameters = (FrameLayout.LayoutParams) menuPanel
				.getLayoutParams();
		menuPanelParameters.width = panelWidth;
		menuPanel.setLayoutParams(menuPanelParameters);

		slidingPanel = (LinearLayout) findViewById(R.id.slidingPanel);
		slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel
				.getLayoutParams();
		slidingPanelParameters.width = metrics.widthPixels;
		slidingPanel.setLayoutParams(slidingPanelParameters);

		listView = (ListView) findViewById(R.id.listView1);
		listViewParameters = (LinearLayout.LayoutParams) listView
				.getLayoutParams();
		listViewParameters.width = metrics.widthPixels;
		listView.setLayoutParams(listViewParameters);

		// Slide the Panel

		menu1 = (Button) findViewById(R.id.menu_item_1);

		menu2 = (Button) findViewById(R.id.menu_item_2);
		menu1.setOnClickListener(this);
		menu2.setOnClickListener(this);
		menu3 = (Button) findViewById(R.id.menu_item_3);
		menu3.setOnClickListener(this);
		menu4 = (Button) findViewById(R.id.menu_item_4);
		menu4.setOnClickListener(this);

		menuViewButton = (ImageView) findViewById(R.id.menuViewButton);

		menuViewButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!isExpanded) {
					isExpanded = true;

					// Expand
					new ExpandAnimation(slidingPanel, panelWidth,
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);
				} else {
					isExpanded = false;

					// Collapse
					new CollapseAnimation(slidingPanel, panelWidth,
							TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
							TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f,
							0, 0.0f);

				}
			}
		});
	}

	@SuppressWarnings({ "deprecation", "finally" })
	private ArrayList<ItemDetails> GetSearchResults() {
		ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();

		Cursor cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { Phone.DISPLAY_NAME, Phone.CONTACT_ID,
						Phone.NUMBER }, null, null, null);
		startManagingCursor(cursor);

		ItemDetails item_details;
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String userPhone = telephonyManager.getLine1Number();
		Inet inet = new Inet();
		try {
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					if (cursor.getString(2).length() > 9
							&& !(inet.ifFriend(userPhone, cursor.getString(2)))) {

						String phone;
						if (cursor.getString(2).length() > 10)
							phone = editPhoneNumber(cursor.getString(2));
						else
							phone = cursor.getString(2);
						item_details = new ItemDetails();
						item_details.setName(cursor.getString(0));
						item_details.setPhoneNumber(phone);
						item_details.setBitmap(getByteContactPhoto(cursor
								.getString(1)));

						if (inet.isRegisteredNumber(phone))
							item_details.setHavingApp(true);
						else
							item_details.setHavingApp(false);

						if (!phone_numbers.contains(phone)) {
							results.add(item_details);
							phone_numbers.add(phone);
						}
					}
				}
			}
		} catch (NullPointerException ex) {
		} finally {
			Collections.sort(results, new CompName());
			return results;
		}

	}

	private String editPhoneNumber(String number) {
		int start = number.length() - 10;
		return number.substring(start);
	}

	private Bitmap getByteContactPhoto(String contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				Long.parseLong(contactId));
		Uri photoUri = Uri.withAppendedPath(contactUri,
				Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = getContentResolver().query(photoUri,
				new String[] { Contacts.Photo.DATA15 }, null, null, null);
		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					return BitmapFactory.decodeStream(new ByteArrayInputStream(
							data));
				}
			}
		} finally {
			cursor.close();
		}

		return null;
	}

	public static class CompName implements Comparator<ItemDetails> {
		@Override
		public int compare(ItemDetails arg0, ItemDetails arg1) {
			return arg0.getName().compareTo(arg1.getName());

		}
	}

	@SuppressWarnings({ "unused", "deprecation" })
	private void sendSMS(String phoneNumber, String message) {
		PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, pi, null);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.menu_item_1:
			intent = new Intent(this, ContactsActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_item_2:
			intent = new Intent(this, FriendsActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_item_3:
			intent = new Intent(this, LocationActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_item_4:
			intent = new Intent(this, NotificationsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
