package com.canvascoders.opaper.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferance {
    //SharedPreferences file name
    private static String SHARED_PREFS_FILE_NAME = "com.canvascoders.opaper";


    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }


    //Strings
    public static void save(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }
}
