package com.bearstouch.android.core.db;

import java.io.IOException;
import java.io.InputStream;

import roboguice.inject.ContextScoped;

import com.bearstouch.android.core.FileUtil;
import com.bearstouch.android.core.Logging;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author HŽlder Vasconcelos heldervasc@bearstouch.com
 *
 */
public class BearDBHelper extends  SQLiteOpenHelper{
	
	public static String MIGRATIONS_DIRECTORY="db_migrations";
	public static String LOGTAG_="BearDBHelper";
	
	//Instance Fields
	String 			mDatabaseName;
	int 				mDatabaseVersion;
	Context 			mContext;
	AssetManager 	mAssetManager;
	Logging 			mLog;
	SQLiteDatabase mDatabase=null;

	
	/**
	 * @param database_name
	 * @param database_version
	 * @param context
	 * @param log
	 */
	@Inject
	public BearDBHelper(@Named("DatabaseName") String database_name ,@Named("DatabaseVersion") Integer database_version,Context context,Logging log){	
		super(context, database_name, null, database_version);			
		mDatabaseName=database_name;
		mDatabaseVersion=database_version;
		mContext=context;
		mAssetManager=mContext.getAssets();	
		mLog=log;
		mLog.info(LOGTAG_, " Database helper for Database Name="+mDatabaseName+" and version "+mDatabaseVersion);		
		mDatabase=getWritableDatabase();		
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		upgradeToVersion(database,mDatabaseVersion,1);
	}


	/**
	 * @param db
	 * @param newVersion
	 * @param oldVersion
	 */
	private void upgradeToVersion(SQLiteDatabase db,int newVersion, int oldVersion) {
		if(newVersion>oldVersion){
			upgrade(db,newVersion, oldVersion);
		}else if(newVersion<oldVersion){			
			downgrade(db,newVersion, oldVersion);
		}
	}


	/**
	 * @param db
	 * @param newVersion
	 * @param oldVersion
	 */
	private void downgrade(SQLiteDatabase db,int newVersion, int oldVersion) {
		mLog.info(LOGTAG_, "Database Downgrading from version["+oldVersion+"] to version ["+newVersion+"]");
		for (int i = oldVersion; i > newVersion; i--) {

			try {
				db.beginTransaction();
				mLog.info(LOGTAG_,"Reading SQL file "+"db_migrations/downgrade_" + i + ".sql");
				InputStream is = mAssetManager.open("db_migrations/downgrade_" + i + ".sql");
				String script = FileUtil.readFileContentToString(is);
				mLog.info(LOGTAG_,"Executing = "+script);
				db.execSQL(script);
				
				db.endTransaction();
			}
			catch (IOException e) {
				db.endTransaction();
				e.printStackTrace();
				//mLog.logAndTrackException(LOGTAG_, "Failed To Upgrade Database", e);
			}
		}
	}


	/**
	 * @param db
	 * @param newVersion
	 * @param oldVersion
	 */
	private void upgrade(SQLiteDatabase db,int newVersion, int oldVersion) {
		
		mLog.info(LOGTAG_, "Database Upgrading from version["+oldVersion+"] to version ["+newVersion+"]");
		for(int i=oldVersion;i<=newVersion;i++){
			
			try {
				db.beginTransaction();
				mLog.info(LOGTAG_,"Reading SQL file "+"db_migrations/upgrade_"+i+".sql");
				InputStream is=mAssetManager.open("db_migrations/upgrade_"+i+".sql");
				String script=FileUtil.readFileContentToString(is);
				mLog.info(LOGTAG_,"Executing = "+script);
				db.execSQL(script);
				db.endTransaction();
				
			}
			catch (IOException e) {
				e.printStackTrace();
				db.endTransaction();				
			}
		}
	}


	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		
		upgradeToVersion(db,oldVersion,mDatabaseVersion);
		
	}
	
	//Open an close Database 
	public void executeSQL(String query){		
		SQLiteDatabase sqldata=getWritableDatabase();
		sqldata.beginTransaction();
		sqldata.execSQL(query);
		sqldata.endTransaction();
		sqldata.close();		
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
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit){
		
		mLog.info(LOGTAG_,"SELECT "+columns.toString()+" FROM "+table+" WHERE "+selection);		
		return mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy,limit);
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
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
		mLog.info(LOGTAG_,"SELECT "+columns.toString()+" FROM "+table+" WHERE "+selection);		
		return mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
	
	
	/**
	 * 
	 */
	public void closeDatabase(){
		mDatabase.close();
	}
}
