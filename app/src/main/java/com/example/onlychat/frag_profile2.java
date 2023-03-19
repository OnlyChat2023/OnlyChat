package com.example.onlychat;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_profile2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_profile2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    MainActivity main;
    Context context = null;
    private CustomIconLabelAdapter adapter;

    String[] contents = {"Facebook", "Instagram"};
    Integer[] thumbnails = { R.drawable.ic_facebook,R.drawable.ic_instagram};

    public frag_profile2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag_profile2.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_profile2 newInstance(String param1, String param2) {
        frag_profile2 fragment = new frag_profile2();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout_profile2 = (LinearLayout) inflater.inflate(R.layout.fragment_frag_profile2, null);
        ListView listView = (ListView) layout_profile2.findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.info_item, contents);

        adapter = new CustomIconLabelAdapter(context, R.layout.info_item, contents, thumbnails);
        listView.setAdapter(adapter);
        listView.setSelection(0);
        listView.smoothScrollToPosition(0);

        return layout_profile2;
    }
}