package com.example.suitapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelpDb extends SQLiteOpenHelper {

    private static HelpDb helpDb;
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "SuitApp.db";


    private HelpDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DataDb.SQL_CREATE_TABLE_GENDERS);
        db.execSQL(DataDb.SQL_CREATE_TABLE_CATEGORIES);
        db.execSQL(DataDb.SQL_CREATE_TABLE_COLOR);
        db.execSQL(DataDb.SQL_CREATE_TABLE_SIZE);
        db.execSQL(DataDb.SQL_CREATE_TABLE_SEARCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DataDb.SQL_DELETE_ENTRIES_GENDERS);
        db.execSQL(DataDb.SQL_DELETE_ENTRIES_CATEGORIES);
        db.execSQL(DataDb.SQL_DELETE_ENTRIES_COLOR);
        db.execSQL(DataDb.SQL_DELETE_ENTRIES_SIZE);
        db.execSQL(DataDb.SQL_DELETE_ENTRIES_SEARCH);
        onCreate(db);
    }

    public static HelpDb getInstance(Context context){
        if(helpDb == null)
            helpDb = new HelpDb(context);
        return helpDb;
    }
}
