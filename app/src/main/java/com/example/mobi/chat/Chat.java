package com.example.mobi.chat;

public class Chat {
    private String message;
    private boolean sentByCurrentUser;

    public boolean isSentByCurrentUser() {
        return sentByCurrentUser;
    }

    public void setSentByCurrentUser(boolean sentByCurrentUser) {
        this.sentByCurrentUser = sentByCurrentUser;
    }

    public Chat(String message, boolean sentByCurrentUser) {
       this.message = message;
       this.sentByCurrentUser = sentByCurrentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
