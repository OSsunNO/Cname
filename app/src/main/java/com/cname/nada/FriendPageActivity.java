package com.cname.nada;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cname.nada.functions.CurrentFriendID;

public class FriendPageActivity extends AppCompatActivity {

    private String userId = CurrentFriendID.getFriendId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_page);


    }
}