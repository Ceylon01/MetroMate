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

        // Intent에서 역 이름 받아오기
        String stationNameFromIntent = getIntent().getStringExtra("stationName");
        if (stationNameFromIntent != null) {
            // 자동으로 해당 역을 검색하고 결과 처리
            searchForStationByName(stationNameFromIntent);
        }

        // 검색창 항목 클릭 이벤트
        searchInput.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            String[] parts = selectedItem.split(" \\(");
            String stationName = parts[0];
            selectedStation = getStationByName(stationName); // 호선 없이 역 이름만으로 검색
            handleSearch(selectedStation);
        });

        // 최근 검색 항목 클릭 이벤트
        recentSearchesListView.setOnItemClickListener((parent, view, position, id) -> {
            String recentItem = recentSearches.get(position);
            String[] parts = recentItem.split(" \\(");
            String stationName = parts[0];
            selectedStation = getStationByName(stationName); // 호선 없이 역 이름만으로 검색
            handleSearch(selectedStation);
        });

        // 이전 역 클릭 이벤트
        previousStationView.setOnClickListener(v -> {
            if (selectedStation != null) {
                // 이전 역이 null인 경우 종착역으로 표시
                if (selectedStation.getPrevious() == null || selectedStation.getPrevious().equals("0")) {
                    previousStationView.setText("◀ 종착역");
                    // 이전 역이 없으면 종착역으로 표시하고 그 이후 기능 중단
                } else {
                    String previousStationName = getStationNameById(Integer.parseInt(selectedStation.getPrevious()));
                    selectedStation = getStationByName(previousStationName);  // 이전 역으로 넘어감
                    handleSearch(selectedStation);
                }
            }
        });

        // 다음 역 클릭 이벤트
        nextStationView.setOnClickListener(v -> {
            if (selectedStation != null) {
                // 다음 역이 null인 경우 종착역으로 표시
                if (selectedStation.getNext() == null || selectedStation.getNext().equals("0")) {
                    nextStationView.setText("종착역 ▶");
                } else {
                    String nextStationName = getStationNameById(Integer.parseInt(selectedStation.getNext()));
                    selectedStation = getStationByName(nextStationName);  // 다음 역으로 넘어감
                    handleSearch(selectedStation);
                }
            }
        });

        // 출발역 설정
        startStationButton.setOnClickListener(v -> {
            if (selectedStation != null) {
                navigateToQuickPathActivity(selectedStation.getName(), null, null);
            } else {
                Toast.makeText(this, "먼저 역을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

// 경유역 설정
        transferStationButton.setOnClickListener(v -> {
            if (selectedStation != null) {
                navigateToQuickPathActivity(null, selectedStation.getName(), null);
            } else {
                Toast.makeText(this, "먼저 역을 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

// 도착역 설정
        endStationButton.setOnClickListener(v -> {
            if (selectedStation != null) {
                navigateToQuickPathActivity(null, null, selectedStation.getName());
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

    // 역 이름만 찾기
    private Station getStationByName(String name) {
        for (List<Station> stations : stationMap.values()) {
            for (Station station : stations) {
                if (station.getName().equals(name)) {
                    return station;
                }
            }
        }
        return null;
    }

    // 자동으로 역 검색 처리하는 메서드
    private void searchForStationByName(String stationName) {
        for (Map.Entry<String, List<Station>> entry : stationMap.entrySet()) {
            for (Station station : entry.getValue()) {
                if (station.getName().equals(stationName)) {
                    selectedStation = station;
                    handleSearch(station); // 검색 결과 처리
                    return;
                }
            }
        }
    }

    private void handleSearch(Station station) {
        if (station != null) {
            selectedStation = station;
            // 역 이름과 호선을 함께 표시
            String displayName = station.getName() + " (" + station.getLine() + "호선)";  // 호선도 포함

            // 이미 최근 검색 목록에 존재하는 항목이 있으면 그 항목을 맨 뒤로 이동
            if (recentSearches.contains(displayName)) {
                recentSearches.remove(displayName);  // 기존 항목 제거
            }

            // 검색 기록에 새 항목 추가 (기존에 없으면 맨 앞에 추가)
            recentSearches.add(0, displayName);  // 맨 앞에 추가

            // 최대 10개의 기록만 저장하도록 갱신
            if (recentSearches.size() > 10) {
                recentSearches.remove(recentSearches.size() - 1);  // 10개 초과 시 가장 오래된 항목 삭제
            }

            // 갱신된 검색 기록을 저장
            saveRecentSearches();

            // 어댑터 갱신
            recentSearchesAdapter.notifyDataSetChanged();

            // 화면 갱신
            updateSearchResult(station);
        }
    }


    private void updateSearchResult(Station station) {
        String previousStationName = getStationNameById(Integer.parseInt(station.getPrevious()));
        String nextStationName = getStationNameById(Integer.parseInt(station.getNext()));

        currentStationView.setText(station.getName());

        previousStationView.setText(previousStationName != null ? previousStationName + " ◀" : "◀ 종착역");
        nextStationView.setText(nextStationName != null ? "▶ " + nextStationName : "종착역 ▶");
    }

    private String getStationNameById(int id) {
        Station station = stationByIdMap.get(id);
        return station != null ? station.getName() : null;
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

                    // 이전, 다음 역, 지하철 타입 및 상태에 대해 null 체크 후 기본값 설정
                    String previous = stationData.get("previous");
                    String next = stationData.get("next");
                    String subwayType = stationData.get("subwayType");
                    String subwayStatus = stationData.get("subwayStatus");

                    // 기본값 처리
                    previous = (previous != null) ? previous : "0";
                    next = (next != null) ? next : "0";
                    subwayType = (subwayType != null) ? subwayType : "";
                    subwayStatus = (subwayStatus != null) ? subwayStatus : "";

                    // Station 객체 생성
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
        // 최대 10개의 검색 기록을 저장하도록 제한
        if (recentSearches.size() > 10) {
            recentSearches.remove(recentSearches.size() - 1);  // 10개 초과 시 가장 오래된 항목 삭제
        }

        StringBuilder data = new StringBuilder();
        for (String search : recentSearches) {
            data.append(search).append(",");
        }

        sharedPreferences.edit().putString(RECENT_SEARCHES_KEY, data.toString()).apply();
    }

    private void navigateToQuickPathActivity(String startStation, String transferStation, String endStation) {
        Intent intent = new Intent(this, QuickPathActivity.class);
        if (startStation != null) intent.putExtra("startStation", startStation);
        if (transferStation != null) intent.putExtra("transferStation", transferStation);
        if (endStation != null) intent.putExtra("endStation", endStation);
        startActivity(intent);
    }


}
