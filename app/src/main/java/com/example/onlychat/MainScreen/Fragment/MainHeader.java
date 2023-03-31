package com.example.onlychat.MainScreen.Fragment;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.example.onlychat.MainActivity;
import com.example.onlychat.MainScreen.Interface.Main_FragmentCallBacks;
import com.example.onlychat.MainScreen.Interface.Main_MainCallBacks;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.R;

public class MainHeader extends Fragment implements Main_FragmentCallBacks {
    MainScreen main;
    TextView txtHeaderTitle;
    ImageView icon;

    public static MainHeader newInstance(String strArg1) {
        MainHeader fragment = new MainHeader();
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
        RelativeLayout view_layout_header = (RelativeLayout) inflater.inflate(R.layout.main_header, null);
        txtHeaderTitle = (TextView) view_layout_header.findViewById(R.id.header_title);
        icon = (ImageView) view_layout_header.findViewById(R.id.icon);
//        txtSubTitle = (TextView) view_layout_header.findViewById(R.id.sub_title);

        try { Bundle arguments = getArguments();
            assert arguments != null;
//            title_id.setText(arguments.getString("arg1", ""));
            }
        catch (Exception e) { Log.e("RED BUNDLE ERROR – ",  e.getMessage()); }

        return view_layout_header;
    }
}