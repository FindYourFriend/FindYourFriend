package com.example.findyourfriend;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsActivity extends Activity {

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
		TextView tv = (TextView) this.findViewById(R.id.yourfriends);
		tv.setText("Your friends");
	}

	@SuppressWarnings({ "deprecation", "finally" })
	private ArrayList<ItemDetails> GetSearchResults() {
		ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();

		Cursor cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { Phone.DISPLAY_NAME, Phone.CONTACT_ID,
						Phone.NUMBER }, null, null, null);
		startManagingCursor(cursor);

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String userPhone = telephonyManager.getLine1Number();

		ItemDetails item_details;
		try {
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					if (new Inet().ifFriend(userPhone, cursor.getString(2))) {
						String phone = editPhoneNumber(cursor.getString(2));
						item_details = new ItemDetails();
						item_details.setName(cursor.getString(0));
						item_details.setPhoneNumber(phone);
						item_details.setBitmap(getByteContactPhoto(cursor
								.getString(1)));
						if (!phone_numbers.contains(phone)) {
							results.add(item_details);
							phone_numbers.add(phone);
						}
					}
				}
			}
		} catch (NullPointerException ex) {
		} catch (RuntimeException ex) {
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
}