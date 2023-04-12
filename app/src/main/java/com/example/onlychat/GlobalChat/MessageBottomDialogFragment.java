package com.example.onlychat.GlobalChat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.onlychat.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class MessageBottomDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public ImageView delete;
    public ImageView block;
    public ImageView leave;

    public MessageBottomDialogFragment() {
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
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_message_bottom_dialog, container, false);
        leave = (ImageView) layout.findViewById(R.id.imageView5);
        delete = (ImageView) layout.findViewById(R.id.imageView7);
        block = (ImageView) layout.findViewById(R.id.imageView10);

        return layout;
    }
}