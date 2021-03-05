package com.example.suitapp.database;

import android.content.ContentValues;
import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Variant;

public class DataDb {

    public static final String TABLE_GENDERS = "Genders";
    public static final String TABLE_CATEGORIES = "Categories";
    public static final String TABLE_COLOR = "Color";
    public static final String TABLE_SIZES = "Size";
    public static final String TABLE_SEARCH = "Search";

    public static final String COL_ID = "Id";
    public static final String COL_NAME = "Name";
    public static final String COL_VISITS = "Visits";
    public static final String COL_IMAGE = "Image";
    public static final String COL_HEX = "Hex";

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

    public static final String SQL_CREATE_TABLE_COLOR = "CREATE TABLE " + TABLE_COLOR +
            " (" + COL_ID + TEXT_TYPE + COMMA_SEP +
            COL_NAME + TEXT_TYPE + COMMA_SEP +
            COL_HEX + TEXT_TYPE + " )";

    public static final String SQL_CREATE_TABLE_SIZE = "CREATE TABLE " + TABLE_SIZES +
            " (" + COL_ID + TEXT_TYPE + COMMA_SEP +
            COL_NAME + TEXT_TYPE + " )";

    public static final String SQL_CREATE_TABLE_SEARCH = "CREATE TABLE " + TABLE_SEARCH +
            " (" + COL_NAME + TEXT_TYPE + " )";


    public static final String SQL_DELETE_ENTRIES_GENDERS = "DROP TABLE IF EXISTS " + TABLE_GENDERS;
    public static final String SQL_DELETE_ENTRIES_CATEGORIES = "DROP TABLE IF EXISTS " + TABLE_CATEGORIES;
    public static final String SQL_DELETE_ENTRIES_COLOR = "DROP TABLE IF EXISTS " + TABLE_COLOR;
    public static final String SQL_DELETE_ENTRIES_SIZE = "DROP TABLE IF EXISTS " + TABLE_SIZES;
    public static final String SQL_DELETE_ENTRIES_SEARCH = "DROP TABLE IF EXISTS " + TABLE_SEARCH;

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

    public static ContentValues getValores(Variant.Color color) {
        ContentValues valores = new ContentValues();
        valores.put(COL_ID, color.getId());
        valores.put(COL_NAME, color.getName());
        valores.put(COL_HEX, color.getHex());
        return valores;
    }

    public static ContentValues getValores(Variant.Size size) {
        ContentValues valores = new ContentValues();
        valores.put(COL_ID, size.getId());
        valores.put(COL_NAME, size.getName());
        return valores;
    }

    public static ContentValues getValoresSearch(String query) {
        ContentValues valores = new ContentValues();
        valores.put(COL_NAME, query);
        return valores;
    }

    //values to get
    public static String[] getProjectionGender() {
        return new String[]{ COL_ID, COL_NAME, COL_IMAGE };
    }

    public static String[] getProjectionCat() {
        return new String[]{ COL_ID, COL_NAME, COL_VISITS };
    }

    public static String[] getProjectionColor() {
        return new String[]{ COL_ID, COL_NAME, COL_HEX };
    }

    public static String[] getProjectionSize() {
        return new String[]{ COL_ID, COL_NAME };
    }

    public static String[] getProjectionSearch() { return new String[] {COL_NAME}; }
}
