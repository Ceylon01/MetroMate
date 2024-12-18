package com.metromate.PathFinding;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metromate.R;
import com.metromate.adapters.RouteStepAdapter;
import com.metromate.models.Edge;
import com.metromate.models.RouteCalculator;
import com.metromate.models.Station;
import com.metromate.models.SubwayData;
import com.metromate.utils.LineColorManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity {

    private TextView departureStationView, waypointStationView, arrivalStationView;
    private TextView travelDetailsView, travelCostView, travelTimeView;
    private RecyclerView routeRecyclerView;
    private List<Station> stations; // 모든 역 데이터
    private List<Edge> edges; // 모든 경로 데이터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        // UI 초기화
        departureStationView = findViewById(R.id.departure_station);
        waypointStationView = findViewById(R.id.waypoint_station);
        arrivalStationView = findViewById(R.id.arrival_station);
        travelDetailsView = findViewById(R.id.travel_details);
        travelCostView = findViewById(R.id.travel_cost);
        travelTimeView = findViewById(R.id.travel_time);
        routeRecyclerView = findViewById(R.id.route_recycler_view);

        // RecyclerView 설정
        routeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 데이터 로드
        SubwayDataLoader.loadSubwayData(this, new SubwayDataLoader.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(SubwayData subwayData) {
                if (subwayData != null) {
                    stations = subwayData.getStations();
                    edges = subwayData.getEdges();
                    handleIntentData();
                } else {
                    stations = new ArrayList<>();
                    edges = new ArrayList<>();
                    Toast.makeText(SearchResultActivity.this, "지하철 데이터 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleIntentData() {
        // Intent에서 입력값 가져오기
        String departureStation = getIntent().getStringExtra("startStation");
        String waypointStation = getIntent().getStringExtra("waypointStation");
        String arrivalStation = getIntent().getStringExtra("endStation");

        // 입력값 UI에 표시
        departureStationView.setText(departureStation != null ? departureStation : "출발역");
        arrivalStationView.setText(arrivalStation != null ? arrivalStation : "도착역");
        waypointStationView.setText(waypointStation != null && !waypointStation.isEmpty() ? waypointStation : "경유 없음");

        // 경로 계산 및 결과 표시
        calculateAndDisplayRoute(departureStation, waypointStation, arrivalStation);
    }

    private void calculateAndDisplayRoute(String departureStation, String waypointStation, String arrivalStation) {
        // ID로 변환
        int startId = getStationId(departureStation);
        int waypointId = waypointStation != null && !waypointStation.isEmpty() ? getStationId(waypointStation) : -1;
        int endId = getStationId(arrivalStation);

        if (startId == -1 || endId == -1 || (waypointId == -1 && waypointStation != null && !waypointStation.isEmpty())) {
            Toast.makeText(this, "잘못된 역 이름이 포함되어 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 경로 계산
        List<Integer> route = findPath(startId, waypointId, endId);

        // 경로가 없을 경우 처리
        if (route.isEmpty()) {
            Toast.makeText(this, "경로를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 경로, 소요 시간, 요금 계산 및 UI 표시
        displayRouteInfo(route);
    }

    private int getStationId(String name) {
        for (Station station : stations) {
            if (station.getName().equals(name)) {
                return station.getId();
            }
        }
        return -1;
    }

    private List<Integer> findPath(int startId, int waypointId, int endId) {
        List<Integer> fullPath = new ArrayList<>();
        if (waypointId != -1) {
            List<Integer> toWaypoint = AStarAlgorithm.findShortestPath(edges, startId, waypointId, false);
            List<Integer> toEnd = AStarAlgorithm.findShortestPath(edges, waypointId, endId, false);

            if (!toWaypoint.isEmpty() && !toEnd.isEmpty()) {
                fullPath.addAll(toWaypoint);
                toEnd.remove(0); // 중복된 경유역 제거
                fullPath.addAll(toEnd);
            }
        } else {
            fullPath = AStarAlgorithm.findShortestPath(edges, startId, endId, false);
        }
        return fullPath;
    }

    private void displayRouteInfo(List<Integer> route) {
        List<Station> routeStations = new ArrayList<>();
        for (int stationId : route) {
            Station station = getStationById(stationId);
            if (station != null) {
                routeStations.add(station);
            }
        }

        Map<String, Integer> lineColorMap = LineColorManager.getLineColors(this);
        RouteStepAdapter adapter = new RouteStepAdapter(this, routeStations, edges, lineColorMap);
        routeRecyclerView.setAdapter(adapter);

        int totalTime = RouteCalculator.calculateTotalTime(edges, route);
        int totalFare = RouteCalculator.calculateTotalFare(edges, route);
        int totalStations = route.size() - 1;

        travelTimeView.setText(totalTime + "분 소요");
        travelDetailsView.setText(totalStations + "개 역 이동");
        travelCostView.setText("카드 " + totalFare + "원");
    }

    private Station getStationById(int id) {
        for (Station station : stations) {
            if (station.getId() == id) {
                return station;
            }
        }
        return null;
    }
}
