package com.example.metro_mate.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.metro_mate.R
import com.example.metro_mate.databinding.ActivityMainBinding
import com.example.metro_mate.fragments.FavoritesFragment
import com.example.metro_mate.fragments.FareCalculatorFragment
import com.example.metro_mate.fragments.TimetableFragment
import com.example.metro_mate.fragments.SettingsFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Sheet Behavior 설정
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED // 초기 상태 설정

        // Bottom Sheet 동작 콜백 추가
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // 상태가 변경되었을 때의 동작
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // Bottom Sheet가 완전히 펼쳐졌을 때 동작
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // Bottom Sheet가 닫혔을 때 동작
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        // Bottom Sheet가 드래그 중일 때 동작
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        // Bottom Sheet가 절반만 펼쳐졌을 때 동작
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        // Bottom Sheet가 완전히 숨겨진 상태 (hideable=true로 설정되어 있어야 동작)
                    }

                    BottomSheetBehavior.STATE_SETTLING -> {
                        // Bottom Sheet가 특정 상태로 이동하는 중간 과정 상태
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Bottom Sheet가 드래그로 슬라이드 중일 때 동작
                // 예: 슬라이드 오프셋에 따라 map_container의 투명도 조정
                binding.mapContainer.alpha = 1 - slideOffset
            }
        })

        // 초기 화면: FavoritesFragment 표시
        if (savedInstanceState == null) {
            replaceFragment(FavoritesFragment())
        }

        // BottomNavigationView 클릭 이벤트 처리
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    replaceFragment(FavoritesFragment())
                    true
                }
                R.id.nav_fare -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    replaceFragment(FareCalculatorFragment())
                    true
                }
                R.id.nav_timetable -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    replaceFragment(TimetableFragment())
                    true
                }
                R.id.nav_settings -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    replaceFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

    // 프래그먼트 전환 함수
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet, fragment)
            .commit()
    }
}