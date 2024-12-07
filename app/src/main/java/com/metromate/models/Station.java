package com.metromate.models;

public class Station {
    private int id;         // 역 ID
    private String name;    // 역 이름
    private String line;    // 노선 이름

    // 생성자
    public Station(int id, String name, String line) {
        this.id = id;
        this.name = name;
        this.line = line;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        return line;
    }
}
