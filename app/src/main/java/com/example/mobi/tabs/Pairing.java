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
    DatabaseReference usersDB;
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
        usersDB = db.getReference().child(User.class.getSimpleName());
        currentUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        getPotentialMatches();

        daoUser = new DAOUser();

        usersDB.child(daoUser.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "ResourceType"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = (User) snapshot.getValue(User.class);
                lookUpForMatches();
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
                Card card = (Card) o;
                potentialPair = card.getUser();
                usersDB.child(potentialPair.getUid()).child("Connections").child("NOPE").child(currentUid).setValue(true);
            }

            @Override
            public void onRightCardExit(Object o) {
                checkIfArrayAdapterIsEmpty();
                Card card = (Card) o;
                potentialPair = card.getUser();
                usersDB.child(potentialPair.getUid()).child("Connections").child("YES").child(currentUid).setValue(true);
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

    private void lookUpForMatches() {
        usersDB.child(currentUid).child("Connections").child("NotifyAboutMatch").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                Toast.makeText(getContext(), "You have new pair!", Toast.LENGTH_SHORT).show();
                usersDB.child(currentUid).child("Connections").child("NotifyAboutMatch").removeValue();
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

    private void isConnectionMatch(String userID) {
        DatabaseReference currentUserDB = usersDB.child(currentUid).child("Connections").child("YES").child(userID);
        if(!currentUid.equals(userID)){
            currentUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        Toast.makeText(getContext(), "You have new Pair!", Toast.LENGTH_SHORT).show();
                        usersDB.child(userID).child("Connections").child("Matches").child(currentUid).setValue(true);
                        usersDB.child(currentUid).child("Connections").child("Matches").child(userID).setValue(true);
                        usersDB.child(userID).child("Connections").child("NotifyAboutMatch").child(userID).setValue(true);
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
        usersDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                if(dataSnapshot.exists() && !dataSnapshot.getKey().equals(currentUid) && !dataSnapshot.child("Connections").child("YES").hasChild(currentUid) && !dataSnapshot.child("Connections").child("NOPE").hasChild(currentUid)) {//TODO
                    Card item = new Card((User) dataSnapshot.getValue(User.class));
                    potentialMatches.add(item);
                    arrayAdapter.notifyDataSetChanged();
                    noMatch = view.findViewById(R.id.noMatches);
                    noMatch.setVisibility(View.INVISIBLE);
                    checkIfArrayAdapterIsEmpty();
                }
                checkIfArrayAdapterIsEmpty();
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