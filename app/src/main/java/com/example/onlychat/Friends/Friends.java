package com.example.onlychat.Friends;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlychat.Friends.AllFriends.AllFriends;
import com.example.onlychat.Friends.Invite.Invite;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.Profile.ProfileNotFound;
import com.example.onlychat.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class Friends extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView addBtn;

    static TextView quatity;

    static Invite invite = new Invite();
    static AllFriends allFriends = new AllFriends();

    public Friends(){}

    public static TextView getQuatity() {
        return quatity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout friends = (LinearLayout) inflater.inflate(R.layout.friends, null);
        Log.i("friends", "onCreateView");

        quatity = (TextView) friends.findViewById(R.id.quantity);
        addBtn = (ImageView) friends.findViewById(R.id.add);

        HttpManager httpManager = new HttpManager(getContext());
        httpManager.getListFriends(
                new HttpResponse() {
                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {
                        JSONArray friends = response.getJSONObject("data").getJSONArray("friends");
                        JSONArray friend_request = response.getJSONObject("data").getJSONArray("friend_requests");

                        ArrayList<UserModel> friends_ = getListValue(friends);
                        allFriends.setFriend_list(friends_);
                        invite.setInvite_list(getListValue(friend_request));
                        quatity.setText(friends_.size()+" available");
//                        setFriend(getListValue(friends),getListValue(friend_request));
                    }
                    @Override
                    public void onError(String error) {
                        Log.i("Friends", error);
                    }
                }
        );

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) friends.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                // overlay
                View overlayView = inflater.inflate(R.layout.global_chat_overlay, null);
//                boolean focusable = true; // lets taps outside the popup also dismiss it
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                final PopupWindow overlayWindow = new PopupWindow(overlayView,width,height,true);
                overlayWindow.showAtLocation(view, Gravity.TOP, 0, 0);

                // Popup
                View popupView = inflater.inflate(R.layout.friend_popup_add_new_friend, null);
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,900,600,focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, -125);

                Button searchBtn = popupView.findViewById(R.id.searchBtn);
                EditText phoneNumber = popupView.findViewById(R.id.phoneNumber);
                searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HttpManager httpManager = new HttpManager(friends.getContext());
                        httpManager.getUserByPhone(phoneNumber.getText().toString(), new HttpResponse() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException {
                                String status = response.getString("status");
                                if(status.length()>0){
                                    Log.i("Status", "onSuccess: ");
                                    Bundle myBundle = new Bundle();
                                    myBundle.putInt("index",0);
                                    myBundle.putString("user_id",response.getJSONObject("data").getString("_id"));

                                    Intent intentToProfile = new Intent (friends.getContext(), Profile.class);
                                    intentToProfile.putExtras(myBundle);
                                    startActivity(intentToProfile);
                                    getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.fixed);

                                }
                                else{
                                    Intent intentToProfile = new Intent (friends.getContext(), ProfileNotFound.class);
                                    startActivity(intentToProfile);
                                    Log.i("Status", "errorrrrrrrr");

                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.i("popup click error", error);
                            }
                        });

//

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

        tabLayout=friends.findViewById(R.id.tabLayout);
        viewPager=friends.findViewById(R.id.viewPaper);

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),0);
        viewPagerAdapter.addFragment(allFriends,"All friends");
        viewPagerAdapter.addFragment(invite,"Invite");
        viewPager.setAdapter(viewPagerAdapter);

        return friends;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<UserModel> getListValue(JSONArray value) throws JSONException {
        ArrayList<UserModel> list = new ArrayList<>();
        for(int i=0;i<value.length();i++){
            JSONObject messageJson = (JSONObject) value.getJSONObject(i);
            UserModel friend = new Gson().fromJson(String.valueOf(messageJson),UserModel.class);
            list.add(friend);
        }
        return  list;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        List<Fragment> fragments = new ArrayList<>();
        List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm,int behavior) {
            super(fm,behavior);
        }

        void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}