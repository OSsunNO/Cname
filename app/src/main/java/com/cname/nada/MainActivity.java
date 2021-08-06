package com.cname.nada;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰 수정한다.
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1 frag1;
    private Frag2 frag2;
    private Frag3 frag3;
    private Frag4 frag4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.action_myNameCard:
                        setFrag(0);
                        break;

                    case R.id.action_list:
                        setFrag(1);
                        break;

                    case R.id.action_record:
                        setFrag(2);
                        break;

                    case R.id.action_more:
                        setFrag(3);
                }
                return true;
            }
        });

        frag1=new Frag1();
        frag2=new Frag2();
        frag3=new Frag3();
        frag4=new Frag4();

        String startFragNum = null;
        FileInputStream inFs;
        try {
            inFs = openFileInput("startFragNum.txt");
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            startFragNum = (new String(txt)).trim();
            switch (startFragNum) {
                case "0":
                    bottomNavigationView.setSelectedItemId(R.id.action_myNameCard);
                    break;
                case "1":
                    bottomNavigationView.setSelectedItemId(R.id.action_list);
                    break;
                case "2":
                    bottomNavigationView.setSelectedItemId(R.id.action_record);
                    break;
                case "3":
                    bottomNavigationView.setSelectedItemId(R.id.action_more);
                    break;
            }
        } catch (IOException e) {
            bottomNavigationView.setSelectedItemId(R.id.action_list); //첫 프래그먼트 화면 지정
        }
    }

    // 프레그먼트 교체
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.Main_Frame,frag1);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.Main_Frame,frag2);
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.Main_Frame,frag3);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.Main_Frame,frag4);
                ft.commit();
                break;

        }
    }
}