package com.example.mobi.chat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobi.R;
import com.example.mobi.user.User;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private List<Chat > chatList;
    private Context context;

    public ChatAdapter(List<Chat> matchesList, Context context) {
        this.chatList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View recentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);

        ChatViewHolder rcv = new ChatViewHolder(recentView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.message.setText(chatList.get(position).getMessage());
        if(chatList.get(position).isSentByCurrentUser()) {
            holder.container.setGravity(Gravity.END);
            holder.message.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_bg_current));
        } else {
            holder.message.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_bg));
            holder.container.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
