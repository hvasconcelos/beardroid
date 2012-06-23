/**
 * 
 * @author HŽlder Vasconcelos heldervasc@bearstouch.com
 *
 */
package com.bearstouch.android.core.ui;


import com.google.inject.Inject;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This class provides instruments to apply fonts to some sub-types of View.
 */

public class FontManager {

	private Typeface typeface ;
	
	/**
	 * @param context
	 * @param fontFile
	 */
	@Inject
	public FontManager(Context context,String fontFile){
		typeface = Typeface.createFromAsset(context.getAssets(), fontFile);
	}
	/**
	 * 
	 */
	/**
	 * @param textView
	 */
	public void apply(TextView textView) {
		textView.setTypeface(typeface);
	}
	
	/**
	 * @param textView
	 */
	public void apply(EditText textView) {
		textView.setTypeface(typeface);
	}

	/**
	 * @return
	 */
	public Typeface getTypeface() {
		return typeface;
	}

}