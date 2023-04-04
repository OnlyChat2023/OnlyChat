package com.example.onlychat.DiaLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.onlychat.GlobalChat.MessageBottomDialogFragment;
import com.example.onlychat.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DMBottomDialog extends BottomSheetDialogFragment {
    int id;
    public DMBottomDialog() {
        // Required empty public constructor
    }

    public static DMBottomDialog newInstance(int id) {
        DMBottomDialog fragment = new DMBottomDialog();
        Bundle args = new Bundle();
        args.putInt("ID", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.direct_message_bottom_dialog, container, false);
        ImageView delete = (ImageView) layout.findViewById(R.id.imageView7);
        ImageView pin = (ImageView) layout.findViewById(R.id.imageView10);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return layout;
    }
}

