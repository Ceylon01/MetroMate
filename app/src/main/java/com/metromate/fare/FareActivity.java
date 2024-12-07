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
import com.metromate.models.Station;
import com.metromate.models.SubwayData;

import java.util.ArrayList;
import java.util.List;

public class FareActivity extends AppCompatActivity {

    private AutoCompleteTextView startStationInput, waypointStationInput, endStationInput;
    private RadioGroup passengerTypeGroup, paymentTypeGroup;
    private TextView fareResult;
    private Button calculateButton;

    private List<String> stationNames;
    private List<Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fare);

        // UI 연결
        startStationInput = findViewById(R.id.start_station_input);
        waypointStationInput = findViewById(R.id.waypoint_station_input);
        endStationInput = findViewById(R.id.end_station_input);
        passengerTypeGroup = findViewById(R.id.passenger_type_group);
        paymentTypeGroup = findViewById(R.id.payment_type_group);
        fareResult = findViewById(R.id.fare_result);
        calculateButton = findViewById(R.id.calculate_fare_button);

        // 데이터 로드
        loadStationData();

        // 요금 계산 버튼 클릭 이벤트
        calculateButton.setOnClickListener(v -> calculateFare());
    }

    /**
     * 역 데이터 로드
     */
    private void loadStationData() {
        Log.d("FareActivity", "지하철 요금 데이터 로드 시작");

        SubwayFareLoader.loadSubwayFareData(this, new SubwayFareLoader.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(SubwayData subwayData) {
                runOnUiThread(() -> {
                    stations = subwayData.getStations();
                    stationNames = new ArrayList<>();
                    for (Station station : stations) {
                        stationNames.add(station.getName());
                    }
                    Log.d("FareActivity", "로드된 역 이름 목록: " + stationNames.toString());

                    // 자동완성 어댑터 연결
                    ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(FareActivity.this, android.R.layout.simple_dropdown_item_1line, stationNames);
                    startStationInput.setAdapter(stationAdapter);
                    waypointStationInput.setAdapter(stationAdapter);
                    endStationInput.setAdapter(stationAdapter);
                });
            }

            @Override
            public void onDataLoadFailed(Exception e) {
                runOnUiThread(() -> Toast.makeText(FareActivity.this, "데이터 로드 실패", Toast.LENGTH_SHORT).show());
            }
        });
    }

    /**
     * 요금 계산 메서드
     */
    private void calculateFare() {
        String startStationName = startStationInput.getText().toString().trim();
        String waypointStationName = waypointStationInput.getText().toString().trim();
        String endStationName = endStationInput.getText().toString().trim();

        if (startStationName.isEmpty() || endStationName.isEmpty()) {
            Toast.makeText(this, "출발역과 도착역을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 출발역, 경유역, 도착역 데이터 유효성 확인
        Station startStation = getStationByName(startStationName);
        Station waypointStation = getStationByName(waypointStationName);
        Station endStation = getStationByName(endStationName);

        if (startStation == null || endStation == null) {
            Toast.makeText(this, "유효하지 않은 역 이름입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 요금 계산 호출
        int totalFare = calculateTotalFare(
                startStation,
                waypointStation,
                endStation,
                passengerTypeGroup.getCheckedRadioButtonId(),
                paymentTypeGroup.getCheckedRadioButtonId()
        );

        // 결과 표시
        fareResult.setText(getString(R.string.total_fare_label) + " " + totalFare + "원");
    }

    /**
     * 이름으로 역 정보를 가져오는 메서드
     */
    private Station getStationByName(String name) {
        for (Station station : stations) {
            if (station.getName().equals(name)) {
                return station;
            }
        }
        return null;
    }

    /**
     * 요금 계산 로직
     */
    private int calculateTotalFare(Station startStation, Station waypointStation, Station endStation, int passengerTypeId, int paymentTypeId) {
        int baseFare = 1250;
        int totalFare = baseFare;

        if (paymentTypeId == R.id.cash_button) {
            totalFare += 100; // 현금 사용 추가 요금
        }

        if (passengerTypeId == R.id.teenager_button) {
            totalFare *= 0.8; // 청소년 할인
        } else if (passengerTypeId == R.id.child_button) {
            totalFare *= 0.5; // 어린이 할인
        }

        return (int) totalFare;
    }
}
