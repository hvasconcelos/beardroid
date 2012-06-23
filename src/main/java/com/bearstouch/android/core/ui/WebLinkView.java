package com.bearstouch.android.core.ui;

import com.bearstouch.android.core.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author H�lder Vasconcelos heldervasc@bearstouch.com
 *
 */
public class WebLinkView extends View{
	
	String http_url="";
	String name="";
	
	/**
	 * @param context
	 * @param url
	 */
	public WebLinkView(Context context,String url)
	{
		super(context);
		throw new RuntimeException("Please pass custom Parameters icon_img and link");
		
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public WebLinkView(Context context, AttributeSet attrs)
	{
		super(context,attrs);
		init(attrs);
	}
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public WebLinkView(Context context,AttributeSet attrs, int defStyle)
	{
		super(context,attrs, defStyle);
		init(attrs);
	}
	
	/**
	 * @param attrs
	 */
	private void init(AttributeSet attrs) {
		TypedArray a =  getContext().obtainStyledAttributes(attrs, com.bearstouch.android.core.R.styleable.WebLinkView);
		String url=a.getString(R.styleable.WebLinkView_link);

		this.http_url=url;
		

		setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(http_url));
				getContext().startActivity(intent);			
				
			}
		});
		
	}
	
	

	
}
