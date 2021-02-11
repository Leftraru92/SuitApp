package com.example.suitapp.dummy;

import com.example.suitapp.R;
import com.example.suitapp.model.Article;
import com.example.suitapp.model.ArticleGroup;

import java.util.ArrayList;
import java.util.List;

public class DummyArticlesGroup {

    public static final List<ArticleGroup> ITEMS = new ArrayList<>();

    static {
            addItems();
    }

    private static void addItems() {
        List<Article> articles =DummyArticles.ITEMS;
        List<Article> articles1 = new ArrayList<>();
        articles1.add(articles.get(0));
        articles1.add(articles.get(1));
        articles1.add(articles.get(2));
        articles1.add(articles.get(3));

        List<Article> articles2 = new ArrayList<>();
        articles2.add(articles.get(4));
        articles2.add(articles.get(5));
        articles2.add(articles.get(6));
        articles2.add(articles.get(7));

        int i = 0;
        ITEMS.add(new ArticleGroup(i++, "Artículos populares", articles2));
        ITEMS.add(new ArticleGroup(i++, "Relacionado con tus vistas en Camisas", articles1));
    }
}