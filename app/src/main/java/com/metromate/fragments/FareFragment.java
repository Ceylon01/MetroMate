package com.metromate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.metromate.R;
import com.metromate.models.Edge;
import com.metromate.models.Station;
import com.metromate.models.SubwayData;
import com.metromate.utils.FareRouteCalculator;
import com.metromate.PathFinding.SubwayDataLoader;

import java.util.ArrayList;
import java.util.List;

public class FareFragment extends Fragment {

    private AutoCompleteTextView startStationInput, waypointStationInput, endStationInput;
    private Button calculateFareButton;
    private TextView fareResultView;
    private RadioGroup passengerTypeGroup, paymentTypeGroup; // 결제 방식 추가

    private List<Edge> edges;
    private List<Station> stations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fare, container, false);

        // UI 요소 초기화
        startStationInput = view.findViewById(R.id.start_station_input);
        waypointStationInput = view.findViewById(R.id.waypoint_station_input);
        endStationInput = view.findViewById(R.id.end_station_input);
        calculateFareButton = view.findViewById(R.id.calculate_fare_button);
        fareResultView = view.findViewById(R.id.fare_result);
        passengerTypeGroup = view.findViewById(R.id.passenger_type_group);
        paymentTypeGroup = view.findViewById(R.id.payment_type_group); // 결제 방식 추가

        // 지하철 데이터 로드
        SubwayDataLoader.loadSubwayData(requireContext(), new SubwayDataLoader.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(SubwayData subwayData) {
                if (subwayData != null) {
                    edges = subwayData.getEdges();
                    stations = subwayData.getStations();

                    List<String> stationNames = new ArrayList<>();
                    for (Station station : stations) {
                        stationNames.add(station.getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, stationNames);
                    startStationInput.setAdapter(adapter);
                    waypointStationInput.setAdapter(adapter);
                    endStationInput.setAdapter(adapter);
                } else {
                    Toast.makeText(requireContext(), "데이터 로드 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        calculateFareButton.setOnClickListener(v -> calculateFare());

        return view;
    }

    private void calculateFare() {
        String startStationName = startStationInput.getText().toString().trim();
        String waypointStationName = waypointStationInput.getText().toString().trim();
        String endStationName = endStationInput.getText().toString().trim();

        if (startStationName.isEmpty() || endStationName.isEmpty()) {
            Toast.makeText(requireContext(), "출발역과 도착역을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int startId = getStationId(startStationName);
        int endId = getStationId(endStationName);
        int waypointId = waypointStationName.isEmpty() ? -1 : getStationId(waypointStationName);

        if (startId == -1 || endId == -1 || (!waypointStationName.isEmpty() && waypointId == -1)) {
            Toast.makeText(requireContext(), "유효한 역 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Integer> route = FareRouteCalculator.calculateRoute(edges, startId, waypointId, endId);
        if (route.isEmpty()) {
            Toast.makeText(requireContext(), "경로를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String passengerType = getPassengerType();
        String paymentType = getPaymentType();

        // 결제 방식에 따라 추가 매개변수 전달
        boolean optimizeForCard = paymentType.equals("card");

        int totalFare = FareRouteCalculator.calculateFare(edges, route, passengerType, !optimizeForCard);

        // 결제 방식에 따라 결과 표시
        String paymentDescription = optimizeForCard ? "카드" : "1회권";
        String passengerDescription;
        switch (passengerType) {
            case "teenager":
                passengerDescription = "청소년";
                break;
            case "child":
                passengerDescription = "어린이";
                break;
            default:
                passengerDescription = "성인";
                break;
        }

        fareResultView.setText(String.format(
                "%d원\n(결제 방식: %s, 승객 유형: %s)",
                totalFare, paymentDescription, passengerDescription
        ));
    }

    private int getStationId(String name) {
        for (Station station : stations) {
            if (station.getName().equals(name)) {
                return station.getId();
            }
        }
        return -1;
    }

    private String getPassengerType() {
        int selectedId = passengerTypeGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.teenager_button) {
            return "teenager";
        } else if (selectedId == R.id.child_button) {
            return "child";
        }
        return "adult";
    }

    private String getPaymentType() {
        int selectedId = paymentTypeGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.cash_button) {
            return "cash";
        }
        return "card";
    }
}