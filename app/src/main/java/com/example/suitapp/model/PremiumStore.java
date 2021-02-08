package com.example.suitapp.model;

public class PremiumStore {
    public int id;
    public int image_url;
    public String title;
    public String description;

    public PremiumStore(int id, int image_url, String title, String description) {
        this.id = id;
        this.image_url = image_url;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getImage_url() {
        return image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
