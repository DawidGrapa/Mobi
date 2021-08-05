package com.example.mobi.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mobi.matches.MatchesFragment;
import com.example.mobi.pairing.PairingFragment;
import com.example.mobi.user.UserProfileFragment;

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
                fragment = new UserProfileFragment();
                break;
            case 1:
                fragment = new PairingFragment();
                break;
            case 2:
                fragment = new MatchesFragment();
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