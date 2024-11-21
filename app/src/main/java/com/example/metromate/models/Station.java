package com.example.metromate.models;

public class Station {
    private String name;
    private int id;

    // 생성자
    public Station(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter 및 Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
