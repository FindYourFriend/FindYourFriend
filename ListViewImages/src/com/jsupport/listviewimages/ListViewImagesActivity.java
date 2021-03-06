package com.jsupport.listviewimages;

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
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.widget.ListView;

public class ListViewImagesActivity extends Activity {

	ArrayList<String> phone_numbers = new ArrayList<String>();

	ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ArrayList<ItemDetails> image_details = GetSearchResults();

		final ListView List_For_Store_Contact_Info = (ListView) findViewById(R.id.listV_main);

		List_For_Store_Contact_Info.setAdapter(new ItemListBaseAdapter(this,
				image_details));

	}

	@SuppressWarnings("deprecation")
	private ArrayList<ItemDetails> GetSearchResults() {
		ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();

		Cursor cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { Phone.DISPLAY_NAME, Phone.CONTACT_ID,
						Phone.NUMBER }, null, null, null);
		startManagingCursor(cursor);

		ItemDetails item_details;

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				item_details = new ItemDetails();
				item_details.setName(cursor.getString(0));
				item_details
						.setBitmap(getByteContactPhoto(cursor.getString(1)));
				if(!phone_numbers.contains(cursor.getString(2))){
					results.add(item_details);
					phone_numbers.add(cursor.getString(2));
				}
//				if (contains(cursor.getString(2)) >= 0) {
//					if (toAddOrNotToAdd(results.get(i), item_details)) {
//						phone_numbers.remove(i);
//						phone_numbers.add(cursor.getString(2));
//						results.remove(i);
//						results.add(item_details);
//					}
//				} else{
//					results.add(item_details);
//					phone_numbers.add(cursor.getString(2));
//				}
					// phone_numbers.
					// results.remove(i);
			}
		}
		Collections.sort(results, new CompName());
		return results;
	}

	private int contains(String str) {
		if(phone_numbers.equals(null))
			return -1;
		for (int i = 0; i < phone_numbers.size(); i++) {
			if (phone_numbers.get(i).equals(str))
				return i;
		}
		return -1;
	}

	private boolean replaceContact(ArrayList<ItemDetails> results,
			ItemDetails itemDetails) {
		for (ItemDetails iDetails : results)
			if (iDetails.getName().equals(itemDetails.getName()))
				if (toAddOrNotToAdd(iDetails, itemDetails))
					return true;
		return false;
	}

	private boolean toAddOrNotToAdd(ItemDetails item1, ItemDetails item2) {
		if (item1.getBitmap() == null && item2.getBitmap() != null)
			return true;
		return false;
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