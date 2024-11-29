package com.metromate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 스플래시 레이아웃 설정
        setContentView(R.layout.activity_splash);

        // 일정 시간 뒤에 DataInitActivity로 이동
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, DataInitActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // 3000ms = 3초
    }
}
