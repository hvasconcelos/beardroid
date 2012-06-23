/**
 * 
 * @author HŽlder Vasconcelos heldervasc@bearstouch.com
 *
 */
package com.bearstouch.android.core.image;

import roboguice.util.Ln;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import android.content.Context;
import android.widget.ImageView;

/**
 *
 */
@Singleton
public class ImageCacheManager {

	private final ImageLoader imgLoader;

	@Inject
	public ImageCacheManager(Context ctx) {
		this.imgLoader = new ImageLoader(ctx);
	}

	/**
	 * 
	 */
	public void clearMemoryCache() {
		Ln.d("Clearing Memory Cache");
		imgLoader.clearMemoryCache();
	}

	/**
	 * 
	 */
	public void clearFileCache() {
		imgLoader.clearFileCache();
	}

	/**
	 * @param url
	 * @param imageView
	 */
	public void displayImage(String url, ImageView imageView) {			
		imgLoader.DisplayImage(url, imageView);
	}

}
