package com.example.mobi.matches;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobi.R;
import com.example.mobi.chat.ChatActivity;
import com.example.mobi.user.User;


public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView pairName;
    public ImageView image;
    public String pairImage;
    public String pairID;

    @SuppressLint("SetTextI18n")
    public MatchesViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        image = itemView.findViewById(R.id.matchIMG);
        pairName = itemView.findViewById(R.id.matchID);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("pairID", pairID);
        bundle.putString("pairImage", pairImage);
        intent.putExtras(bundle);
        v.getContext().startActivity(intent);
    }
}
