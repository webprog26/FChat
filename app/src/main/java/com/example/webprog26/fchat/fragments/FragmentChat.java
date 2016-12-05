package com.example.webprog26.fchat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.webprog26.fchat.R;

/**
 * Created by webprog26 on 05.12.2016.
 */

public class FragmentChat extends Fragment {

    private static final String TAG = "FragmentChat";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void displayMessages(){
        Log.i(TAG, "displayMessages()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
