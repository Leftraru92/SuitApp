package com.example.suitapp.model;

import java.util.List;

public class ArticleGroup {
    int id;
    String title;
    List<Article> articleList;

    public ArticleGroup(int id, String title, List<Article> articleList) {
        this.id = id;
        this.title = title;
        this.articleList = articleList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
