package com.cname.nada;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cname.nada.functions.ChangeStartFrag;

public class Frag4 extends Fragment {
    private View view;
    private Button changeStartFragBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag4, container, false);
        changeStartFragBtn = (Button) view.findViewById(R.id.changeFragNumButton);
        changeStartFragBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                Menu menu = popup.getMenu();

                inflater.inflate(R.menu.menulist, menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ChangeStartFrag csf = new ChangeStartFrag();
                        switch (item.getItemId()) {
                            case R.id.action_myNameCard:
                                Toast.makeText(getContext(), "안녕", Toast.LENGTH_SHORT).show();
                                csf.changeStartFrag("action_myNameCard");
                                break;
                            case R.id.action_list:
                                csf.changeStartFrag("action_list");
                                break;
                            case R.id.action_record:
                                csf.changeStartFrag("action_record");
                                break;
                            case R.id.action_more:
                                csf.changeStartFrag("action_more");
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