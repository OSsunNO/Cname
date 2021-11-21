package com.cname.nada;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cname.nada.functions.CurrentFriendID;
import com.cname.nada.functions.RecyclerViewAdapterInFrag2;

import java.util.ArrayList;

public class Frag2 extends Fragment {
    private View view;
    private ImageView searchBtn, sendBtn, settingsInListBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag2,container,false);

        //리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        for (int i=0; i<100; i++) {
            ArrayList<String> innerList = new ArrayList<>();
            innerList.add(String.format("&d", i));
            innerList.add(String.format("name %d", i));
            innerList.add(String.format("belong %d", i));
            innerList.add(String.format("position %d", i));
            list.add(innerList);
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = view.findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 리사이클러뷰에 RecyclerViewAdapter 객체 지정.
        RecyclerViewAdapterInFrag2 adapter = new RecyclerViewAdapterInFrag2(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapterInFrag2.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, String userId) {
                CurrentFriendID.setFriendId(userId);
                Intent intent = new Intent(getContext(), FriendPageActivity.class);
                startActivity(intent);
            }
        });

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
