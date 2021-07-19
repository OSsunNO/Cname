package com.example.cname;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1_list frag1_list;
    private Frag2_send frag2_send;
    private Frag3_record frag3_record;
    private Frag4_user frag4_user;

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
                    case R.id.action_list:
                        setFrag(0);
                        break;
                    case R.id.action_sending:
                        setFrag(1);
                        break;
                    case R.id.action_record:
                        setFrag(2);
                        break;
                    case R.id.action_myNameCard:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

        frag1_list=new Frag1_list();
        frag2_send=new Frag2_send();
        frag3_record=new Frag3_record();
        frag4_user=new Frag4_user();
        setFrag(0); // 첫 프래그먼트 화면 지정
    }

    // 프레그먼트 교체
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.Main_Frame,frag1_list);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.Main_Frame,frag2_send);
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.Main_Frame,frag3_record);
                ft.commit();
                break;

            case 3:
                ft.replace(R.id.Main_Frame,frag4_user);
                ft.commit();
                break;

        }
    }
}