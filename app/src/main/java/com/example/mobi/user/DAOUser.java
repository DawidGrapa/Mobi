package com.example.mobi.user;

import com.example.mobi.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class DAOUser {

    FirebaseDatabase db;

    FirebaseAuth firebaseAuth;

    public DAOUser() {
        db = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void add(User user) {
        String id = firebaseAuth.getUid();
        assert id != null;
        user.setUid(id);
        db.getReference().child(User.class.getSimpleName()).child(id).setValue(user);
    }

    public void changeLogin(String login) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(login).build();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.updateProfile(profileUpdates);
    }

    public String getUid() {
        return firebaseAuth.getUid();
    }

    public void updateLikedUsers(User user) {
        db.getReference().child(User.class.getSimpleName()).child(user.getUid()).child("LikedUsers").updateChildren(user.getLikedUsers());
    }
}
