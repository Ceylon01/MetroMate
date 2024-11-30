//package com.metromate.PathFinding;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.metromate.R;
//import com.metromate.models.SubwayData;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.lang.reflect.Type;
//import java.util.List;
//
//public class PathFindingActivity extends AppCompatActivity {
//    private SubwayData subwayData;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_astar_test); // 수정된 레이아웃 파일
//
//        // UI 요소 가져오기
//        EditText startStationInput = findViewById(R.id.start_station_input);
//        EditText endStationInput = findViewById(R.id.end_station_input);
//        Button searchButton = findViewById(R.id.search_button);
//        TextView costPathTextView = findViewById(R.id.cost_path_text_view);
//        TextView timePathTextView = findViewById(R.id.time_path_text_view);
//
//        // 데이터 로드
//        loadSubwayData();
//
//        // 검색 버튼 클릭 이벤트
//        searchButton.setOnClickListener(v -> {
//            try {
//                // 입력 값 가져오기
//                int startId = Integer.parseInt(startStationInput.getText().toString().trim());
//                int endId = Integer.parseInt(endStationInput.getText().toString().trim());
//
//                // 요금 기준 경로 찾기
//                List<Integer> costPath = AStarAlgorithm.findShortestPath(subwayData.edges, startId, endId, false);
//                costPathTextView.setText("요금 기준 경로: " + costPath);
//
//                // 시간 기준 경로 찾기
//                List<Integer> timePath = AStarAlgorithm.findShortestPath(subwayData.edges, startId, endId, true);
//                timePathTextView.setText("시간 기준 경로: " + timePath);
//            } catch (Exception e) {
//                Toast.makeText(this, "입력을 확인하세요.", Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        });
//    }
//
//    /**
//     * 'assets/data/subway_data.json'에서 데이터 로드
//     */
//    private void loadSubwayData() {
//        try {
//            InputStream inputStream = getAssets().open("data/subway_data.json");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            Gson gson = new Gson();
//            Type subwayDataType = new TypeToken<SubwayData>() {}.getType();
//            subwayData = gson.fromJson(reader, subwayDataType);
//            reader.close();
//
//            Toast.makeText(this, "데이터 로드 성공!", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Toast.makeText(this, "데이터 로드 실패: " + e.getMessage(), Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }
//    }
//}
