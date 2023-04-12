package com.example.onlychat.Manager;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlychat.Interfaces.HttpResponse;

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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url, new JSONObject(postParam),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    httpResponse.onSuccess(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    httpResponse.onError(error.getMessage());
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
}
