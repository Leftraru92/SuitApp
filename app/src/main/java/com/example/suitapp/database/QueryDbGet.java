package com.example.suitapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QueryDbGet {

    public static Cursor getGenders(Context context) {

        HelpDb helpDb = HelpDb.getInstance(context);
        SQLiteDatabase db = helpDb.getReadableDatabase();

        Cursor c = db.query(DataDb.TABLE_GENDERS, DataDb.getProjectionGender(), null, null, null, null, null);
        return c;
    }

    public static Cursor getCategories(Context context) {

        HelpDb helpDb = HelpDb.getInstance(context);
        SQLiteDatabase db = helpDb.getReadableDatabase();

        Cursor c = db.query(DataDb.TABLE_CATEGORIES, DataDb.getProjectionCat(), null, null, null, null, null);
        return c;
    }

    public static Cursor getColors(Context context) {

        HelpDb helpDb = HelpDb.getInstance(context);
        SQLiteDatabase db = helpDb.getReadableDatabase();

        Cursor c = db.query(DataDb.TABLE_COLOR, DataDb.getProjectionColor(), null, null, null, null, null);
        return c;
    }

    public static Cursor getColorHexById(Context context, int id) {
        HelpDb helpDb = HelpDb.getInstance(context);
        SQLiteDatabase db = helpDb.getReadableDatabase();

        Cursor c = db.query(DataDb.TABLE_COLOR, DataDb.getProjectionColor(), DataDb.COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        return c;
    }

    public static Cursor getSizes(Context context) {

        HelpDb helpDb = HelpDb.getInstance(context);
        SQLiteDatabase db = helpDb.getReadableDatabase();

        Cursor c = db.query(DataDb.TABLE_SIZES, DataDb.getProjectionSize(), null, null, null, null, null);
        return c;
    }

    public static Cursor getPreviusSearch(Context context) {
        HelpDb helpDb = HelpDb.getInstance(context);
        SQLiteDatabase db = helpDb.getReadableDatabase();

        Cursor c = db.query(DataDb.TABLE_SEARCH, DataDb.getProjectionSearch(), null, null, null, null, "rowid DESC");
        return c;
    }
}
