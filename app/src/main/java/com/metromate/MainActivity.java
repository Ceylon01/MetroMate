package com.metromate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.res.ColorStateList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.metromate.fragments.FavoritesFragment;
import com.metromate.fragments.FareFragment;
import com.metromate.fragments.TimetableFragment;
import com.metromate.PathFinding.SearchActivity;
import com.metromate.PathFinding.QuickPathActivity;

public class MainActivity extends AppCompatActivity {

    private BottomSheetBehavior<View> bottomSheetBehavior;
    private DrawerLayout drawerLayout;
    private View toolbar;
    private View bottomNavigationView;
    private View searchBar;
    private Handler handler = new Handler();
    private boolean isFareTabClicked = false;  // 요금 탭 클릭 방지용 플래그

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

        // UI 요소 초기화
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchBar = findViewById(R.id.search_input);

        // ZoomableImageView 초기화
        ZoomableImageView subwayMapView = findViewById(R.id.subway_map_view);
        subwayMapView.post(() -> {
            subwayMapView.resetZoom(); // 초기 확대 비율을 0.3배로 설정
        });

        // 🟢 맵 터치 리스너 등록
        subwayMapView.setOnClickListener(v -> {
            toggleUIVisibility(true); // UI 보이기
            handler.postDelayed(() -> toggleUIVisibility(false), 3000); // 3초 후 다시 숨김
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

        // 드로어 메뉴 항목 클릭 리스너
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_notice) {
                startActivity(new Intent(MainActivity.this, NoticeActivity.class));
            } else if (item.getItemId() == R.id.menu_terms_of_service) {
                startActivity(new Intent(MainActivity.this, TermsOfServiceActivity.class));
            } else if (item.getItemId() == R.id.menu_privacy_policy) {
                startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
            }

            drawerLayout.closeDrawers(); // 드로어 닫기
            return true;
        });

        // 검색창 클릭 이벤트 추가
        EditText searchInput = findViewById(R.id.search_input);
        searchInput.setFocusable(false);
        searchInput.setFocusableInTouchMode(false);
        searchInput.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // "빠른 길찾기" 버튼 클릭 이벤트 추가
        View findPathButton = findViewById(R.id.find_path_button);
        findPathButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuickPathActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        int customPurpleColor = ContextCompat.getColor(this, R.color.indicatorColor);
        bottomNavigationView.setItemActiveIndicatorColor(ColorStateList.valueOf(customPurpleColor));

        if (savedInstanceState == null) {
            loadFragment(new FavoritesFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_favorites) {
                selectedFragment = new FavoritesFragment();
            } else if (item.getItemId() == R.id.nav_fare) {
                if (!isFareTabClicked) {  // 클릭을 막는 플래그 체크
                    isFareTabClicked = true;  // 클릭 처리
                    selectedFragment = new FareFragment();  // 요금 탭 클릭 시 FareFragment로 이동
                    handler.postDelayed(() -> isFareTabClicked = false, 500);  // 0.5초 후 클릭 방지 해제
                }
            } else if (item.getItemId() == R.id.nav_timetable) {
                selectedFragment = new TimetableFragment();
            }

            if (selectedFragment != null) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_sheet_content, fragment)
                .commit();
    }

    // 🟢 UI 숨기기 / 보이기 기능
    private void toggleUIVisibility(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        toolbar.setVisibility(visibility);
        bottomNavigationView.setVisibility(visibility);
        searchBar.setVisibility(visibility);
    }
}
