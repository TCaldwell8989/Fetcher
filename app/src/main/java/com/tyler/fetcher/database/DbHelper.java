package com.tyler.fetcher.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tyler.fetcher.database.DbSchema.DogParkTable;

public class DbHelper extends SQLiteOpenHelper {

    // Constants for DB name and version
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "fetcherDataBase.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Create Table and Columns
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DogParkTable.NAME + "(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DogParkTable.Cols.UUID + ", " +
                DogParkTable.Cols.NAME + " TEXT, " +
                DogParkTable.Cols.LOCATION + " TEXT, " +
                DogParkTable.Cols.NOTE + " TEXT, " +
                DogParkTable.Cols.RATING + " INTEGER, " +
                DogParkTable.Cols.CREATED + " TEXT default CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
