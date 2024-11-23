package com.example.metro_mate.domain

import com.example.metro_mate.data.Edge
import com.example.metro_mate.data.SubwayGraph

fun aStarSearch(
    graph: SubwayGraph,
    start: Int,
    goal: Int,
    heuristic: (Int, Int) -> Int,
    criterion: (Edge) -> Int // 기준 함수: 시간, 비용, 또는 조합
): List<Int> {
    val openSet = mutableMapOf(start to 0) // 탐색 중인 노드
    val cameFrom = mutableMapOf<Int, Int>() // 경로 추적
    val gScore = mutableMapOf(start to 0) // 시작점에서 현재 노드까지의 비용
    val fScore = mutableMapOf(start to heuristic(start, goal)) // 예상 총 비용 (g + h)

    while (openSet.isNotEmpty()) {
        // fScore가 가장 낮은 노드를 선택
        val current = openSet.minByOrNull { fScore[it.key] ?: Int.MAX_VALUE }!!.key

        // 목적지에 도달하면 경로 반환
        if (current == goal) {
            return reconstructPath(cameFrom, current)
        }

        openSet.remove(current)

        // 현재 노드의 이웃 탐색
        for (neighborEdge in graph.getNeighbors(current)) {
            val neighbor = neighborEdge.end
            val cost = criterion(neighborEdge) // 선택한 기준 적용
            val tentativeGScore = gScore[current]!! + cost

            if (tentativeGScore < (gScore[neighbor] ?: Int.MAX_VALUE)) {
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeGScore
                fScore[neighbor] = tentativeGScore + heuristic(neighbor, goal)
                openSet[neighbor] = tentativeGScore
            }
        }
    }

    // 경로를 찾을 수 없으면 빈 리스트 반환
    return emptyList()
}

// 경로 재구성 함수
fun reconstructPath(cameFrom: Map<Int, Int>, current: Int): List<Int> {
    val path = mutableListOf(current)
    var node = current
    while (cameFrom.containsKey(node)) {
        node = cameFrom[node]!!
        path.add(0, node)
    }
    return path
}
