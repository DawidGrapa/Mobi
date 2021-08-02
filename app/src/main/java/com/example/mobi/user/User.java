package com.example.mobi.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class User {

    private String firstName;
    private String email;
    private String description;
    private String age;
    private String sex;
    private String imageUri;
    private HashMap<String, Object> likedUsers = new HashMap<>();
    private String uid;

    public void setLikedUsers(HashMap<String, Object> likedUsers) {
        if(likedUsers != null)
            this.likedUsers = likedUsers;
    }

    public HashMap<String, Object> getLikedUsers() {
        return likedUsers;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public User(String firstName, String email) {
        this.firstName = firstName;
        this.email = email;
    }

    public User() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void addLikedUser(User user) {
        likedUsers.put(user.getUid(), user);
    }

    public boolean containsMeAsLiked(User user) {
        return likedUsers.containsKey(user.getUid());
    }

}
