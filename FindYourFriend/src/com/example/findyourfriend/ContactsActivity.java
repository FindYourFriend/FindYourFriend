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
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactsActivity extends Activity implements OnClickListener{

	
	
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
		List_For_Store_Contact_Info.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object o = List_For_Store_Contact_Info.getItemAtPosition(position);
				ItemDetails obj_itemDetails = (ItemDetails)o;
				Log.d("my", "You have chosen : " + " " + obj_itemDetails.getName());				
			}
			
		});
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
				item_details.setPhoneNumber(cursor.getString(2));
				item_details
						.setBitmap(getByteContactPhoto(cursor.getString(1)));

//TODO solve troubles with server 
//				if(new Inet().isRegisteredNumber(cursor.getString(2)))
//					item_details.setHavingApp(true);
//				else
//					item_details.setHavingApp(false);	
				
				if(!phone_numbers.contains(cursor.getString(2))){
					results.add(item_details);
					phone_numbers.add(cursor.getString(2));
				}
			}
		}
		Collections.sort(results, new CompName());
		return results;
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
    
    @Override
    public void onClick(View v) {
    	
    }
	
	
}