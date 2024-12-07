package com.metromate.models;

public class Edge {
    private int start;
    private int end;
    private int distance;
    private int time;

    public Edge(int start, int end, int distance, int time) {
        this.start = start;
        this.end = end;
        this.distance = distance;
        this.time = time;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getDistance() {
        return distance;
    }

    public int getTime() {
        return time;
    }
}
