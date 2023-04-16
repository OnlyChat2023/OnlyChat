package com.example.onlychat.Interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface HttpResponse {
    void onSuccess(JSONObject response) throws JSONException, InterruptedException;
    void onError(String error);
}