package com.bearstouch.android.core.db;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.bearstouch.android.core.Logging;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author HŽlder Vasconcelos heldervasc@bearstouch.com
 *
 */

public class AssetDBHelper extends SQLiteOpenHelper {

	public static String	LOGTAG_		= "BearDBHelper";
	Context					mContext;
	String					mDatabaseName;
	int						mDatabaseVersion;
	AssetManager			mAssetManager;
	Logging					mLog;
	SQLiteDatabase			mDatabase	= null;
	String					mPackageName;
	String					mDBPath;

	/**
	 * @param package_name
	 * @param database_name
	 * @param database_version
	 * @param context
	 * @param log
	 */
	@Inject
	public AssetDBHelper(@Named("PackageName") String package_name,
			@Named("DatabaseName") String database_name,
			@Named("DatabaseVersion") int database_version, Context context, Logging log) {
		super(context, database_name, null, database_version);

		mContext = context;
		mDatabaseName = database_name;
		mDatabaseVersion = database_version;
		mLog = log;
		mAssetManager = mContext.getAssets();
		mLog.info(LOGTAG_, " Database helper for Database Name=" + mDatabaseName + " and version "
				+ mDatabaseVersion);
		
		mPackageName = package_name;
		mDBPath = "/data/data/" + mPackageName + "/databases/";

		try {
			createDataBase();
		}
		catch (IOException e) {
			mLog.error(LOGTAG_, " Failed Creating Database to " + mDBPath + mDBPath);
			e.printStackTrace();
		}
		openDataBase();
	}

	/**
	 * @throws IOException
	 */
	public void createDataBase() throws IOException {
		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			try {
				copyDataBase();
			}
			catch (IOException mIOException) {
				throw new Error("Error Copying DataBase");
			}
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public boolean openDataBase() throws SQLException {
		String mPath = mDBPath + mDatabaseName;
		mDatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		return mDatabase != null;
	}

	/**
	 * @return
	 */
	private boolean checkDataBase() {
		SQLiteDatabase mCheckDataBase = null;
		try {
			String myPath = mDBPath + mDBPath;
			mCheckDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		}
		catch (SQLiteException mSQLiteException) {
			mLog.warning(LOGTAG_, "DatabaseNotFound " + mSQLiteException.toString());
		}

		if (mCheckDataBase != null) {
			mCheckDataBase.close();
		}
		return mCheckDataBase != null;
	}

	/**
	 * @throws IOException
	 */
	private void copyDataBase() throws IOException {

		mLog.info(LOGTAG_, " Copying Database [" + mDatabaseName + "] to [" + mDBPath + mDBPath + "]");
		InputStream mInput = mContext.getAssets().open(mDatabaseName);
		String outFileName = mDBPath + mDatabaseName;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	/**
	 * @param numchunks
	 * @throws IOException
	 */
	private void copyDataBaseInChunks(int numchunks) throws IOException {
		 AssetManager am = mContext.getAssets();
		 File Path = mContext.getDir("Data", 0);
		 File DBFile= new File(Path, mDatabaseName);
	    OutputStream os = new FileOutputStream(DBFile);
	    DBFile.createNewFile();
	    byte []b = new byte[1024];
	    int i, r;
	    String []Files = am.list("");
	    Arrays.sort(Files);
	    for(i=1;i<numchunks;i++)
	    {	        
	        InputStream is = mContext.getAssets().open(mDatabaseName);
	        while((r = is.read(b)) != -1)
	            os.write(b, 0, r);
	        is.close();
	    }
	    os.close();
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	/**
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @param limit
	 * @return
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, int limit) {

		//mLog.info(LOGTAG_, "SELECT " + columns.toString() + " FROM " + table + " WHERE " + selection);
		return mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy,
				Integer.toString(limit));
	}

	/**
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		//mLog.info(LOGTAG_, "SELECT " + columns.toString() + " FROM " + table + " WHERE " + selection);
		return mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#close()
	 */
	@Override
	public synchronized void close() {
		if (mDatabase != null) mDatabase.close();
		super.close();
	}

}
