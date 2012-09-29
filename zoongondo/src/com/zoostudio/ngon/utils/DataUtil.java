package com.zoostudio.ngon.utils;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class DataUtil {
	public static File createTemporaryFile(String part, String ext) throws IOException
	{
	    File tempDir= Environment.getExternalStorageDirectory();
	    tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
	    if(!tempDir.exists())
	    {
	        tempDir.mkdir();
	    }
	    return File.createTempFile(part, ext, tempDir);
	}
}
