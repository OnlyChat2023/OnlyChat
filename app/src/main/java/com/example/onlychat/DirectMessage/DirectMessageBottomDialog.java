package com.example.onlychat.DirectMessage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.onlychat.GlobalChat.MessageBottomDialogFragment;
import com.example.onlychat.GroupChat.GroupChat;
import com.example.onlychat.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DirectMessageBottomDialog extends BottomSheetDialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String id;
    private DirectMessage activity;

    public ImageView delete;
    public ImageView lock;

    public DirectMessageBottomDialog() {
        // Required empty public constructor
    }

    public static MessageBottomDialogFragment newInstance(String param1, String param2) {
        MessageBottomDialogFragment fragment = new MessageBottomDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_message_bottom_dialog_, container, false);
        delete = (ImageView) layout.findViewById(R.id.imageView7);
        lock = (ImageView) layout.findViewById(R.id.imageView10);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DirectMessage.removeRoom(id);
                dismiss();
            }
        });

        return layout;
    }


    public void setActivity(DirectMessage activity) {
        this.activity = activity;
    }

    public void setId(String id) {
        this.id = id;
    }
}
