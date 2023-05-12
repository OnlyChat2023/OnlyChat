package com.example.onlychat.DirectMessage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.onlychat.GlobalChat.ListMessage.RecyclerItemClickListener;
import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.ImageModel;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.RoomModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;
import com.example.onlychat.ViewLargerImageMessage.ViewLargerImageMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MessageReceive extends ArrayAdapter<MessageModel> {
    Context context; Bitmap avatar; ArrayList<MessageModel> message; String me_id;
    RecyclerView imageLayout;
    GlobalPreferenceManager pref;
    UserModel myInfo;
    RoomModel roomModel;

    public MessageReceive(Context context, String me_id, RoomModel message) {
        super(context, R.layout.main_chat_content_item, message.getMessages());
        this.message = message.getMessages();
        this.roomModel = message;
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

                imageLayout.addOnItemTouchListener(new RecyclerItemClickListener(context, imageLayout, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, ViewLargerImageMessage.class);
                        intent.putExtra("data", messageItem.getImages().get(position));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
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

                                imageLayout.addOnItemTouchListener(new RecyclerItemClickListener(context, imageLayout, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        System.out.println("HERE1");
                                        Intent intent = new Intent(context, ViewLargerImageMessage.class);
                                        intent.putExtra("data", messageItem.getImages().get(position));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        view.getContext().startActivity(intent);
                                    }

                                    @Override
                                    public void onLongItemClick(View view, int position) {

                                    }
                                }));
                            }
                        });
                    }
                }).execute();
            }

            msg.setText(messageItem.getMessage());

            SimpleDateFormat writeDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
            String s = writeDate.format(messageItem.getTime());

            time.setText("Sent at " + s);
            time.setVisibility(View.GONE);
            return row;
        } else {
            row = inflater.inflate(R.layout.chat_message_receive, null);
            TextView msg = (TextView) row.findViewById(R.id.chatContent);
            TextView time = (TextView) row.findViewById(R.id.timeMessage);
            // set image
//            new HttpManager.GetImageFromServer(avt).execute(this.avatar);

            if (roomModel.hasBitmapAvatar()) {
                ImageView imageView = (ImageView) row.findViewById(R.id.avatar);
                imageView.setImageBitmap(roomModel.getBitmapAvatar());
            }
            else if (roomModel.hasAvatar()){
                ArrayList<String> userAvt = new ArrayList<String>();
                userAvt.add(roomModel.getAvatar());
                new DownloadImage(userAvt, new ConvertListener() {
                    @Override
                    public void onSuccess(ImageModel result) {

                    }

                    @Override
                    public void onDownloadSuccess(ArrayList<Bitmap> result) {
                        for (int i = 0; i < result.size(); ++i) {
                            roomModel.setBitmapAvatar(result.get(i));
                            ImageView imageView = (ImageView) row.findViewById(R.id.avatar);
                            imageView.setImageBitmap(result.get(i));
                        }
                    }
                }).execute();
            }

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
                imageLayout.addOnItemTouchListener(new RecyclerItemClickListener(context, imageLayout, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, ViewLargerImageMessage.class);
                        intent.putExtra("data", messageItem.getImages().get(position));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
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
                                imageLayout.addOnItemTouchListener(new RecyclerItemClickListener(context, imageLayout, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        System.out.println("HERE1");
                                        Intent intent = new Intent(context, ViewLargerImageMessage.class);
                                        intent.putExtra("data", messageItem.getImages().get(position));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        view.getContext().startActivity(intent);
                                    }

                                    @Override
                                    public void onLongItemClick(View view, int position) {

                                    }
                                }));
                            }
                        });
                    }
                }).execute();
            }

            msg.setText(messageItem.getMessage());

            SimpleDateFormat writeDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
            String s = writeDate.format(messageItem.getTime());

            time.setText("Sent at " + s);
            time.setVisibility(View.GONE);

            return row;
        }
    }

    public void AddMessage(MessageModel msg){
        this.message.add(msg);
        this.notifyDataSetChanged();
    }
}
