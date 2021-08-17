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

public class Frag2 extends Fragment {
    private View view;
    private ImageView searchBtn, sendBtn, settingsInListBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag2,container,false);

        searchBtn = (ImageView) view.findViewById(R.id.SearchBtn);
        sendBtn = (ImageView) view.findViewById(R.id.SendBtn);
        settingsInListBtn = (ImageView) view.findViewById(R.id.SettingBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),List_search.class);
                startActivity(intent);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Send.class);
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
