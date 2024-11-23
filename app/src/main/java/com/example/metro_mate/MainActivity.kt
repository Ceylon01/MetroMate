package com.example.metro_mate

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.metro_mate.data.Edge
import com.example.metro_mate.data.SubwayDataLoader
import com.example.metro_mate.data.SubwayGraph
import com.example.metro_mate.domain.aStarSearch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 데이터 로드
        val (stations, edges) = SubwayDataLoader(this).loadSubwayData()

        // 그래프 생성
        val graph = SubwayGraph(edges)

        // 경로 탐색 예제
        val shortestPathByTime = aStarSearch(
            graph = graph,
            start = 101,
            goal = 104,
            heuristic = { _, _ -> 0 },
            criterion = { it.time } // 시간 기준
        )

        val shortestPathByCost = aStarSearch(
            graph = graph,
            start = 101,
            goal = 104,
            heuristic = { _, _ -> 0 },
            criterion = { it.cost } // 비용 기준
        )

        Log.d("MetroMate", "Shortest Path by Time: $shortestPathByTime")
        Log.d("MetroMate", "Shortest Path by Cost: $shortestPathByCost")
    }
}
