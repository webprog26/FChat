package com.example.webprog26.fchat.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webprog26.fchat.R;
import com.example.webprog26.fchat.adapters.ViewPagerAdapter;
import com.example.webprog26.fchat.fragments.FragmentChat;
import com.example.webprog26.fchat.fragments.FragmentUsersOnline;
import com.example.webprog26.fchat.interfaces.FirebaseChatListener;
import com.example.webprog26.fchat.models.ChatMessage;
import com.example.webprog26.fchat.models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements FirebaseChatListener{

    private static final String TAG = "MainActivity_TAG";

    private static final int SIGN_IN_REQUEST_CODE = 101;

    private static final String FIREBASE_DATABASE_URL = "https://fir-2-e0ff8.firebaseio.com/";
    private static final String FIREBASE_DATABASE_MESSAGES = FIREBASE_DATABASE_URL + "messages";
    private static final String FIREBASE_DATABASE_USERS = FIREBASE_DATABASE_URL + "users";

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

        //If user is not an authentificated one, we providing this operation
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE);
        } else {
            //User is authentificated, greetings
            mUser = new User(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), true);
            Toast.makeText(this, getResources().getString(R.string.welcome, mUser.getUserName()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mUser != null){
            //App is in foreground, so user status changes to "online"
            mUser.setUserOnline(true);
            onUserStatusChanged(mUser);
        }
    }

    /**
     * Setting up {@link ViewPager}, adding Fragments, create an instance of {@link ViewPagerAdapter} and setting it to {@link ViewPager}
     * @param viewPager {@link ViewPager}
     */
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
                //Signing this user out
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(mUser != null){
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.bye, mUser.getUserName()), Toast.LENGTH_LONG).show();
                            //App may still be in foreground, but user status changes to "offline"
                            mUser.setUserOnline(false);
                            onUserStatusChanged(mUser);
                        }
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
                //App is in foreground, and user status changes to "online"
                onUserStatusChanged(mUser);
                Toast.makeText(this, getResources().getString(R.string.welcome, mUser.getUserName()), Toast.LENGTH_LONG).show();
                mFragmentChat.displayMessages();
                mFragmentUsersOnline.displayUsersOnline();
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
            //App is in background, so user status changes to "online"
            mUser.setUserOnline(false);
            onUserStatusChanged(mUser);
        }
    }

    @Override
    public void onSendMessage(String text) {
        FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_DATABASE_MESSAGES).push().setValue(new ChatMessage(text, mUser.getUserName()));
    }

    @Override
    public void onUserStatusChanged(User user) {
        FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_DATABASE_USERS).child(mUser.getUserName()).setValue(mUser);
    }

    @Override
    public void onMessagesRead(ListView listView) {
        FirebaseListAdapter<ChatMessage> adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message_item, FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_DATABASE_MESSAGES)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = (TextView) v.findViewById(R.id.tvMessageText);
                TextView messageUser = (TextView) v.findViewById(R.id.tvMessageUser);
                TextView messageTime = (TextView) v.findViewById(R.id.tvMessageTime);

                messageText.setText(model.getText());
                messageUser.setText(model.getUserName());

                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    public void onUsersStatusRead(ListView listView) {
        FirebaseListAdapter<User> adapter = new FirebaseListAdapter<User>(this, User.class, R.layout.user_online_item, FirebaseDatabase.getInstance().getReferenceFromUrl(FIREBASE_DATABASE_USERS)) {
            @Override
            protected void populateView(View v, User model, int position) {
                 TextView userOnlineName = (TextView) v.findViewById(R.id.tvUserOnline);
                 TextView userStatus = (TextView) v.findViewById(R.id.tvUserStatus);

                 userOnlineName.setText(model.getUserName());

                 if(model.isUserOnline()){
                     userStatus.setText(getResources().getString(R.string.online));
                 } else {
                    userStatus.setText(getResources().getString(R.string.offline));
                 }

            }
        };
        listView.setAdapter(adapter);
    }
}
