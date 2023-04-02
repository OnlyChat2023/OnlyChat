package com.example.onlychat.MainScreen.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Button;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.onlychat.MainActivity;
import com.example.onlychat.MainScreen.Interface.Main_FragmentCallBacks;
import com.example.onlychat.MainScreen.Interface.Main_MainCallBacks;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.R;

public class MainNavbar extends Fragment {
    MainScreen main;
    Button btnChat, btnGroupChat, btnGlobalChat, btnBotChat, btnFriends;

    public static MainNavbar newInstance(String strArg1) {
        MainNavbar fragment = new MainNavbar();
        Bundle bundle = new Bundle();
        bundle.putString("arg1", strArg1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof Main_MainCallBacks)) {
            throw new IllegalStateException("Activity must implement MainCallbacks");
        }
        main = (MainScreen) getActivity(); // use this reference to invoke main callbacks
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout view_layout_navbar = (LinearLayout) inflater.inflate(R.layout.fragment_main_navbar, null);
        btnChat = (Button) view_layout_navbar.findViewById(R.id.btn_chat);
        btnGroupChat = (Button) view_layout_navbar.findViewById(R.id.btn_groupchat);
        btnGlobalChat = (Button) view_layout_navbar.findViewById(R.id.btn_globalchat);
        btnBotChat = (Button) view_layout_navbar.findViewById(R.id.btn_botchat);
        btnFriends = (Button) view_layout_navbar.findViewById(R.id.btn_friends);

//        try { Bundle arguments = getArguments();
//            assert arguments != null;
////            title_id.setText(arguments.getString("arg1", ""));
//        }
//        catch (Exception e) { Log.e("RED BUNDLE ERROR – ",  e.getMessage()); }

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.onMsgFromFragToMain("NAVBAR", "CHAT");
            }
        });
        btnBotChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.onMsgFromFragToMain("NAVBAR", "BOTCHAT");
            }
        });
        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.onMsgFromFragToMain("NAVBAR", "FRIEND");
            }
        });
        btnGlobalChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.onMsgFromFragToMain("NAVBAR", "GLOBALCHAT");
            }
        });
        btnGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.onMsgFromFragToMain("NAVBAR", "GROUPCHAT");
            }
        });

        return view_layout_navbar;
    }
}
