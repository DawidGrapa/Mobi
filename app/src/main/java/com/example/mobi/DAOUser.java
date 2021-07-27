package com.example.mobi;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DAOUser {

    FirebaseDatabase db;
    DatabaseReference reference;


    public DAOUser() {
        db = FirebaseDatabase.getInstance();
        reference = db.getReference(User.class.getSimpleName());
    }

    public void add(User user) {
        reference.push().setValue(user);
    }

}
