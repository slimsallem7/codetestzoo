package org.bookmark.helper;

import android.content.Context;

import com.zoostudio.ngon.R;

public class ErrorHelper {

	public static String getErrorMessage(Context context, int code) {
		String errorCode = new StringBuilder().append(context.getString(R.string.error_prefix)).append(code).toString();
		return getResourceString(errorCode, context);
	}
	
	public static String getResourceString(String name, Context context) {
	    int nameResourceID = context.getResources().getIdentifier(name, "string", context.getApplicationInfo().packageName);
	    if (nameResourceID == 0) {
	        throw new IllegalArgumentException("No resource string found with name " + name);
	    } else {
	        return context.getString(nameResourceID);
	    }
	}
}
