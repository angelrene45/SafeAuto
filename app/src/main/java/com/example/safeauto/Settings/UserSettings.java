package com.example.safeauto.Settings;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettings {

    public static final String SETTINGS_ALARMA = "status_alarma";
    public static final String KEY_STATUS = "status";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserSettings(Context context) {
        this.context = context;
        settings = context.getSharedPreferences(SETTINGS_ALARMA, 0);
        editor = settings.edit();
    }

    public boolean getStatusAlarm(){
        return settings.getBoolean(KEY_STATUS,false);
    }

    public void setStatusAlarm(Boolean value)
    {
        editor.putBoolean(KEY_STATUS, value);
        editor.commit();
    }


}
