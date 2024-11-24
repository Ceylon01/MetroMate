package com.example.metro_mate.domain

import com.example.metro_mate.data.Edge
import com.example.metro_mate.data.SubwayGraph
import java.util.PriorityQueue

fun aStarSearch(
    graph: SubwayGraph,
    start: Int,
    goal: Int,
    heuristic: (Int, Int) -> Int,
    criterion: (Edge) -> Int
): List<Int> {
    // PriorityQueue를 사용하여 (노드 ID, fScore) 형태로 저장
    val openSet = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
    openSet.add(Pair(start, 0)) // 시작 노드와 초기 fScore 추가

    val cameFrom = mutableMapOf<Int, Int>() // 경로 추적
    val gScore = mutableMapOf(start to 0) // 시작점에서 현재 노드까지의 비용
    val fScore = mutableMapOf(start to heuristic(start, goal)) // 예상 총 비용 (g + h)

    while (openSet.isNotEmpty()) {
        // PriorityQueue에서 값을 가져오고 null 시 루프 종료
        val current = openSet.poll()?.first ?: break

        // 목표 노드에 도달하면 경로 반환
        if (current == goal) {
            return reconstructPath(cameFrom, current)
        }

        // 현재 노드의 이웃 탐색
        for (neighborEdge in graph.getNeighbors(current)) {
            val neighbor = neighborEdge.end
            val cost = criterion(neighborEdge) // 기준 가중치 적용
            val tentativeGScore = gScore[current]!! + cost

            if (tentativeGScore < (gScore[neighbor] ?: Int.MAX_VALUE)) {
                // 더 나은 경로 발견
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeGScore
                fScore[neighbor] = tentativeGScore + heuristic(neighbor, goal)

                // openSet에 이웃 노드 추가
                if (openSet.none { it.first == neighbor }) {
                    openSet.add(Pair(neighbor, fScore[neighbor]!!))
                }
            }
        }
    }

    // 경로를 찾을 수 없으면 빈 리스트 반환
    return emptyList()
}


/**
 * 탐색된 경로를 재구성합니다.
 * @param cameFrom Map<Int, Int> - 경로 추적 정보
 * @param current Int - 현재 노드
 * @return List<Int> - 재구성된 경로
 */
fun reconstructPath(cameFrom: Map<Int, Int>, current: Int): List<Int> {
    val path = mutableListOf(current)
    var node = current
    while (cameFrom.containsKey(node)) {
        node = cameFrom[node]!!
        path.add(0, node)
    }
    return path
}