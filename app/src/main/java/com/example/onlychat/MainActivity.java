package com.example.onlychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;

public class MainActivity extends FragmentActivity implements Main_MainCallBacks{
    FragmentTransaction ft;
    MainHeader mainHeader;
    MainNavbar mainNavbar;
    MainContent mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ft = getSupportFragmentManager().beginTransaction();
        mainHeader = MainHeader.newInstance("header");
        ft.replace(R.id.main_header, mainHeader);
        ft.commit();

        ft = getSupportFragmentManager().beginTransaction();
        mainContent = MainContent.newInstance("content");
        ft.replace(R.id.main_content, mainContent);
        ft.commit();

        ft = getSupportFragmentManager().beginTransaction();
        mainNavbar = MainNavbar.newInstance("navbar");
        ft.replace(R.id.main_navbar, mainNavbar);
        ft.commit();
    }
}