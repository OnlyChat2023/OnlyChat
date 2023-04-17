package com.example.onlychat.DirectMessage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlychat.Adapter.ImageChat;
import com.example.onlychat.Async.DownloadImage;
import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.ImageModel;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;

import java.util.ArrayList;
import java.util.Date;

public class MessageReceive extends ArrayAdapter<MessageModel> {
    Context context; String avatar; ArrayList<MessageModel> message; String me_id;
    RecyclerView imageLayout;
    GlobalPreferenceManager pref;
    UserModel myInfo;

    public MessageReceive(Context context, String avatar, String me_id, ArrayList<MessageModel> message) {
        super(context, R.layout.main_chat_content_item, message);
        this.avatar = avatar;
        this.message = message;
        this.me_id = me_id;
        this.context = context;

        pref = new GlobalPreferenceManager(context);
        myInfo = pref.getUserModel();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MessageModel messageItem = this.message.get(position);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View row;
        if (messageItem.getUserId().equals(this.me_id)) {
            row = inflater.inflate(R.layout.chat_message_send, null);
            TextView msg = (TextView) row.findViewById(R.id.messageSend);
            TextView time = (TextView) row.findViewById(R.id.timeMessage);

            if (messageItem.hasImages()) {
                imageLayout = (RecyclerView)row.findViewById(R.id.imagesLayout);
                imageLayout.setItemAnimator(null);

                int num_col = messageItem.getImages().size() > 1 ? 2 : 1;

                imageLayout.setLayoutManager(new GridLayoutManager(context, num_col) {
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
//                new LoadImage(imageLayout).execute(messageItem.getTempImages());

                new DownloadImage(messageItem.getImagesStr(), new ConvertListener(){
                    @Override
                    public void onSuccess(ImageModel result) {

                    }

                    @Override
                    public void onDownloadSuccess(ArrayList<Bitmap> res) {
                        row.post(new Runnable() {
                            @Override
                            public void run() {
                                messageItem.setImages(res);

                                imageLayout = (RecyclerView)row.findViewById(R.id.imagesLayout);
                                imageLayout.setItemAnimator(null);

                                int num_col = messageItem.getImagesStr().size() > 1 ? 2 : 1;

                                imageLayout.setLayoutManager(new GridLayoutManager(context, num_col) {
                                    @Override
                                    public boolean canScrollVertically() {
                                        return false;
                                    }
                                });
                                imageLayout.setAdapter(new ImageChat(res));
                            }
                        });
                    }
                }).execute();
            }

            msg.setText(messageItem.getMessage());

            Date timeMsg = messageItem.getTime();
            time.setText("Sent at " + timeMsg.getHours() + ":" + timeMsg.getMinutes() + " " + timeMsg.getDate());
            time.setVisibility(View.GONE);
            return row;
        } else {
            row = inflater.inflate(R.layout.chat_message_receive, null);
            TextView msg = (TextView) row.findViewById(R.id.chatContent);
            ImageView avt = (ImageView) row.findViewById(R.id.avatar);
            TextView time = (TextView) row.findViewById(R.id.timeMessage);
            // set image
            new HttpManager.GetImageFromServer(avt).execute(this.avatar);

            if (messageItem.hasImages()) {
                imageLayout = (RecyclerView)row.findViewById(R.id.imagesLayout);
                imageLayout.setItemAnimator(null);

                int num_col = messageItem.getImages().size() > 1 ? 2 : 1;

                imageLayout.setLayoutManager(new GridLayoutManager(context, num_col) {
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
//                new LoadImage(imageLayout).execute(messageItem.getTempImages());
                new DownloadImage(messageItem.getImagesStr(), new ConvertListener(){
                    @Override
                    public void onSuccess(ImageModel result) {

                    }

                    @Override
                    public void onDownloadSuccess(ArrayList<Bitmap> res) {
                        row.post(new Runnable() {
                            @Override
                            public void run() {
                                messageItem.setImages(res);

                                imageLayout = (RecyclerView)row.findViewById(R.id.imagesLayout);
                                imageLayout.setItemAnimator(null);

                                int num_col = messageItem.getImagesStr().size() > 1 ? 2 : 1;

                                imageLayout.setLayoutManager(new GridLayoutManager(context, num_col) {
                                    @Override
                                    public boolean canScrollVertically() {
                                        return false;
                                    }
                                });
                                imageLayout.setAdapter(new ImageChat(res));
                            }
                        });
                    }
                }).execute();
            }

            msg.setText(messageItem.getMessage());
            Date timeMsg = messageItem.getTime();
            time.setText("Sent at " + timeMsg.getHours() + ":" + timeMsg.getMinutes() + " " + timeMsg.getDate());
            time.setVisibility(View.GONE);

            return row;
        }
    }

    public void AddMessage(MessageModel msg){
        this.message.add(msg);
        this.notifyDataSetChanged();
    }
}
