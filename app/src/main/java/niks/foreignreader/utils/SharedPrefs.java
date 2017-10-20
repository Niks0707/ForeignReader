package niks.foreignreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import niks.foreignreader.BuildConfig;

public enum SharedPrefs {
    INSTANCE;

    public static final String STORAGE_NAME = BuildConfig.APPLICATION_ID + ".preferences";

    public enum Preference {
        APP_PREFERENCES_BOOK_PATH,
        APP_PREFERENCES_BOOK_PROGRESS,
        APP_PREFERENCES_TEXT_FONT,
        APP_PREFERENCES_TEXT_SIZE;
    }

    public void setProperty(Context context, Preference preference, String value) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
                .edit();
        editor.putString(preference.name(), value);
        editor.apply();
    }

    public String getProperty(Context context, Preference preference) {
        SharedPreferences preferences = context
                .getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(preference.name(), null);
    }

    public Map<String, ?> getAllProperties(Context context) {
        SharedPreferences preferences = context
                .getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        return preferences.getAll();
    }
}
