package com.bearstouch.android.core;

import com.bearstouch.android.core.R;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author HŽlder Vasconcelos heldervasc@bearstouch.com
 *
 */
public class AndroidUtil {
	
	private static double density = -1;
	
	
	/**
	 * @param ctx
	 * @return
	 */
	public static boolean isDebugVersion(Context ctx){	
		int flags = ctx.getApplicationInfo().flags;
		if ((flags &= ApplicationInfo.FLAG_DEBUGGABLE)!=0){
			
				return true;
		}else{
			return false;
		}		
	}

	/**
	 * @param ctx
	 * @param layoutid
	 * @param textid
	 * @param alertTitle
	 * @param alertMessage
	 * @return
	 */
	public static Dialog createAlert(Context ctx, int layoutid,int textid,String alertTitle,String alertMessage)
	{
		Dialog dialog;
		dialog = new Dialog(ctx);
		dialog.setContentView(layoutid);
		dialog.setTitle(alertTitle);
		LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.layout_ct_dialog);
		TextView text = (TextView) layout.findViewById(textid);
		text.setText(alertMessage);
		return dialog;

	}

	 /**
	 * @param activity
	 * @return
	 */
	public static double getDensity(Activity activity) {
	    if (density == -1) {
	      DisplayMetrics displayMetrics = new DisplayMetrics();
	      activity.getWindowManager().getDefaultDisplay()
	          .getMetrics(displayMetrics);
	      density = displayMetrics.density;
	   
	    }
	    return density;
	  }
	 
	 /**
	 * @param activity
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Activity activity){
		  DisplayMetrics displayMetrics=null;
		  if (density == -1) {
			  displayMetrics= new DisplayMetrics();
		      activity.getWindowManager().getDefaultDisplay()
		          .getMetrics(displayMetrics);
		    
		    
		    }
		  return displayMetrics; 
	  }
	 
	 
   
	/**
	 * @param ctx
	 * @param emailAddresses
	 * @param CCAddresses
	 * @param subject
	 * @param message
	 */
	public static void sendEmail(Context ctx, String[] emailAddresses,
			String[] CCAddresses, String subject, String message)
	{

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		// emailIntent.setType("text/plain");
		emailIntent.setData(Uri.parse("mailto:"));
		String[] to = emailAddresses;
		String[] cc = CCAddresses;
		emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
		if (CCAddresses != null)
		{
			emailIntent.putExtra(Intent.EXTRA_CC, cc);
		}
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, message);
		emailIntent.setType("message/rfc822");

		ctx.startActivity(Intent.createChooser(emailIntent, "Email"));

	}

	/**
	 * @param ctx
	 * @param id
	 */
	public static void showToast(Context ctx, int id)
	{

		Context context = ctx;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, context.getResources()
				.getString(id), duration);
		toast.show();
	}

	/**
	 * @param ctx
	 * @param message
	 */
	public static void showToast(Context ctx, String message)
	{

		Context context = ctx;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
	
	
	
	/**
	 * @return
	 */
	public static boolean isExternalStorageAvailableforWriting(){
		
	
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		  return true;
		}else{
			 return false;
		}
		
	}
	
	/**
	 * @return
	 */
	public static boolean isExternalStorageAvailableforReading(){
		
		
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)||Environment.MEDIA_MOUNTED.equals(state)) {		
		  return true;
		}else{
			 return false;
		}
		
	}
		
	/**
	 * @param ctx
	 * @return
	 */
	public static boolean isRunningOnEmulator(Context ctx){
		if (Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID)==null){			
			return true;
		} else{			
			return false;
		}
	}
	
	/**
	 * @param ctx
	 * @param pref
	 * @return
	 */
	public static boolean savePreferencesInExternal(Context ctx,SharedPreferences pref){
		
		Properties propfile=new Properties();
		Map<String,?>  keymap=pref.getAll();
		Iterator<String> keyit=keymap.keySet().iterator();
		Log.d("External", "Saving prefrences to external Storages");
		
		
		while (keyit.hasNext()) {
			String key=keyit.next();
			propfile.put(key, keymap.get(key));
		}
		if(isExternalStorageAvailableforWriting()==true){
			try {
				propfile.storeToXML(new FileOutputStream(new File(ctx.getExternalFilesDir(null),"install_info")),null);
			}
			catch (Exception e) {						
				
				e.printStackTrace();
			}
			
			Log.d("External", "Saved prefrences to external ");
			return true;
		}else{
			Log.d("External", "Failed to Save prefrences for external ");
			return false;
		}
	}

	
}
