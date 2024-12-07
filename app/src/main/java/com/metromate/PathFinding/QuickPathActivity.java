package com.metromate.PathFinding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.metromate.R;
import com.metromate.models.Station;
import com.metromate.models.SubwayData;

import java.util.ArrayList;
import java.util.List;

public class QuickPathActivity extends AppCompatActivity {

    private AutoCompleteTextView startStationInput, waypointStationInput, endStationInput;
    private Button searchButton;
    private ListView recentSearchesListView;

    private List<String> stationNames; // 모든 역 이름
    private List<String> recentSearches; // 최근 검색 기록
    private ArrayAdapter<String> recentSearchesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_path);

        // UI 초기화
        startStationInput = findViewById(R.id.start_station_input);
        waypointStationInput = findViewById(R.id.waypoint_station_input);
        endStationInput = findViewById(R.id.end_station_input);
        searchButton = findViewById(R.id.search_button);
        recentSearchesListView = findViewById(R.id.recent_searches_list);

        // Intent에서 전달된 데이터 처리
        handleIncomingData();

        // 역 데이터 로드
        SubwayDataLoader.loadSubwayData(this, new SubwayDataLoader.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(SubwayData subwayData) {
                if (subwayData != null) {
                    Log.d("QuickPathActivity", "역 데이터 로드 성공");
                    stationNames = new ArrayList<>();
                    for (Station station : subwayData.getStations()) {
                        stationNames.add(station.getName());
                    }

                    // AutoCompleteTextView에 어댑터 설정
                    ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(QuickPathActivity.this, android.R.layout.simple_dropdown_item_1line, stationNames);
                    startStationInput.setAdapter(stationAdapter);
                    waypointStationInput.setAdapter(stationAdapter);
                    endStationInput.setAdapter(stationAdapter);
                } else {
                    Log.e("QuickPathActivity", "역 데이터 로드 실패");
                    Toast.makeText(QuickPathActivity.this, "역 데이터 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 최근 검색어 초기화
        recentSearches = new ArrayList<>();
        recentSearchesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recentSearches);
        recentSearchesListView.setAdapter(recentSearchesAdapter);

        // 검색 버튼 클릭 이벤트
        searchButton.setOnClickListener(v -> handlePathSearch());

        // 최근 검색 기록 클릭 이벤트
        recentSearchesListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedRecord = recentSearches.get(position);
            handleRecentSearch(selectedRecord);
        });
    }

    // Intent로 전달된 데이터를 처리하여 입력 필드에 채우기
    private void handleIncomingData() {
        Intent intent = getIntent();
        String start = intent.getStringExtra("startStation");
        String transfer = intent.getStringExtra("transferStation");
        String end = intent.getStringExtra("endStation");

        if (start != null) {
            startStationInput.setText(start);
        }
        if (transfer != null) {
            waypointStationInput.setText(transfer);
        }
        if (end != null) {
            endStationInput.setText(end);
        }
    }

    // 입력값을 처리하고 SearchResultActivity로 전달
    private void handlePathSearch() {
        String start = startStationInput.getText().toString().trim();
        String waypoint = waypointStationInput.getText().toString().trim();
        String end = endStationInput.getText().toString().trim();

        if (start.isEmpty() || end.isEmpty()) {
            Toast.makeText(this, "출발역과 도착역을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 최근 검색 기록에 추가
        String recentRecord = waypoint.isEmpty() ? start + " → " + end : start + " → " + waypoint + " → " + end;
        if (!recentSearches.contains(recentRecord)) {
            recentSearches.add(0, recentRecord);
            recentSearchesAdapter.notifyDataSetChanged();
        }

        // SearchResultActivity로 데이터 전달
        Intent intent = new Intent(QuickPathActivity.this, SearchResultActivity.class);
        intent.putExtra("startStation", start);
        intent.putExtra("waypointStation", waypoint);
        intent.putExtra("endStation", end);
        startActivity(intent);
    }

    // 최근 검색 기록을 처리하고 SearchResultActivity로 이동
    private void handleRecentSearch(String recentRecord) {
        // 최근 기록에서 출발역, 경유역, 도착역 추출
        String[] stations = recentRecord.split(" → ");
        String start = stations[0].trim();
        String waypoint = stations.length == 3 ? stations[1].trim() : "";
        String end = stations.length == 3 ? stations[2].trim() : stations[1].trim();

        // SearchResultActivity로 데이터 전달
        Intent intent = new Intent(QuickPathActivity.this, SearchResultActivity.class);
        intent.putExtra("startStation", start);
        intent.putExtra("waypointStation", waypoint);
        intent.putExtra("endStation", end);
        startActivity(intent);
    }
}
