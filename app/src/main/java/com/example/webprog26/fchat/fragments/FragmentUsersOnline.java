package com.example.webprog26.fchat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.webprog26.fchat.R;
import com.example.webprog26.fchat.interfaces.FirebaseChatListener;


/**
 * Created by webprog26 on 05.12.2016.
 */

public class FragmentUsersOnline extends Fragment {

    private static final String TAG = "FragmentUsersOnline";

    private ListView mListViewUsersOnline;

    private FirebaseChatListener mFirebaseChatListener;

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
        return inflater.inflate(R.layout.fragment_users_online, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListViewUsersOnline = (ListView) view.findViewById(R.id.lvUsersOnline);
        displayUsersOnline();
    }

    public void displayUsersOnline(){
        mFirebaseChatListener.onUsersStatusRead(mListViewUsersOnline);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFirebaseChatListener = null;
    }
}
