package com.example.suitapp.util;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

import static android.content.Context.MODE_PRIVATE;

public class SingletonUser {
    private static SingletonUser instance;
    private Context context;
    private String email;
    private String name;
    private String uri;
    private String deviceId;
    private String hash;

    private SingletonUser(Context context) {
        this.context = context;
    }

    public static SingletonUser getInstance(Context context) {
        if (instance == null)
            instance = new SingletonUser(context);
        return instance;
    }

    public boolean isLogued() {
        if (email == null)
            email = context.getSharedPreferences("_", MODE_PRIVATE).getString("USER_EMAIL", "");
        return (email != "");
    }

    public String getEmail() {
        if (email == null)
            email = context.getSharedPreferences("_", MODE_PRIVATE).getString("USER_EMAIL", "");
        return email;
    }

    public String getName() {
        if (name == null)
            name = context.getSharedPreferences("_", MODE_PRIVATE).getString("USER_NAME", "");
        return name;
    }

    public String getUri() {
        if (uri == null)
            uri = context.getSharedPreferences("_", MODE_PRIVATE).getString("USER_PHOTO", "");
        return uri;
    }

    public String getDeviceId() {
        if (deviceId == null  )
            deviceId = context.getSharedPreferences("_", MODE_PRIVATE).getString("DEVICE_ID", "");
        return deviceId;
    }

    public String getHash() {
        if (hash == null)
            hash = context.getSharedPreferences("_", MODE_PRIVATE).getString("HASH", "");
        return hash;
    }

    public void setData(FirebaseUser account) {
        this.name = account.getDisplayName();
        this.email = account.getEmail();
        this.uri = account.getPhotoUrl().toString();

        context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("USER_NAME", name).apply();
        context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("USER_EMAIL", email).apply();
        context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("USER_PHOTO", uri).apply();
    }

    public void setTokenDevice(String fbToken) {
        this.deviceId = fbToken;
        context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("DEVICE_ID", deviceId).apply();
    }

    public void setHash(String hash) {
        this.hash = hash;
        context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("HASH", hash).apply();
    }

    public void clearData() {
        this.name = "";
        this.email = "";
        this.uri = "";
        this.deviceId = "";

        context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("USER_NAME", "").apply();
        context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("USER_EMAIL", "").apply();
        context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("USER_PHOTO", "").apply();
        context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("DEVICE_ID", "").apply();
        context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("HASH", "").apply();
    }
}
