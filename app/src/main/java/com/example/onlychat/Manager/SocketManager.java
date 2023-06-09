package com.example.onlychat.Manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.onlychat.Async.ConvertImage;
import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Interfaces.MessageListener;
import com.example.onlychat.Interfaces.ProfileReceiver;
import com.example.onlychat.Interfaces.RoomListener;
import com.example.onlychat.Interfaces.SeenMessageListener;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketManager {
    private static Socket socket;
    public synchronized static void getInstance() {
        if (socket == null) {
            try {
                socket = IO.socket("http://"+ Utils.ip+":5000");

                socket.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void disconnect() {
        if (socket != null) {
            socket.off();
            socket.disconnect();
            socket = null;
        }
    }

    public static void register(UserModel user) {
        if (socket != null) {
            socket.emit("register", new Gson().toJson(user));
        }
    }

    public static void joinRoom(String roomName, UserModel user) {
        if (socket != null) {
            socket.emit("joinRoom", roomName, new Gson().toJson(user));
        }
    }

    public static void sendMessage(String message, int position, UserModel user) {
        if (socket != null){
            socket.emit("sendStringMessage", message, position, new Gson().toJson(user));
        }
    }

    public static void sendRequestAddFriend(String id, UserModel user) {
        if (socket != null){
            socket.emit("sendRequestAddFriend", id, new Gson().toJson(user));
        }
    }

    public static void acceptRequestAddFriend(String id, UserModel user){
        if(socket != null){
            socket.emit("acceptRequestAddFriend",id,new Gson().toJson(user));
        }
    }

    public static void removeRequestAddFriend(String id, UserModel user){
        if(socket != null){
            socket.emit("removeRequestAddFriend",id,new Gson().toJson(user));
        }
    }

    public static void changeNickname(String id_1,String nickname_1,String id_2,String nickname_2,String chat_id){
        if(socket != null){
            socket.emit("changeNickname",id_1,nickname_1,id_2,nickname_2,chat_id);
        }
    }

    public static void changeGroupName(String chat_id, String newGroupName){
        if(socket != null){
            socket.emit("changeGroupName", chat_id, newGroupName);
        }
    }

    public static void deleteMessage(String chat_id,String message_id){
        if(socket != null){
            socket.emit("deleteMessage",chat_id,message_id);
        }
    }

    public static void deleteFriend(String id, UserModel user){
        if(socket != null){
            socket.emit("deleteFriend",id,new Gson().toJson(user));
        }
    }

    public static void blockFriend(String id, String user_id){
        if(socket != null){
            socket.emit("blockFriend",id,user_id);
        }
    }

    public static void unblockFriend(String id,String user_id){
        if(socket != null){
            socket.emit("unblockFriend",id,user_id);
        }
    }

    public static void sendImageMessage(Context ctx, ArrayList<String> arrayList, int position, UserModel user) {
        if (socket != null) {
            socket.emit("sendImageMessage", new Gson().toJson(arrayList), position, new Gson().toJson(user));
        }
    }

    public static void addNewAvatarToServer(Context ctx, String avt, String user_id) {
        if (socket != null) {
            socket.emit("addNewAvatarToServer", "data:image/png;base64,"+avt, user_id);
        }
    }

    public static void notifyUpdateMessage(String LastMessageID) {
        if (socket != null) {
            socket.emit("notifyUpdateMessage", LastMessageID);
        }
    }

    public static void waitMessage(MessageListener listener) {
        if (socket != null) {
            socket.on("messageListener", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject messageJson = (JSONObject) args[0];
                    int position = (int) args[1];

                    MessageModel message = new Gson().fromJson(String.valueOf(messageJson), MessageModel.class);

                    String dtStart = null;
                    try {
                        dtStart = messageJson.getString("time");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    format.setTimeZone(TimeZone.getTimeZone("GMT"));

                    try {
                        java.util.Date date = format.parse(dtStart);
                        message.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    listener.onMessage(message, position);
                }
            });
        }
    }

    public static void waitFinishEditProfile(ProfileReceiver listener) {
        if (socket != null) {
            socket.on("editDone", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    String new_filename = (String) args[0];
                    listener.onSuccess(new_filename);
                }
            });
        }
    }

//    public static void waitFinishSettingGroupName(ProfileReceiver listener) {
//        if (socket != null) {
//            socket.on("waitSetGroupName", new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    String newGroupName = (String) args[0];
//                    listener.onSuccess(newGroupName);
////                    System.out.printf("Socket run here");
//                }
//            });
//        }
//    }

    public static void getMetaData(UserModel user) {
        if (socket != null) {
            socket.emit("getMetaData", new Gson().toJson(user));
        }
    }

    public static void leaveRoom() {
        if (socket != null) {
            socket.emit("leaveRoom");
        }
    }

    public static void waitRoomChange(RoomListener listener) {
        if (socket != null) {
            socket.on("roomListener", new Emitter.Listener(){

                @Override
                public void call(Object... args) {
                    String roomID = (String) args[0];
                    String channel = (String) args[1];

                    listener.onLoaded(roomID, channel);
                }
            });
        }
    }

    public static void notifyUpdateRoom(String room_id, String channel) {
        if (socket != null) {
            socket.emit("updateRoom", room_id, channel);
        }
    }

    public static void seenMessageListener(SeenMessageListener listener) {
        if (socket != null) {
            socket.on("seenMessageListener", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    String RoomID = (String) args[0];
                    JSONArray SeenUser = (JSONArray) args[1];

                    ArrayList<String> UserList = new ArrayList<String>();
                    try {
                        for (int i = 0; i < SeenUser.length(); i++) {
                            UserList.add(SeenUser.getString(i));
                        }
                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
                        e.printStackTrace();
                    }

                    listener.onSeen(RoomID, UserList);
                }
            });
        }
    }

    public static void seenMessage(String roomID, String channel, String user_id) {
        if (socket != null) {
            socket.emit("seenMessage", roomID, channel, user_id);
        }
    }

    public static void uploadAvatarChat(String channel, String avt, String groupID) {
        if (socket != null) {
            socket.emit("uploadChatAvatar", channel, "data:image/png;base64," + avt, groupID);
        }
    }

    public static void uploadDoneChatAvatar(ProfileReceiver listener) {
        if (socket != null) {
            socket.on("uploadDoneChatAvatar", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    String new_filename = (String) args[0];
                    listener.onSuccess(new_filename);
                }
            });
        }
    }
}
