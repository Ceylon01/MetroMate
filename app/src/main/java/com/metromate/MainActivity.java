package com.metromate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.metromate.fragments.FareFragment;
import com.metromate.fragments.HomeFragment;
import com.metromate.fragments.SettingsFragment;
import com.metromate.fragments.TimetableFragment;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 초기화 상태 확인
        if (!isInitializationComplete()) {
            // 초기화가 완료되지 않았다면 DataInitializationActivity 실행
            Intent intent = new Intent(this, DataInitializationActivity.class);
            startActivity(intent);
            finish(); // MainActivity 종료
            return;
        }

        // 초기화가 완료된 경우, 레이아웃 설정
        setContentView(R.layout.activity_main);

        // UI 및 네비게이션 초기화
        setupBottomNavigation();
        setupBottomSheet();

        // 첫 화면: HomeFragment로 초기화
        if (savedInstanceState == null) {
            switchFragment(new HomeFragment());
        }
    }

    private boolean isInitializationComplete() {
        // 초기화 여부 확인 (SharedPreferences 사용)
        return getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getBoolean("isInitialized", false);
    }

    private void setupBottomNavigation() {
        // BottomNavigationView와 Fragment 초기화
        fragmentMap.put(R.id.nav_home, new HomeFragment());
        fragmentMap.put(R.id.nav_timetable, new TimetableFragment());
        fragmentMap.put(R.id.nav_fare, new FareFragment());
        fragmentMap.put(R.id.nav_settings, new SettingsFragment());

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = fragmentMap.get(item.getItemId());
            if (selectedFragment != null) {
                switchFragment(selectedFragment);
            }
            return true;
        });
    }

    private void setupBottomSheet() {
        // BottomSheetBehavior 초기화
        View bottomSheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // BottomSheet 기본 설정
        bottomSheetBehavior.setPeekHeight(300); // 최소 높이
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // 접힌 상태

        // BottomSheet 상태 변경 리스너
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // 확장 상태 처리
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // 접힘 상태 처리
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // 슬라이드 중 처리
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        // 프래그먼트 전환
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_sheet_content, fragment)
                .commit();
    }
}
