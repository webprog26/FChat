package com.example.webprog26.fchat.interfaces;

import android.widget.ListView;
import com.example.webprog26.fchat.models.User;


/**
 * Created by webprog26 on 05.12.2016.
 */

public interface FirebaseChatListener {

    public void onSendMessage(String text);
    public void onUserStatusChanged(User user);
    public void onMessagesRead(ListView listView);
    public void onUsersStatusRead(ListView listView);
}
