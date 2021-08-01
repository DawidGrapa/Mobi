package com.example.mobi.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobi.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Matches#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Matches extends Fragment {

    public Matches() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_matches, container, false);
    }
}