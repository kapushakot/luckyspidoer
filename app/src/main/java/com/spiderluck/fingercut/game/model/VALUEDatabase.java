package com.spiderluck.fingercut.game.model;

import android.content.Context;
import android.content.SharedPreferences;

public class VALUEDatabase {
    private static String stringId = "stringid";
    private SharedPreferences preferences;

    public VALUEDatabase(Context context){
        String NAME = "dataspi";
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public void VALUESETING(String id){
        preferences.edit().putString(VALUEDatabase.stringId, id).apply();
    }

    public String VALUESETING(){
        return preferences.getString(stringId, "");
    }

}
