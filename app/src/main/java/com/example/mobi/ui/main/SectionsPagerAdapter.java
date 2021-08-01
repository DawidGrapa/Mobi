package com.example.mobi.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mobi.tabs.Matches;
import com.example.mobi.tabs.Pairing;
import com.example.mobi.tabs.UserProfile;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @SuppressLint("SupportAnnotationUsage")
    @StringRes
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new UserProfile();
                break;
            case 1:
                fragment = new Pairing();
                break;
            case 2:
                fragment = new Matches();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        // Show total number of pages.
        return 3;
    }
}