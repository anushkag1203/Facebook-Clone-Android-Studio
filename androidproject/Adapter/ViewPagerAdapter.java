package com.example.androidproject.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.androidproject.Fragement.NotificationFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:return new NotificationFragment();

            default:return new NotificationFragment();
        }

    }

    @Override
    public int getCount() {


        return 1;
    }

    public CharSequence getPageTitle(int position){

        String title=null;
        if(position==0) {
            title = "Notification";
        }

        return title;
}}