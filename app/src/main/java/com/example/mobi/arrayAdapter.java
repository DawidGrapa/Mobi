package com.example.mobi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.mobi.cards.Card;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Card> {

    Context context;

    public arrayAdapter(@NonNull Context context, int resource, List<Card> cards) {
        super(context, resource, cards);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Card item = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        return convertView;
    }
}
