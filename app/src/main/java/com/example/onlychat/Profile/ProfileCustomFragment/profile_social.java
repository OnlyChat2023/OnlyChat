package com.example.onlychat.Profile.ProfileCustomFragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.onlychat.Profile.CustomItem.CustomIconLabelAdapter;
import com.example.onlychat.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_social#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_social extends Fragment {

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
    String[] links = {"", ""};

    Integer[] thumbnails = { R.drawable.ic_facebook,R.drawable.ic_instagram};

    public profile_social() {
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
    public static profile_social newInstance(String param1, String param2) {
        profile_social fragment = new profile_social();
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

    void getData() {
        Intent intent = getActivity().getIntent();
        Bundle myBundle = intent.getExtras();
        links[0] = myBundle.getString("facebook");
        links[1] = myBundle.getString("instagram");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getData();
        LinearLayout layout_profile2 = (LinearLayout) inflater.inflate(R.layout.fragment_profile_social, null);
        ListView listView = (ListView) layout_profile2.findViewById(R.id.list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.info_item, contents);

        adapter = new CustomIconLabelAdapter(context, R.layout.info_item, contents, thumbnails);
        listView.setAdapter(adapter);
        listView.setSelection(0);
        listView.smoothScrollToPosition(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!links[i].equals("")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links[i]));
                    startActivity(browserIntent);
                }
            }
        });

        return layout_profile2;
    }
}
