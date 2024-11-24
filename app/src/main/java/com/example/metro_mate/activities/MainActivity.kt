package com.example.metro_mate.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.metro_mate.R
import com.example.metro_mate.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Sheet Behavior 설정
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        // BottomNavigationView 클릭 이벤트 처리
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    true
                }
                R.id.nav_fare -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    true
                }
                R.id.nav_timetable -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    true
                }
                R.id.nav_settings -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    true
                }
                else -> false
            }
        }
    }
}
