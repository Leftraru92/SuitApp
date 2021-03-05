package com.example.suitapp.database;

import android.content.Context;
import android.database.Cursor;

import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Item;
import com.example.suitapp.model.Variant;

import java.util.ArrayList;
import java.util.List;

public class AccessDataDb {
    static AccessDataDb accessDataDb;
    Context context;

    private AccessDataDb(Context context) {
        this.context = context;
    }

    public static AccessDataDb getInstance(Context context) {
        if (accessDataDb == null)
            accessDataDb = new AccessDataDb(context);
        return accessDataDb;
    }

    public List<Gender> getGenders() {
        List<Gender> genders = new ArrayList<>();
        Cursor c = QueryDbGet.getGenders(context);

        //Genero la lista de géneros
        try {
            while (c.moveToNext()) {
                Gender gender = new Gender(c);
                genders.add(gender);
            }
        } finally {
            c.close();
            return genders;
        }
    }

    public List<Item> getGendersItems() {
        List<Item> gendersItem = new ArrayList<>();
        Cursor c = QueryDbGet.getGenders(context);

        //Genero la lista de géneros
        try {
            while (c.moveToNext()) {
                Gender gender = new Gender(c);
                gendersItem.add(gender);
            }
        } finally {
            c.close();
            return gendersItem;
        }
    }

    public List<Item> getCategories() {
        List<Item> categories = new ArrayList<>();
        Cursor c = QueryDbGet.getCategories(context);

        //Genero la lista de géneros
        try {
            while (c.moveToNext()) {
                Category category = new Category(c);
                categories.add(category);
            }
        } finally {
            c.close();
            return categories;
        }
    }

    public List<Variant.Color> getColors() {
        List<Variant.Color> colors = new ArrayList<>();
        Cursor c = QueryDbGet.getColors(context);

        //Genero la lista de géneros
        try {
            while (c.moveToNext()) {
                Variant.Color color = new Variant.Color(c);
                colors.add(color);
            }
        } finally {
            c.close();
            return colors;
        }
    }

    public List<Item> getColorsItems() {
        List<Item> colors = new ArrayList<>();
        Cursor c = QueryDbGet.getColors(context);

        //Genero la lista de géneros
        try {
            while (c.moveToNext()) {
                Variant.Color color = new Variant.Color(c);
                colors.add(color);
            }
        } finally {
            c.close();
            return colors;
        }
    }

    public String getColorHexById(int id) {
        String hex = "";
        Cursor c = QueryDbGet.getColorHexById(context, id);

        if (c.getCount() > 0) {
            c.moveToFirst();
            hex = c.getString(c.getColumnIndex(DataDb.COL_HEX));
        }
        return hex;
    }

    public List<Item> getSizes() {
        List<Item> sizes = new ArrayList<>();
        Cursor c = QueryDbGet.getSizes(context);

        //Genero la lista de géneros
        try {
            while (c.moveToNext()) {
                Variant.Size size = new Variant.Size(c);
                sizes.add(size);
            }
        } finally {
            c.close();
            return sizes;
        }
    }

    public List<String> getPreviusSearch() {
        List<String> searchList = new ArrayList<>();
        Cursor c = QueryDbGet.getPreviusSearch(context);

        //Genero la lista de géneros
        try {
            while (c.moveToNext()) {
                searchList.add(c.getString(c.getColumnIndex(DataDb.COL_NAME)));
            }
        } finally {
            c.close();
            return searchList;
        }
    }
}
