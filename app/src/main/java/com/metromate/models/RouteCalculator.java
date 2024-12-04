package com.metromate.models;

import java.util.List;

public class RouteCalculator {

    private static final int BASE_FARE = 1250; // 기본 요금
    private static final int EXTRA_FARE_DISTANCE = 100; // 추가 요금 기준 거리 (10km)
    private static final int EXTRA_FARE = 100; // 10km 초과 시 km당 추가 요금

    /**
     * 경로의 총 소요 시간 계산
     */
    public static int calculateTotalTime(List<Edge> edges, List<Integer> route) {
        int totalTime = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            int startId = route.get(i);
            int endId = route.get(i + 1);
            for (Edge edge : edges) {
                if (edge.getStart() == startId && edge.getEnd() == endId) {
                    totalTime += edge.getTime(); // 초 단위
                    break;
                }
            }
        }
        return totalTime / 60; // 분 단위로 변환
    }

    /**
     * 경로의 총 요금 계산
     */
    public static int calculateTotalFare(List<Edge> edges, List<Integer> route) {
        int totalDistance = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            int startId = route.get(i);
            int endId = route.get(i + 1);
            for (Edge edge : edges) {
                if (edge.getStart() == startId && edge.getEnd() == endId) {
                    totalDistance += edge.getDistance();
                    break;
                }
            }
        }

        int fare = BASE_FARE;
        if (totalDistance > EXTRA_FARE_DISTANCE * 1000) {
            int extraDistance = totalDistance - EXTRA_FARE_DISTANCE * 1000; // 초과 거리
            fare += (extraDistance / 1000) * EXTRA_FARE; // 추가 요금 계산
        }

        return fare;
    }
}
