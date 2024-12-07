package com.metromate.models;

import java.util.List;

public class SubwayData {
    private List<Station> stations;
    private List<Edge> edges;

    public SubwayData(List<Station> stations, List<Edge> edges) {
        this.stations = stations;
        this.edges = edges;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
