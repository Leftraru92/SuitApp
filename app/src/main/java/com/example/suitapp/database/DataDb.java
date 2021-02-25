package com.example.suitapp.database;

import android.content.ContentValues;
import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;

public class DataDb {

    public static final String TABLE_GENDERS = "Genders";
    public static final String TABLE_CATEGORIES = "Categories";

    public static final String COL_ID = "Id";
    public static final String COL_NAME = "Name";
    public static final String COL_VISITS = "Visits";
    public static final String COL_IMAGE = "Image";

    protected static final String TEXT_TYPE = " TEXT ";
    protected static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_TABLE_GENDERS = "CREATE TABLE " + TABLE_GENDERS +
            " (" + COL_ID + TEXT_TYPE + COMMA_SEP +
            COL_NAME + TEXT_TYPE + COMMA_SEP +
            COL_IMAGE + TEXT_TYPE + " )";

    public static final String SQL_CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES +
            " (" + COL_ID + TEXT_TYPE + COMMA_SEP +
            COL_NAME + TEXT_TYPE + COMMA_SEP +
            COL_VISITS + TEXT_TYPE + " )";


    public static final String SQL_DELETE_ENTRIES_GENDERS = "DROP TABLE IF EXISTS " + TABLE_GENDERS;
    public static final String SQL_DELETE_ENTRIES_CATEGORIES = "DROP TABLE IF EXISTS " + TABLE_CATEGORIES;

    //get values to insert
    public static ContentValues getValores(Gender gender) {
        ContentValues valores = new ContentValues();
        valores.put(COL_ID, gender.getId());
        valores.put(COL_NAME, gender.getName());
        valores.put(COL_IMAGE, gender.getImage());
        return valores;
    }

    public static ContentValues getValores(Category category) {
        ContentValues valores = new ContentValues();
        valores.put(COL_ID, category.getId());
        valores.put(COL_NAME, category.getName());
        valores.put(COL_VISITS, category.getVisits());
        return valores;
    }

    //values to get
    public static String[] getProjectionGender() {
        return new String[]{ COL_ID, COL_NAME, COL_IMAGE };
    }

    public static String[] getProjectionCat() {
        return new String[]{ COL_ID, COL_NAME, COL_VISITS };
    }

}
