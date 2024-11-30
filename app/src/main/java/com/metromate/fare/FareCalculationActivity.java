/*
package com.metromate.fare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metromate.R;
import com.metromate.models.Edge;
import com.metromate.models.Journey;
import com.metromate.models.Passenger;
import com.metromate.fare.FareCalculatorLogic;
import com.metromate.PathFinding.AStarAlgorithm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

public class FareCalculationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_calculation);

        // UI 요소 참조
        EditText editTextStartStation = findViewById(R.id.edittext_start_station);
        EditText editTextEndStation = findViewById(R.id.edittext_end_station);
        EditText editTextAge = findViewById(R.id.edittext_age);
        Button buttonCalculate = findViewById(R.id.button_calculate);
        TextView textViewResultCard = findViewById(R.id.textview_result_card);
        TextView textViewResultCash = findViewById(R.id.textview_result_cash);

        // 계산 버튼 클릭 이벤트
        buttonCalculate.setOnClickListener(v -> {
            try {
                String startStation = editTextStartStation.getText().toString().trim();
                String endStation = editTextEndStation.getText().toString().trim();
                int age = Integer.parseInt(editTextAge.getText().toString().trim());

                if (startStation.isEmpty() || endStation.isEmpty()) {
                    throw new IllegalArgumentException("출발역과 도착역을 입력하세요.");
                }

                // A* 알고리즘을 통해 경로 탐색
                List<Integer> path = AStarAlgorithm.findShortestPath(getEdgesAsList(),
                        Integer.parseInt(startStation), Integer.parseInt(endStation), false);

                // 경로를 통해 총 거리 계산
                int totalDistance = calculateTotalDistance(path);

                // 사용자와 여정 정보 생성
                Passenger passengerCard = new Passenger(age, true); // 카드 사용
                Passenger passengerCash = new Passenger(age, false); // 현금 사용

                Journey journey = new Journey(startStation, endStation, totalDistance, true, false);

                // 요금 계산
                int fareCard = FareCalculatorLogic.calculateFare(passengerCard, journey);
                int fareCash = FareCalculatorLogic.calculateFare(passengerCash, journey);

                // 결과 출력
                textViewResultCard.setText(String.format("카드 요금 결과: %d원", fareCard));
                textViewResultCash.setText(String.format("현금 요금 결과: %d원", fareCash));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "올바른 나이를 입력하세요.", Toast.LENGTH_SHORT).show();
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    // A* 알고리즘 경로로부터 총 거리 계산
    private int calculateTotalDistance(List<Integer> path) {
        int totalDistance = 0;
        Map<Integer, List<Edge>> edges = getEdges();

        for (int i = 0; i < path.size() - 1; i++) {
            int start = path.get(i);
            int end = path.get(i + 1);

            // 간선 데이터에서 거리 찾기
            for (Edge edge : edges.getOrDefault(start, Collections.emptyList())) {
                if (edge.getEnd() == end) {
                    totalDistance += edge.getDistance();
                    break;
                }
            }
        }
        return totalDistance;
    }

    // 간선 데이터 로드
    private Map<Integer, List<Edge>> getEdges() {
        try {
            InputStream inputStream = getAssets().open("data/subway_data.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            Gson gson = new Gson();
            Type edgeMapType = new TypeToken<Map<Integer, List<Edge>>>() {}.getType();
            Map<Integer, List<Edge>> edgeMap = gson.fromJson(reader, edgeMapType);
            reader.close();
            return edgeMap;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "간선 데이터를 로드하는 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            return new HashMap<>();
        }
    }

    // 간선 데이터를 List<Edge>로 변환
    private List<Edge> getEdgesAsList() {
        Map<Integer, List<Edge>> edgeMap = getEdges();
        List<Edge> edgeList = new ArrayList<>();
        for (List<Edge> edges : edgeMap.values()) {
            edgeList.addAll(edges);
        }
        return edgeList;
    }
}
*/
