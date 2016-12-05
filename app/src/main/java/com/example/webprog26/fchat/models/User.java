package com.example.webprog26.fchat.models;

/**
 * Created by webprog26 on 05.12.2016.
 */

public class User {

    private String mUserName;
    private boolean isUserOnline;

    public User(String mUserName, boolean isUserOnline) {
        this.mUserName = mUserName;
        this.isUserOnline = isUserOnline;
    }

    //Empty constructor in class-model is Firebase compulsory condition
    public User() {
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public boolean isUserOnline() {
        return isUserOnline;
    }

    public void setUserOnline(boolean userOnline) {
        isUserOnline = userOnline;
    }
}
