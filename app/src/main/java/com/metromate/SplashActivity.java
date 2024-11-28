package com.metromate;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 스플래시 레이아웃 설정
        setContentView(R.layout.activity_splash);

        // 일정 시간 뒤에 MainActivity로 이동
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, DataInitializationActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // 3000ms = 3초
    }
}
