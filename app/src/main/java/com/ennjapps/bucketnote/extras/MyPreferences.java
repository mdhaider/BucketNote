package com.ennjapps.bucketnote.extras;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by haider on 07-05-2016.
 */
public class MyPreferences {
    Context _context;
    SharedPreferences pref;
    private static final String PREF_NAME="DailyFortune";
    int MODE_PRIVATE=0;
    SharedPreferences.Editor editor;
    private static final String IS_FIRSTTIME="IsFirstTime";
    private static final String UserName="name";

    public MyPreferences(Context context){
        this._context= context;
        pref=_context.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        editor= pref.edit();

    }
    public boolean isFirstTime(){
        return pref.getBoolean(IS_FIRSTTIME,true);
    }
    public void setOld(boolean b){
        if(b){
            editor.putBoolean(IS_FIRSTTIME,false);
            editor.commit();
        }
    }
    public String getUsername(){
        return pref.getString(UserName, "");
    }
    public void setUsername(String name){
        editor.putString(UserName,name);
        editor.commit();

    }
}
