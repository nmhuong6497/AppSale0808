package com.example.appsale.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class AppCache {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static AppCache instance = null;

    private AppCache(Context context) {
        sharedPreferences = context.getSharedPreferences("app_cache", Context.MODE_PRIVATE);
    }

    public static AppCache getInstance(Context context) {
        if (instance == null) {
            instance = new AppCache(context);
        }
        return instance;
    }

    public void saveDataString(String key, String value) {
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getDataString (String key) {
        return sharedPreferences.getString(key, null);
    }

    public void clearCache() {
        sharedPreferences.edit().clear().commit();
    }
}
