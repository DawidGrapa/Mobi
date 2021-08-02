package com.example.mobi.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mobi.arrayAdapter;
import com.example.mobi.cards.Card;

import com.example.mobi.R;
import com.example.mobi.user.DAOUser;
import com.example.mobi.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Pairing extends Fragment {

    List<Card> potentialMatches = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    String currentUid;
    View view;
    SwipeFlingAdapterView swipeFlingAdapterView;
    LinearLayout noMatch;
    User user;
    DAOUser daoUser;
    User potentialPair;

    public Pairing() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pairing, container, false);

        swipeFlingAdapterView = view.findViewById(R.id.frame);

        db = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();

        currentUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        getPotentialMatches();

        daoUser = new DAOUser();

        FirebaseDatabase.getInstance().getReference().child(User.class.getSimpleName()).child(daoUser.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "ResourceType"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = (User) snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        arrayAdapter = new arrayAdapter(view.getContext(), R.layout.item, potentialMatches);

        swipeFlingAdapterView.setAdapter(arrayAdapter);

        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                potentialMatches.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                checkIfArrayAdapterIsEmpty();
            }

            @Override
            public void onRightCardExit(Object o) {
                checkIfArrayAdapterIsEmpty();
                Card card = (Card) o;
                potentialPair = card.getUser();
                db.getReference().child(User.class.getSimpleName()).child(potentialPair.getUid()).child("Connections").child(currentUid).setValue(true);
                isConnectionMatch(potentialPair.getUid());
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });

        return view;
    }

    private void isConnectionMatch(String userID) {
        DatabaseReference currentUserDB = db.getReference().child(User.class.getSimpleName()).child(currentUid).child("Connections").child(userID);
        //db.getReference().child(User.class.getSimpleName()).
        if(!currentUid.equals(userID)){
            currentUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        Toast.makeText(getContext(), "You have new Pair!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void checkIfArrayAdapterIsEmpty() {
        if(arrayAdapter.isEmpty()) {
            noMatch = view.findViewById(R.id.noMatches);
            noMatch.setVisibility(View.VISIBLE);
        }
    }

    private void getPotentialMatches() {
        db.getReference().child(User.class.getSimpleName()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if(dataSnapshot.exists() && !dataSnapshot.getKey().equals(currentUid)) {//TODO
                    Card item = new Card((User) dataSnapshot.getValue(User.class));
                    potentialMatches.add(item);
                    arrayAdapter.notifyDataSetChanged();
                    noMatch = view.findViewById(R.id.noMatches);
                    noMatch.setVisibility(View.INVISIBLE);
                    checkIfArrayAdapterIsEmpty();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}