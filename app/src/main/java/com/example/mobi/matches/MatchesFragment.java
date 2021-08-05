package com.example.mobi.matches;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MatchesFragment extends Fragment {

    FirebaseDatabase db;
    DatabaseReference usersDB;
    FirebaseAuth mAuth;
    String currentUid;
    View view;
    User user;
    DAOUser daoUser;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    List<User> matchesList;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_matches, container, false);
        matchesList = new ArrayList<>();

        db = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        usersDB = db.getReference().child(User.class.getSimpleName());
        currentUid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();


        daoUser = new DAOUser();

        getUser();

        getMatches();

        setRecyclerView();

        return view;
    }

    private List<User> getMatches() {

        usersDB.child(currentUid).child("Connections").child("Matches").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                    getPair(snapshot.getKey());
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
        return matchesList;

    }

    private void getPair(String pairID) {
        usersDB.child(pairID).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    User newUser = matchesList.stream()
                            .filter(p -> p.getUid().equals(snapshot.getKey()))
                            .findAny().orElse(null);
                    if(newUser == null) {
                        matchesList.add(snapshot.getValue(User.class));
                        if (!matchesList.isEmpty()) {
                            RelativeLayout relativeLayout = view.findViewById(R.id.noMatches);
                            relativeLayout.setVisibility(View.INVISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUser() {
        usersDB.child(currentUid).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "ResourceType"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = (User) snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setRecyclerView() {
        recyclerView = view.findViewById(R.id.matchesRecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        manager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(manager);
        adapter = new MatchesAdapter(matchesList, view.getContext());


        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

}
