/**
 * Copyright (C) 2013
 * Bearstouch Software : <mail@bearstouch.com>
 *
 * This file is part of Bearstouch Android Lib.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 * @author H�lder Vasconcelos heldervasc@bearstouch.com
 *
 */
package com.bearstouch.android.core.image;


import android.content.Context;
import android.widget.ImageView;

/**
 *
 */
public class ImageCacheManager {

    private final ImageLoader imgLoader;

    public ImageCacheManager(Context ctx) {
        this.imgLoader = new ImageLoader(ctx);
    }

    /**
     *
     */
    public void clearMemoryCache() {
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
