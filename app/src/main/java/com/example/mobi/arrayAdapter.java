package com.example.mobi;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mobi.cards.Card;
import com.example.mobi.tabs.Pairing;
import com.example.mobi.user.User;


import org.w3c.dom.Text;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Card> {

    public arrayAdapter(@NonNull Context context, int resource, List<Card> cards) {
        super(context, resource, cards);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Card item = getItem(position);
        User user = item.getUser();

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        ImageView img = (ImageView) convertView.findViewById(R.id.pairImage);
        TextView name = (TextView) convertView.findViewById(R.id.pairName);
        img.setImageResource(R.drawable.ic_anxiety);
        name.setText(user.getFirstName());

        return convertView;
    }
}
