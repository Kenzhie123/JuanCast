package com.JuanCast.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationUtil {

    private static final String PREFS_NAME = "notification_prefs";
    private static final String KEY_HAS_NEW_ANNOUNCEMENT = "has_new_announcement";

    public static void saveNotificationState(Context context, boolean hasNewAnnouncement) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_HAS_NEW_ANNOUNCEMENT, hasNewAnnouncement);
        editor.apply();
    }

    public static boolean getNotificationState(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_HAS_NEW_ANNOUNCEMENT, false);
    }
}



