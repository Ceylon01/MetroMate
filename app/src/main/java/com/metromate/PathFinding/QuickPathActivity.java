package com.metromate.PathFinding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private SharedPreferences sharedPreferences;
    private static final String RECENT_SEARCHES_KEY = "recent_searches_quickpath";  // QuickPath 전용 저장 키
    private static final String RECENT_STATION_SEARCHES_KEY = "recent_station_searches"; // 역 검색 기록

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

        // SharedPreferences로부터 최근 검색 기록 로드
        sharedPreferences = getSharedPreferences("QuickPathPrefs", Context.MODE_PRIVATE);
        recentSearches = loadRecentSearches();
        recentSearchesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recentSearches);
        recentSearchesListView.setAdapter(recentSearchesAdapter);

        // 역 데이터 로드
        SubwayDataLoader.loadSubwayData(this, new SubwayDataLoader.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(SubwayData subwayData) {
                if (subwayData != null) {
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
                    Toast.makeText(QuickPathActivity.this, "역 데이터 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Intent로 전달된 데이터 처리
        String startStation = getIntent().getStringExtra("startStation");
        String waypointStation = getIntent().getStringExtra("transferStation");
        String endStation = getIntent().getStringExtra("endStation");

        // 각 입력란에 데이터 설정
        if (startStation != null) {
            startStationInput.setText(startStation);
        }
        if (waypointStation != null) {
            waypointStationInput.setText(waypointStation);
        }
        if (endStation != null) {
            endStationInput.setText(endStation);
        }

        // 검색 버튼 클릭 이벤트
        searchButton.setOnClickListener(v -> handlePathSearch());

        // 최근 검색 기록 클릭 이벤트
        recentSearchesListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedRecord = recentSearches.get(position);
            handleRecentSearch(selectedRecord);
        });
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

        // 이미 존재하면 맨 앞에 올리기
        if (recentSearches.contains(recentRecord)) {
            recentSearches.remove(recentRecord);  // 기존 항목 제거
            recentSearches.add(0, recentRecord);  // 맨 앞에 추가
        } else {
            // 맨 앞에 추가하고, 최대 10개까지만 저장
            recentSearches.add(0, recentRecord);
            if (recentSearches.size() > 10) {
                recentSearches.remove(recentSearches.size() - 1);  // 가장 오래된 항목 삭제
            }
        }

        // 최근 검색 기록 갱신
        saveRecentSearches();  // 저장
        recentSearchesAdapter.notifyDataSetChanged();

        // SearchResultActivity로 데이터 전달
        Intent intent = new Intent(QuickPathActivity.this, SearchResultActivity.class);
        intent.putExtra("startStation", start);
        intent.putExtra("waypointStation", waypoint);
        intent.putExtra("endStation", end);
        startActivity(intent);
    }

    private void handleRecentSearch(String recentRecord) {
        // 최근 기록에서 출발역, 경유역, 도착역 추출
        String[] stations = recentRecord.split(" → ");
        String start = stations[0].trim();
        String waypoint = stations.length == 3 ? stations[1].trim() : "";
        String end = stations.length == 3 ? stations[2].trim() : stations[1].trim();

        // 최근 검색 기록에서 클릭된 항목을 맨 앞에 올리기
        if (recentSearches.contains(recentRecord)) {
            recentSearches.remove(recentRecord);  // 기존 항목 제거
            recentSearches.add(0, recentRecord);  // 맨 앞에 추가
            saveRecentSearches();  // 저장
            recentSearchesAdapter.notifyDataSetChanged();  // 어댑터 갱신
        }

        // SearchResultActivity로 데이터 전달
        Intent intent = new Intent(QuickPathActivity.this, SearchResultActivity.class);
        intent.putExtra("startStation", start);
        intent.putExtra("waypointStation", waypoint);
        intent.putExtra("endStation", end);
        startActivity(intent);
    }

    // 최근 검색 기록 저장
    private void saveRecentSearches() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder data = new StringBuilder();
        for (String search : recentSearches) {
            data.append(search).append(",");
        }
        // Remove trailing comma
        if (data.length() > 0) {
            data.deleteCharAt(data.length() - 1);
        }
        editor.putString(RECENT_SEARCHES_KEY, data.toString());
        editor.apply();
    }

    // 최근 검색 기록 로드
    private ArrayList<String> loadRecentSearches() {
        String savedData = sharedPreferences.getString(RECENT_SEARCHES_KEY, "");
        ArrayList<String> searches = new ArrayList<>();
        if (!savedData.isEmpty()) {
            String[] items = savedData.split(",");
            for (String item : items) {
                searches.add(item.trim());
            }
        }
        return searches;
    }

    // 역 검색 기록 저장
    private void saveRecentStationSearches() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder data = new StringBuilder();
        for (String search : recentSearches) {
            data.append(search).append(",");
        }
        editor.putString(RECENT_STATION_SEARCHES_KEY, data.toString());
        editor.apply();
    }

    // 역 검색 기록 로드
    private ArrayList<String> loadRecentStationSearches() {
        String savedData = sharedPreferences.getString(RECENT_STATION_SEARCHES_KEY, "");
        ArrayList<String> searches = new ArrayList<>();
        if (!savedData.isEmpty()) {
            String[] items = savedData.split(",");
            for (String item : items) {
                searches.add(item.trim());
            }
        }
        return searches;
    }
}
