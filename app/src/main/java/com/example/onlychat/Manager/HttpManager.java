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
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpManager {
    private Context context;
    private GlobalPreferenceManager pref;
    static private UserModel user = new UserModel();

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

    public UserModel getUser(){
        createRequest("http://192.168.1.45:5000/api/onlychat/v1/user/userInformation",Request.Method.GET,"userprofile", null,
        new HttpResponse(){
            @Override
            public void onSuccess(JSONObject Response) {
                try{
                    JSONObject information = Response.getJSONObject("data").getJSONObject("user");
                    JSONArray directChat = Response.getJSONObject("data").getJSONArray("directChat");
                    JSONArray groupChat = Response.getJSONObject("data").getJSONArray("groupChat");
                    JSONArray globalChat = Response.getJSONObject("data").getJSONArray("globalChat");
                    JSONArray botChat = Response.getJSONObject("data").getJSONArray("botChat");
//                    Log.i("friend",information.getJSONArray("friend").toString());
//                    Log.i("friend_request",information.getJSONArray("friend_request").toString());
                    user.setName(information.getString("name"));
//                    user.setAvatar(information.getString("avatar"));
                    user.setEmail(information.getString("email"));
                    user.setPhone(information.getString("phone"));
                    user.setFacebook(information.getString("facebook"));
                    user.setInstagram(information.getString("instagram"));
                    user.setUniversity(information.getString("university"));
                    ArrayList<RoomModel> global = new ArrayList<>();
                    for(int i=0;i<globalChat.length();i++){
//                        String id = globalChat.getString(i);
                          Log.i("Item",globalChat.getJSONObject(i).getString("_id"));
//                        RoomModel roomModel = new RoomModel();
                    }

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
        return user;
    }


    public UserModel getUserById(String _id){
        UserModel userModel = new UserModel();
        createRequest("http://192.168.2.16:5000/api/onlychat/v1/user/userProfile", Request.Method.PATCH, "userprofile",new HashMap<String,String>(){{put("_id",_id);}} ,
                new HttpResponse() {
                    @Override
                    public void onSuccess(JSONObject Response) {
                        try {
                            Log.i("HTTP Success", "success");
                            Log.i("User Profile",Response.getJSONObject("data").toString());
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
}
