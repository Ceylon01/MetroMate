package com.metromate.models;

public class Journey {
    private String startStation;
    private String endStation;
    private int distance;
    private boolean isTransfer;
    private boolean isSameLine;

    public Journey(String startStation, String endStation, int distance, boolean isTransfer, boolean isSameLine) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
        this.isTransfer = isTransfer;
        this.isSameLine = isSameLine;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isTransfer() {
        return isTransfer;
    }

    public boolean isSameLine() {
        return isSameLine;
    }
}
