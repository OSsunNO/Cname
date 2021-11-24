package com.cname.nada;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cname.nada.functions.RecyclerViewAdapterInFrag1AndFriendPage;
import com.cname.nada.functions.RecyclerViewAdapterInFrag2;
import com.cname.nada.functions.UserID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class Frag1 extends Fragment {
    private View view;
    private TextView nameCardIntroduction;
    private TextView name, belong, position, call, email;
    private ImageView editBtn;
    private Button addCareerBtn, sendBtn;
    private String url1 = "http://ec2-3-37-249-141.ap-northeast-2.compute.amazonaws.com:8080/namecard/my/" + UserID.getUserId() + "/";
    private final String TAG = this.getClass().getSimpleName();

    private JSONArray careerInfoJsonArray, userInfoJsonArray, belongInfoJsonArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag1,container,false);

        nameCardIntroduction = (TextView) view.findViewById(R.id.nameCardIntroduction);
        editBtn = (ImageView) view.findViewById(R.id.EditBtn);
        sendBtn = (Button) view.findViewById(R.id.SendBtn);
        addCareerBtn = (Button) view.findViewById(R.id.plusCareerBtn);

        // https://huiveloper.tistory.com/7 이거보고 recyclerview item 누르면 여기 페이지로 이동하는 거 구현함
        String url1 = "http://ec2-3-37-249-141.ap-northeast-2.compute.amazonaws.com:8080/namecard/my/" + UserID.getUserId() + "/";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        careerInfoJsonArray = response.optJSONArray("career_info");
                        userInfoJsonArray = response.optJSONArray("user_info");
                        belongInfoJsonArray = response.optJSONArray("belong_info");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getContext(), "유저 정보가 정상적으로 전송되지 않습니다.", Toast.LENGTH_LONG);
                        toast.show();

                        error.printStackTrace();
                        Log.d(TAG, "Post Fail");
                    }
                });

        queue.add(jsonObjectRequest);

        name = view.findViewById(R.id.nameOnNameCard);
        belong = view.findViewById(R.id.belongOnNameCard);
        position = view.findViewById(R.id.positionOnNameCard);
        call = view.findViewById(R.id.callNumberOnNameCard);
        email = view.findViewById(R.id.emailOnNameCard);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                JSONObject element1 = new JSONObject();
                JSONObject element2 = new JSONObject();
                element1 = (JSONObject) userInfoJsonArray.opt(0);
                element2 = (JSONObject) belongInfoJsonArray.opt(0);

                nameCardIntroduction.setText(element1.optString("name") + "님의 명함");
                name.setText(element1.optString("name"));
                belong.setText(element2.optString("belong_data"));
                position.setText(element2.optString("Position_data"));
                call.setText(element2.optString("tel_data"));
                email.setText(element1.optString("email"));
            }
        }, 150);



        Handler mHandler1 = new Handler();
        mHandler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<ArrayList<String>> list = new ArrayList<>();

                for (int i = 0; i < careerInfoJsonArray.length(); i++) {
                    JSONObject element3 = (JSONObject) careerInfoJsonArray.opt(i);
                    ArrayList<String> innerArrayList = new ArrayList<>();
                    innerArrayList.add(element3.optString("careerCategory"));
                    innerArrayList.add(element3.optString("careerTitle"));
                    innerArrayList.add(element3.optString("careerStartDate"));

                    list.add(innerArrayList);
                }

                // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                RecyclerView recyclerView = view.findViewById(R.id.CareerRecyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                // 리사이클러뷰에 RecyclerViewAdapter 객체 지정.
                RecyclerViewAdapterInFrag1AndFriendPage adapter = new RecyclerViewAdapterInFrag1AndFriendPage(list);
                recyclerView.setAdapter(adapter);

                // adapter.setOnItemClickListener(new RecyclerViewAdapterInFrag2.OnItemClickListener() {
                //  @Override
                //  public void onItemClick(View v, int pos) {
                //    Intent intent = new Intent(getContext(), FriendPageActivity.class);
                //    startActivity(intent);
                //  }
                //});
            }
        }, 250);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SendActivity.class);
                startActivity(intent);
            }
        });

        addCareerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddCareer.class);
                startActivity(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MyProfileEditPage.class);
                startActivity(intent);
            }
        });
        return view;
    }
}












//        카드 뒤집는거. 야심차게 만들었지만 데이터 가져오는 데 어려움을 겪어 실패
//
//    private boolean showingBack;
//    private FrameLayout nameCardView;
//
//showingBack = false;
//
//        if(savedInstanceState == null) {
//            getFragmentManager().beginTransaction().add(R.id.container, new CardFrontFragment()).commit();
//        }
//
//        nameCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                flipCard();
//                if (showingBack == true) {
//                    showingBack = false;
//                } else {
//                    showingBack = true;
//                }
//            }
//        });
//
//        return view;
//    }
//
//    private void flipCard() {
//        if(showingBack) {
//            getFragmentManager().beginTransaction()
//                    .setCustomAnimations(
//                            R.animator.card_flip_right_in,
//                            R.animator.card_flip_right_out,
//                            R.animator.card_flip_left_in,
//                            R.animator.card_flip_left_out)
//                    .replace(R.id.container, new CardFrontFragment()).commit();
//
//            return;
//        }
//
//        // Flip to the back.
//
//        // Create and commit a new fragment transaction that adds the fragment for
//        // the back of the card, uses custom animations, and is part of the fragment
//        // manager's back stack.
//
//        getFragmentManager().beginTransaction()
//                // Replace the default fragment animations with animator resources
//                // representing rotations when switching to the back of the card, as
//                // well as animator resources representing rotations when flipping
//                // back to the front (e.g. when the system Back button is pressed).
//                .setCustomAnimations(
//                        R.animator.card_flip_right_in,
//                        R.animator.card_flip_right_out,
//                        R.animator.card_flip_left_in,
//                        R.animator.card_flip_left_out)
//                // Replace any fragments currently in the container view with a
//                // fragment representing the next page (indicated by the
//                // just-incremented currentPage variable).
//                .replace(R.id.container, new CardBackFragment())
//
//                // Add this transaction to the back stack, allowing users to press
//                // Back to get to the front of the card.
//
//                // Commit the transaction.
//                .commit();
//    }
//
//    // A fragment representing the front of the card.
//    public static class CardFrontFragment extends Fragment {
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View v = inflater.inflate(R.layout.fragment_card_front, container, false);
//            TextView name, belong, position, call, email;
//
//            name = v.findViewById(R.id.nameOnNameCard);
//            belong = v.findViewById(R.id.belongOnNameCard);
//            position = v.findViewById(R.id.positionOnNameCard);
//            call = v.findViewById(R.id.callNumberOnNameCard);
//            email = v.findViewById(R.id.emailOnNameCard);
//
//            JSONObject element1, element2;
//            element1 = (JSONObject) userInfoJsonArray.opt(0);
//            element2 = (JSONObject) belongInfoJsonArray.opt(0);
//
//            name.setText(element1.optString("name"));
//            belong.setText(element2.optString("belong_data"));
//            position.setText(element2.optString("position_data"));
//            call.setText(element2.optString("tel_data"));
//            email.setText(element1.optString("email"));
//
//            return v;
//        }
//    }
//
//    // A fragment representing the back of the card.
//    public static class CardBackFragment extends Fragment {
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            return inflater.inflate(R.layout.fragment_card_back, container, false);
//        }