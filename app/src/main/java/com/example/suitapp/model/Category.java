package com.example.suitapp.model;

import android.database.Cursor;

import com.example.suitapp.database.DataDb;

import org.json.JSONException;
import org.json.JSONObject;

public class Category extends Item{
    private int visits;

    public Category(int id, String name, int visits) {
        super(id, name);
        this.visits = visits;
    }

    public Category(JSONObject dataItem) throws JSONException {
        super();
        super.setId(dataItem.getInt("idCategory"));
        super.setName(dataItem.getString("nameCategory"));
        visits = dataItem.getInt("visits");
    }

    public Category(Cursor c) {
        setId(Integer.parseInt(c.getString(c.getColumnIndex(DataDb.COL_ID))));
        setName(c.getString(c.getColumnIndex(DataDb.COL_NAME)));
        visits = Integer.parseInt(c.getString(c.getColumnIndex(DataDb.COL_VISITS)));
    }

    public int getVisits() {
        return visits;
    }
}
