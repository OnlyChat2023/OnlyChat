package com.example.onlychat.Interfaces;

import org.json.JSONObject;

public interface HttpResponse {
    void onSuccess(JSONObject response);
    void onError(String error);
}