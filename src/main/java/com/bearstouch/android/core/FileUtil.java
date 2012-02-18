package com.bearstouch.android.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.content.Context;
import android.os.Environment;

public class FileUtil {
	
	public static String readFileContentToString(InputStream fis)
	{
		StringBuffer stringBuffer = new StringBuffer();
		
		if(fis==null){
			return "";
		}
		
		try {

			char[] buffer = new char[1024];
			Reader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				stringBuffer.append(buffer, 0, n);
			}
			fis.close();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return stringBuffer.toString();

	}
	
	
	public static String getSDCardCachePath(Context context){
		 String cacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/"
       + context.getPackageName() + "/cache";
		return cacheDir;
	}
	
	public static String getInternalCachePath(Context context){
		return context.getCacheDir().getAbsolutePath();
	}
	
}
