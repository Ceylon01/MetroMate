package com.metromate.PathFinding;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.metromate.R;
import com.metromate.models.SubwayData;

import java.util.List;

public class AStarTestActivity extends AppCompatActivity {

    private TextView costPathTextView;
    private TextView timePathTextView;
    private EditText startStationInput;
    private EditText endStationInput;
    private Button searchButton;
    private SubwayData subwayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astar_test);

        // UI 요소 초기화
        costPathTextView = findViewById(R.id.cost_path_text_view);
        timePathTextView = findViewById(R.id.time_path_text_view);
        startStationInput = findViewById(R.id.start_station_input);
        endStationInput = findViewById(R.id.end_station_input);
        searchButton = findViewById(R.id.search_button);

        // Subway 데이터 로드
        subwayData = SubwayDataLoader.loadSubwayData(this);

        if (subwayData == null) {
            costPathTextView.setText("지하철 데이터를 불러오지 못했습니다.");
            timePathTextView.setText("지하철 데이터를 불러오지 못했습니다.");
            return;
        }

        // 검색 버튼 클릭 이벤트
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startInput = startStationInput.getText().toString();
                String endInput = endStationInput.getText().toString();

                if (startInput.isEmpty() || endInput.isEmpty()) {
                    costPathTextView.setText("출발역과 도착역을 입력하세요.");
                    timePathTextView.setText("출발역과 도착역을 입력하세요.");
                    return;
                }

                int startStation = Integer.parseInt(startInput);
                int endStation = Integer.parseInt(endInput);

                // 경로 계산
                calculatePaths(startStation, endStation);
            }
        });
    }

    private void calculatePaths(int startStation, int endStation) {
        List<Integer> costPath = AStarAlgorithm.findShortestPath(
                subwayData.edges, startStation, endStation, false);
        List<Integer> timePath = AStarAlgorithm.findShortestPath(
                subwayData.edges, startStation, endStation, true);

        if (costPath.isEmpty()) {
            costPathTextView.setText("요금 기준 경로를 찾을 수 없습니다.");
        } else {
            costPathTextView.setText("요금 기준 경로: " + costPath.toString());
        }

        if (timePath.isEmpty()) {
            timePathTextView.setText("시간 기준 경로를 찾을 수 없습니다.");
        } else {
            timePathTextView.setText("시간 기준 경로: " + timePath.toString());
        }
    }
}
