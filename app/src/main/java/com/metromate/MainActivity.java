package com.metromate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.metromate.fare.FareCalculationActivity;
import com.metromate.PathFinding.PathFindingActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button findPathButton = findViewById(R.id.find_path_button);
        Button fareCalculationButton = findViewById(R.id.fare_calculation_button);

        // 길찾기 화면 이동
        findPathButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PathFindingActivity.class);
            startActivity(intent);
        });

        // 요금 계산 화면 이동
        fareCalculationButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FareCalculationActivity.class);
            startActivity(intent);
        });
    }
}
