package com.metromate.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metromate.R;
import com.metromate.adapters.StationAdapter;
import com.metromate.data.Station;
import com.metromate.utils.StationSearcher;
import java.util.ArrayList;
import java.util.List;

public class SearchStationActivity extends AppCompatActivity {

    private EditText searchInput;
    private RecyclerView resultsRecyclerView;
    private StationAdapter stationAdapter; // 어댑터 클래스는 별도로 구현해야 함
    private List<Station> stations;
    private StationSearcher stationSearcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_station);

        // View 초기화
        searchInput = findViewById(R.id.search_input);
        resultsRecyclerView = findViewById(R.id.recycler_view_results);

        // RecyclerView 설정
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stations = createStationList(); // 기본 역 리스트 생성
        stationAdapter = new StationAdapter(new ArrayList<>()); // 빈 리스트로 초기화
        resultsRecyclerView.setAdapter(stationAdapter);

        // 어댑터 생성 및 설정
        stationAdapter = new StationAdapter(new ArrayList<>());
        resultsRecyclerView.setAdapter(stationAdapter);

        // StationSearcher 초기화
        stationSearcher = new StationSearcher(stations);

        // 검색 입력 이벤트
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 생략: 필요 시 구현
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 생략: 필요 시 구현
            }
        });
    }

    // 검색 메서드
    private void performSearch(String query) {
        List<Station> results = stationSearcher.search(query);
        stationAdapter.updateData(results); // 어댑터에 검색 결과 반영
    }

    // 기본 역 리스트 생성
    private List<Station> createStationList() {
        List<Station> stationList = new ArrayList<>();
        stationList.add(new Station(1, "서울역",1));
        stationList.add(new Station(2, "강남역",1));
        stationList.add(new Station(3, "잠실역",1));
        stationList.add(new Station(4, "신촌역",1));
        return stationList;
    }
}
