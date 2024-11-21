package com.example.metromate;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.metromate.fragments.FareFragment;
import com.example.metromate.fragments.HomeFragment;
import com.example.metromate.fragments.TimetableFragment;
import com.example.metromate.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BottomNavigationView 초기화
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        // 첫 화면: 홈 화면 표시
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }

    // BottomNavigationView 아이템 선택 리스너 구현
    private final BottomNavigationView.OnItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                // 메뉴 ID에 따라 프래그먼트 전환
                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.nav_timetable) {
                    selectedFragment = new TimetableFragment();
                } else if (item.getItemId() == R.id.nav_fare) {
                    selectedFragment = new FareFragment();
                } else if (item.getItemId() == R.id.nav_settings) {
                    selectedFragment = new SettingsFragment();
                }

                // 선택된 프래그먼트를 화면에 표시
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                return true;
            };
}
