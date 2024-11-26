package com.metromate.data;

public class Station {
    private int id;
    private String name;
    private int line;

    // 기본 생성자
    public Station() {
    }

    // 매개변수가 있는 생성자
    public Station(int id, String name, int line) {
        this.id = id;
        this.name = name;
        this.line = line;
    }

    // Getter와 Setter 메소드
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

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    // toString 메소드(디버깅용)
    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", line=" + line +
                '}';
    }
}
