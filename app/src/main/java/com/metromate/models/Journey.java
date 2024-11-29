package com.metromate.models;

public class Journey {
    public String startStation;
    public String endStation;
    public int distance; // 이동 거리 (km)
    public boolean isTransfer; // 환승 여부
    public boolean isSameLine; // 동일 노선 여부

    public Journey(String startStation, String endStation, int distance, boolean isTransfer, boolean isSameLine) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
        this.isTransfer = isTransfer;
        this.isSameLine = isSameLine;
    }
}
