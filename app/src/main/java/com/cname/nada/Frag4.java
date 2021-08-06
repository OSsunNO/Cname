package com.cname.nada;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.OutputStream;

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
                        try{
                            OutputStream outFs = getActivity().openFileOutput("startFragNum.txt", Context.MODE_PRIVATE);
                            switch (item.getItemId()) {
                                case R.id.action_myNameCard:
                                    outFs.write("0".getBytes());
                                    outFs.close();
                                    break;
                                case R.id.action_list:
                                    outFs.write("1".getBytes());
                                    outFs.close();
                                    break;
                                case R.id.action_record:
                                    outFs.write("2".getBytes());
                                    outFs.close();
                                    break;
                                case R.id.action_more:
                                    outFs.write("3".getBytes());
                                    outFs.close();
                                    break;
                            }
                        } catch (IOException e) { }
                        return false;
                    }
                });
                popup.show();
            }
        });
        return view;
    }
}