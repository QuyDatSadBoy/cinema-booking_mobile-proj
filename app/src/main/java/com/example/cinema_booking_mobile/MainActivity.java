package com.example.cinema_booking_mobile;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.cinema_booking_mobile.fragment.HomeFragment;
import com.example.cinema_booking_mobile.fragment.SearchFragment;
import com.example.cinema_booking_mobile.fragment.adapter.MainFragmentAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    MainFragmentAdapter mainFragmentAdapter;
    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new SearchFragment());
        fragmentManager = getSupportFragmentManager();
        mainFragmentAdapter = new MainFragmentAdapter(
                fragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );
        mainFragmentAdapter.setFragments(fragments);
        viewPager.setAdapter(mainFragmentAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: {
                        bottomNavigationView.getMenu().findItem(R.id.mHome).setChecked(true);
                        break;
                    }
                    case 1: {
                        bottomNavigationView.getMenu().findItem(R.id.mSearch).setChecked(true);
                        break;
                    }
                    case 2: {
                        bottomNavigationView.getMenu().findItem(R.id.mAccount).setChecked(true);
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (String.valueOf(item.getTitle())) {
                    case "Trang chủ": {
                        viewPager.setCurrentItem(0);
                        break;
                    }
                    case "Tìm kiếm": {
                        viewPager.setCurrentItem(1);
                        break;
                    }
                    case "Trang cá nhân": {
                        viewPager.setCurrentItem(2);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            super.onBackPressed();
        }
    }
}