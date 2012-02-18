package com.bearstouch.android.core;

import java.io.InputStream;
import java.util.ArrayList;

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
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactsUtil {
	public static Bitmap getContactPhoto(Context ctx, int id)
	{

		Uri photoUri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, id);
		ContentResolver cr = ctx.getContentResolver();

		if (photoUri != null)
		{
			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(cr, photoUri);
			if (input != null) {

			return BitmapFactory.decodeStream(input); }
		}

		return null;

	}

	public static String getDysplayNamefromId(Context ctx, long contactId)
	{

		Uri cUri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, contactId);

		Cursor cur = ctx.getContentResolver().query(cUri, null, null, null, null);

		if (cur.moveToFirst())
		{
			int dysplayIndex = cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			String dysplayName = cur.getString(dysplayIndex);
			return dysplayName;
		}

		return "";
	}

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

	public static MatrixCursor getOrderedContactsWithPhoneList(Context ctx)
	{
		MatrixCursor cursortoreturn;
		String [] columnnames=new String[5];
		
		columnnames[0]="_id";
		columnnames[1]="database_id";		
		columnnames[2]="display_name";
		columnnames[3]="phone_number";
		columnnames[4]="phone_type";
		cursortoreturn=new MatrixCursor(columnnames);

		ContentResolver cr = ctx.getContentResolver();
		String[] columns = new String[]
		{ BaseColumns._ID, ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER };
		Cursor people = cr.query(ContactsContract.Contacts.CONTENT_URI, columns,
				ContactsContract.Contacts.HAS_PHONE_NUMBER + ">=1", null,
				ContactsContract.Contacts.DISPLAY_NAME + " COLLATE NOCASE ASC");

		people.moveToFirst();

		
		while (people.moveToNext()) {
			
			 String contactId =
				 people.getString(people.getColumnIndex(ContactsContract.Contacts._ID));
			 String diplayName =
				 people.getString(people.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			 
			 Cursor phoneCursor = ctx.getContentResolver().query(Data.CONTENT_URI,
		          new String[] {Data._ID, Phone.NUMBER, Phone.TYPE, Phone.LABEL},
		          Data.CONTACT_ID + "=?" + " AND "
		                  + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'",
		          new String[] {String.valueOf(contactId)}, null);
			 
			 String Phonenumber="";
	        while (phoneCursor.moveToNext()) {
	            String number = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER));
	            long dataid=phoneCursor.getLong(phoneCursor.getColumnIndex(Data._ID));
	            String type = Integer.toString(phoneCursor.getInt(phoneCursor.getColumnIndex(Phone.TYPE)));
	            if(Phonenumber.compareTo(number)!=0){
	            	cursortoreturn.addRow(new String[]{Long.toString(dataid),contactId,diplayName,number,type});
	            	Phonenumber=number;
	            }

	        }
	        phoneCursor.close();
			
		}
		people.close();
		
		return cursortoreturn;

	}


}
