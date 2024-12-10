package com.metromate.fare;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.metromate.R;
import com.metromate.models.Edge;
import com.metromate.models.RouteCalculator;
import com.metromate.models.Station;
import com.metromate.models.SubwayData;
import com.metromate.PathFinding.AStarAlgorithm;
import com.metromate.PathFinding.SubwayDataLoader;

import java.util.ArrayList;
import java.util.List;

public class FareActivity extends AppCompatActivity {

    private AutoCompleteTextView startStationInput, waypointStationInput, endStationInput;
    private Button calculateFareButton;
    private TextView fareResultView;
    private RadioGroup passengerTypeGroup;

    private List<Station> stations; // 역 데이터
    private List<Edge> edges; // 경로 데이터
    private List<String> stationNames; // 역 이름 데이터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fare);

        // UI 초기화
        startStationInput = findViewById(R.id.start_station_input);
        waypointStationInput = findViewById(R.id.waypoint_station_input);
        endStationInput = findViewById(R.id.end_station_input);
        calculateFareButton = findViewById(R.id.calculate_fare_button);
        fareResultView = findViewById(R.id.fare_result);
        passengerTypeGroup = findViewById(R.id.passenger_type_group);

        // 역 데이터 로드
        SubwayDataLoader.loadSubwayData(this, new SubwayDataLoader.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(SubwayData subwayData) {
                if (subwayData != null) {
                    Log.d("FareActivity", "역 데이터 로드 성공");
                    stations = subwayData.getStations();
                    edges = subwayData.getEdges();
                    stationNames = new ArrayList<>();
                    for (Station station : stations) {
                        stationNames.add(station.getName());
                    }

                    // AutoCompleteTextView에 어댑터 설정
                    ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(FareActivity.this, android.R.layout.simple_dropdown_item_1line, stationNames);
                    startStationInput.setAdapter(stationAdapter);
                    waypointStationInput.setAdapter(stationAdapter);
                    endStationInput.setAdapter(stationAdapter);
                } else {
                    Log.e("FareActivity", "역 데이터 로드 실패");
                    Toast.makeText(FareActivity.this, "역 데이터 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 요금 계산 버튼 클릭 리스너
        calculateFareButton.setOnClickListener(v -> calculateFare());
    }

    private void calculateFare() {
        String startStationName = startStationInput.getText().toString().trim();
        String waypointStationName = waypointStationInput.getText().toString().trim();
        String endStationName = endStationInput.getText().toString().trim();

        // 승객 유형 확인
        int passengerTypeId = passengerTypeGroup.getCheckedRadioButtonId();
        String passengerType = "adult"; // 기본값
        if (passengerTypeId == R.id.teenager_button) {
            passengerType = "teenager";
        } else if (passengerTypeId == R.id.child_button) {
            passengerType = "child";
        }

        // 입력값 검증
        if (startStationName.isEmpty() || endStationName.isEmpty()) {
            Toast.makeText(this, "출발역과 도착역을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int startId = getStationId(startStationName);
        int endId = getStationId(endStationName);
        int waypointId = waypointStationName.isEmpty() ? -1 : getStationId(waypointStationName);

        if (startId == -1 || endId == -1 || (!waypointStationName.isEmpty() && waypointId == -1)) {
            Toast.makeText(this, "올바른 역 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 경로 계산
        List<Integer> route = calculateRoute(startId, waypointId, endId);

        if (route.isEmpty()) {
            Toast.makeText(this, "경로를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 요금 계산
        int totalFare = RouteCalculator.calculateTotalFare(edges, route);

        // 결과 표시
        fareResultView.setText(totalFare + "원");
    }

    private int getStationId(String name) {
        for (Station station : stations) {
            if (station.getName().equals(name)) {
                return station.getId();
            }
        }
        return -1;
    }

    private List<Integer> calculateRoute(int startId, int waypointId, int endId) {
        List<Integer> fullPath = new ArrayList<>();
        if (waypointId != -1) {
            // 경유지 있는 경로
            List<Integer> toWaypoint = AStarAlgorithm.findShortestPath(edges, startId, waypointId, false);
            List<Integer> toEnd = AStarAlgorithm.findShortestPath(edges, waypointId, endId, false);

            if (!toWaypoint.isEmpty() && !toEnd.isEmpty()) {
                fullPath.addAll(toWaypoint);
                toEnd.remove(0); // 중복된 경유지 제거
                fullPath.addAll(toEnd);
            }
        } else {
            // 경유지 없는 경로
            fullPath = AStarAlgorithm.findShortestPath(edges, startId, endId, false);
        }
        return fullPath;
    }
}
