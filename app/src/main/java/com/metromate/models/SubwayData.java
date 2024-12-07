package com.metromate.models;

import java.util.List;

public class SubwayData {
    private List<Station> stations;
    private List<Edge> edges; // Edge 데이터 추가

    public SubwayData(List<Station> stations, List<Edge> edges) { // Edge 추가
        this.stations = stations;
        this.edges = edges;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Edge> getEdges() { // Edge getter 추가
        return edges;
    }
}
