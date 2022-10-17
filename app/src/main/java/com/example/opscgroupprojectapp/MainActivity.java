package com.example.opscgroupprojectapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().setReorderingAllowed(true).add(R.id.WelcomeFrag, WelcomeFragment.class,null).commitNow();
        //FragmentTransaction ft = fm.beginTransaction();
        //ft.replace(R.id.frameLayout);
        //ft.commit();
    }


}