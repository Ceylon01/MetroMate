package com.metromate.PathFinding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metromate.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView searchInput; // 검색 입력 필드
    private ListView recentSearchesListView;  // 최근 검색 결과 리스트
    private ArrayAdapter<String> recentSearchesAdapter; // 최근 검색 어댑터
    private TextView currentStationView, previousStationView, nextStationView; // 검색 결과 표시 뷰
    private Button startStationButton, transferStationButton, endStationButton; // 출발/경유/도착 버튼

    private List<String> allStationNames; // 모든 역 이름
    private ArrayList<String> recentSearches;  // 최근 검색어 목록
    private String selectedStation = null; // 현재 선택된 역

    private SharedPreferences sharedPreferences;
    private static final String RECENT_SEARCHES_KEY = "recent_searches";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // UI 요소 초기화
        searchInput = findViewById(R.id.search_input);
        recentSearchesListView = findViewById(R.id.recent_searches_list);
        currentStationView = findViewById(R.id.current_station_highlight);
        previousStationView = findViewById(R.id.previous_station);
        nextStationView = findViewById(R.id.next_station);
        startStationButton = findViewById(R.id.start_station_button);
        transferStationButton = findViewById(R.id.transfer_station_button);
        endStationButton = findViewById(R.id.end_station_button);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("MetroMatePrefs", Context.MODE_PRIVATE);

        // 역 데이터 로드
        allStationNames = loadStationNamesFromJSON();
        if (allStationNames == null) {
            allStationNames = new ArrayList<>();
        }

        // 최근 검색 기록 불러오기
        recentSearches = loadRecentSearches();
        recentSearchesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recentSearches);
        recentSearchesListView.setAdapter(recentSearchesAdapter);

        // 검색창 자동완성 어댑터 설정
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allStationNames);
        searchInput.setAdapter(autoCompleteAdapter);

        // 초기 상태: 기본 값 설정
        selectedStation = allStationNames.size() > 0 ? allStationNames.get(0) : null; // 첫 번째 역 선택
        if (selectedStation != null) {
            updateSearchResult(selectedStation);
        }

        // 검색창 항목 클릭 이벤트
        searchInput.setOnItemClickListener((parent, view, position, id) -> {
            selectedStation = (String) parent.getItemAtPosition(position);
            handleSearch(selectedStation);
        });

        // 최근 검색 항목 클릭 이벤트
        recentSearchesListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedStation = (String) parent.getItemAtPosition(position);
            handleSearch(selectedStation);
        });

        // 이전 역 클릭 이벤트
        previousStationView.setOnClickListener(v -> {
            String previousStation = getPreviousStation(selectedStation);
            if (previousStation != null) {
                handleSearch(previousStation);
            } else {
                Toast.makeText(this, "이전 역이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 다음 역 클릭 이벤트
        nextStationView.setOnClickListener(v -> {
            String nextStation = getNextStation(selectedStation);
            if (nextStation != null) {
                handleSearch(nextStation);
            } else {
                Toast.makeText(this, "다음 역이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 출발역 버튼 클릭 이벤트
        startStationButton.setOnClickListener(v -> {
            if (selectedStation != null) {
                navigateToQuickPathActivity(selectedStation, null, null);
            } else {
                Toast.makeText(this, "먼저 역을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 경유역 버튼 클릭 이벤트
        transferStationButton.setOnClickListener(v -> {
            if (selectedStation != null) {
                navigateToQuickPathActivity(null, selectedStation, null);
            } else {
                Toast.makeText(this, "먼저 역을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 도착역 버튼 클릭 이벤트
        endStationButton.setOnClickListener(v -> {
            if (selectedStation != null) {
                navigateToQuickPathActivity(null, null, selectedStation);
            } else {
                Toast.makeText(this, "먼저 역을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSearch(String stationName) {
        selectedStation = stationName;
        if (!recentSearches.contains(stationName)) {
            recentSearches.add(0, stationName);
            saveRecentSearches(); // 변경사항 저장
            recentSearchesAdapter.notifyDataSetChanged();
        }
        updateSearchResult(stationName);
    }

    private void updateSearchResult(String stationName) {
        String previousStation = getPreviousStation(stationName);
        String nextStation = getNextStation(stationName);

        // 검색 결과 UI 업데이트
        previousStationView.setText(previousStation != null ? previousStation + " ◀" : "◀ 없음");
        currentStationView.setText(stationName);
        nextStationView.setText(nextStation != null ? "▶ " + nextStation : "없음 ▶");
    }

    private List<String> loadStationNamesFromJSON() {
        List<String> stationNames = new ArrayList<>();
        try {
            InputStream is = getAssets().open("data/subway_stations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            Type type = new TypeToken<Map<String, List<Map<String, String>>>>() {}.getType();
            Map<String, List<Map<String, String>>> data = new Gson().fromJson(json, type);

            if (data != null && data.containsKey("stations")) {
                for (Map<String, String> station : data.get("stations")) {
                    stationNames.add(station.get("name"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stationNames;
    }

    private String getPreviousStation(String stationName) {
        int index = allStationNames.indexOf(stationName);
        return (index > 0) ? allStationNames.get(index - 1) : null;
    }

    private String getNextStation(String stationName) {
        int index = allStationNames.indexOf(stationName);
        return (index < allStationNames.size() - 1) ? allStationNames.get(index + 1) : null;
    }

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

    private void saveRecentSearches() {
        StringBuilder data = new StringBuilder();
        for (String search : recentSearches) {
            data.append(search).append(",");
        }
        sharedPreferences.edit().putString(RECENT_SEARCHES_KEY, data.toString()).apply();
    }

    // QuickPathActivity로 이동
    private void navigateToQuickPathActivity(String start, String transfer, String end) {
        Intent intent = new Intent(this, QuickPathActivity.class);
        if (start != null) intent.putExtra("startStation", start);
        if (transfer != null) intent.putExtra("transferStation", transfer);
        if (end != null) intent.putExtra("endStation", end);
        startActivity(intent);
    }
}
