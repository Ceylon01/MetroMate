package com.example.metromate.utils; // 적절한 패키지로 설정

import java.util.ArrayList;
import java.util.List;

import com.example.metromate.models.Station; // Station 클래스가 있어야 함

public class StationSearcher {

    private List<Station> stationList;

    // StationSearcher 생성자
    public StationSearcher(List<Station> stationList) {
        this.stationList = stationList;
    }

    // 검색 메서드: 입력된 query와 일치하거나 포함된 역 반환
    public List<Station> search(String query) {
        List<Station> results = new ArrayList<>();
        for (Station station : stationList) {
            if (station.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(station);
            }
        }
        return results;
    }
}
