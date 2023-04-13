package com.example.onlychat.DiaLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.onlychat.DirectMessage.Option.OptionActivity;
import com.example.onlychat.R;

public class ChangeNickNameDialog extends DialogFragment {
    String fr_name, fr_nickname, user_nickname;
    Button yes;
    Button no, refresh;
    EditText friendNickname, userNickname;
    OptionActivity activity;
    public ChangeNickNameDialog(){
    }

    public ChangeNickNameDialog(OptionActivity activity){
        this.activity = activity;
    }

    public static ChangeNickNameDialog newInstance(OptionActivity activity, String name_fr, String nn_fr, String nn_user){
        ChangeNickNameDialog f = new ChangeNickNameDialog(activity);

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("frName", name_fr);
        args.putString("frNN", nn_fr);
        args.putString("userNN", nn_user);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fr_name= getArguments().getString("frName");
        fr_nickname= getArguments().getString("frNN");
        user_nickname= getArguments().getString("userNN");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.change_nickname_dialog, container, false);

        TextView friendName = (TextView) layout.findViewById(R.id.friend_title);
        friendNickname = (EditText) layout.findViewById(R.id.friend_nickname);
        userNickname = (EditText) layout.findViewById(R.id.user_nickname);
        yes = (Button) layout.findViewById(R.id.yes_btn);
        refresh = (Button) layout.findViewById(R.id.refresh_btn);
        no = (Button) layout.findViewById(R.id.no_btn);

        friendName.setText("Editting " + fr_name + "'s nickname");
        friendNickname.setHint(fr_nickname);
        userNickname.setHint(user_nickname);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp1 = friendNickname.getText().toString();
                String temp2 = userNickname.getText().toString();
                Boolean flag = false;
                if (!temp1.equals(fr_nickname) && !temp1.equals("")) {
                    fr_nickname = temp1;
                    flag = true;
                }
                if (!temp2.equals(user_nickname) && !temp2.equals("")) {
                    user_nickname = temp2;
                    flag = true;
                }
                if (flag == true)
                    activity.setNickname(fr_nickname, user_nickname);
                dismiss();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                friendNickname.setText("");
                userNickname.setText("");
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return layout;
    }

    public String getFr_nickname() {
        return fr_nickname;
    }

    public String getUser_nickname() {
        return user_nickname;
    }
}
