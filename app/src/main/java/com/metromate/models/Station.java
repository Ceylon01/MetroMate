package com.metromate.models;

public class Station {
    private int id;            // 역 ID
    private String name;       // 역 이름
    private String line;       // 호선
    private String previous;   // 이전 역
    private String next;       // 다음 역

    public Station(int id, String name, String line, String previous, String next) {
        this.id = id;
        this.name = name;
        this.line = line;
        this.previous = previous;
        this.next = next;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        return line;
    }

    public String getPrevious() {
        return previous;
    }

    public String getNext() {
        return next;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
