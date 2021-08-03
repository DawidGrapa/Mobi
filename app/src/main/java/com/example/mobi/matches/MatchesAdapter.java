package com.example.mobi.matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobi.R;
import com.example.mobi.user.User;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolder> {

    private List<User> matchesList;
    private Context context;

    public MatchesAdapter(List<User> matchesList, Context context) {
        this.matchesList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View recentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_card, parent, false);

        MatchesViewHolder rcv = new MatchesViewHolder(recentView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolder holder, int position) {
        User user = matchesList.get(position);
        holder.matchID.setText(user.getFirstName());
        if (user.getImageUri() == null && user.getSex() == null) {
            holder.image.setImageResource(R.drawable.ic_user);
        } else if (user.getSex().equals("Male") && user.getImageUri() == null) {
            holder.image.setImageResource(R.drawable.ic_man);
        } else if (user.getSex().equals("Female") && user.getImageUri() == null) {
            holder.image.setImageResource(R.drawable.ic_woman);
        } else if (user.getImageUri() != null){
            Glide.with(context).load(user.getImageUri()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
