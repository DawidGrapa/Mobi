package com.example.mobi.matches;
import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobi.R;


public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView matchID;
    public ImageView image;

    @SuppressLint("SetTextI18n")
    public MatchesViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        image = itemView.findViewById(R.id.matchIMG);
        matchID = itemView.findViewById(R.id.matchID);
    }

    @Override
    public void onClick(View v) {

    }
}
