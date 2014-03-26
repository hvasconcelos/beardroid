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
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.bearstouch.android.core.Logging;

import java.io.*;
import java.util.Arrays;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class AssetDBHelper extends SQLiteOpenHelper
{

    public static String LOGTAG_ = "BearDBHelper";

    private Context mContext;
    private String mDatabaseName;
    private int mDatabaseVersion;
    private AssetManager mAssetManager;
    private Logging mLog;
    private SQLiteDatabase mDatabase = null;
    private String mPackageName;
    private String mDBPath;

    public AssetDBHelper(String package_name, String database_name,
            int database_version, Context context, Logging log)
    {
        super(context, database_name, null, database_version);

        mContext = context;
        mDatabaseName = database_name;
        mDatabaseVersion = database_version;
        mLog = log;
        mAssetManager = mContext.getAssets();

        mLog.info(mContext, " Database helper for Database Name="
                + mDatabaseName + " and version " + mDatabaseVersion);

        mPackageName = package_name;
        mDBPath = "/data/data/" + mPackageName + "/databases/";

        try
        {
            createDataBase();
        } catch (IOException e)
        {
            mLog.error(mContext, " Failed Creating Database to " + mDBPath
                    + mDBPath);
            e.printStackTrace();
        }
        openDataBase();
    }

    public void createDataBase() throws IOException
    {
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist)
        {
            this.getReadableDatabase();
            try
            {
                copyDataBase();
            } catch (IOException mIOException)
            {
                throw new Error("Error Copying DataBase");
            }
        }
    }

    public boolean openDataBase() throws SQLException
    {
        String mPath = mDBPath + mDatabaseName;
        mDatabase = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDatabase != null;
    }

    private boolean checkDataBase()
    {
        SQLiteDatabase mCheckDataBase = null;
        try
        {
            String myPath = mDBPath + mDBPath;
            mCheckDataBase = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (SQLiteException mSQLiteException)
        {
            mLog.warning(mContext,
                    "DatabaseNotFound " + mSQLiteException.toString());
        }

        if (mCheckDataBase != null)
        {
            mCheckDataBase.close();
        }
        return mCheckDataBase != null;
    }

    private void copyDataBase() throws IOException
    {

        mLog.info(mContext, " Copying Database [" + mDatabaseName + "] to ["
                + mDBPath + mDBPath + "]");
        InputStream mInput = mContext.getAssets().open(mDatabaseName);
        String outFileName = mDBPath + mDatabaseName;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    private void copyDataBaseInChunks(int numchunks) throws IOException
    {
        AssetManager am = mContext.getAssets();
        File Path = mContext.getDir("Data", 0);
        File DBFile = new File(Path, mDatabaseName);
        OutputStream os = new FileOutputStream(DBFile);
        DBFile.createNewFile();
        byte[] b = new byte[1024];
        int i, r;
        String[] Files = am.list("");
        Arrays.sort(Files);
        for (i = 1; i < numchunks; i++)
        {
            InputStream is = mContext.getAssets().open(mDatabaseName);
            while ((r = is.read(b)) != -1)
                os.write(b, 0, r);
            is.close();
        }
        os.close();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
    {

    }

    public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy, int limit)
    {

        return mDatabase.query(table, columns, selection, selectionArgs,
                groupBy, having, orderBy, Integer.toString(limit));
    }

    public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy)
    {
        return mDatabase.query(table, columns, selection, selectionArgs,
                groupBy, having, orderBy);
    }

    @Override
    public synchronized void close()
    {
        if (mDatabase != null)
            mDatabase.close();
        super.close();
    }
}
