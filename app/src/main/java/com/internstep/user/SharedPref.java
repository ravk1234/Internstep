package com.internstep.user;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Context context;
    int Private_Mode = 0;
    private static  final String Pref_name ="Profile_check";
    private static  final String Is_first_time ="complete";


    public SharedPref(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(Pref_name,Private_Mode);
        editor = preferences.edit();
    }

    public void  setIsProfile(boolean isfirstTimeLaunch){
        editor.putBoolean(Is_first_time,isfirstTimeLaunch);
        editor.commit();
    }

    public boolean isComplete() {
        return preferences.getBoolean(Is_first_time, false);
    }

}
