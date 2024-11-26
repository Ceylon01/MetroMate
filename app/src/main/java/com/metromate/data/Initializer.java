package com.metromate.data;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.metromate.R;

public class Initializer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SubwayDataLoader를 사용하여 데이터 로드
        SubwayData subwayData = SubwayDataLoader.loadSubwayData(this);

        if (subwayData != null) {
            System.out.println("Total stations: " + subwayData.stations.size());
            System.out.println("Total edges: " + subwayData.edges.size());
        } else {
            System.out.println("Failed to load subway data!");
        }
    }
}
