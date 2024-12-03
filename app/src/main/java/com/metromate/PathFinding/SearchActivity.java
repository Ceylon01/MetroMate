package com.metromate.PathFinding;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metromate.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView searchInput;
    private ListView recentSearchesListView;
    private ArrayAdapter<String> recentSearchesAdapter;

    private List<String> allStations; // 모든 역 이름
    private List<String> recentSearches; // 최근 검색어 목록

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 검색창 및 리스트 초기화
        searchInput = findViewById(R.id.search_input);
        recentSearchesListView = findViewById(R.id.recent_searches_list);

        // 역 데이터 로드
        allStations = loadStationNamesFromJSON();
        if (allStations == null) {
            allStations = new ArrayList<>(); // 데이터 로드 실패 시 빈 리스트로 초기화
        }

        // 최근 검색어 목록 초기화
        recentSearches = new ArrayList<>();

        // 최근 검색 리스트 어댑터 설정
        recentSearchesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recentSearches);
        recentSearchesListView.setAdapter(recentSearchesAdapter);

        // 검색 입력창에서 실시간으로 역 이름을 필터링하는 어댑터 설정
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        searchInput.setAdapter(autoCompleteAdapter);

        // 입력 텍스트 변경 이벤트 (실시간 자동완성)
        searchInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // 사용자가 입력한 텍스트를 기준으로 역 이름을 필터링
                String query = charSequence.toString().toLowerCase();
                List<String> filteredStations = new ArrayList<>();

                for (String station : allStations) {
                    if (station.toLowerCase().contains(query)) {
                        filteredStations.add(station);
                    }
                }

                // 필터링된 결과로 AutoCompleteTextView의 어댑터 갱신
                autoCompleteAdapter.clear();
                autoCompleteAdapter.addAll(filteredStations);
                autoCompleteAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        // 최근 검색 항목 클릭 이벤트
        recentSearchesListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedStation = (String) parent.getItemAtPosition(position);
            searchInput.setText(selectedStation);  // 선택된 역 이름을 입력창에 반영
            searchForStation(selectedStation);  // 역 검색 수행
        });

        // AutoCompleteTextView의 자동 완성 항목 클릭 이벤트
        searchInput.setOnItemClickListener((parent, view, position, id) -> {
            String selectedStation = (String) parent.getItemAtPosition(position);
            searchForStation(selectedStation);  // 선택된 역 이름으로 검색
        });
    }

    // 역 검색 함수
    private void searchForStation(String stationName) {
        // 예시: 역 검색 결과 처리
        android.widget.Toast.makeText(this, "Searching for: " + stationName, android.widget.Toast.LENGTH_SHORT).show();

        // 최근 검색어 목록에 추가 (중복 방지)
        if (!recentSearches.contains(stationName)) {
            recentSearches.add(0, stationName);  // 최근 검색어 맨 앞에 추가
            recentSearchesAdapter.notifyDataSetChanged();  // 리스트 갱신
        }
    }

    // JSON에서 역 이름 로드 함수
    private List<String> loadStationNamesFromJSON() {
        List<String> stationNames = new ArrayList<>();

        try {
            // assets/subway_stations.json 파일 읽기
            InputStream is = getAssets().open("subway_stations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            // JSON 파싱
            Type type = new TypeToken<Map<String, List<Map<String, String>>>>() {}.getType();
            Map<String, List<Map<String, String>>> data = new Gson().fromJson(json, type);

            // "stations" 키의 값에서 역 이름 가져오기
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
}
