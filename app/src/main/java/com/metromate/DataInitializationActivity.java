package com.metromate;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.metromate.models.SubwayData;

public class DataInitializationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if consent is given
        if (!PrivacyConsentDialog.isConsentGiven(this)) {
            // Show the privacy consent dialog
            PrivacyConsentDialog.showConsentDialog(this, this::initializeApp);
        } else {
            // Initialize the app directly if consent is already given
            initializeApp();
        }
    }

    private void initializeApp() {
        // SubwayDataLoader를 사용하여 데이터 로드
        SubwayData subwayData = SubwayDataLoader.loadSubwayData(this);

        if (subwayData != null) {
            System.out.println("Total stations: " + subwayData.stations.size());
            System.out.println("Total edges: " + subwayData.edges.size());

            // 데이터 로드 완료 후 MainActivity로 이동
            startMainActivity();
        } else {
            System.out.println("Failed to load subway data!");
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // DataInitializationActivity 종료
    }
}