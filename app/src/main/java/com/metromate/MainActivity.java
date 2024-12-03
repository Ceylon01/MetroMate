package com.metromate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.res.ColorStateList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.metromate.fragments.BookmarksFragment;
import com.metromate.fragments.FareFragment;
import com.metromate.fragments.HomeFragment;
import com.metromate.fragments.TimetableFragment;
import com.metromate.PathFinding.SearchActivity;
import com.metromate.PathFinding.QuickPathActivity;

public class MainActivity extends AppCompatActivity {

    private BottomSheetBehavior<View> bottomSheetBehavior;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom Sheet 초기화
        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // 중간 상태 높이 설정 (화면 높이의 1/2)
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int halfScreenHeight = screenHeight / 2;

        // Bottom Sheet 초기 상태 설정
        bottomSheetBehavior.setPeekHeight(halfScreenHeight); // 중간 상태 높이 설정
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // 중간 상태로 시작
        bottomSheetBehavior.setHideable(true); // 완전히 숨길 수 있도록 설정

        // Bottom Sheet 상태 변경 리스너 추가
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // 완전히 확장된 상태
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // 중간 상태
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    // 완전히 숨겨진 상태
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // 슬라이드 중
            }
        });

        // 드로어 메뉴 초기화
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // 메뉴 버튼 클릭 이벤트
        ImageView menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            if (drawerLayout != null) {
                drawerLayout.openDrawer(navigationView); // 드로어 메뉴 열기
            }
        });

        // 검색창 클릭 이벤트 추가
        EditText searchInput = findViewById(R.id.search_input); // 검색창

        // 검색창 클릭 시 키보드가 뜨지 않도록 설정
        searchInput.setFocusable(false);
        searchInput.setFocusableInTouchMode(false);

        searchInput.setOnClickListener(v -> {
            // 검색 화면으로 이동
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // "빠른 길찾기" 버튼 클릭 이벤트 추가
        View findPathButton = findViewById(R.id.find_path_button);
        findPathButton.setOnClickListener(v -> {
            // QuickPathActivity로 이동
            Intent intent = new Intent(MainActivity.this, QuickPathActivity.class);
            startActivity(intent);
        });

        // BottomNavigationView 초기화
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Active Indicator 색상 변경
        int customPurpleColor = ContextCompat.getColor(this, R.color.indicatorColor); // 원하는 색상
        bottomNavigationView.setItemActiveIndicatorColor(ColorStateList.valueOf(customPurpleColor));


        // Default 화면으로 HomeFragment 설정
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // BottomNavigationView 버튼 클릭 리스너
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment(); // HomeFragment 로드
            } else if (item.getItemId() == R.id.nav_fare) {
                selectedFragment = new FareFragment(); // FareFragment 로드
            } else if (item.getItemId() == R.id.nav_timetable) {
                selectedFragment = new TimetableFragment(); // TimetableFragment 로드
            } else if (item.getItemId() == R.id.nav_bookmark) { // 이름 변경 반영
                selectedFragment = new BookmarksFragment(); // BookmarksFragment 로드
            }

            if (selectedFragment != null) {
                // 숨겨진 상태라면 중간 단계로 복귀
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                // Fragment 로드
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    // Fragment 로드 메서드
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_sheet_content, fragment) // `R.id.bottom_sheet_content`가 FragmentContainerView ID여야 함
                .commit();
    }
}
