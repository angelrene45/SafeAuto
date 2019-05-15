package com.example.safeauto.Settings;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettings {

    public static final String SETTINGS_USER = "settings_user";
    public static final String KEY_STATUS_VIDEO_STREAM = "status_video";
    public static final String KEY_STATUS_ALARM = "status";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserSettings(Context context) {
        this.context = context;
        settings = context.getSharedPreferences(SETTINGS_USER, 0);
        editor = settings.edit();
    }

    public boolean getStatusAlarm(){
        return settings.getBoolean(KEY_STATUS_ALARM,false);
    }

    public void setStatusAlarm(Boolean value)
    {
        editor.putBoolean(KEY_STATUS_ALARM, value);
        editor.commit();
    }

    public boolean getStatusVideoStream(){
        return settings.getBoolean(KEY_STATUS_VIDEO_STREAM,false);
    }

    public void setStatusVideoStream(Boolean value)
    {
        editor.putBoolean(KEY_STATUS_VIDEO_STREAM, value);
        editor.commit();
    }


}
