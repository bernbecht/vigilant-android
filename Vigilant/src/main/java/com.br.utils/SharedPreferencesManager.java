package com.br;

import android.content.Context;
import android.content.SharedPreferences;

import com.br.vigilant.MapActivity;

/**
 * Created by Berhell on 18/07/14.
 */
public class SharedPreferencesManager {

    public static final String PREFS_NAME = "VigilantPrefs";
    public static final String PREF_LOGGED = "logged";

    public static boolean isLogged(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(PREF_LOGGED, false);
    }

    public static void setIsLogged(Context context, boolean status){
        SharedPreferences settings = context.getSharedPreferences(SharedPreferencesManager.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREF_LOGGED, status);
        editor.commit();
    }

}
