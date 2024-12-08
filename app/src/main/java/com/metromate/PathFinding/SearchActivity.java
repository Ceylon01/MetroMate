package com.metromate.PathFinding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.metromate.R;
import com.metromate.models.FavoriteStation;
import com.metromate.models.Station;
import com.metromate.utils.FavoriteManager;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView searchInput;
    private ListView recentSearchesListView;
    private ArrayAdapter<String> recentSearchesAdapter;
    private TextView currentStationView, previousStationView, nextStationView;
    private Button startStationButton, transferStationButton, endStationButton, favoriteButton;

    private Map<String, List<Station>> stationMap; // 역 이름 -> 여러 호선 정보
    private Map<Integer, Station> stationByIdMap; // id -> Station 객체
    private ArrayList<String> recentSearches;
    private Station selectedStation; // 현재 선택된 역

    private SharedPreferences sharedPreferences;
    private static final String RECENT_SEARCHES_KEY = "recent_searches";

    private FavoriteManager favoriteManager;

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
        favoriteButton = findViewById(R.id.favorite_button); // 즐겨찾기 버튼

        sharedPreferences = getSharedPreferences("MetroMatePrefs", Context.MODE_PRIVATE);
        favoriteManager = new FavoriteManager(this);

        // 역 데이터 로드
        stationMap = loadStationDataFromJSON();
        if (stationMap.isEmpty()) {
            Toast.makeText(this, "역 데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 자동완성 목록 생성: "명지대 (2호선)"처럼 표시
        List<String> autoCompleteList = new ArrayList<>();
        for (Map.Entry<String, List<Station>> entry : stationMap.entrySet()) {
            for (Station station : entry.getValue()) {
                autoCompleteList.add(station.getName() + " (" + station.getLine() + "호선)");
            }
        }

        // 최근 검색 기록 불러오기
        recentSearches = loadRecentSearches();
        recentSearchesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recentSearches);
        recentSearchesListView.setAdapter(recentSearchesAdapter);

        // 검색창 자동완성 어댑터 설정
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, autoCompleteList);
        searchInput.setAdapter(autoCompleteAdapter);

        // 초기 상태 설정
        selectedStation = null; // 기본 선택 없음
        if (!stationMap.isEmpty()) {
            String firstStationName = stationMap.keySet().iterator().next();
            selectedStation = stationMap.get(firstStationName).get(0); // 첫 번째 역과 호선
            updateSearchResult(selectedStation);
        }

        // 검색창 항목 클릭 이벤트
        searchInput.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            String[] parts = selectedItem.split(" \\(");
            String stationName = parts[0];
            String line = parts[1].replace("호선)", "");
            selectedStation = getStationByNameAndLine(stationName, line);
            handleSearch(selectedStation);
        });

        // 최근 검색 항목 클릭 이벤트
        recentSearchesListView.setOnItemClickListener((parent, view, position, id) -> {
            String recentItem = recentSearches.get(position);
            String[] parts = recentItem.split(" \\(");
            String stationName = parts[0];
            String line = parts[1].replace("호선)", "");
            selectedStation = getStationByNameAndLine(stationName, line);
            handleSearch(selectedStation);
        });

        // 이전 역 클릭 이벤트
        previousStationView.setOnClickListener(v -> {
            if (selectedStation != null && selectedStation.getPrevious() != null) {
                String previousStationName = getStationNameById(Integer.parseInt(selectedStation.getPrevious()));
                selectedStation = getStationByNameAndLine(previousStationName, selectedStation.getLine());
                handleSearch(selectedStation);
            } else {
                Toast.makeText(this, "이전 역이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 다음 역 클릭 이벤트
        nextStationView.setOnClickListener(v -> {
            if (selectedStation != null && selectedStation.getNext() != null) {
                String nextStationName = getStationNameById(Integer.parseInt(selectedStation.getNext()));
                selectedStation = getStationByNameAndLine(nextStationName, selectedStation.getLine());
                handleSearch(selectedStation);
            } else {
                Toast.makeText(this, "다음 역이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 출발역 설정
        startStationButton.setOnClickListener(v -> {
            if (selectedStation != null) {
                navigateToQuickPathActivity(selectedStation.getName(), selectedStation.getLine(), null, null, null, null);
            } else {
                Toast.makeText(this, "먼저 역을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 경유역 설정
        transferStationButton.setOnClickListener(v -> {
            if (selectedStation != null) {
                navigateToQuickPathActivity(null, null, selectedStation.getName(), selectedStation.getLine(), null, null);
            } else {
                Toast.makeText(this, "먼저 역을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 도착역 설정
        endStationButton.setOnClickListener(v -> {
            if (selectedStation != null) {
                navigateToQuickPathActivity(null, null, null, null, selectedStation.getName(), selectedStation.getLine());
            } else {
                Toast.makeText(this, "먼저 역을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 즐겨찾기 버튼 클릭 이벤트
        favoriteButton.setOnClickListener(v -> {
            if (selectedStation != null) {
                FavoriteStation favoriteStation = new FavoriteStation(
                        selectedStation.getId(),
                        selectedStation.getName(),
                        selectedStation.getLine(),
                        selectedStation.getSubwayType(),
                        selectedStation.getSubwayStatus()
                );

                if (favoriteManager.getFavoriteStations().contains(favoriteStation)) {
                    favoriteManager.removeFavoriteStation(favoriteStation);
                    Toast.makeText(this, "즐겨찾기에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    favoriteManager.addFavoriteStation(favoriteStation);
                    Toast.makeText(this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "먼저 역을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSearch(Station station) {
        if (station != null) {
            selectedStation = station;
            String displayName = station.getName() + " (" + station.getLine() + "호선)";
            if (!recentSearches.contains(displayName)) {
                recentSearches.add(0, displayName);
                saveRecentSearches();
                recentSearchesAdapter.notifyDataSetChanged();
            }
            updateSearchResult(station);
        }
    }

    private void updateSearchResult(Station station) {
        String previousStationName = getStationNameById(Integer.parseInt(station.getPrevious()));
        String nextStationName = getStationNameById(Integer.parseInt(station.getNext()));

        currentStationView.setText(station.getName() + " (" + station.getLine() + "호선)");
        previousStationView.setText(previousStationName != null ? previousStationName + " ◀" : "◀ 종착");
        nextStationView.setText(nextStationName != null ? "▶ " + nextStationName : "종착 ▶");
    }

    private String getStationNameById(int id) {
        Station station = stationByIdMap.get(id);
        return station != null ? station.getName() : null;
    }

    private Station getStationByNameAndLine(String name, String line) {
        if (stationMap.containsKey(name)) {
            for (Station station : stationMap.get(name)) {
                if (station.getLine().equals(line)) {
                    return station;
                }
            }
        }
        return null;
    }

    private Map<String, List<Station>> loadStationDataFromJSON() {
        Map<String, List<Station>> stationMap = new HashMap<>();
        stationByIdMap = new HashMap<>();
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
                for (Map<String, String> stationData : data.get("stations")) {
                    int id = Integer.parseInt(stationData.get("id"));
                    String name = stationData.get("name");
                    String line = stationData.get("line");
                    String previous = stationData.get("previous");
                    String next = stationData.get("next");
                    String subwayType = stationData.get("subwayType");  // 추가된 subwayType
                    String subwayStatus = stationData.get("subwayStatus"); // 추가된 subwayStatus

                    Station station = new Station(id, name, line, previous, next, subwayType, subwayStatus);
                    stationMap.putIfAbsent(name, new ArrayList<>());
                    stationMap.get(name).add(station);

                    // id로 Station 저장
                    stationByIdMap.put(id, station);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stationMap;
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

    private void navigateToQuickPathActivity(String startStation, String startLine, String transferStation, String transferLine, String endStation, String endLine) {
        Intent intent = new Intent(this, QuickPathActivity.class);
        if (startStation != null) intent.putExtra("startStation", startStation);
        if (startLine != null) intent.putExtra("startLine", startLine);
        if (transferStation != null) intent.putExtra("transferStation", transferStation);
        if (transferLine != null) intent.putExtra("transferLine", transferLine);
        if (endStation != null) intent.putExtra("endStation", endStation);
        if (endLine != null) intent.putExtra("endLine", endLine);
        startActivity(intent);
    }
}
