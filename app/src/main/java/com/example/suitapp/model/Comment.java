package com.example.suitapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
    int id;
    String question;
    String user;
    String answer;
    String date;
    boolean owner;

    public Comment(JSONObject dataItem) throws JSONException {
        id = dataItem.getInt("questionId");
        question = dataItem.getString("questions");
        user = dataItem.getString("user_name");
        owner = dataItem.getBoolean("isOwner");

        if (dataItem.has("answer") && dataItem.getString("answer") != null && !dataItem.getString("answer").equals("null"))
            answer = dataItem.getString("answer");

        if (dataItem.has("dateAnswer") && dataItem.getString("dateAnswer") != null && !dataItem.getString("dateAnswer").equals("null"))
            date = dataItem.getString("dateAnswer");
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getUser() {
        return user;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDate() {
        return date;
    }

    public boolean isOwner() {
        return owner;
    }
}
