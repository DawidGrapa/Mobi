package com.example.mobi.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobi.user.DAOUser;
import com.example.mobi.R;
import com.example.mobi.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends Fragment {

    public UserProfile() {
    }

    User user;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_user_profile, container, false); //pass the correct layout name for the fragment

        DAOUser daoUser = new DAOUser();
        progressBar = view.findViewById(R.id.userProgressBar);
        FirebaseDatabase.getInstance().getReference().child(User.class.getSimpleName()).child(daoUser.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = (User) snapshot.getValue(User.class);
                TextView text = (TextView) view.findViewById(R.id.helloUserProfile);

                text.setText("Hello " + user.getFirstName() + "\nEmail: " + user.getEmail());
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }



}