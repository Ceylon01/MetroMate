package com.example.metro_mate.data

data class Edge(val start: Int, val end: Int, val time: Int, val distance: Int, val cost: Int)

class SubwayGraph(edges: List<Edge>) {
    private val adjacencyList = mutableMapOf<Int, MutableList<Edge>>()

    init {
        edges.forEach { edge ->
            adjacencyList.computeIfAbsent(edge.start) { mutableListOf() }.add(edge)
        }
    }

    // 특정 노드의 이웃 반환 (Edge 전체 반환)
    fun getNeighbors(node: Int): List<Edge> {
        return adjacencyList[node] ?: emptyList()
    }
}