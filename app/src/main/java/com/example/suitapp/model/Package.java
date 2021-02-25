package com.example.suitapp.model;

import java.util.List;

public class Package {
    private Store store;
    private List<Article> articleList;

    public Package(Store store, List<Article> articles) {
        this.store = store;
        this.articleList = articles;
    }

    public Store getStore() {
        return store;
    }

    public List<Article> getArticleList() {
        return articleList;
    }
}
