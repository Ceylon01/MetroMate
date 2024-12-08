package com.metromate.utils;

import com.metromate.models.Edge;

import java.util.List;

public class FareRouteCalculator {

    public static int calculateFare(List<Edge> edges, List<Integer> route, String passengerType, boolean isCash) {
        int totalDistance = calculateTotalDistance(edges, route);

        // 승객 유형 및 결제 방식에 따른 기본 요금 설정
        int baseFare;
        if (isCash) { // 1회권 요금
            switch (passengerType) {
                case "teenager":
                    baseFare = 1500; // 청소년 1회권 요금
                    break;
                case "child":
                    baseFare = 500; // 어린이 1회권 요금
                    break;
                default:
                    baseFare = 1500; // 성인 1회권 요금
                    break;
            }
        } else { // 교통카드 요금
            switch (passengerType) {
                case "teenager":
                    baseFare = 800; // 청소년 카드 요금
                    break;
                case "child":
                    baseFare = 500; // 어린이 카드 요금
                    break;
                default:
                    baseFare = 1400; // 성인 카드 요금
                    break;
            }
        }

        // 거리 초과에 따른 추가 요금 계산
        int additionalFare = calculateAdditionalFare(totalDistance, isCash);

        return baseFare + additionalFare;
    }

    private static int calculateTotalDistance(List<Edge> edges, List<Integer> route) {
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
        return totalDistance;
    }

    private static int calculateAdditionalFare(int totalDistance, boolean isCash) {
        int additionalFare = 0;

        if (totalDistance > 10_000) { // 10km 초과
            int extraDistance = totalDistance - 10_000;
            int fareStepCount = (int) Math.ceil((double) extraDistance / 5_000); // 5km마다

            // 추가 요금 계산 (1회권/카드 기준)
            additionalFare = fareStepCount * (isCash ? 150 : 100);
        }

        return additionalFare;
    }

    public static List<Integer> calculateRoute(List<Edge> edges, int startId, int waypointId, int endId) {
        // 경로 계산 로직 (예: 다익스트라, A* 알고리즘)
        return List.of(startId, endId); // 단순히 시작-끝 경로만 반환 (데모용)
    }
}
