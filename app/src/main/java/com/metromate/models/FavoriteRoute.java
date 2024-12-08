package com.metromate.models;

import java.util.List;

public class FavoriteRoute {
    private List<String> routeStations;  // 경로에 포함된 역들
    private int totalTime;
    private int totalFare;
    private int totalStations;

    public FavoriteRoute(List<String> routeStations, int totalTime, int totalFare, int totalStations) {
        this.routeStations = routeStations;
        this.totalTime = totalTime;
        this.totalFare = totalFare;
        this.totalStations = totalStations;
    }

    public List<String> getRouteStations() {
        return routeStations;
    }

    public String getRouteName() {
        return String.join(" → ", routeStations);
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getTotalFare() {
        return totalFare;
    }

    public int getTotalStations() {
        return totalStations;
    }
}
