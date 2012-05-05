//////////////////////////////////////////////////////////////////////
//	
// Mobitto Android Application
//	
//	Author:  Helder Vasconcelos (Bearstouch Software) 
//			  <helder.vasc@beartsouch.com>
// 
//////////////////////////////////////////////////////////////////////
package com.bearstouch.android.core.image;

import roboguice.util.Ln;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

@Singleton
public class ImageCacheManager {

	private final Context ctx;
	private final ImageLoader imgLoader;

	@Inject
	public ImageCacheManager(Context ctx) {
		this.ctx = ctx;
		this.imgLoader = new ImageLoader(ctx);
	}

	public void clearMemoryCache() {
		Ln.d("Clearing Memory Cache");
		imgLoader.clearMemoryCache();
	}

	public void clearFileCache() {
		imgLoader.clearFileCache();
	}

	public void displayImage(String url, ImageView imageView) {			
		imgLoader.DisplayImage(url, imageView);
	}

}
