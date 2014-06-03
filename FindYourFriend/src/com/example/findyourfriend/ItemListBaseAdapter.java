package com.example.findyourfriend;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListBaseAdapter extends BaseAdapter {
	static class ViewHolder {
		TextView txt_Name;
		TextView txt_PhoneNumber;
		ImageView contactPhoto;
		ImageView addFriendImageOrIcon;
	}

	private static ArrayList<ItemDetails> itemDetailsArrayList;

	private LayoutInflater l_Inflater;

	public ItemListBaseAdapter(Context context, ArrayList<ItemDetails> results) {
		itemDetailsArrayList = results;
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return itemDetailsArrayList.size();
	}

	public Object getItem(int position) {
		return itemDetailsArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.item_details_view, null);
			holder = new ViewHolder();
			holder.txt_Name = (TextView) convertView
					.findViewById(R.id.name);
			holder.txt_PhoneNumber = (TextView) convertView.findViewById(R.id.phone);
			holder.contactPhoto = (ImageView) convertView.findViewById(R.id.photo);
			holder.addFriendImageOrIcon = (ImageView) convertView.findViewById(R.id.appIcon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txt_Name
				.setText(itemDetailsArrayList.get(position).getName());
		holder.txt_PhoneNumber.setText(itemDetailsArrayList.get(position).getPhoneNumber());
		Bitmap b = itemDetailsArrayList.get(position).getBitmap();
		if (b != null) {
			holder.contactPhoto.setImageBitmap(b);
		} else
			holder.contactPhoto.setImageResource(R.drawable.noimage);
		if(itemDetailsArrayList.get(position).hasApp())
				holder.addFriendImageOrIcon.setImageResource(R.drawable.blackplus);
		else
			holder.addFriendImageOrIcon.setImageResource(R.drawable.message);
		return convertView;
	}

	
}
