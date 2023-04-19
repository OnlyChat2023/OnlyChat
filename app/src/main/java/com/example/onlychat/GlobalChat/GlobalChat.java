package com.example.onlychat.GlobalChat;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.onlychat.GlobalChat.ListMessage.ListMessage;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Interfaces.MessageListener;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.Profile.ProfileNotFound;
import com.example.onlychat.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class GlobalChat extends Fragment {
    TextView chatTitle;
    ImageView chatIcon;
    ImageView profile;
    ImageView addChat;
    ListView listChat;
    String typeChat = "globalChat";
    ArrayList<RoomModel> roomModels = new ArrayList<>();

    CustomChatItem customChatItem;

    GlobalPreferenceManager pref;
    static UserModel myInfo;

    public ArrayList<RoomModel> getRoomModels() {
        return roomModels;
    }

    public void setRoomModels(ArrayList<RoomModel> roomModels) {
        this.roomModels.clear();

        this.roomModels.addAll(roomModels);
        customChatItem.notifyDataSetChanged();
    }

    Integer avatarsImage[] = {
            R.raw.a_1, R.raw.a_2, R.raw.a_3, R.raw.a_4, R.raw.a_5,
            R.raw.a_6, R.raw.a_7, R.raw.a_8, R.raw.a_9, R.raw.a_10,
            R.raw.a_11, R.raw.a_12, R.raw.a_13, R.raw.a_14, R.raw.a_15,
            R.raw.a_16,R.raw.a_17, R.raw.a_18, R.raw.a_19, R.raw.a_20,
            R.raw.a_21, R.raw.a_22, R.raw.a_23, R.raw.a_24, R.raw.a_25,
    };

    GridView androidGridView;

    public GlobalChat(){}

    @Override
    public void onResume() {
        super.onResume();
        customChatItem.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout globalChat = (RelativeLayout) inflater.inflate(R.layout.fragment_main_content, null);

        // set value for widget
        chatTitle=(TextView) globalChat.findViewById(R.id.header_title);
        chatTitle.setText("global chat channel");
        chatIcon = (ImageView) globalChat.findViewById(R.id.chatIcon);
        profile=(ImageView) globalChat.findViewById(R.id.profile);
        addChat = (ImageView) globalChat.findViewById(R.id.addChat);
        listChat = (ListView) globalChat.findViewById(R.id.listChat);

        new HttpManager.GetImageFromServer(profile).execute(new GlobalPreferenceManager(getContext()).getUserModel().getAnonymous_avatar());

        pref = new GlobalPreferenceManager(getContext());
        myInfo = pref.getUserModel();

        listChat.setSelection(0);
        listChat.smoothScrollToPosition(0);
        Log.i("Global chat", Integer.toString(roomModels.size()));
        customChatItem = new CustomChatItem(globalChat.getContext(),roomModels );
        listChat.setAdapter(customChatItem);

        listChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                MessageBottomDialogFragment messageBottomDialogFragment = new MessageBottomDialogFragment();
                messageBottomDialogFragment.show(getChildFragmentManager(), messageBottomDialogFragment.getTag());

                return true;
            }
        });

        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(listChat.getContext(), ListMessage.class);
                intent.putExtra("typeChat", typeChat);
                intent.putExtra("Data", roomModels.get(i));
                intent.putExtra("Position", i);
                intent.putExtra("channel", "global_chat");
                startActivityForResult(intent, 0);
                getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.fixed);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) globalChat.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                // overlay
                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
//                boolean focusable = true; // lets taps outside the popup also dismiss it
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                // Popup
                View popupView = inflater.inflate(R.layout.global_chat_popup, null);
                EditText nickname = popupView.findViewById(R.id.nickname);


//                Log.i("anonymous avatar", myInfo.getAnonymous_avatar());


//                Log.i("anonymous avatar",part2[0]);
                final int[] avatarIndex = {1};

                HttpManager httpManager= new HttpManager(globalChat.getContext());
                httpManager.getUserById(myInfo.get_id(), new HttpResponse() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException, InterruptedException {
                        JSONObject profile = response.getJSONObject("data");
                        Log.i("all friends click item", profile.toString());

                        UserModel user = new Gson().fromJson(profile.toString(), UserModel.class);
                        nickname.setText(user.getNickName());

                        String[] part1 =user.getAnonymous_avatar().split("/");
                        String[] part2= part1[1].split("\\.");
                        avatarIndex[0] = Integer.parseInt(part2[0]);

                    }

                    @Override
                    public void onError(String error) {

                    }
                });



                Button okBtn = popupView.findViewById(R.id.okBtn);

                BaseAdapter baseAdapter = new ImageAdapterGridView(popupView.getContext());

                androidGridView = (GridView) popupView.findViewById(R.id.gridview_android_example);
                androidGridView.setAdapter(baseAdapter);
                Log.i("TAG=======", Integer.toString(androidGridView.getChildCount()));;

//                Log.i("TAG=======", androidGridView.getChildAt(Integer.parseInt(part2[0])-1).toString());;
                for(int i=0;i<androidGridView.getChildCount();i++){
                    androidGridView.getChildAt(i).setBackgroundColor(Color.BLACK);
                }
                androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        for(int i=0;i<androidGridView.getChildCount();i++){
                            androidGridView.getChildAt(i).setBackgroundColor(0);
                        }
                        avatarIndex[0] = position+1;
                        v.setBackgroundColor(Color.parseColor("#adb5bd"));

                    }
                });
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,900,1360,focusable);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        HttpManager httpManager = new HttpManager(globalChat.getContext());
                        httpManager.setAnonymousInformation(nickname.getText().toString(),"avatar/"+avatarIndex[0]+".png", new HttpResponse() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException, ParseException {
                                popupWindow.dismiss();
                                new HttpManager.GetImageFromServer(profile).execute(response.getJSONObject("data").getString("anonymous_avatar"));
                                JSONArray globalChat = response.getJSONObject("data").getJSONArray("globalChat");
//                                Log.i("OK BUTTON", Integer.toString(globalChat.length()));
                                if(globalChat.length()>0){
                                   setRoomModels(MainScreen.getListRoom(globalChat));
                                }

                            }

                            @Override
                            public void onError(String error) {
                                Log.i("popup click error", error);
                            }
                        });
                    }

                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        overlayWindow.dismiss();
                    }
                });

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });

        addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) globalChat.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                // overlay
                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
//                boolean focusable = true; // lets taps outside the popup also dismiss it
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                // Popup
                View popupView = inflater.inflate(R.layout.global_chat_popup_new_group, null);
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,900,500,focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button searchBtn = (Button) popupView.findViewById(R.id.searchBtn);
                searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("popup", "onClick: ");
                    }
                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        overlayWindow.dismiss();
                    }
                });
            }
        });
        return globalChat;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
//            int pos = data.getIntExtra("Position", -1);
//            boolean update = data.getBooleanExtra("Update", false);
//
//            if (pos != -1 && update) {
//                RoomModel newRoom = (RoomModel) data.getSerializableExtra("RoomModel");
//
//                roomModels.remove(pos);
//                roomModels.add(newRoom);
//
//                customChatItem.notifyDataSetChanged();
//            }
        }
    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return avatarsImage.length;
        }

        public ImageView getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;
            if (convertView == null) {
                mImageView = new ImageView(mContext);
                mImageView.setLayoutParams(new GridView.LayoutParams(160, 160));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(16, 16, 16, 16);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(avatarsImage[position]);
            return mImageView;
        }
    }
}

