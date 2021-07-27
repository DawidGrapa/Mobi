package com.example.mobi;

import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class DAOHuman {

    FirebaseDatabase db;
    DatabaseReference reference;


    public DAOHuman() {
        db = FirebaseDatabase.getInstance();
        reference = db.getReference(Human.class.getSimpleName());

        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Human>> t = new GenericTypeIndicator<HashMap<String, Human>>() {};
                HashMap<String, Human> humanList = dataSnapshot.getValue(t);
                for(String key : humanList.keySet()) {
                    Log.w("dane", humanList.get(key).getFirstName());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("asd","Failed to read value.", error.toException());
            }
        });

    }

    public void add(Human human) {
        reference.push().setValue(human);
    }

}
