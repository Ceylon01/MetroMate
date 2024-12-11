package com.metromate.models;

import java.util.List;

public class Station {
    private int id;            // 역 ID
    private String name;       // 역 이름
    private String line;        // 호선
    private List<String> lines; // 추가된 필드: 해당 역이 포함된 모든 노선
    private String previous;   // 이전 역
    private String next;       // 다음 역
    private String subwayType; // 지하철 타입 (예: 1호선)
    private String subwayStatus; // 지하철 상태 (예: 운행중, 운행중지 등)

    // 생성자
    public Station(int id, String name, String line, String previous, String next, String subwayType, String subwayStatus) {
        this.id = id;
        this.name = name;
        this.line = line;
        this.previous = previous;
        this.next = next;
        this.subwayType = subwayType; // subwayType 초기화
        this.subwayStatus = subwayStatus; // subwayStatus 초기화
    }

    // Getter 메소드
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

    public List<String> getLines() { return lines; }

    public String getSubwayType() {
        return subwayType; // subwayType 반환
    }

    public String getSubwayStatus() {
        return subwayStatus; // subwayStatus 반환
    }

    // Setter 메소드
    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public void setNext(String next) {
        this.next = next;
    }

    // subwayType을 설정하는 setter 추가
    public void setSubwayType(String subwayType) {
        this.subwayType = subwayType;
    }

    // subwayStatus를 설정하는 setter 추가
    public void setSubwayStatus(String subwayStatus) {
        this.subwayStatus = subwayStatus;
    }

    // equals()와 hashCode() 메소드 (필요시 추가)
}
