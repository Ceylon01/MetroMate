package com.example.metro_mate.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.metro_mate.R
import com.example.metro_mate.databinding.ActivityMainBinding
import com.example.metro_mate.fragments.FavoritesFragment
import com.example.metro_mate.fragments.FareCalculatorFragment
import com.example.metro_mate.fragments.TimetableFragment
import com.example.metro_mate.fragments.SettingsFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Sheet에 모서리 둥글게 설정
        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setTopLeftCornerSize(16f)  // 각 모서리의 반지름 설정
            .setTopRightCornerSize(16f)
            .build()

        val materialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            fillColor = getColorStateList(R.color.colorPrimaryAccent)
        }

        binding.bottomSheet.background = materialShapeDrawable

        // Bottom Sheet Behavior 설정
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED // 초기 상태 설정

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
