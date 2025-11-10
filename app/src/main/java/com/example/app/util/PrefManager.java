package com.example.app.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF = "app_prefs";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_USERNAME = "key_username";

    private final SharedPreferences prefs;

    public PrefManager(Context ctx) {
        prefs = ctx.getApplicationContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void saveUsername(String username) {
        prefs.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
