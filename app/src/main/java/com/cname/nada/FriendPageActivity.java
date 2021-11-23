package com.cname.nada;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cname.nada.functions.CurrentFriendID;

public class FriendPageActivity extends AppCompatActivity {

    private static android.app.Fragment cardFrontFragment;
    private static android.app.Fragment cardBackFragment;
    private String userId = CurrentFriendID.getFriendId();
    private TextView nameCardIntroduction;
    private boolean showingBack;
    private FrameLayout nameCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_page);

        nameCardIntroduction = (TextView) findViewById(R.id.nameCardIntroduction);
        nameCardView = (FrameLayout) findViewById(R.id.container);

        showingBack = false;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, cardFrontFragment).commit();
            // R.id.container, new FriendPageActivity.CardFrontFragment()
        }

        nameCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
                if (showingBack == true) {
                    showingBack = false;
                } else {
                    showingBack = true;
                }
            }
        });
    }

    private void flipCard() {
        if(showingBack) {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.animator.card_flip_right_in,
                            R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in,
                            R.animator.card_flip_left_out)
                    .replace(R.id.container, cardFrontFragment).commit();
            return;
        }

        // Flip to the back.

        // Create and commit a new fragment transaction that adds the fragment for
        // the back of the card, uses custom animations, and is part of the fragment
        // manager's back stack.

        getFragmentManager().beginTransaction()
                // Replace the default fragment animations with animator resources
                // representing rotations when switching to the back of the card, as
                // well as animator resources representing rotations when flipping
                // back to the front (e.g. when the system Back button is pressed).
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                // Replace any fragments currently in the container view with a
                // fragment representing the next page (indicated by the
                // just-incremented currentPage variable).
                .replace(R.id.container, cardBackFragment)

                // Add this transaction to the back stack, allowing users to press
                // Back to get to the front of the card.

                // Commit the transaction.
                .commit();
    }

    public class CardFlipActivity extends FragmentActivity {
        // A fragment representing the front of the card.
        public class CardFrontFragment extends Fragment {
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                return inflater.inflate(R.layout.fragment_card_front, container, false);
            }
        }

        // A fragment representing the back of the card.
        public class CardBackFragment extends Fragment {
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                return inflater.inflate(R.layout.fragment_card_back, container, false);
            }
        }
    }
}