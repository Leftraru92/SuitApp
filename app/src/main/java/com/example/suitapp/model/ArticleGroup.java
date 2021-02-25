package com.example.suitapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    public ArticleGroup(JSONObject dataItem) throws JSONException {
        id = dataItem.getInt("groupId");
        title = dataItem.getString("title");
        articleList = new ArrayList<>();

        JSONArray articlesArray = dataItem.getJSONArray("articulos");
        for (int i = 0; i < articlesArray.length(); i++) {
            JSONObject articleItem = articlesArray.getJSONObject(i);
            articleList.add(new Article(articleItem));
        }
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
