package com.example.suitapp.model;

import android.content.Context;
import android.database.Cursor;

import com.example.suitapp.R;
import com.example.suitapp.database.DataDb;

import org.json.JSONException;
import org.json.JSONObject;

public class Gender extends Item{
    private int image;
    private int visits;

    public Gender(int id, String name, int image, int visits) {
        super(id, name);
        this.image = image;
        this.visits = visits;
    }

    public Gender(JSONObject dataItem) throws JSONException {
        super();
        super.setId(dataItem.getInt("idGender"));
        super.setName(dataItem.getString("nameGender"));
        switch (getName()){
            case "Hombres":
                image = R.drawable.card_hombre;
                break;
            case "Mujeres":
                image = R.drawable.card_mujer;
                break;
            case "Niños":
                image = R.drawable.card_nino;
                break;
            case "Niñas":
                image = R.drawable.card_nina;
                break;
            case "Bebés":
                image = R.drawable.card_bebe;
                break;
            case "Sin género":
                image = R.drawable.card_unisex;
                break;
            default:
                break;
        }
    }

    public Gender(Cursor c) {
        setId(Integer.parseInt(c.getString(c.getColumnIndex(DataDb.COL_ID))));
        setName(c.getString(c.getColumnIndex(DataDb.COL_NAME)));
        image = Integer.parseInt(c.getString(c.getColumnIndex(DataDb.COL_IMAGE)));
    }

    public int getImage() {
        return image;
    }

    public int getVisits() {
        return visits;
    }
}
