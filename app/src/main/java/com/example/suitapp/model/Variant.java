package com.example.suitapp.model;

public class Variant {
    private int id;
    private String size;
    private Color color;
    private int stock;

    public Variant(Color color, String size, int stock){
        this.color = color;
        this.size = size;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
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

        public Color(int id, String value, String hex){
            super(id, value);
            this.hex = hex;
        }

        public String getHex() {
            return hex;
        }

        public void setHex(String hex) {
            this.hex = hex;
        }
    }
}
