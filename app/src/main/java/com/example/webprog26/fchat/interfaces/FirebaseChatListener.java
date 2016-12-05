package com.example.webprog26.fchat.interfaces;

import android.widget.ListView;

import com.example.webprog26.fchat.models.ChatMessage;
import com.example.webprog26.fchat.models.User;
import com.firebase.ui.database.FirebaseListAdapter;

/**
 * Created by webprog26 on 05.12.2016.
 */

public interface FirebaseChatListener {

    public void onSendMessage(String text);
    public void onUserStatusChanged(User user);
    public void omMessagesRead(ListView listView);
}
