package com.svp.db.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Created by Pasha on 3/22/2016.
 */
public abstract class DatabaseOrmLiteHelper extends OrmLiteSqliteOpenHelper {

    private final String className;
    public DatabaseOrmLiteHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
        className = DatabaseOrmLiteHelper.class.getName();
    }


    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        Log.i(className, "onCreate");
        onTableCreate(db,connectionSource);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(className, "onUpgrade");
        onTableUpdate(db, connectionSource);
    }
    @Override
    public void close() {
        super.close();
//        simpleDao = null;
//        simpleRuntimeDao = null;
    }

    protected abstract void onTableCreate(SQLiteDatabase db,ConnectionSource connectionSource);
    protected abstract void onTableUpdate(SQLiteDatabase db,ConnectionSource connectionSource);

    public void vacuum() {
        new Thread(){
            @Override
            public void run(){
                SQLiteDatabase sqldb = getWritableDatabase();
                sqldb.execSQL("VACUUM");
            }
        }.start();
    }


}
