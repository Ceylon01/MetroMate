package com.metromate.PathFinding;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.metromate.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView searchInput;
    private ListView recentSearchesListView;
    private ArrayAdapter<String> recentSearchesAdapter;

    // 예시: 역 데이터 (실제로는 파일이나 DB에서 불러올 데이터)
    private List<String> allStations;

    // 최근 검색어 목록
    private List<String> recentSearches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 검색창 및 리스트 초기화
        searchInput = findViewById(R.id.search_input);
        recentSearchesListView = findViewById(R.id.recent_searches_list);

        // 예시 역 데이터 (실제 앱에서는 이 데이터를 DB나 파일에서 읽어올 수 있음)
        allStations = new ArrayList<>();
        allStations.add("Seoul Station");
        allStations.add("Gangnam Station");
        allStations.add("City Hall Station");
        allStations.add("Myeongji University Station");
        allStations.add("Myeongdong Station");

        // 최근 검색어 목록 초기화
        recentSearches = new ArrayList<>();
        recentSearches.add("Seoul Station");
        recentSearches.add("Gangnam Station");
        recentSearches.add("City Hall Station");

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
        // 예시: 역 검색 결과 처리 (A* 알고리즘 적용 등)
        // 실제로는 여기서 검색 후 필요한 작업을 할 수 있습니다.
        // 예시로 Toast로 출력
        android.widget.Toast.makeText(this, "Searching for: " + stationName, android.widget.Toast.LENGTH_SHORT).show();

        // 최근 검색어 목록에 추가 (중복 방지)
        if (!recentSearches.contains(stationName)) {
            recentSearches.add(0, stationName);  // 최근 검색어 맨 앞에 추가
            recentSearchesAdapter.notifyDataSetChanged();  // 리스트 갱신
        }
    }
}
