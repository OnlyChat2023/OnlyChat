package com.example.onlychat.DiaLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.onlychat.R;

import java.util.prefs.BackingStoreException;
import java.util.zip.Inflater;

public class BasicDialog extends DialogFragment {
    String notify;

    public BasicDialog(){

    }

    public static BasicDialog newInstance(String notification){
        BasicDialog f = new BasicDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("notify", notification);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notify = getArguments().getString("notify");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.basic_dialog_layout, container, false);

        TextView notification = (TextView) layout.findViewById(R.id.notification);
        Button yes = (Button) layout.findViewById(R.id.yes_btn);
        Button no = (Button) layout.findViewById(R.id.no_btn);

        notification.setText(notify);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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
}