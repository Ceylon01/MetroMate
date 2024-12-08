package com.metromate.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.metromate.R;
import com.metromate.adapters.GridAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recentPathRecycler;
    private RecyclerView recentStationRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_home.xml 레이아웃 로드
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // RecyclerView 초기화
        recentPathRecycler = view.findViewById(R.id.recent_path_recycler);
        recentStationRecycler = view.findViewById(R.id.recent_station_recycler);

        // GridLayoutManager 설정 (3열 그리드)
        recentPathRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recentStationRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // 데이터 로드 및 어댑터 설정
        loadRecentData();

        return view;
    }

    private void loadRecentData() {
        // 최근 경로 데이터 로드
        List<String> recentPaths = loadRecentPaths();
        GridAdapter pathAdapter = new GridAdapter(recentPaths);
        recentPathRecycler.setAdapter(pathAdapter);

        // 최근 검색한 역 데이터 로드
        List<String> recentStations = loadRecentStations();
        GridAdapter stationAdapter = new GridAdapter(recentStations);
        recentStationRecycler.setAdapter(stationAdapter);
    }

    private List<String> loadRecentPaths() {
        // SharedPreferences에서 최근 경로 데이터 로드
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("RecentPaths", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("path_list", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>(); // 초기 상태: 빈 리스트 반환
    }

    private List<String> loadRecentStations() {
        // SharedPreferences에서 최근 검색한 역 데이터 로드
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("RecentStations", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("station_list", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>(); // 초기 상태: 빈 리스트 반환
    }

    private void saveRecentPath(String path) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("RecentPaths", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 기존 데이터 불러오기
        List<String> recentPaths = loadRecentPaths();
        if (!recentPaths.contains(path)) {
            recentPaths.add(0, path); // 최신 데이터 맨 앞에 추가
            if (recentPaths.size() > 5) { // 최대 5개만 유지
                recentPaths.remove(recentPaths.size() - 1);
            }
        }

        // JSON으로 변환하여 저장
        Gson gson = new Gson();
        String json = gson.toJson(recentPaths);
        editor.putString("path_list", json);
        editor.apply();
    }

    private void saveRecentStation(String station) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("RecentStations", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 기존 데이터 불러오기
        List<String> recentStations = loadRecentStations();
        if (!recentStations.contains(station)) {
            recentStations.add(0, station); // 최신 데이터 맨 앞에 추가
            if (recentStations.size() > 5) { // 최대 5개만 유지
                recentStations.remove(recentStations.size() - 1);
            }
        }

        // JSON으로 변환하여 저장
        Gson gson = new Gson();
        String json = gson.toJson(recentStations);
        editor.putString("station_list", json);
        editor.apply();
    }
}
