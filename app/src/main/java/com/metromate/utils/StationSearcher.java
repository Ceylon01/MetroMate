package com.metromate.utils; // 적절한 패키지로 설정

import com.metromate.data.Station;

import java.util.ArrayList;
import java.util.List;

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

        }
        return results;
    }
}
