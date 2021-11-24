package com.cname.nada;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cname.nada.functions.RecyclerViewAdapterInFrag2;
import com.cname.nada.functions.UserID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Frag2 extends Fragment {
    private View view;
    private ImageView searchBtn, sendBtn, settingsInListBtn;
    private String url1 = "http://ec2-3-37-249-141.ap-northeast-2.compute.amazonaws.com:8080/view/friend/" + UserID.getUserId() + "/";
    private final String TAG = this.getClass().getSimpleName();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag2,container,false);

        ArrayList<ArrayList<String>> list = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(getContext());

        try {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url1, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            for (int i = 0; i < response.length(); i++) {
                                ArrayList<String> innerArrayList = new ArrayList<>();
                                try {
                                    JSONObject jsonObject = new JSONObject(response.getString(i));
                                    innerArrayList.add(jsonObject.getString("id"));
                                    innerArrayList.add(jsonObject.getString("name"));
                                    innerArrayList.add(jsonObject.getString("belong_data"));
                                    innerArrayList.add(jsonObject.getString("position_data"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                list.add(innerArrayList);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            error.printStackTrace();
                            Log.d(TAG, "Post Fail");
                        }
                    });

            queue.add(jsonArrayRequest);
        }catch (Exception e){ e.printStackTrace();}

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                RecyclerView recyclerView = view.findViewById(R.id.recycler1);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                // 리사이클러뷰에 RecyclerViewAdapter 객체 지정.
                RecyclerViewAdapterInFrag2 adapter = new RecyclerViewAdapterInFrag2(getContext(), list);
                recyclerView.setAdapter(adapter);

                // adapter.setOnItemClickListener(new RecyclerViewAdapterInFrag2.OnItemClickListener() {
                  //  @Override
                  //  public void onItemClick(View v, int pos) {
                    //    Intent intent = new Intent(getContext(), FriendPageActivity.class);
                    //    startActivity(intent);
                  //  }
                //});
            }
        }, 300);


        searchBtn = (ImageView) view.findViewById(R.id.SearchBtn);
        sendBtn = (ImageView) view.findViewById(R.id.SendBtn);
        settingsInListBtn = (ImageView) view.findViewById(R.id.SettingBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListSearchActivity.class);
                startActivity(intent);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SendActivity.class);
                startActivity(intent);
            }
        });

        settingsInListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                Menu menu = popup.getMenu();

                inflater.inflate(R.menu.listmenu, menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                break;
                            case R.id.block:
                                break;
                            case R.id.tmenu:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
    });
        return view;
    }

}
