package com.example.onlychat.DiaLog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.onlychat.DirectMessage.Option.OptionActivity;
import com.example.onlychat.GlobalChat.ListMessage.Options.Options;
import com.example.onlychat.R;

public class ChangeGroupNameDialog extends DialogFragment  {
    String group_name;

    Options optionActivity;
    public ChangeGroupNameDialog(){

    }

    public interface OnInputListener {
        void sendInput(String input);
    }
    private static final String TAG = "DialogFragment";

    public ChangeGroupNameDialog(Options optionActivity){
        this.optionActivity = optionActivity;
    }

//    @Override
//    public void onAttach(Context context)
//    {
//        super.onAttach(context);
//        try {
//            mOnInputListener
//                    = (OnInputListener)getActivity();
//        }
//        catch (ClassCastException e) {
//            Log.e(TAG, "onAttach: ClassCastException: "
//                    + e.getMessage());
//        }
//    }

    public static ChangeGroupNameDialog newInstance(Options activity){
        ChangeGroupNameDialog f = new ChangeGroupNameDialog(activity);

        // Supply num input as an argument.
        Bundle args = new Bundle();
//        args.putString("frName", name_fr);
//        args.putString("frNN", nn_fr);
//        args.putString("userNN", nn_user);
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
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.change_group_name_dialog, container, false);

        EditText et_group_name = (EditText) layout.findViewById(R.id.et_group_name);
        Button saveBtn = (Button) layout.findViewById(R.id.saveBtn);
        Button cancelBtn = (Button) layout.findViewById(R.id.cancelBtn);
//        Button refreshBtn = (Button) layout.findViewById(R.id.refresh_btn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("GROUP: ", et_group_name.getText().toString());
                optionActivity.setGroupName(ChangeGroupNameDialog.this, et_group_name.getText().toString());
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

//        refreshBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                et_group_name.setText("");
//            }
//        });

        return layout;
    }
}
