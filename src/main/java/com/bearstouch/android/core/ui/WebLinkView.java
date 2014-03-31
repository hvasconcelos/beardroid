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

package com.bearstouch.android.core.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import com.bearstouch.android.core.R;

public class WebLinkView extends View
{

    String http_url = "";
    String name = "";

    /**
     * @param context
     * @param url
     */
    public WebLinkView(Context context, String url)
    {
        super(context);
        throw new RuntimeException(
                "Please pass custom Parameters icon_img and link");

    }

    /**
     * @param context
     * @param attrs
     */
    public WebLinkView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public WebLinkView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * @param attrs
     */
    private void init(AttributeSet attrs)
    {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                com.bearstouch.android.core.R.styleable.WebLinkView);
        String url = a.getString(R.styleable.WebLinkView_link);

        this.http_url = url;

        setOnClickListener(new OnClickListener()
        {

            public void onClick(View arg0)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(http_url));
                getContext().startActivity(intent);

            }
        });

    }

}
