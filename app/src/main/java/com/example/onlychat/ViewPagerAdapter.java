package com.example.onlychat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new frag_profile1();
            case 1:
                return new frag_profile2();
            case 2:
                return new frag_profile3();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle (int position) {
        String title = "";
        switch (position) {
            case 0:
                return title = "Information";
            case 1:
                return title = "Socials";
            case 2:
                return title = "Description";
        }
        return title;
    }

}
