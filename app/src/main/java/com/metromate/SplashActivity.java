package com.metromate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.metromate.PathFinding.SubwayDataLoader;
import com.metromate.models.SubwayData;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 스플래시 레이아웃 설정
        setContentView(R.layout.activity_splash);

        // 데이터를 초기화한 뒤 MainActivity로 이동
        initializeApp();
    }

    private void initializeApp() {
        // 데이터를 로드하고 MainActivity로 이동
        SubwayDataLoader.loadSubwayData(this, new SubwayDataLoader.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(SubwayData subwayData) {
                if (subwayData != null) {
                    System.out.println("Total stations: " + subwayData.getStations().size());
                    System.out.println("Total edges: " + subwayData.getEdges().size());
                } else {
                    System.out.println("Failed to load subway data!");
                    // 오류 처리를 추가할 수도 있음
                }

                // 3초 대기 후 MainActivity로 이동
                new Handler(Looper.getMainLooper()).postDelayed(() -> startMainActivity(), 3000);
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // SplashActivity 종료
    }
}
