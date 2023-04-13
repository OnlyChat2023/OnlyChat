package com.example.onlychat.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.onlychat.Model.UserModel;
import com.google.gson.Gson;

public class GlobalPreferenceManager {

    private Context mContext;
    private SharedPreferences pref;

    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String LOGIN_USERNAME = "LOGIN_USERNAME";
    private static final String LOGIN_REMEMBER = "LOGIN_REMEMBER";
    private static final String LOGIN_TOKEN = "LOGIN_TOKEN";

    private static final String SERVER_ROOT = "SERVER_ROOT";

    private static final String LOGIN_USERMODEL = "LOGIN_USERMODEL";
    private static final String IS_FIRST_TIME = "IS_FIRST_TIME_v1_4";

    public GlobalPreferenceManager(Context context) {
        this.mContext = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveUser(UserModel user) {
        String jsonObject = new Gson().toJson(user);
        pref.edit().putString(LOGIN_USERMODEL, jsonObject).apply();
    }

    public void Login(UserModel user, Boolean remember) {
        saveUser(user);
        pref.edit().putString(LOGIN_TOKEN, user.getToken()).apply();
        pref.edit().putBoolean(LOGIN_REMEMBER, remember).apply();
        pref.edit().putString(LOGIN_USERNAME, user.getUsername()).apply();
        pref.edit().putBoolean(IS_LOGIN, true).apply();
    }

    public Boolean getIsLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getUserToken() {
        return pref.getString(LOGIN_TOKEN, "");
    }

    public void SignOut() {
        pref.edit().remove(LOGIN_USERNAME).apply();
        pref.edit().remove(LOGIN_TOKEN).apply();
        pref.edit().remove(LOGIN_USERMODEL).apply();
        pref.edit().remove(LOGIN_USERMODEL).apply();
        pref.edit().remove(IS_LOGIN).apply();
    }

    public void ValidateRemember() {
        if (!pref.getBoolean(LOGIN_REMEMBER, false))
            SignOut();
    }
}
