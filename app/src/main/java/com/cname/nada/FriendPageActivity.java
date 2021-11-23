package com.cname.nada;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FriendPageActivity extends AppCompatActivity {

    private static android.app.Fragment cardFrontFragment;
    private static android.app.Fragment cardBackFragment;
    private TextView nameCardIntroduction;
    private boolean showingBack;
    private FrameLayout nameCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_page);

        nameCardIntroduction = (TextView) findViewById(R.id.nameCardIntroduction);
        nameCardView = (FrameLayout) findViewById(R.id.container2);

        showingBack = false;


    }
}