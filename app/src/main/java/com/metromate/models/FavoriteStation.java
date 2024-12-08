package com.metromate.models;

import java.util.Objects;  // 이 줄 추가

public class FavoriteStation {
    private int id;
    private String name;
    private String line;
    private String subwayType;
    private String subwayStatus;

    // 생성자
    public FavoriteStation(int id, String name, String line, String subwayType, String subwayStatus) {
        this.id = id;
        this.name = name;
        this.line = line;
        this.subwayType = subwayType;
        this.subwayStatus = subwayStatus;
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

    public String getSubwayType() {
        return subwayType;
    }

    public String getSubwayStatus() {
        return subwayStatus;
    }

    // equals()와 hashCode() 메소드 추가 (Set에서 사용할 때 필요)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FavoriteStation that = (FavoriteStation) obj;
        return id == that.id && name.equals(that.name) && line.equals(that.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, line);
    }
}
