package com.example.onlychat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class profile extends ListActivity {
    private CustomIconLabelAdapter adapter;

    String[] contents = {"Tran Nguyen Phong", "092 790 74 19", "tranphong@gmail.com", "University Science"};
    Integer[] thumbnails = { R.drawable.ic_user_svg,R.drawable.ic_phone,R.drawable.ic_email,R.drawable.ic_graduated};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        adapter = new CustomIconLabelAdapter(this, R.layout.info_item, contents, thumbnails);
        setListAdapter(adapter);
    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        String text = names[position];
////        result.setText(text);
//    }
}

class CustomIconLabelAdapter extends ArrayAdapter<String> {
    Context context;
    Integer[] thumbnails;
    String[] contents;

    public CustomIconLabelAdapter( Context context, int layoutToBeInflated, String[] contents, Integer[] thumbnails) {
        super(context, R.layout.info_item, contents);
        this.context = context;
        this.thumbnails = thumbnails;
        this.contents = contents;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View row = inflater.inflate(R.layout.info_item, null);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView content = (TextView) row.findViewById(R.id.content);
        ImageView icon = (ImageView) row.findViewById(R.id.icon);
        System.out.println(contents[position]);
        content.setText(contents[position]);
        icon.setImageResource(thumbnails[position]);
        return (row);
    }
}