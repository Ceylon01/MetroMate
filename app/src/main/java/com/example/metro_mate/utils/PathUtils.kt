package com.example.metro_mate.utils

import com.example.metro_mate.data.Edge

/**
 * Calculate the total cost of a given path using the edges data.
 * @param path List of station IDs representing the path.
 * @param edges List of Edge objects representing the graph connections.
 * @return Total cost of the path.
 */
fun calculateTotalCost(path: List<Int>, edges: List<Edge>): Int {
    var totalCost = 0

    for (i in 0 until path.size - 1) {
        val start = path[i]
        val end = path[i + 1]

        // Find the edge matching the current segment
        val edge = edges.find { it.start == start && it.end == end }
        if (edge != null) {
            totalCost += edge.cost
        } else {
            // Handle the case where an edge is missing
            throw IllegalArgumentException("No edge found between $start and $end")
        }
    }

    return totalCost
}
