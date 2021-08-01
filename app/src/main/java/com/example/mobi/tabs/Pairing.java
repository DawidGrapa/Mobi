package com.example.mobi.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobi.cards.Card;
import com.example.mobi.user.DAOUser;
import com.example.mobi.R;

import java.util.ArrayList;
import java.util.List;

public class Pairing extends Fragment {

    private DAOUser daoUser;
    List<Card> potentialMatches = new ArrayList<>();

    public Pairing() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pairing, container, false);
    }
}