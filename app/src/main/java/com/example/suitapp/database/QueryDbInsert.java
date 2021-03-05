package com.example.suitapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.suitapp.fragment.SearchFragment;
import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Variant;
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

    public static boolean insertColor(Context context, List<Variant.Color> colorList) {
        HelpDb helpDb = HelpDb.getInstance(context);
        SQLiteDatabase db = helpDb.getWritableDatabase();
        int i = 0;

        //Elimino los registros existentes
        db.delete(DataDb.TABLE_COLOR, null, null);

        //Inserto los nuevos
        for (Variant.Color color : colorList) {
            ContentValues values = DataDb.getValores(color);
            try {
                db.insertOrThrow(DataDb.TABLE_COLOR, null, values);
                i++;
            } catch (Exception ex) {
                return false;
            } finally {
                //ayudaDb.close();
            }
        }
        Log.d(Constants.LOG, "Se insertaron " + i + " colores");
        return true;
    }

    public static boolean insertSize(Context context, List<Variant.Size> sizeList) {
        HelpDb helpDb = HelpDb.getInstance(context);
        SQLiteDatabase db = helpDb.getWritableDatabase();
        int i = 0;

        //Elimino los registros existentes
        db.delete(DataDb.TABLE_SIZES, null, null);

        //Inserto los nuevos
        for (Variant.Size item : sizeList) {
            ContentValues values = DataDb.getValores(item);
            try {
                db.insertOrThrow(DataDb.TABLE_SIZES, null, values);
                i++;
            } catch (Exception ex) {
                return false;
            } finally {
                //ayudaDb.close();
            }
        }
        Log.d(Constants.LOG, "Se insertaron " + i + " talles");
        return true;
    }

    public static void insertSearch(Context context, String query) {
        HelpDb helpDb = HelpDb.getInstance(context);
        SQLiteDatabase db = helpDb.getWritableDatabase();

        ContentValues values = DataDb.getValoresSearch(query);
        try {
            db.insertOrThrow(DataDb.TABLE_SEARCH, null, values);
        } catch (Exception ex) {
        } finally {
            //ayudaDb.close();
        }
    }
}
