package com.example.onlychat;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;

public class MainHeader extends Fragment implements Main_FragmentCallBacks {
    MainActivity main;
    TextView txtHeaderTitle, txtSubTitle;

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
        main = (MainActivity) getActivity(); // use this reference to invoke main callbacks
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout view_layout_header = (LinearLayout) inflater.inflate(R.layout.main_header, null);
        txtHeaderTitle = (TextView) view_layout_header.findViewById(R.id.header_title);
        txtSubTitle = (TextView) view_layout_header.findViewById(R.id.sub_title);

        try { Bundle arguments = getArguments();
            assert arguments != null;
//            title_id.setText(arguments.getString("arg1", ""));
            }
        catch (Exception e) { Log.e("RED BUNDLE ERROR – ",  e.getMessage()); }

        return view_layout_header;
    }
}
