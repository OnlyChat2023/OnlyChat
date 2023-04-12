package com.example.onlychat.Manager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlychat.Interfaces.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpManager {
    private Context context;
    private GlobalPreferenceManager pref;

    public  HttpManager(Context _context) {
        this.context = _context;
        this.pref = new GlobalPreferenceManager(_context);
    }

    public void createRequest(String url, int method, String tag, Map<String, String> postParam, HttpResponse httpResponse) {
        RequestQueue queue = Volley.newRequestQueue(this.context);

        JSONObject jsonRequest = null;

        if (postParam != null)
            jsonRequest = new JSONObject(postParam);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url, jsonRequest,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    httpResponse.onSuccess(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    httpResponse.onError(error.getMessage()==null?error.toString():error.getMessage());
                }
            }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        jsonObjReq.setTag(tag);
        queue.add(jsonObjReq);
    }

    public void getUserProfile(){
        createRequest("http://192.168.2.58:5000/api/onlychat/v1/user/userProfile",Request.Method.GET,"userprofile", null,
        new HttpResponse(){
            @Override
            public void onSuccess(JSONObject Response) {
                try{
                    JSONObject information = Response.getJSONObject("data").getJSONObject("user");
                    Log.i("_id",information.getString("_id"));
                    Log.i("username",information.getString("username"));
                    Log.i("password",information.getString("password"));
                    Log.i("name",information.getString("name"));
                    Log.i("avatar",information.getString("avatar"));
                    Log.i("email",information.getString("email"));
                    Log.i("phone",information.getString("phone"));
                    Log.i("facebook",information.getString("facebook"));
                    Log.i("instagram",information.getString("instagram"));
                    Log.i("university",information.getString("university"));
                    Log.i("chatbot_channel",information.getJSONArray("chatbot_channel").toString());
                    Log.i("directmessage_channel",information.getJSONArray("directmessage_channel").toString());
                    Log.i("globalchat_channel",information.getJSONArray("globalchat_channel").toString());
                    Log.i("groupchat_channel",information.getJSONArray("groupchat_channel").toString());
                    Log.i("friend",information.getJSONArray("friend").toString());
                    Log.i("friend_request",information.getJSONArray("friend_request").toString());

                }
                catch (Exception e){
                    Log.i("HTTP Success Error",e.toString());
                }
            }

            @Override
            public void onError(String error) {
                Log.i("HTTP Error",error);
            }
        });
    }
}
