package com.example.webprog26.fchat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.webprog26.fchat.R;
import com.example.webprog26.fchat.interfaces.FirebaseChatListener;

/**
 * Created by webprog26 on 05.12.2016.
 */

public class FragmentChat extends Fragment {


    private static final String TAG = "FragmentChat";


    private EditText mEtInputText;

    private FirebaseChatListener mFirebaseChatListener;
    private ListView mListViewMessages;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FirebaseChatListener){
            mFirebaseChatListener = (FirebaseChatListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEtInputText= (EditText) view.findViewById(R.id.etInput);
        FloatingActionButton btnSendChatMessage = (FloatingActionButton) view.findViewById(R.id.btnSend);
        btnSendChatMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseChatListener.onSendMessage(mEtInputText.getText().toString());
                mEtInputText.setText("");
            }
        });
        mListViewMessages = (ListView) view.findViewById(R.id.lvMessages);
        displayMessages();
    }

    /**
     * Calling host {@link android.app.Activity} onMessagesRead({@link ListView}) method
     * to display chat messages
     */
    public void displayMessages(){
        mFirebaseChatListener.onMessagesRead(mListViewMessages);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFirebaseChatListener = null;
    }
}
