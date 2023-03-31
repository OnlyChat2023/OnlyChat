package com.example.onlychat.MainScreen.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.content.Context;
import androidx.fragment.app.Fragment;
import android.content.Intent;

import com.example.onlychat.DirectMessage.ChattingActivity;
import com.example.onlychat.GlobalChat.CustomChatItem;
import com.example.onlychat.MainActivity;
import com.example.onlychat.MainScreen.Interface.Main_FragmentCallBacks;
import com.example.onlychat.MainScreen.Interface.Main_MainCallBacks;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.R;

public class MainContent extends Fragment implements Main_FragmentCallBacks {
    MainScreen main;
    Context context = null;
    EditText searchbox;
    ListView listChat;
    Spinner spinner;

    String[] more_content = {"Delete", "Block", "Profile"};
    String names[] = {
            "Anonymous","Anonymous Private","Anonymous Publish"
    };

    Integer avatars[]={
            R.drawable.global_chat_message_avatar,R.drawable.global_chat_message_avatar,R.drawable.global_chat_message_avatar
    };

    String messages[] = {
            "Sorry to bother you. I have a questi...",
            "Sorry to bother you. I have a questi...",
            "Sorry to bother you. I have a questi...",
    };

    String times[]={"2:00 PM","4:00 PM","6:00 PM"};

    public static MainContent newInstance(String strArg1) {
        MainContent fragment = new MainContent();
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
        context = getActivity();
        main = (MainScreen) getActivity(); // use this reference to invoke main callbacks
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout view_layout_content = (RelativeLayout) inflater.inflate(R.layout.main_main_content, null);

        listChat = (ListView) view_layout_content.findViewById(R.id.listMessage);

        try { Bundle arguments = getArguments();
            assert arguments != null;
//            title_id.setText(arguments.getString("arg1", ""));
        }
        catch (Exception e) { Log.e("RED BUNDLE ERROR – ",  e.getMessage()); }

        CustomChatItem adapter = new CustomChatItem(context,avatars,names,messages,times);
        listChat.setAdapter(adapter);

        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(context, ChattingActivity.class);
                Bundle userInf = new Bundle();
                TextView name = (TextView) v.findViewById(R.id.main_chat_item_name);
                ImageView avatar = (ImageView) v.findViewById(R.id.main_chat_item_avatar);

                userInf.putString("name", name.getText().toString());
                avatar.setDrawingCacheEnabled(true);
                Bitmap b = avatar.getDrawingCache();
                intent.putExtras(userInf);
                intent.putExtra("Bitmap", b);
                startActivity(intent);
            }});

//        spinner = (Spinner) listChat.findViewById(R.id.main_chat_item_more);
//        ArrayAdapter<String> adt = new ArrayAdapter<String>(getContext(), R.layout.spinner_friend_item, more_content);
//        spinner.setAdapter(adt);

        return view_layout_content;
    }
}