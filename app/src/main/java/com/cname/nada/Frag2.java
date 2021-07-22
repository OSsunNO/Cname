package com.cname.nada;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
                Intent intent = new Intent(getContext(),List_settings.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
