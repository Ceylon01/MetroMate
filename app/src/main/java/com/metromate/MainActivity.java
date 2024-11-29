package com.metromate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.metromate.fragments.FareFragment;
import com.metromate.fragments.HomeFragment;
import com.metromate.fragments.SettingsFragment;
import com.metromate.fragments.TimetableFragment;
import com.metromate.fare.FareCalculationActivity;
import com.metromate.PathFinding.PathFindingActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isInitializationComplete()) {
            Intent intent = new Intent(this, DataInitActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // 추가된 버튼 설정
        setupButtons();

        // 기존 기능: BottomNavigation 및 BottomSheet 설정
        setupBottomNavigation();
        setupBottomSheet();

        // 첫 화면 초기화
        if (savedInstanceState == null) {
            switchFragment(new HomeFragment());
        }
    }

    private boolean isInitializationComplete() {
        return getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getBoolean("isInitialized", false);
    }

    private void setupButtons() {
        // 길찾기 및 요금 계산 버튼 설정
        ImageButton findPathButton = findViewById(R.id.find_path_button);
        ImageButton fareCalculationButton = findViewById(R.id.nav_fare);

        findPathButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PathFindingActivity.class);
            startActivity(intent);
        });

        fareCalculationButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FareCalculationActivity.class);
            startActivity(intent);
        });
    }

    private void setupBottomNavigation() {
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
        View bottomSheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setPeekHeight(300);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // 상태 변경 처리
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // 슬라이드 중 처리
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_sheet_content, fragment)
                .commit();
    }
}
