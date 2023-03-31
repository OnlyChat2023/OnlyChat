package com.example.onlychat.MainScreen;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.example.onlychat.MainScreen.Interface.Main_MainCallBacks;
import com.example.onlychat.MainScreen.Fragment.MainContent;
import com.example.onlychat.MainScreen.Fragment.MainHeader;
import com.example.onlychat.MainScreen.Fragment.MainNavbar;
import com.example.onlychat.MainScreen.Fragment.CustomizeChatItem;
import com.example.onlychat.R;

public class MainScreen extends FragmentActivity implements Main_MainCallBacks{
     FragmentTransaction ft;
     MainHeader mainHeader;
     MainNavbar mainNavbar;
     MainContent mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.main_screen);

         ft = getSupportFragmentManager().beginTransaction();
         mainHeader = MainHeader.newInstance("header");
         ft.replace(R.id.main_header, mainHeader);
         ft.commit();

         ft = getSupportFragmentManager().beginTransaction();
         mainContent = MainContent.newInstance("content");
         ft.replace(R.id.main_content, mainContent);
         ft.commit();
//
//         ft = getSupportFragmentManager().beginTransaction();
//         mainNavbar = MainNavbar.newInstance("navbar");
//         ft.replace(R.id.main_navbar, mainNavbar);
//         ft.commit();
    }
}