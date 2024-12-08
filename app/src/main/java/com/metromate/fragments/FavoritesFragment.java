package com.metromate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metromate.R;
import com.metromate.adapters.FavoritesAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private List<String> favoritesList;  // 즐겨찾기 리스트
    private List<Station> stations;      // 역 데이터 리스트

    private Button addButton;  // 역 추가 버튼
    private EditText stationInput;  // 추가할 역 입력 필드

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 역 데이터 초기화
        stations = loadStations();
        favoritesList = new ArrayList<>();

        // 역 추가 버튼과 입력 필드 초기화
        addButton = view.findViewById(R.id.addButton);
        stationInput = view.findViewById(R.id.stationInput);

        // Adapter 설정
        adapter = new FavoritesAdapter(favoritesList);
        recyclerView.setAdapter(adapter);

        // 역 추가 버튼 클릭 리스너
        addButton.setOnClickListener(v -> {
            String input = stationInput.getText().toString().trim();
            addFavoriteStation(input);
        });

        return view;
    }

    // 역 추가 메소드
    private void addFavoriteStation(String stationName) {
        for (Station station : stations) {
            if (station.getName().equals(stationName)) {
                if (!favoritesList.contains(stationName)) {
                    favoritesList.add(stationName);
                    adapter.notifyDataSetChanged();
                    stationInput.setText(""); // 입력 필드 비우기
                    Toast.makeText(getContext(), "역이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(getContext(), "이미 추가된 역입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        Toast.makeText(getContext(), "존재하지 않는 역입니다.", Toast.LENGTH_SHORT).show();
    }

    // 더미 역 데이터 로드 (실제 앱에서는 JSON에서 불러올 수 있음)
    private List<Station> loadStations() {
        List<Station> stationList = new ArrayList<>();
        // 역 데이터 추가
        stationList.add(new Station(101, "솔빛", "1"));
        stationList.add(new Station(102, "달새", "1"));
        // 필요에 따라 더 많은 역 추가
        return stationList;
    }

    // 역 모델 클래스
    public static class Station {
        private int id;
        private String name;
        private String line;

        public Station(int id, String name, String line) {
            this.id = id;
            this.name = name;
            this.line = line;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getLine() {
            return line;
        }
    }
}
