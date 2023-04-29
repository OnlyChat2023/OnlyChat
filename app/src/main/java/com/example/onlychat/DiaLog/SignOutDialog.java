package com.example.onlychat.DiaLog;

//import static androidx.core.app.ActivityCompat.Api16Impl.finishAffinity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;

import com.example.onlychat.MainActivity;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Manager.SocketManager;
import com.example.onlychat.Profile.Profile;
import com.example.onlychat.R;

public class SignOutDialog extends DialogFragment {
    String group_name;
    GlobalPreferenceManager pref;
    public SignOutDialog.OnInputListener mOnInputListener;
    public SignOutDialog(){

    }

    public interface OnInputListener {
        void sendInput(String input);
    }
    private static final String TAG = "DialogFragment";
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            mOnInputListener
                    = (SignOutDialog.OnInputListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }

    public static SignOutDialog newInstance(String group_name){
        SignOutDialog f = new SignOutDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("group_name", group_name);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group_name= getArguments().getString("group_name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.sign_out_dialog, container, false);

        Button saveBtn = (Button) layout.findViewById(R.id.signOutBtn);
        Button cancelBtn = (Button) layout.findViewById(R.id.cancelBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = new GlobalPreferenceManager(saveBtn.getContext());
                HttpManager httpManager = new HttpManager(saveBtn.getContext());
                httpManager.removeNotify(pref.getNotify());
                pref.SignOut();
                SocketManager.disconnect();
                Intent intent = new Intent(saveBtn.getContext(), MainActivity.class);
                startActivity(intent);
//                finishAffinity();
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return layout;
    }
}
