package com.xp.music.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SUtils {
    private final SharedPreferences preferences;
    public SUtils(Context context){
        preferences =context.getSharedPreferences("data",Context.MODE_PRIVATE);
    }
    public void setString(String key,String value){
        preferences.edit().putString(key, value).apply();
    }
    public String getString(String key){
       return preferences.getString(key,"0");
    }
}
