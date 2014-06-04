package com.example.findyourfriend;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class Inet {	
	
	public void register(String number) {
		List<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("phonenumber", number));
		String result = getData("http://www.findyourfriend.ho.ua/register.php", NameValuePairs);
	}
	
	/**
	 * 
	 * ���������� ������������������, ���������������� ���� phonenumber ������������ friendphonenumber
	 * @param phonenumber ������ ����������
	 * @param friendphonenumber ���������� ����������
	 * @return True - �������� ����������������
	 */
	public boolean ifFriend(String phonenumber, String friendphonenumber) {
		List<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("phonenumber", phonenumber));
		NameValuePairs.add(new BasicNameValuePair("friendphonenumber", friendphonenumber));
		String result = getData("http://www.findyourfriend.ho.ua/isfriend.php", NameValuePairs);
		Log.i("my",result);
		return result.getBytes()[0] == '1' ? true : false;
	}
	
	/**
	 * ���������� �������������������� �������������������� ���� ������������
	 * @param location ��������������������
	 * @return
	 */
	public boolean sendLocation(String location) {
		List<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("location", location));
		String result = getData("http://www.findyourfriend.ho.ua/isfriend.php", NameValuePairs);
		return true;
	}
	
	/**
	 * ���������� ������������������, ������������������������������ ���� ���������� �� �������������� FindYourFriend
	 * @param phonenumber ���������� ����������������
	 * @return True - �������� ������������������������������
	 */
	public boolean isRegisteredNumber(String phonenumber) {
		List<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("phonenumber", phonenumber));
		String result = getData("http://www.findyourfriend.ho.ua/phoneexist.php", NameValuePairs);
		return result.charAt(0) == '1' ? true : false;
		//return result.getBytes()[0] == '1' ? true : false; 
	}
	
	/**
	 * �������������������� ������������ ���� �������������������� ����������, �������� ������������ ������������ "�� ������", ���� �������� ����������
	 * ���������������������� �� ���������������� ���� ����������
	 * @param phonenumber
	 * @param friendphonenumber
	 */
	public void addFriend(String phonenumber, String friendphonenumber) {
		List<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("phonenumber", phonenumber));
		NameValuePairs.add(new BasicNameValuePair("friendphonenumber", friendphonenumber));
		String result = getData("http://www.findyourfriend.ho.ua/addFriend.php", NameValuePairs);
	}
	
	/**
	 * ���������� �������������������� ������������ �������������� ���� �������������� ������������������ ����������������������, �������������������� ���������� ������������
	 * @param phonenumber ������ ����������
	 * @return ������������ ��������������
	 */
	public String getListOfNotices(String phonenumber) {
		List<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("phonenumber", phonenumber));
		String result = getData("http://www.findyourfriend.ho.ua/getNotice.php", NameValuePairs);
		return result;
	}
	
	public void SendLocaion() {
		
	}
	
	private String getData(String url, List<NameValuePair> nameValuePairs) {
		ThreadRequest thread = new ThreadRequest();
		url += "?";
		for(int i = 0; i < nameValuePairs.size(); i++) {
			url += nameValuePairs.get(i).getName();
			url += "=";
			url += nameValuePairs.get(i).getValue();
			url += "&";
		}
		thread.setURL(url);
		thread.start();
		if (thread.isAlive()){
            try {
                synchronized (thread) {
                	thread.wait();
                }
            } catch (InterruptedException e) {
            }
        }        
		return thread.getText();
	}
}
