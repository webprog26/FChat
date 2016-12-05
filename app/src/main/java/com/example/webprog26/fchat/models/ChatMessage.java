package com.example.webprog26.fchat.models;

import java.util.Date;

/**
 * Created by webprog26 on 05.12.2016.
 */

public class ChatMessage {

    private String mText;
    private String mUserName;
    private long messageTime;

    public ChatMessage(String text, String userName) {
        this.mText = text;
        this.mUserName = userName;
        this.messageTime = new Date().getTime();
    }

    public ChatMessage() {
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
