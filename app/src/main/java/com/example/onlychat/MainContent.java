package com.example.onlychat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.content.Context;
import androidx.fragment.app.Fragment;

public class MainContent extends Fragment implements Main_FragmentCallBacks {
    MainActivity main;
    Context context = null;
    EditText searchbox;
    ListView listChat;

    String[] names = {"Adam M.Mathew", "Albert Willson", "Andrew McLeod", "Brittany Smith"};
    String[] phones = {"0916231763", "0916472323", "0165227367", "0743267612"};
    Integer[] thumbnails = {R.drawable.p4, R.drawable.p4, R.drawable.p4, R.drawable.p4};

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
        main = (MainActivity) getActivity(); // use this reference to invoke main callbacks
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout view_layout_content = (LinearLayout) inflater.inflate(R.layout.main_main_content, null);
        searchbox = (EditText) view_layout_content.findViewById(R.id.chat_search_box);
        listChat = (ListView) view_layout_content.findViewById(R.id.chat_content_listview);

        try { Bundle arguments = getArguments();
            assert arguments != null;
//            title_id.setText(arguments.getString("arg1", ""));
        }
        catch (Exception e) { Log.e("RED BUNDLE ERROR – ",  e.getMessage()); }

        CustomizeChatItem adapter = new CustomizeChatItem(context,thumbnails,names,phones);
        listChat.setAdapter(adapter);
        listChat.setSelection(0);
        listChat.smoothScrollToPosition(0);
        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                ChangeColorSelectedItem(v);
//                main.onMsgFromFragToMain("LEFT-FRAG", Integer.toString(position));
//                idTextView.setText("Mã số: " + ids[position]);
            }});
        return view_layout_content;
    }
}
