package com.example.findyourfriend;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Inet {	
	/**
	 * Метод проверяет, является ли phonenumber другом friendphonenumber
	 * @param phonenumber Наш номер
	 * @param friendphonenumber Номер друга
	 * @return True - если является
	 */
	public boolean ifFriend(String phonenumber, String friendphonenumber) {
		List<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("phonenumber", phonenumber));
		NameValuePairs.add(new BasicNameValuePair("friendphonenumber", friendphonenumber));
		String result = getData("http://w ww.findyourfriend.ho.ua/isfriend.php", NameValuePairs);
		return result.getBytes()[0] == '1' ? true : false;
	}
	
	/**
	 * Метод отправляет координаты на сервер
	 * @param location Координаты
	 * @return
	 */
	public boolean sendLocation(String location) {
		List<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("location", location));
		String result = getData("http://www.findyourfriend.ho.ua/isfriend.php", NameValuePairs);
		return true;
	}
	
	/**
	 * Метод проверяет, зарегистрирован ли номер в системе FindYourFriend
	 * @param phonenumber Номер телефона
	 * @return True - Если зарегистрирован
	 */
	public boolean isRegisteredNumber(String phonenumber) {
		List<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();
		NameValuePairs.add(new BasicNameValuePair("phonenumber", phonenumber));
		String result = getData("http://www.findyourfriend.ho.ua/phoneexist.php", NameValuePairs);
		return result.getBytes()[0] == '1' ? true : false; 
	}
	
	/**
	 * Отправляет запрос на добавление друга, если запрос пришел "к нам", то этот метод
	 * соглашается с запросом от друга
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
	 * Метод возвращает список номеров от которых приходили уведомления, записанные через пробел
	 * @param phonenumber Наш номер
	 * @return Строка номеров
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
