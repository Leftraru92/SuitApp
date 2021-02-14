package com.example.suitapp.model;

public class Province {
    private int id;
    private String name;

    public Province(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
