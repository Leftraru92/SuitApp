package com.example.suitapp.dummy;

import com.example.suitapp.model.Article;
import com.example.suitapp.model.ArticleGroup;
import com.example.suitapp.model.Package;

import java.util.ArrayList;
import java.util.List;

public class DummyPackage {

    public static final List<Package> ITEMS = new ArrayList<>();

    static {
            addItems();
    }

    private static void addItems() {
        List<Article> articles =DummyArticles.ITEMS;
        List<Article> articles1 = new ArrayList<>();
        articles1.add(articles.get(0));
        articles1.add(articles.get(1));

        List<Article> articles2 = new ArrayList<>();
        articles2.add(articles.get(4));

        int i = 0;
        ITEMS.add(new Package(DummyStores.ITEMS.get(0), articles2));
        ITEMS.add(new Package(DummyStores.ITEMS.get(1), articles1));
    }
}