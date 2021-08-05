package com.example.mobi.chat;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobi.R;


public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView message;
    public LinearLayout container;


    @SuppressLint("SetTextI18n")
    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        container = itemView.findViewById(R.id.messageViewContainer);
        message = itemView.findViewById(R.id.viewMessage);
    }

    @Override
    public void onClick(View v) {

    }
}
