package com.cname.nada;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class Frag4 extends Fragment implements View.OnClickListener {
    GoogleSignInClient mGoogleSignInClient;
    private final String TAG = this.getClass().getSimpleName();
    private View view;
    private Button changeStartFragBtn, loginOutBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag4, container, false);
        changeStartFragBtn = (Button) view.findViewById(R.id.changeFragNumButton);
        loginOutBtn = (Button) view.findViewById(R.id.logoutButton);

        changeStartFragBtn.setOnClickListener(this);
        loginOutBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeFragNumButton:
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
                break;
            case R.id.logoutButton:
                File file = new File("/data/data/com.cname.nada/files/userId.txt");
                // 파일이 존재하는지 체크
                if(file.exists()) {
                    file.delete();
                } else {
                    Toast.makeText(getContext(), "파일이 삭제되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}