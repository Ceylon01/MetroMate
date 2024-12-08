package com.metromate.models;

import java.util.List;
import java.util.Map;

public class SubwayData {
    private List<Station> stations; // 역 데이터
    private List<Edge> edges; // 경로 데이터
    private Map<String, Integer> baseFare; // 성인, 청소년, 어린이 기본 요금
    private List<DistanceFare> distanceAdditionalFare; // 거리 추가 요금

    // 기존 생성자에 기본 요금 및 거리 추가 요금 필드를 포함
    public SubwayData(List<Station> stations, List<Edge> edges, Map<String, Integer> baseFare, List<DistanceFare> distanceAdditionalFare) {
        this.stations = stations;
        this.edges = edges;
        this.baseFare = baseFare;
        this.distanceAdditionalFare = distanceAdditionalFare;
    }

    // Getter 메서드 추가
    public List<Station> getStations() {
        return stations;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Map<String, Integer> getBaseFare() {
        return baseFare;
    }

    public List<DistanceFare> getDistanceAdditionalFare() {
        return distanceAdditionalFare;
    }

    // 내부 클래스: 거리 추가 요금 구조
    public static class DistanceFare {
        private int[] range; // 거리 범위 (예: [0, 10])
        private int farePerKm; // 해당 범위 내 거리당 추가 요금

        // Getter 메서드 추가
        public int[] getRange() {
            return range;
        }

        public int getFarePerKm() {
            return farePerKm;
        }
    }
}
