package com.metromate;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metromate.fare.FareCalculatorLogic;
import com.metromate.models.Journey;
import com.metromate.models.Passenger;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FareCalculatorActivity extends AppCompatActivity {

    private AutoCompleteTextView startStationInput;
    private AutoCompleteTextView endStationInput;
    private AutoCompleteTextView waypointStationInput;
    private RadioGroup passengerTypeGroup;
    private RadioGroup paymentTypeGroup;
    private TextView fareResult;
    private Button calculateFareButton;

    private Map<String, Map<String, Integer>> subwayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fare);

        // Initialize views
        startStationInput = findViewById(R.id.start_station_input);
        endStationInput = findViewById(R.id.end_station_input);
        waypointStationInput = findViewById(R.id.waypoint_station_input);
        passengerTypeGroup = findViewById(R.id.passenger_type_group);
        paymentTypeGroup = findViewById(R.id.payment_type_group);
        fareResult = findViewById(R.id.fare_result);
        calculateFareButton = findViewById(R.id.calculate_fare_button);

        // Load subway data
        loadSubwayData();

        // Set up autocomplete
        setupAutoComplete();

        calculateFareButton.setOnClickListener(v -> {
            String startStation = startStationInput.getText().toString();
            String endStation = endStationInput.getText().toString();
            String waypointStation = waypointStationInput.getText().toString();

            if (!subwayData.containsKey(startStation) || !subwayData.containsKey(endStation)) {
                fareResult.setText("유효하지 않은 역 이름입니다.");
                return;
            }

            // Calculate distance
            int distance = calculateDistance(startStation, endStation, waypointStation);

            // Determine passenger type
            int selectedPassengerType = passengerTypeGroup.getCheckedRadioButtonId();
            double discountRate = 1.0; // Default for adults
            if (selectedPassengerType == R.id.teenager_button) {
                discountRate = 0.8; // 20% discount
            } else if (selectedPassengerType == R.id.child_button) {
                discountRate = 0.5; // 50% discount
            }

            // Get payment type
            boolean isUsingCard = paymentTypeGroup.getCheckedRadioButtonId() == R.id.card_button;

            // Create passenger and journey objects
            Passenger passenger = new Passenger(discountRate, isUsingCard);
            Journey journey = new Journey(startStation, endStation, distance, false, true);

            // Calculate fare
            int totalFare = FareCalculatorLogic.calculateFare(passenger, journey);

            // Display fare
            fareResult.setText(totalFare + "원");
        });
    }

    private void loadSubwayData() {
        try {
            InputStream inputStream = getAssets().open("data/subway_data.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");
            Type type = new TypeToken<Map<String, Map<String, Integer>>>() {}.getType();
            subwayData = new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            fareResult.setText("지하철 데이터를 불러오는 데 실패했습니다.");
        }
    }

    private void setupAutoComplete() {
        List<String> stationNames = new ArrayList<>(subwayData.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stationNames);

        startStationInput.setAdapter(adapter);
        endStationInput.setAdapter(adapter);
        waypointStationInput.setAdapter(adapter);
    }

    private int calculateDistance(String start, String end, String waypoint) {
        if (waypoint.isEmpty()) {
            return getShortestDistance(start, end);
        } else {
            int distance1 = getShortestDistance(start, waypoint);
            int distance2 = getShortestDistance(waypoint, end);
            return distance1 + distance2;
        }
    }

    private int getShortestDistance(String start, String end) {
        // Implement A* algorithm or other pathfinding logic here using `subwayData`
        // Placeholder: Return direct distance if available
        if (subwayData.containsKey(start) && subwayData.get(start).containsKey(end)) {
            return subwayData.get(start).get(end);
        }
        return Integer.MAX_VALUE; // Indicating no path found
    }
}
