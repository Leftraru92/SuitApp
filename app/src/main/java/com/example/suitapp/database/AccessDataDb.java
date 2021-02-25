package com.example.suitapp.database;

import android.content.Context;
import android.database.Cursor;

import com.example.suitapp.model.Category;
import com.example.suitapp.model.Gender;
import com.example.suitapp.model.Item;
import com.example.suitapp.util.Constants;

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

    public List<Item> getGenders(boolean itemList) {
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
}
