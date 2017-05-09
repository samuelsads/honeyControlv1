package com.example.sads.honeycontrol.Utils;

import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by sads on 26/03/17.
 */
public class Util {



    public static boolean isValidText(String text){
        return text.length()> 0;
    }

    public static  String getUserPrefs(SharedPreferences preferences){
        return preferences.getString("user","");
    }

    public static String getPassPrefs(SharedPreferences preferences){
        return preferences.getString("password","");
    }

    public static int getIdPrefs(SharedPreferences preferences){
        return preferences.getInt("id",0);
    }

    public static void removeSharedPreferences(SharedPreferences preferences){
        SharedPreferences.Editor editor= preferences.edit();
        editor.remove("user");
        editor.remove("password");
        editor.remove("id");
        editor.apply();

    }

    public static String formatName(String name, String father_surname, String mother_surname){
        String complet_name="";
        if(!TextUtils.isEmpty(name)){
            if(!TextUtils.isEmpty(father_surname)){
                complet_name  = name+" "+father_surname;
                if(!TextUtils.isEmpty(mother_surname)){
                    complet_name = complet_name+ " "+mother_surname;
                }
            }
        }
        return complet_name;
    }
}
