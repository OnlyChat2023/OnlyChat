package com.example.onlychat.Manager;

import android.content.Context;
import android.preference.PreferenceManager;

public class GlobalPreferenceManager {

    private Context mContext;
    private PreferenceManager pref;

    public GlobalPreferenceManager(Context context) {
        this.mContext = context;
        pref = (PreferenceManager) PreferenceManager.getDefaultSharedPreferences(context);
    }
}
