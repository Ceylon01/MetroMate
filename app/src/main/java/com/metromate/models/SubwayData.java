package com.metromate.models;

import java.util.List;

public class SubwayData {
    public List<Station> stations;
    public List<Edge> edges;

    public SubwayData(List<Station> stations, List<Edge> edges) {
        this.stations = stations;
        this.edges = edges;
    }
}
