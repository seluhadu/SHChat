package com.seluhadu.shchat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import android.text.TextUtils;


public class PreferenceUtils {
    private static PreferenceUtils mInstance;
    private final SharedPreferences mPreferences;

    private PreferenceUtils(@NonNull final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtils getInstance(@NonNull final Context context) {
        if (mInstance == null) {
            mInstance = new PreferenceUtils(context.getApplicationContext());
        }
        return mInstance;
    }


    public void set(@NonNull String key, @NonNull Object value) {
        if (TextUtils.isEmpty(key)) {
            throw new NullPointerException(String.format("Key and value can not be null key=%s, value=%s", key, value));
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            throw new IllegalArgumentException(String.format("Type of value unsupported key=%s, value=%s", key, value));
        }
        editor.apply();
    }
}
