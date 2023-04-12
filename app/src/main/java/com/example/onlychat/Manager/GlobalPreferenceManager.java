package com.example.onlychat.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GlobalPreferenceManager {

    private Context mContext;
    private SharedPreferences pref;

    public GlobalPreferenceManager(Context context) {
        this.mContext = context;
        pref = (SharedPreferences) PreferenceManager.getDefaultSharedPreferences(context);
    }
}
