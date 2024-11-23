package com.example.metro_mate

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.metro_mate.data.SubwayDataLoader
import com.example.metro_mate.data.SubwayGraph
import com.example.metro_mate.domain.aStarSearch
import com.example.metro_mate.utils.calculateTotalCost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // 잘못된 괄호 제거

        // 데이터 로드
        val subwayData = SubwayDataLoader(this).loadSubwayData() // 데이터 로드
        val stations = subwayData.first // 역 정보
        val edges = subwayData.second // 경로 정보

        if (stations.isEmpty() || edges.isEmpty()) {
            // 데이터가 비어 있을 경우 처리
            throw IllegalStateException("지하철 데이터가 비어 있습니다.")
        }

        // 그래프 생성
        val graph = SubwayGraph(edges)

        // A* 알고리즘 실행
        val shortestPath = aStarSearch(
            graph = graph,
            start = 101,   // 출발역 ID
            goal = 104,    // 도착역 ID
            heuristic = { _, _ -> 0 },  // 휴리스틱 함수
            criterion = { it.time }    // 경로 기준 (시간)
        )

        // 경로가 없을 경우 처리
        if (shortestPath.isEmpty()) {
            throw IllegalStateException("경로를 찾을 수 없습니다.")
        }

        // 총 비용 계산
        val totalCost = calculateTotalCost(shortestPath, edges)

        // 결과 사용 예시 (추가 로직에서 활용)
        println("Shortest Path: $shortestPath")
        println("Total Cost: $totalCost")
    }
}
