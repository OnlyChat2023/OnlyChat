package com.example.onlychat.GroupChat.CustomComponents;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.onlychat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link members#newInstance} factory method to
 * create an instance of this fragment.
 */
public class members extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context context = null;
    String[] names = {"Adam M.Mathew", "Albert Willson", "Andrew McLeod", "Brittany Smith"};
    Integer[] thumbnails = { R.drawable.avatar_img,R.drawable.avatar_img,R.drawable.avatar_img,R.drawable.avatar_img};
    public members() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment members_list_in_chat.
     */
    // TODO: Rename and change types and number of parameters
    public static members newInstance(String param1, String param2) {
        members fragment = new members();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getActivity(); // use this reference to invoke main callbacks
//            main = (MainActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        System.out.println("ON CREATE RUNINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout_profile1 = (LinearLayout) inflater.inflate(R.layout.fragment_members, null);
        ListView listView = (ListView) layout_profile1.findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.member_item, names);

        adapter = new CustomMembersListInChat(context, R.layout.member_item, names, thumbnails);
        listView.setAdapter(adapter);
        listView.setSelection(0);
        listView.smoothScrollToPosition(0);
//        System.out.println("ON CREATE VIEWWWW RUNINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");

        return layout_profile1;
    }
}