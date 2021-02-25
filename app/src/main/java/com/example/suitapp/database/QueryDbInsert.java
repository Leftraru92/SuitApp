package com.example.suitapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.util.Constants;

import java.util.List;

public class QueryDbInsert {

    public static boolean insertGender(Context context, List<Gender> genders) {

        HelpDb helpDb = HelpDb.getInstance(context);
        SQLiteDatabase db = helpDb.getWritableDatabase();
        int i = 0;

        //Elimino los registros existentes
        db.delete(DataDb.TABLE_GENDERS, null, null);

        //Inserto los nuevos
        for (Gender gender : genders) {
            ContentValues values = DataDb.getValores(gender);
            try {
                db.insertOrThrow(DataDb.TABLE_GENDERS, null, values);
                i++;
            } catch (Exception ex) {
                return false;
            } finally {
                //ayudaDb.close();
            }
        }
        Log.d(Constants.LOG, "Se insertaron " + i + " géneros");
        return true;
    }

    public static boolean insertCategories(Context context, List<Category> categories) {

        HelpDb helpDb = HelpDb.getInstance(context);
        SQLiteDatabase db = helpDb.getWritableDatabase();
        int i = 0;

        //Elimino los registros existentes
        db.delete(DataDb.TABLE_CATEGORIES, null, null);

        //Inserto los nuevos
        for (Category category : categories) {
            ContentValues values = DataDb.getValores(category);
            try {
                db.insertOrThrow(DataDb.TABLE_CATEGORIES, null, values);
                i++;
            } catch (Exception ex) {
                return false;
            } finally {
                //ayudaDb.close();
            }
        }
        Log.d(Constants.LOG, "Se insertaron " + i + " categorías");
        return true;
    }

}
