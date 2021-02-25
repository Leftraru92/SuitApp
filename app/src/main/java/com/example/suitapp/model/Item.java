package com.example.suitapp.model;

public abstract class Item {
    private int id;
    private String name;

    public Item(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Item() {

    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
