package com.example.onlychat.GlobalChat.ListMessage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.onlychat.EditProfile.EditProfile;
import com.example.onlychat.Interfaces.ConvertListener;
import com.example.onlychat.Interfaces.Member;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Model.ImageModel;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.Model.MessageModel;
import com.example.onlychat.Model.UserModel;
import com.example.onlychat.R;
import com.example.onlychat.ViewLargerImageMessage.ViewLargerImageMessage;

import java.util.ArrayList;
import java.util.Collections;

public class CustomMessageItem extends ArrayAdapter<MessageModel> {

    ArrayList<MessageModel> messageModels;
    GlobalPreferenceManager pref;
    UserModel myInfo;
    Context context;
    TextView message;
    RecyclerView imageLayout;
    TextView name;
    ImageView imageView;
    ArrayList<Member> members;

    public CustomMessageItem(Context context, ArrayList<MessageModel> messageModels) {
        super(context, R.layout.global_chat_custom_chat_item, messageModels);
        this.context = context;
        this.messageModels = messageModels;

        pref = new GlobalPreferenceManager(context);
        myInfo = pref.getUserModel();
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final MessageModel messageItem = messageModels.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row;
        if (messageModels.get(position).getUserId().equals(myInfo.getId())) {
            row = inflater.inflate(R.layout.global_chat_custom_message_item_me,null);
            message = (TextView)row.findViewById(R.id.message);

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
                new DownloadImage(messageItem.getImagesStr(), new ConvertListener(){
                    @Override
                    public void onSuccess(ImageModel result) {

                    }

                    @Override
                    public void onDownloadSuccess(ArrayList<Bitmap> res) {
                        row.post(new Runnable() {
                            @Override
                            public void run() {
//                                System.out.println("RUN FUN");
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

            message.setText(messageModels.get(position).getMessage());
        }
        else {
            row = inflater.inflate(R.layout.global_chat_custom_message_item,null);
            message = (TextView) row.findViewById(R.id.chatContent);
            name = (TextView) row.findViewById(R.id.name);

            if (!messageItem.hasAvatar() && !messageItem.hasBitmapAvatar()){
                messageItem.setAvatar("avatar/bot.png");
                messageItem.setNickName("Bot");
            }

            if (messageItem.hasBitmapAvatar()) {
                imageView = (ImageView) row.findViewById(R.id.avatar);
                imageView.setImageBitmap(messageItem.getBitmapAvatar());
            }
//            else if (messageItem.hasAvatar()){
//                ArrayList<String> userAvt = new ArrayList<String>();
//                userAvt.add(messageItem.getAvatar());
//                new DownloadImage(userAvt, new ConvertListener() {
//                    @Override
//                    public void onSuccess(ImageModel result) {
//
//                    }
//
//                    @Override
//                    public void onDownloadSuccess(ArrayList<Bitmap> result) {
//                        for (int i = 0; i < result.size(); ++i) {
//                            messageItem.setBitmapAvatar(result.get(i));
//                            imageView = (ImageView) row.findViewById(R.id.avatar);
//                            imageView.setImageBitmap(result.get(i));
//                        }
//                    }
//                }).execute();
//            }

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
            message.setText(messageModels.get(position).getMessage());
            name.setText(messageModels.get(position).getNickName());
        }

        return row;
    }

    public void setMembers(ArrayList<Member> members, CustomMessageItem customMessageItem) {
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            new DownloadImage(new ArrayList<>(Collections.singleton(member.getAvatar())), new ConvertListener() {
                @Override
                public void onSuccess(ImageModel result) {

                }

                @Override
                public void onDownloadSuccess(ArrayList<Bitmap> result) {
                    for (int i = 0; i < result.size(); ++i) {
                        member.setAvatarBitmap(result.get(i));
                        for (MessageModel messageItem : messageModels) {
                            if (messageItem.getUserId().equals(member.getUser_id())) {
                                Log.i("setMembers: ", "123");
                                messageItem.setBitmapAvatar(member.getAvatarBitmap());
                            }
                        }
                    }
                    customMessageItem.notifyDataSetChanged();
                }
            }).execute();
        }

        this.members = members;
    }
}