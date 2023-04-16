package com.example.onlychat.GlobalChat.ListMessage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.onlychat.Adapter.ImageChat;
//import com.example.onlychat.Async.LoadImage;
import com.example.onlychat.Async.DownloadImage;
import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Model.ImageModel;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;

import java.util.ArrayList;

public class CustomMessageItem extends ArrayAdapter<MessageModel> {

    ArrayList<MessageModel> messageModels;
    GlobalPreferenceManager pref;
    UserModel myInfo;
    Context context;
    TextView message;
    RecyclerView imageLayout;
    TextView name;
    ImageView imageView;

    public CustomMessageItem(Context context, ArrayList<MessageModel> messageModels) {
        super(context, R.layout.global_chat_custom_chat_item, messageModels);
        this.context = context;
        this.messageModels = messageModels;

        pref = new GlobalPreferenceManager(context);
        myInfo = pref.getUserModel();
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final int viewType = this.getItemViewType(position);
        final MessageModel messageItem = messageModels.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row;
        if (messageModels.get(position).getUserId().equals(myInfo.getId())) {
            row = inflater.inflate(R.layout.global_chat_custom_message_item_me,null);
            message = (TextView)row.findViewById(R.id.message);

            if (messageItem.hasImages()) {
                imageLayout = (RecyclerView)row.findViewById(R.id.imagesLayout);
                imageLayout.setItemAnimator(null);

                int numcol = 1;
                if (messageItem.getImages().size() > 1)
                    numcol = 2;

                imageLayout.setLayoutManager(new GridLayoutManager(context, numcol) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
//                new LoadImage(imageLayout).execute(messageItem.getTempImages());
                ImageChat myImageChat = new ImageChat(messageItem.getImages());
                imageLayout.setAdapter(myImageChat);
            }
            else if (messageItem.hasImagesStr()) {
                imageLayout = (RecyclerView)row.findViewById(R.id.imagesLayout);
                imageLayout.setItemAnimator(null);

                int numcol = 1;
                if (messageItem.getImagesStr().size() > 1)
                    numcol = 2;

                imageLayout.setLayoutManager(new GridLayoutManager(context, numcol) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
//                new LoadImage(imageLayout).execute(messageItem.getTempImages());

                new DownloadImage(messageItem.getImagesStr(), new ConvertListener(){
                    @Override
                    public void onSuccess(ImageModel result) {

                    }

                    @Override
                    public void onDownloadSuccess(ArrayList<Bitmap> res) {
                        ImageChat myImageChat = new ImageChat(res);
                        messageItem.setImages(res);
                        imageLayout.setAdapter(myImageChat);
                    }
                }).execute();
            }

            message.setText(messageModels.get(position).getMessage());
        }
        else {
            row = inflater.inflate(R.layout.global_chat_custom_message_item,null);
            message = (TextView) row.findViewById(R.id.chatContent);
            name = (TextView) row.findViewById(R.id.name);
            imageView = (ImageView) row.findViewById(R.id.avatar);

            message.setText(messageModels.get(position).getMessage());
            name.setText(messageModels.get(position).getNickName());
            // set image
//            Log.i("Custom message user", messageModels.get(position).getAvatar());
            new HttpManager.GetImageFromServer(imageView).execute(messageModels.get(position).getAvatar());
//            imageView.setImageResource(messageModels.get(position).getAvatar());

        }
//        if(position== names.length-1) row.setPadding(0,0,0,120);
        return row;
    }

//     private class ViewHolder {
//         public RecyclerView imageLayout;
//         public TextView name;
//         public TextView message;

//         public ViewHolder(View convertView, boolean isMe) {
//             if (isMe) {
//                 message = convertView.findViewById(R.id.message);
//                 imageLayout = convertView.findViewById(R.id.imagesLayout);
//                 imageLayout.setLayoutManager(new GridLayoutManager(context, 2));
//             } else {
//                 message = convertView.findViewById(R.id.chatContent);
//                 name = convertView.findViewById(R.id.name);
// //                imageLayout = convertView.findViewById(R.id.imagesLayout);
//             }
//         }
//     }
}