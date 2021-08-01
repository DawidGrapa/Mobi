package com.example.mobi;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobi.LoginRegister.LoginActivity;
import com.example.mobi.databinding.ActivityLoggedInBinding;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;


import com.example.mobi.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;


public class LoggedIn extends AppCompatActivity {

    private ActivityLoggedInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoggedInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(1);

        tabs.getTabAt(0).setIcon(R.drawable.ic_user);
        tabs.getTabAt(1).setIcon(R.drawable.ic_fire);
        tabs.getTabAt(2).setIcon(R.drawable.ic_speak);



    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}