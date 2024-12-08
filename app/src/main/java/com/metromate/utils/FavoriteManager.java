package com.metromate.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.metromate.models.FavoriteRoute;
import com.metromate.models.FavoriteStation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteManager {

    private static final String PREFS_NAME = "MetroMatePrefs";
    private static final String FAVORITE_STATIONS_KEY = "favoriteStations";
    private static final String FAVORITE_ROUTES_KEY = "favoriteRoutes";
    private SharedPreferences sharedPreferences;

    private List<FavoriteStation> favoriteStations;
    private List<FavoriteRoute> favoriteRoutes;

    public FavoriteManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.favoriteStations = loadFavoriteStations();  // SharedPreferences에서 불러오기
        this.favoriteRoutes = loadFavoriteRoutes();  // SharedPreferences에서 불러오기
    }

    // 즐겨찾기 역 목록 불러오기 (SharedPreferences에서)
    private List<FavoriteStation> loadFavoriteStations() {
        Set<String> favorites = sharedPreferences.getStringSet(FAVORITE_STATIONS_KEY, new HashSet<>());
        List<FavoriteStation> favoriteStations = new ArrayList<>();

        // String으로 저장된 즐겨찾기 목록을 FavoriteStation 객체로 변환
        for (String favorite : favorites) {
            String[] parts = favorite.split(",");  // "id,name,line,subwayType,subwayStatus" 형식으로 저장
            if (parts.length == 5) {
                try {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String line = parts[2];
                    String subwayType = parts[3];
                    String subwayStatus = parts[4];
                    favoriteStations.add(new FavoriteStation(id, name, line, subwayType, subwayStatus));
                } catch (NumberFormatException e) {
                    Log.e("FavoriteManager", "Error parsing favorite station: " + e.getMessage());
                }
            }
        }
        return favoriteStations;
    }

    // 즐겨찾기 경로 목록 불러오기 (SharedPreferences에서)
    private List<FavoriteRoute> loadFavoriteRoutes() {
        Set<String> favorites = sharedPreferences.getStringSet(FAVORITE_ROUTES_KEY, new HashSet<>());
        List<FavoriteRoute> favoriteRoutes = new ArrayList<>();

        // String으로 저장된 즐겨찾기 경로 목록을 FavoriteRoute 객체로 변환
        for (String favorite : favorites) {
            String[] parts = favorite.split(",");  // "stations,totalTime,totalFare,totalStations" 형식으로 저장
            if (parts.length == 4) {
                try {
                    String stations = parts[0]; // 역 리스트
                    int totalTime = Integer.parseInt(parts[1]);
                    int totalFare = Integer.parseInt(parts[2]);
                    int totalStations = Integer.parseInt(parts[3]);
                    String[] stationArray = stations.split(";"); // 역 리스트를 분리
                    favoriteRoutes.add(new FavoriteRoute(java.util.Arrays.asList(stationArray), totalTime, totalFare, totalStations));
                } catch (NumberFormatException e) {
                    Log.e("FavoriteManager", "Error parsing favorite route: " + e.getMessage());
                }
            }
        }
        return favoriteRoutes;
    }

    // 즐겨찾기 역 목록 저장 (SharedPreferences에)
    private void saveFavoriteStations() {
        Set<String> favorites = new HashSet<>();
        for (FavoriteStation station : favoriteStations) {
            // FavoriteStation을 String으로 변환하여 저장
            favorites.add(station.getId() + "," + station.getName() + "," + station.getLine() + "," +
                    station.getSubwayType() + "," + station.getSubwayStatus());
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(FAVORITE_STATIONS_KEY, favorites);
        editor.apply();
    }

    // 즐겨찾기 경로 목록 저장 (SharedPreferences에)
    private void saveFavoriteRoutes() {
        Set<String> favorites = new HashSet<>();
        for (FavoriteRoute route : favoriteRoutes) {
            // FavoriteRoute를 String으로 변환하여 저장
            String stations = String.join(";", route.getRouteStations());
            favorites.add(stations + "," + route.getTotalTime() + "," + route.getTotalFare() + "," + route.getTotalStations());
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(FAVORITE_ROUTES_KEY, favorites);
        editor.apply();
    }

    // 즐겨찾기 역 추가
    public void addFavoriteStation(FavoriteStation station) {
        if (!favoriteStations.contains(station)) {
            favoriteStations.add(station);  // 메모리 내에 역 추가
            saveFavoriteStations();  // SharedPreferences에 저장
        }
    }

    // 즐겨찾기 경로 추가
    public void addFavoriteRoute(FavoriteRoute route) {
        if (!favoriteRoutes.contains(route)) {
            favoriteRoutes.add(route);  // 메모리 내에 경로 추가
            saveFavoriteRoutes();  // SharedPreferences에 저장
        }
    }

    // 즐겨찾기 역 삭제 (이름으로 삭제)
    public void removeFavoriteStation(FavoriteStation station) {
        favoriteStations.remove(station);  // 메모리 내에서 삭제
        saveFavoriteStations();  // SharedPreferences에 저장
    }

    // 즐겨찾기 경로 삭제 (경로 이름으로 삭제)
    public void removeFavoriteRoute(FavoriteRoute route) {
        favoriteRoutes.remove(route);  // 메모리 내에서 삭제
        saveFavoriteRoutes();  // SharedPreferences에 저장
    }

    // 즐겨찾기 역 목록 반환
    public List<FavoriteStation> getFavoriteStations() {
        return favoriteStations;
    }

    // 즐겨찾기 경로 목록 반환
    public List<FavoriteRoute> getFavoriteRoutes() {
        return favoriteRoutes;
    }
}
