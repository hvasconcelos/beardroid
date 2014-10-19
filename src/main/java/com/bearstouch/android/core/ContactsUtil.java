/**
 * Copyright (C) 2014
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

package com.bearstouch.android.core;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;

import java.io.InputStream;
import java.util.ArrayList;

public class ContactsUtil
{
    /**
     * @param ctx
     * @param id
     * @return
     */
    public static Bitmap getContactPhoto(Context ctx, int id)
    {

        Uri photoUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, id);
        ContentResolver cr = ctx.getContentResolver();

        if (photoUri != null)
        {
            InputStream input = ContactsContract.Contacts
                    .openContactPhotoInputStream(cr, photoUri);
            if (input != null)
            {

                return BitmapFactory.decodeStream(input);
            }
        }

        return null;

    }

    /**
     * @param ctx
     * @param contactId
     * @return
     */
    public static String getDysplayNamefromId(Context ctx, long contactId)
    {

        Uri cUri = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, contactId);

        Cursor cur = ctx.getContentResolver().query(cUri, null, null, null,
                null);

        if (cur.moveToFirst())
        {
            int dysplayIndex = cur
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String dysplayName = cur.getString(dysplayIndex);
            return dysplayName;
        }

        return "";
    }

    /**
     * @param ctx
     * @return
     */
    public static ArrayList<Integer> getOrderedContactsListIds(Context ctx)
    {

        ArrayList<Integer> contact_list = new ArrayList<Integer>();
        ContentResolver cr = ctx.getContentResolver();

        String[] columns = new String[]
        { BaseColumns._ID, ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER };
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, columns,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + ">=1", null,
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE NOCASE ASC");

        if (cur.getCount() > 0)
        {
            while (cur.moveToNext())
            {
                String id = cur.getString(cur.getColumnIndex(BaseColumns._ID));
                contact_list.add(Integer.parseInt(id));

            }
        }
        return contact_list;

    }

    /**
     * @param ctx
     * @return
     */
    public static Cursor getContactPhoneList(Context ctx){
        return ctx.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[] {  ContactsContract.CommonDataKinds.Phone._ID,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.NUMBER},
                        null,
                        null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
    }

}
