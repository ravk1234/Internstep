package com.internstep.user;

import android.content.Context;
import android.content.SharedPreferences;

public class IntroPref  {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Context context;
    int Private_Mode = 0;
    private static  final String Pref_name ="xyz";
    private static  final String Is_first_time ="firstTime";
    //private static final String  profile ="complete";


    public IntroPref(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(Pref_name,Private_Mode);
        editor = preferences.edit();
    }

    public void  setIsfirstTimeLaunch(boolean isfirstTimeLaunch){
        editor.putBoolean(Is_first_time,isfirstTimeLaunch);
        editor.commit();
    }

    public boolean isFirstTimeLaunch(){
        return preferences.getBoolean(Is_first_time,true);
    }
}

