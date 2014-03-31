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

package com.bearstouch.android.core.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Set;

public class FontManager
{

    private HashMap<String, Typeface> fonts = new HashMap<String, Typeface>();
    private static FontManager mInstance = null;
    private Context context;

    public static FontManager getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new FontManager(context);
        }
        return mInstance;
    }

    private FontManager(Context context)
    {
        this.context = context;
    }

    public void addFont(String name, String fontFile)
    {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                fontFile);
    }

    public Set<String> getFonts()
    {
        return fonts.keySet();
    }

    public void apply(String name, TextView textView)
    {
        textView.setTypeface(fonts.get(name));
    }
}