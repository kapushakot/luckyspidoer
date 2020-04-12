package com.spiderluck.fingercut.game.model;

import android.content.Context;
import android.content.SharedPreferences;

public class Database {
    private static String id = "id";
    private SharedPreferences preferences;

    public Database(Context context){
        String NAME = "spiderpidor";
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public void id(String id){
        preferences.edit().putString(Database.id, id).apply();
    }

    public String id(){
        return preferences.getString(id, "");
    }

}
