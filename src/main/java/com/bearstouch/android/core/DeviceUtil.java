package com.bearstouch.android.core;
import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;

/**
 * 
 * @author HŽlder Vasconcelos heldervasc@bearstouch.com
 *
 */

public class DeviceUtil
{
	 private static double density = -1;
	  
	 /**
	   * Detect Screen density
	   * 
	   * @param Activity to verify Activity density 
	   *          
	   * @return screen density
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
	   * Verify if its running on emulator
	   *
	   *
	   * @return String MD5 hash 
	   */
	 public static boolean isEmulator() {
	    return (Build.BOARD.equals("unknown")
	        && Build.DEVICE.equals("generic")
	        && Build.BRAND.equals("generic"));
	  }
	 

	  /**
	   * Converts device independent pixels to screen pixels.
	   * 
	   * @param dipPixels
	   *          is the amount of device independent pixels.
	   * @param density
	   *          is the device's screen density.
	   * 
	   * @return An integer representing the value in screen pixels.
	   */
	  public static int convertToScreenPixels(int dipPixels, double density) {
	    return (int) convertToScreenPixels((double) dipPixels, density);
	  }

	  /**
	   * Converts device independent pixels to screen pixels.
	   * 
	   * @param dipPixels
	   *          is the amount of device independent pixels.
	   * @param density
	   *          is the device's screen density.
	   * 
	   * @return A double representing the value in screen pixels.
	   */
	  public static double convertToScreenPixels(double dipPixels, double density) {
	    return (density > 0) ? (dipPixels * density) : dipPixels;
	  }
	  
	  /**
	   * Gets the md5 hashed and upper-cased device id.
	   *
	   * @param context
	   *          the application context.
	   *
	   * @return The encoded device id.
	   */
}
