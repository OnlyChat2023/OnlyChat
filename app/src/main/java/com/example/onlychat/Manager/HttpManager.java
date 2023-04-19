package com.example.onlychat.Manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.MySingleton;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HttpManager {
    private Context context;
    private static GlobalPreferenceManager pref;
    private static final String ip = Utils.ip;
    private String accessToken = "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";
    static private UserModel user = new UserModel();

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
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            //handle your network error here.
                        } else if (error instanceof ServerError) {
                            //handle if server error occurs with 5** status code
                        } else if (error instanceof AuthFailureError) {
                            //handle if authFailure occurs.This is generally because of invalid credentials
                        } else if (error instanceof ParseError) {
                            //handle if the volley is unable to parse the response data.
                        } else if (error instanceof TimeoutError) {
                            //handle if socket time out is occurred.
                        }


                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                String message = data.getString("message");
                                Log.e("HTTP ERROR", message);
                            } catch (UnsupportedEncodingException | JSONException e) {
                                //throw new RuntimeException(e);
                            }
                        }
                        httpResponse.onError("ERROR");
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

    public void getListChat(HttpResponse responseReceiver) {
        createRequest("http://" + ip + ":5000/api/onlychat/v1/user/userInformation", Request.Method.GET, "userprofile", null, responseReceiver);
    }

    public void getListFriends(HttpResponse responseReceiver) {
        createRequest("http://" + ip + ":5000/api/onlychat/v1/user/friends", Request.Method.GET, "friends", null, responseReceiver);
    }

    public void getUserById(String _id, HttpResponse response) {
        createRequest("http://" + ip + ":5000/api/onlychat/v1/user/getUserById", Request.Method.PATCH, "userprofile",
                new HashMap<String, String>() {{
                    put("_id", _id);
                }}, response);
    }

    public void getUserByPhone(String phone, HttpResponse response) {
        createRequest("http://" + ip + ":5000/api/onlychat/v1/user/getUserByPhone", Request.Method.PATCH, "userprofile",
                new HashMap<String, String>() {{
                    put("phone", phone);
                }}, response);
    }

    public void setAnonymousInformation(String nickname, String avatar,HttpResponse response){
        createRequest("http://" + ip + ":5000/api/onlychat/v1/user/setAnonymousInformation", Request.Method.PATCH, "userprofile",
                new HashMap<String, String>() {{
                    put("nickname",nickname);
                    put("anonymous_avatar",avatar);
                }}, response);
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

    public static class GetImageFromServer extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public void DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        public GetImageFromServer(ImageView avatar) {
            this.bmImage = avatar;
        }


        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL("http://" + ip + ":5000/assets/" + urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void AddGroupChat(String typeChat, String newName, String userID, HttpResponse responseReceiver) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("_id", userID);
        params.put("name", newName);
        params.put("update_time", Calendar.getInstance().getTime().toString());

        createRequest("http://" + ip + ":5000/api/onlychat/v1/" + typeChat +"/addGroup", Request.Method.POST, "addGroup", params, responseReceiver);
    }

    public void GetListGroupChat(String typeChat, String userID, HttpResponse responseReceiver) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("_id", userID);

        createRequest("http://" + ip + ":5000/api/onlychat/v1/" + typeChat +"/getListGroupChat", Request.Method.POST, "getListGroupChat", params, responseReceiver);
    }

    public void LeaveGroupChat(String typeChat, String userID, String GroupChatID, HttpResponse responseReceiver) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("_id", userID);
        params.put("grc_id", GroupChatID);

        createRequest("http://" + ip + ":5000/api/onlychat/v1/" + typeChat +"/leaveGroupChat", Request.Method.POST, "leaveGroupChat", params, responseReceiver);
    }

    public void updateProfile(UserModel userModel) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", userModel.getName());
        params.put("avatar", userModel.getAvatar());
        params.put("phone", userModel.getPhone());
        params.put("email", userModel.getEmail());
        params.put("university", userModel.getUniversity());
        params.put("facebook", userModel.getFacebook());
        params.put("instagram", userModel.getInstagram());
        params.put("description", userModel.getDescription());

        createRequest("http://" + ip + ":5000/api/onlychat/v1/user/updateProfile", Request.Method.POST, "userprofile", params, null);}

        public void UpdateGroupNotufy (String typeChat, String userID, String GroupChatID, Boolean notify, Boolean
        block, HttpResponse responseReceiver){
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", userID);
            params.put("grc_id", GroupChatID);
            params.put("notify", notify.toString());
            params.put("block", block.toString());

            createRequest("http://" + ip + ":5000/api/onlychat/v1/" + typeChat +"/updateNotify", Request.Method.POST, "updateNotify", params, responseReceiver);
        }

        public void GetListNewMember (String typeChat, String userID, String GroupChatID, HttpResponse responseReceiver){
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", userID);
            params.put("grc_id", GroupChatID);

            createRequest("http://" + ip + ":5000/api/onlychat/v1/" + typeChat +"/getAddMember", Request.Method.POST, "getListNewMember", params, responseReceiver);
        }

        public void addMemberGroup (String typeChat, String userID, String GroupChatID, String name, String nickname, String avatar, HttpResponse responseReceiver){
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", userID);
            params.put("grc_id", GroupChatID);
            params.put("name", name);
            params.put("nickname", nickname);
            params.put("avatar", avatar);

            createRequest("http://" + ip + ":5000/api/onlychat/v1/" + typeChat +"/addMember", Request.Method.POST, "getListNewMember", params, responseReceiver);
        }

        public void addDirectMessage(String user_id1, String user_id2, HttpResponse responseReceiver){
            Map<String, String> params = new HashMap<String, String>();
            params.put("id_1", user_id1);
            params.put("id_2", user_id2);

            createRequest("http://" + ip + ":5000/api/onlychat/v1/directMessage/addDirectMessage", Request.Method.POST, "addDirectMessage", params, responseReceiver);
        }

        public void changeOptionDM(String user_id, String dmc_id, Boolean notify, Boolean block, HttpResponse responseReceiver){
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", user_id);
            params.put("dmc_id", dmc_id);
            params.put("notify", notify.toString());
            params.put("block", block.toString());

            createRequest("http://" + ip + ":5000/api/onlychat/v1/directMessage/changeOptions", Request.Method.POST, "changeOptions", params, responseReceiver);
        }

        public void getBlockDM(String user_id, String dmc_id, HttpResponse responseReceiver){
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", user_id);
            params.put("dmc_id", dmc_id);

            createRequest("http://" + ip + ":5000/api/onlychat/v1/directMessage/getBlock", Request.Method.POST, "getBlock", params, responseReceiver);
        }

        public void changeNicknameDM(String user_id, String dmc_id, String nickname, HttpResponse responseReceiver){
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", user_id);
            params.put("dmc_id", dmc_id);
            params.put("nickname", nickname);

            createRequest("http://" + ip + ":5000/api/onlychat/v1/directMessage/changeNickname", Request.Method.POST, "changeNickname", params, responseReceiver);
        }

        public void SendBotChat(String message, Response.Listener<JSONObject> responseReceiver, Response.ErrorListener responseError){
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("model", "text-davinci-003");
                requestBody.put("prompt", message);
                requestBody.put("max_tokens", 1000);
                requestBody.put("temperature", 1);
                requestBody.put("top_p", 1);
                requestBody.put("frequency_penalty", 0.0);
                requestBody.put("presence_penalty", 0.0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://api.openai.com/v1/completions", requestBody, responseReceiver, responseError){
                @Override
                public Map < String, String > getHeaders() throws AuthFailureError {
                    Map < String, String > headers = new HashMap < > ();
                    headers.put("Authorization", "Bearer " + accessToken);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
                @Override
                protected Response < JSONObject > parseNetworkResponse(NetworkResponse response) {
                    return super.parseNetworkResponse(response);
                }
            };
            int timeoutMs = 25000; // 25 seconds timeout
            RetryPolicy policy = new DefaultRetryPolicy(timeoutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            // Add the request to the RequestQueue
            MySingleton.getInstance(context).addToRequestQueue(request);
        }

        public void addBotChat(String newName, String user_id, HttpResponse responseReceiver){
            Map<String, String> params = new HashMap<String, String>();
            params.put("_id", user_id   );
            params.put("name", newName);
            params.put("update_time", Calendar.getInstance().getTime().toString());

            createRequest("http://" + ip + ":5000/api/onlychat/v1/botChat/addBotChat", Request.Method.POST, "addBotChat", params, responseReceiver);
        }
        
        public void getGlobalMetaData(HttpResponse responseReceiver) {
            createRequest("http://" + ip + ":5000/api/onlychat/v1/metadata/globalchat", Request.Method.GET, "getGlobalMetaData", null, responseReceiver);
        }

        public void getGroupMetaData(HttpResponse responseReceiver) {
            createRequest("http://" + ip + ":5000/api/onlychat/v1/metadata/groupchat", Request.Method.GET, "getGroupMetaData", null, responseReceiver);
        }
        public void getDirectMetaData(HttpResponse responseReceiver) {
            createRequest("http://" + ip + ":5000/api/onlychat/v1/metadata/directchat", Request.Method.GET, "getDirectMetaData", null, responseReceiver);
        }
    }
