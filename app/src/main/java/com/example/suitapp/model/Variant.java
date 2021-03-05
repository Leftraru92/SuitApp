package com.example.suitapp.model;

import android.database.Cursor;

import com.example.suitapp.database.DataDb;

import org.json.JSONException;
import org.json.JSONObject;

public class Variant {
    private int id;
    private Size size;
    private Color color;
    private int stock;

    public Variant(Color color, Size size, int stock) {
        this.color = color;
        this.size = size;
        this.stock = stock;
    }

    public Variant(JSONObject dataItem, int i) throws JSONException {
        id = dataItem.getInt("id");
        size = new Size(i, dataItem.getString("size"));
        stock = dataItem.getInt("stock");
        color = new Color(i, dataItem.getString("color"), dataItem.getString("hexCode"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public static class Color extends Item {
        private String hex;

        public Color(int id, String value, String hex) {
            super(id, value);
            this.hex = hex;
        }

        public Color(int id, String value) {
            super(id, value);
        }

        public Color(JSONObject dataItem) throws JSONException {
            int colourId = dataItem.getInt("colourId");
            String colourName = dataItem.getString("colourName");
            super.setId(colourId);
            super.setName(colourName);
            hex = dataItem.getString("colourHexCode");
        }

        public Color(Cursor c) {
            setId(Integer.parseInt(c.getString(c.getColumnIndex(DataDb.COL_ID))));
            setName(c.getString(c.getColumnIndex(DataDb.COL_NAME)));
            hex = c.getString(c.getColumnIndex(DataDb.COL_HEX));
        }

        public String getHex() {
            return hex;
        }

        public void setHex(String hex) {
            this.hex = hex;
        }
    }

    public static class Size extends Item{

        public Size(int id, String name) {
            super(id, name);
        }

        public Size(JSONObject dataItem) throws JSONException {
            super();
            super.setId(dataItem.getInt("sizeId"));
            super.setName(dataItem.getString("sizeName"));
        }

        public Size(Cursor c) {
            setId(Integer.parseInt(c.getString(c.getColumnIndex(DataDb.COL_ID))));
            setName(c.getString(c.getColumnIndex(DataDb.COL_NAME)));
        }

        public Size(int id) {
            super(id);
        }
    }
}
