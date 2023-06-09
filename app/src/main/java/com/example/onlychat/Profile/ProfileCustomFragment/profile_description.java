package com.example.onlychat.Profile.ProfileCustomFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_description#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_description extends Fragment {
    TextView description;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile_description() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_description.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_description newInstance(String param1, String param2) {
        profile_description fragment = new profile_description();
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

    public void setData(UserModel userModel) {
        description.setText(userModel.getDescription());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout layout_profile3 = (LinearLayout) inflater.inflate(R.layout.fragment_profile_description, null);
        description = (TextView) layout_profile3.findViewById(R.id.content);

        return layout_profile3;
//        return inflater.inflate(R.layout.fragment_profile_description, container, false);
    }
}