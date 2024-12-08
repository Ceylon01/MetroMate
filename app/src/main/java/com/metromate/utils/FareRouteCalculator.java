package com.metromate.utils;

import com.metromate.models.Edge;
import com.metromate.PathFinding.AStarAlgorithm;

import java.util.ArrayList; // ArrayList import 추가
import java.util.List;

public class FareRouteCalculator {

    public static int calculateFare(List<Edge> edges, List<Integer> route, String passengerType, boolean isCash) {
        // 경로에 따른 총 거리 계산
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

        // 기본 요금과 추가 요금을 합산하여 반환
        return baseFare + additionalFare;
    }

    // 경로에 따른 총 거리 계산
    private static int calculateTotalDistance(List<Edge> edges, List<Integer> route) {
        int totalDistance = 0;

        // route에서 두 역씩 가져와 연결된 Edge를 찾아 거리 합산
        for (int i = 0; i < route.size() - 1; i++) {
            int startId = route.get(i);
            int endId = route.get(i + 1);

            for (Edge edge : edges) {
                if (edge.getStart() == startId && edge.getEnd() == endId) {
                    totalDistance += edge.getDistance(); // 거리 합산
                    break;
                }
            }
        }
        return totalDistance;
    }

    // 추가 요금 계산
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

    // A* 알고리즘을 사용하여 경로 계산
    public static List<Integer> calculateRoute(List<Edge> edges, int startId, int waypointId, int endId) {
        if (waypointId != -1) {
            // 경유지가 있는 경우, 두 번의 경로 계산
            List<Integer> toWaypoint = AStarAlgorithm.findShortestPath(edges, startId, waypointId, false);
            List<Integer> toEnd = AStarAlgorithm.findShortestPath(edges, waypointId, endId, false);

            if (!toWaypoint.isEmpty() && !toEnd.isEmpty()) {
                toWaypoint.remove(toWaypoint.size() - 1); // 중복된 경유지 제거
                toWaypoint.addAll(toEnd);
                return toWaypoint;
            }
        } else {
            // 경유지 없는 경우
            return AStarAlgorithm.findShortestPath(edges, startId, endId, false);
        }

        return new ArrayList<>();
    }
}
