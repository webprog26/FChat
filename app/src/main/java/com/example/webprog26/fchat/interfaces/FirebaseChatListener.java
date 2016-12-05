package com.example.webprog26.fchat.interfaces;

import android.widget.ListView;
import com.example.webprog26.fchat.models.User;


/**
 * Created by webprog26 on 05.12.2016.
 */

public interface FirebaseChatListener {

    /**
     * Sends message to {@link com.google.firebase.database.FirebaseDatabase}
     * @param text {@link String}
     */
    public void onSendMessage(String text);

    /**
     * Updates userStatus (true/ false) in {@link com.google.firebase.database.FirebaseDatabase}
     * @param user {@link User}
     */
    public void onUserStatusChanged(User user);

    /**
     * Receives {@link com.example.webprog26.fchat.models.ChatMessage} objects from
     * {@link com.google.firebase.database.FirebaseDatabase} and types it in {@link ListView}
     * via {@link com.firebase.ui.database.FirebaseListAdapter}
     * @param listView {@link ListView}
     */
    public void onMessagesRead(ListView listView);

    /**
     * Receives {@link com.example.webprog26.fchat.models.User} objects from
     * {@link com.google.firebase.database.FirebaseDatabase} and types it in {@link ListView}
     * via {@link com.firebase.ui.database.FirebaseListAdapter}
     * @param listView {@link ListView}
     */
    public void onUsersStatusRead(ListView listView);
}
