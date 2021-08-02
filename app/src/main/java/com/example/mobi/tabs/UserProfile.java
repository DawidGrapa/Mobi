package com.example.mobi.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobi.user.DAOUser;
import com.example.mobi.R;
import com.example.mobi.user.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class UserProfile extends Fragment {

    public UserProfile() {
    }

    User user;
    ImageView userImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_user_profile, container, false); //pass the correct layout name for the fragment

        DAOUser daoUser = new DAOUser();

        FirebaseDatabase.getInstance().getReference().child(User.class.getSimpleName()).child(daoUser.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = (User) snapshot.getValue(User.class);
                TextView name = (TextView) view.findViewById(R.id.helloUserProfile);
                TextView desc = (TextView) view.findViewById(R.id.userProfileDescription);
                userImage = (ImageView) view.findViewById(R.id.userProfileImage);



                desc.setText(user.getDescription());
                name.setText("Hello " + user.getFirstName());

                if (user.getImageUri() == null && user.getSex() == null) {
                    userImage.setImageResource(R.drawable.ic_user);
                } else if (user.getSex().equals("Male") && user.getImageUri() == null) {
                    userImage.setImageResource(R.drawable.ic_man);
                } else if (user.getSex().equals("Female") && user.getImageUri() == null) {
                    userImage.setImageResource(R.drawable.ic_woman);
                } else if (user.getImageUri() != null){
                    Glide.with(view.getContext()).load(user.getImageUri()).into(userImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }



}