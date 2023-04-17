package com.example.onlychat.Profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.onlychat.Profile.ProfileCustomFragment.profile_description;
import com.example.onlychat.Profile.ProfileCustomFragment.profile_information;
import com.example.onlychat.Profile.ProfileCustomFragment.profile_social;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new profile_information();
            case 1:
                return new profile_social();
            case 2:
                return new profile_description();
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
                return title = "Social media";
            case 2:
                return title = "Description";
        }
        return title;
    }

}
