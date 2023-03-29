package com.example.onlychat.Profile.CustomItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.R;

public class CustomIconLabelAdapter extends ArrayAdapter<String> {
    Context context;
    Integer[] thumbnails;
    String[] contents;

    public CustomIconLabelAdapter(Context context, int layoutToBeInflated, String[] contents, Integer[] thumbnails) {
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
//        System.out.println(contents[position]);
        content.setText(contents[position]);
        icon.setImageResource(thumbnails[position]);
        return (row);
    }
}
