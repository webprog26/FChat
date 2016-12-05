package com.example.webprog26.fchat.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.webprog26.fchat.R;
import com.example.webprog26.fchat.adapters.ViewPagerAdapter;
import com.example.webprog26.fchat.fragments.FragmentChat;
import com.example.webprog26.fchat.fragments.FragmentUsersOnline;
import com.example.webprog26.fchat.interfaces.FirebaseChatListener;
import com.example.webprog26.fchat.models.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements FirebaseChatListener{

    private static final String TAG = "MainActivity_TAG";

    private static final int SIGN_IN_REQUEST_CODE = 101;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private User mUser;

    private FragmentChat mFragmentChat;
    private FragmentUsersOnline mFragmentUsersOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentChat = new FragmentChat();
        mFragmentUsersOnline = new FragmentUsersOnline();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE);
        } else {
            mUser = new User(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), true);
            Log.i(TAG, "Welcome, " + mUser.getUserName());
            mFragmentChat.displayMessages();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mUser != null){
            mUser.setUserOnline(true);
            onUserStatusChanged(mUser);
        }
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mFragmentChat, getResources().getString(R.string.chat));
        adapter.addFragment(mFragmentUsersOnline, getResources().getString(R.string.users_online));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionSignOut:
                Log.i(TAG, "actionSignOut");
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivityForResult(AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .build(), SIGN_IN_REQUEST_CODE);
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                mUser = new User(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), true);
                onUserStatusChanged(mUser);
                Log.i(TAG, "Welcome, " + mUser.getUserName());
                mFragmentChat.displayMessages();
            } else {
                Toast.makeText(this, getResources().getString(R.string.unable_to_sign_in), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mUser != null){
            mUser.setUserOnline(false);
            onUserStatusChanged(mUser);
        }
    }

    @Override
    public void onSendMessage(String text) {
        Log.i(TAG, "ready to send message " + text + " from " + mUser.getUserName());
//        FirebaseDatabase.getInstance().getReferenceFromUrl()
    }

    @Override
    public void onUserStatusChanged(User user) {
        Log.i(TAG, user.getUserName() + " status changed to " + user.isUserOnline());
    }
}
