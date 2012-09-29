package com.zoostudio.ngon.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;

import android.os.Debug;
import android.os.Environment;
import android.util.Log;

public class Logger {
	public static boolean isPrintLog = true;

	public static void e(String TAG, String message, Exception e) {
		if (!isPrintLog) return;
		if (e == null) return;
		
		Log.e(TAG, getMessage(e, message));
	}
	
	public static String getMessage(Exception e, String message) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("************************");
		builder.append(System.getProperty("line.separator"));
		builder.append(message);
		builder.append(System.getProperty("line.separator"));
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		builder.append(sw.toString());
		builder.append(System.getProperty("line.separator"));
		builder.append("************************");
		
		return builder.toString();
	}

	public static void e(String TAG, String message) {
		if (message == null) message = "null";
		if (isPrintLog) Log.e(TAG, message);
	}

	public static void heap(String TAG, Class<?> clazz) {
		if (TAG == null) TAG = "HEAPLOG:";
		
		Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576));
		Double available = new Double(Debug.getNativeHeapSize())/1048576.0;
		Double free = new Double(Debug.getNativeHeapFreeSize())/1048576.0;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		Log.e(TAG, "debug. =================================");
		Log.e(TAG, "debug.heap native: allocated " 
				+ df.format(allocated) + "MB of " 
				+ df.format(available) + "MB (" 
				+ df.format(free) + "MB free) in [" 
				+ clazz.getName().replaceAll("com.myTAG.android.","") + "]");
		Log.e(TAG, "debug.memory: allocated: " 
				+ df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " 
				+ df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" 
				+ df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)");
		System.gc();
		System.gc();
	}
	
	public static void logToSdcard(String TAG, String message, Exception ex) {
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "imedialog.txt");
		try {
			if (!file.exists()) file.createNewFile();
			FileWriter writer = new FileWriter(file, true);
			writer.append(System.getProperty("line.separator"));
			writer.append(System.getProperty("line.separator"));
			writer.append("**************** " + DateTimeUtils.getDateString("dd/MM/yyyy HH:mm") + " *******************");
			writer.append(System.getProperty("line.separator"));
			writer.append(message);
			writer.append(System.getProperty("line.separator"));
			
			if (ex != null) {
				StringWriter sw = new StringWriter();
				ex.printStackTrace(new PrintWriter(sw));
				String stacktrace = sw.toString();
				writer.append(stacktrace);
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			Log.e(TAG, "Error@logToSdcard:" + e.getMessage());
		}
	}
}

