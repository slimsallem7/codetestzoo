package com.zoostudio.ngon.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
	
	/*
	 *  --- LETTER ---- DATE OR TIME ---- PRESENTATION ---- EXAMPLE ----
	 *  	  G			Era designator		  Text			AD
	 *  	  y			Year				  Year			1996; 96
	 *  	  M			Month in year		  Month			July; Jul; 07
	 *  	  w			Week in year		  Number		27
	 *  	  W			Week in month		  Number		2
	 *  	  D			Day in year			  Number		167
	 *  	  d			Day in month		  Number		10
	 *  	  F			Day of week in month  Number		2
	 *  	  E			Day in week			  Text			Tuesday; Tue
	 *  	  a			Am/pm marker		  Text			PM
	 *  	  H			Hour in day (0-23)	  Number		0
	 *  	  k			Hour in day (1-24)	  Number		24
	 *  	  K			Hour in am/pm (0-11)  Number		0
	 *  	  h			Hour in am/pm (1-12)  Number		12
	 *  	  m			Minute in hour		  Number		30
	 *  	  s			Second in minute	  Number		55
	 *  	  S			Millisecond			  Number		978
	 *  	  z			TimeZone		General time zone	Pacific Standart Time; PST; GMT-08:00
	 *  	  Z			TimeZone		RFC 822 timezone	-0800
	 */
	
	public static String getDateString(String pattern) {
        Date dateNow = new Date ();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        StringBuilder stringBuilder = new StringBuilder(sdf.format(dateNow));
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * @param pattern
	 * @param time đơn vị là milliseconds
	 * @return
	 */
	public static String getDateString(String pattern, long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        StringBuilder stringBuilder = new StringBuilder(sdf.format(date));
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * @param time đơn vị là giây
	 * @return
	 */
	public static String getEllapsedTime(long oldTime) {
//		long oldTime = Long.parseLong(time);
		
		int year 	= 0;
		int month 	= 0;
		int day 	= 0;
		int hour 	= 0;
		int minute 	= 0;
		int second 	= 0;
		StringBuilder builder = new StringBuilder();
		
		long ellapseTime = (System.currentTimeMillis() / 1000) - oldTime;
		second 	= (int) (ellapseTime % 60);
		minute 	= (int) ((ellapseTime / 60) % 60);
		hour 	= (int)	(((ellapseTime / 60) / 60) % 24);
		day 	= (int)	((((ellapseTime / 60) / 60) / 24) % 30);
		month 	= (int) (((((ellapseTime / 60) / 60) / 24) / 30) % 12);
		year 	= (int) (((((ellapseTime / 60) / 60) / 24) / 30) / 12);
		
		if (year != 0) {
			builder.append(year).append(" năm trước");
		} else if (month != 0) {
			builder.append(month).append(" tháng trước");
		} else if (day != 0) {
			builder.append(day).append(" ngày trước");
		} else if (hour != 0) {
			builder.append(hour).append(" giờ ");
			builder.append(minute).append(" phút trước");
		} else if (minute != 0) {
			builder.append(minute).append(" phút trước");
		} else {
			builder.append(second).append("vài giây trước");
		}
		
		return builder.toString();
	}
}
