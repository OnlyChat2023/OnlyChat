package com.example.onlychat.Interfaces;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public interface HttpResponse {
    void onSuccess(JSONObject response) throws JSONException, InterruptedException, ParseException;
    void onError(String error);
}