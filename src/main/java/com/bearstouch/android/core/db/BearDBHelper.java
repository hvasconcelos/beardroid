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

package com.bearstouch.android.core.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bearstouch.android.core.FileUtil;
import com.bearstouch.android.core.Logger;

import java.io.IOException;
import java.io.InputStream;

public class BearDBHelper extends SQLiteOpenHelper
{

    public static String MIGRATIONS_DIRECTORY = "db_migrations";
    public static String LOGTAG_ = "BearDBHelper";

    private String mDatabaseName;
    private int mDatabaseVersion;
    private Context mContext;
    private AssetManager mAssetManager;
    private Logger mLog;
    private SQLiteDatabase mDatabase = null;

    public BearDBHelper(String database_name, Integer database_version,
            Context context, Logger log)
    {
        super(context, database_name, null, database_version);

        mDatabaseName = database_name;
        mDatabaseVersion = database_version;
        mContext = context;
        mAssetManager = mContext.getAssets();
        mLog = log;
        mLog.info(" Database helper for Database Name="
                + mDatabaseName + " and version " + mDatabaseVersion);
        mDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        upgradeToVersion(database, mDatabaseVersion, 1);
    }

    private void upgradeToVersion(SQLiteDatabase db, int newVersion,
            int oldVersion)
    {
        if (newVersion > oldVersion)
        {
            upgrade(db, newVersion, oldVersion);
        } else if (newVersion < oldVersion)
        {
            downgrade(db, newVersion, oldVersion);
        }
    }

    private void downgrade(SQLiteDatabase db, int newVersion, int oldVersion)
    {
        mLog.info("Database Downgrading from version[" + oldVersion
                + "] to version [" + newVersion + "]");
        for (int i = oldVersion; i > newVersion; i--)
        {

            try
            {
                db.beginTransaction();
                mLog.info("Reading SQL file "
                        + "db_migrations/downgrade_" + i + ".sql");
                InputStream is = mAssetManager.open("db_migrations/downgrade_"
                        + i + ".sql");
                String script = FileUtil.readFileContentToString(is);
                mLog.info("Executing = " + script);
                db.execSQL(script);
                db.endTransaction();
            } catch (IOException e)
            {
                db.endTransaction();
                e.printStackTrace();
                // mLog.logAndTrackException(LOGTAG_,
                // "Failed To Upgrade Database", e);
            }
        }
    }

    private void upgrade(SQLiteDatabase db, int newVersion, int oldVersion)
    {

        mLog.info("Database Upgrading from version[" + oldVersion
                + "] to version [" + newVersion + "]");
        for (int i = oldVersion; i <= newVersion; i++)
        {

            try
            {
                db.beginTransaction();
                mLog.info("Reading SQL file "
                        + "db_migrations/upgrade_" + i + ".sql");
                InputStream is = mAssetManager.open("db_migrations/upgrade_"
                        + i + ".sql");
                String script = FileUtil.readFileContentToString(is);
                mLog.info("Executing = " + script);
                db.execSQL(script);
                db.endTransaction();

            } catch (IOException e)
            {
                e.printStackTrace();
                db.endTransaction();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        upgradeToVersion(db, oldVersion, mDatabaseVersion);
    }

    // Open an close Database
    public void executeSQL(String query)
    {
        SQLiteDatabase sqldata = getWritableDatabase();
        sqldata.beginTransaction();
        sqldata.execSQL(query);
        sqldata.endTransaction();
        sqldata.close();
    }

    public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy, String limit)
    {

        mLog.info("SELECT " + columns.toString() + " FROM " + table
                + " WHERE " + selection);
        return mDatabase.query(table, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
    }

    public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy)
    {
        mLog.info("SELECT " + columns.toString() + " FROM " + table
                + " WHERE " + selection);
        return mDatabase.query(table, columns, selection, selectionArgs,
                groupBy, having, orderBy);
    }

    public void closeDatabase()
    {
        mDatabase.close();
    }
}
