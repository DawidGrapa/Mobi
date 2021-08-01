package com.example.mobi.cards;

import com.example.mobi.user.User;

public class Card {
    private User user;

    public Card() {}

    public Card(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
