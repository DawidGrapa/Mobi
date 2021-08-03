package com.example.mobi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.mobi.cards.Card;
import com.example.mobi.tabs.Pairing;
import com.example.mobi.user.User;


import org.w3c.dom.Text;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Card> {

    public arrayAdapter(@NonNull Context context, int resource, List<Card> cards) {
        super(context, resource, cards);
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {
        Card item = getItem(position);
        User user = item.getUser();

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.pairImage);
        TextView name = (TextView) convertView.findViewById(R.id.pairName);
        TextView desc = convertView.findViewById(R.id.pairDesc);

        if(user.getAge() == null) {
            name.setText(user.getFirstName());
        } else {
            name.setText(user.getFirstName() + ", " +user.getAge());
        }


        if (user.getImageUri() == null && user.getSex() == null) {
            img.setImageResource(R.drawable.ic_user);
        } else if (user.getSex().equals("Male") && user.getImageUri() == null) {
            img.setImageResource(R.drawable.ic_man);
        } else if (user.getSex().equals("Female") && user.getImageUri() == null) {
            img.setImageResource(R.drawable.ic_woman);
        } else if (user.getImageUri() != null){
            Glide.with(convertView.getContext()).load(user.getImageUri()).into(img);
        }

        if(user.getDescription()!=null) {
            if(user.getDescription().length() > 100)
                desc.setText(user.getDescription().substring(0, 100));
        }

        return convertView;
    }
}
