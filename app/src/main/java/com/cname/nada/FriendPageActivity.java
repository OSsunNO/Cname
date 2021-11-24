package com.cname.nada;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cname.nada.functions.RecyclerViewAdapterInFrag1AndFriendPage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendPageActivity extends AppCompatActivity {

    private TextView nameCardIntroduction;
    private final String TAG = this.getClass().getSimpleName();
    private TextView name, belong, position, call, email;
    private JSONArray careerInfoJsonArray, userInfoJsonArray, belongInfoJsonArray;
    private Button callBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_page);

        callBtn = findViewById(R.id.CallBtn);

        Intent intent = getIntent();
        String url1 = "http://ec2-3-37-249-141.ap-northeast-2.compute.amazonaws.com:8080/namecard/my/" + intent.getStringExtra("UserName") + "/";

        RequestQueue queue = Volley.newRequestQueue(FriendPageActivity.this);

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
                        Toast toast = Toast.makeText(FriendPageActivity.this, "유저 정보가 정상적으로 전송되지 않습니다.", Toast.LENGTH_LONG);
                        toast.show();

                        error.printStackTrace();
                        Log.d(TAG, "Post Fail");
                    }
                });

        queue.add(jsonObjectRequest);

        nameCardIntroduction = findViewById(R.id.nameCardIntroduction);
        name = findViewById(R.id.nameOnNameCard);
        belong = findViewById(R.id.belongOnNameCard);
        position = findViewById(R.id.positionOnNameCard);
        call = findViewById(R.id.callNumberOnNameCard);
        email = findViewById(R.id.emailOnNameCard);

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
        }, 80);

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
                RecyclerView recyclerView = findViewById(R.id.FriendCareerRecyclerView2);
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendPageActivity.this));

                // 리사이클러뷰에 RecyclerViewAdapter 객체 지정.
                RecyclerViewAdapterInFrag1AndFriendPage adapter = new RecyclerViewAdapterInFrag1AndFriendPage(list);
                recyclerView.setAdapter(adapter);

                Toast toast = Toast.makeText(FriendPageActivity.this, "유저 정보가 서버로 전송되었습니다.", Toast.LENGTH_LONG);
                toast.show();

                // adapter.setOnItemClickListener(new RecyclerViewAdapterInFrag2.OnItemClickListener() {
                //  @Override
                //  public void onItemClick(View v, int pos) {
                //    Intent intent = new Intent(getContext(), FriendPageActivity.class);
                //    startActivity(intent);
                //  }
                //});
            }
        }, 180);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject element4 = new JSONObject();
                element4 = (JSONObject) userInfoJsonArray.opt(0);
                String phoneNum = "tel:" + element4.optString("phone_num");
                Intent call = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNum));
                startActivity(call);


            }
        });


    }
}