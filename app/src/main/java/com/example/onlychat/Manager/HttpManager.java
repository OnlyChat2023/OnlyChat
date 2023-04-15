package com.example.onlychat.Manager;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlychat.Interfaces.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpManager {
    private Context context;
    private GlobalPreferenceManager pref;
    static private com.example.onlychat.Model.UserModel user = new com.example.onlychat.Model.UserModel();
    private final String ip = "192.168.2.16";

    public HttpManager(Context _context) {
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
                    try {
                        httpResponse.onSuccess(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        //handle your network error here.
                    } else if(error instanceof ServerError) {
                        //handle if server error occurs with 5** status code
                    } else if(error instanceof AuthFailureError) {
                        //handle if authFailure occurs.This is generally because of invalid credentials
                    } else if(error instanceof ParseError) {
                        //handle if the volley is unable to parse the response data.
                    } else if(error instanceof TimeoutError) {
                        //handle if socket time out is occurred.
                    }

                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);
                            String message = data.getString("message");
                            Log.e("HTTP ERROR", message);
                        } catch (UnsupportedEncodingException | JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    httpResponse.onError(error.getMessage()==null?error.toString():error.getMessage());
                }
            }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                if (pref.getIsLoggedIn())
                    headers.put("Authorization", "Bearer " + pref.getUserToken());
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjReq.setShouldCache(false);
        jsonObjReq.setTag(tag);

        queue.add(jsonObjReq);
    }

    public void getListChat(HttpResponse responseReceiver){
        createRequest("http://"+ip+":5000/api/onlychat/v1/user/userInformation",Request.Method.GET,"userprofile", null, responseReceiver);
    }

    public com.example.onlychat.Model.UserModel getUserById(String _id) {
        com.example.onlychat.Model.UserModel userModel = new com.example.onlychat.Model.UserModel();
        createRequest("http://"+ip+":5000/api/onlychat/v1/user/userProfile", Request.Method.PATCH, "userprofile", new HashMap<String, String>() {{
                    put("_id", _id);
                }},
                new HttpResponse() {
                    @Override
                    public void onSuccess(JSONObject Response) {
                        try {
                            Log.i("HTTP Success", "success");
                        } catch (Exception e) {
                            Log.i("HTTP Success Error", e.toString());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.i("HTTP Error", error);
                    }
                });


        return userModel;
    }
    public void validateAccount(String phoneNumber, HttpResponse responseReceiver) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phonenumber", phoneNumber);

        createRequest("http://" + ip + ":5000/api/onlychat/v1/auth/register/validate", Request.Method.POST, "register-validate", params, responseReceiver);
    }

    public void Register(String username, String phoneNumber, String password, String confirmPassword, String firebaseToken, HttpResponse responseReceiver) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("phonenumber", phoneNumber.replace("+84", "0"));
        params.put("password", password);
        params.put("passwordConfirm", confirmPassword);
        params.put("firebaseToken", firebaseToken);

        createRequest("http://" + ip + ":5000/api/onlychat/v1/auth/register", Request.Method.POST, "register", params, responseReceiver);
    }

    public void Login(String phoneNumber, String password, HttpResponse responseReceiver) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phonenumber", phoneNumber);
        params.put("password", password);

        createRequest("http://" + ip + ":5000/api/onlychat/v1/auth/login", Request.Method.POST, "login", params, responseReceiver);
    }
}
