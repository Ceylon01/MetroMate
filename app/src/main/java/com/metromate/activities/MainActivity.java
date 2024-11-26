package com.metromate.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.metromate.R;
import com.metromate.fragments.FareFragment;
import com.metromate.fragments.HomeFragment;
import com.metromate.fragments.SettingsFragment;
import com.metromate.fragments.TimetableFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BottomSheetBehavior 초기화
        View bottomSheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // BottomSheet 기본 설정
        bottomSheetBehavior.setPeekHeight(300); // BottomSheet 최소 높이
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // 기본 상태: 접힘

        // BottomSheet 상태 변경 리스너
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // BottomSheet가 확장되었을 때 처리
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // BottomSheet가 접힘 상태로 돌아왔을 때 처리
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // BottomSheet 슬라이드 중 처리
                // slideOffset: 0.0 (접힘) ~ 1.0 (완전 확장)
            }
        });

        // Fragment Map 초기화
        fragmentMap.put(R.id.nav_home, new HomeFragment());
        fragmentMap.put(R.id.nav_timetable, new TimetableFragment());
        fragmentMap.put(R.id.nav_fare, new FareFragment());
        fragmentMap.put(R.id.nav_settings, new SettingsFragment());

        // BottomNavigationView 초기화
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = fragmentMap.get(item.getItemId());
            if (selectedFragment != null) {
                switchFragment(selectedFragment);
            }
            return true;
        });

        // 첫 화면: HomeFragment로 초기화
        if (savedInstanceState == null) {
            switchFragment(new HomeFragment());
        }
    }

    // 프래그먼트 전환 메서드
    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_sheet_content, fragment)
                .commit();
    }
}
