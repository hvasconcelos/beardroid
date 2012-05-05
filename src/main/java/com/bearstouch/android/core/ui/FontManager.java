//////////////////////////////////////////////////////////////////////
//	
// Mobitto Android Application
//	
//	Author:  Helder Vasconcelos (Bearstouch Software) 
//			  <helder.vasc@beartsouch.com>
// 
//////////////////////////////////////////////////////////////////////
package com.bearstouch.android.core.ui;

import java.util.Hashtable;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This class provides instruments to apply fonts to some sub-types of View.
 */

public class FontManager {

	private Typeface typeface ;

	/**
	 * Private helper
	 * 
	 * @param context
	 * @param textView
	 * @param value
	 */
	
	@Inject
	public FontManager(Context context){
		typeface = Typeface.createFromAsset(context.getAssets(), "helvetica_light.ttf");
	}
	/**
	 * 
	 */
	public void apply(TextView textView) {
		textView.setTypeface(typeface);
	}
	
	public void apply(EditText textView) {
		textView.setTypeface(typeface);
	}

	public Typeface getTypeface() {
		return typeface;
	}

}