package com.example.safeauto.Settings;

import android.content.Context;
import android.content.SharedPreferences;

public class UserData {
    public static final String SETTINGS_DATA_USER = "data_user";
    public static final String KEY_NAME= "name";
    public static final String KEY_EMAIL= "email";
    public static final String KEY_PHOTO= "photo";
    public static final String KEY_PHONE= "phone";
    public static final String KEY_TYPE= "type";
    public static final String KEY_MAC= "mac";
    public static final String KEY_PROVIDER= "provider";


    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserData(Context context) {
        this.context = context;
        settings = context.getSharedPreferences(SETTINGS_DATA_USER, 0);
        editor = settings.edit();
    }

    public void setUserData(String name, String email, String photo, String phone, String type,  String provider)
    {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHOTO, photo);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_TYPE, type);
        editor.putString(KEY_PROVIDER, provider);
        editor.commit();
    }

    public void cleanDataUser(){
        editor.clear();
    }

    public String getName(){
        return settings.getString(KEY_NAME,"");
    }
    public String getEmail(){
        return settings.getString(KEY_EMAIL,"");
    }
    public String getPhoto(){
        return settings.getString(KEY_PHOTO,"");
    }
    public String getPhone(){
        return settings.getString(KEY_PHONE,"");
    }
    public String getType(){
        return settings.getString(KEY_TYPE,"");
    }
    public String getProvider(){
        return settings.getString(KEY_PROVIDER,"");
    }

}
