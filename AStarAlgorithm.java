package com.metromate;

import com.metromate.models.Edge;

import java.util.*;

class Node {
    int stationId;
    int gCost; // 현재까지의 실제 비용
    int hCost; // 휴리스틱 비용
    Node parent;

    public Node(int stationId, int gCost, int hCost, Node parent) {
        this.stationId = stationId;
        this.gCost = gCost;
        this.hCost = hCost;
        this.parent = parent;
    }

    public int getFCost() {
        return gCost + hCost;
    }
}

public class AStarAlgorithm {
    public static List<Integer> findShortestPath(List<Edge> edges, int startId, int goalId, boolean optimizeForTime) {
        Map<Integer, List<Edge>> graph = buildGraph(edges);
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getFCost));
        Set<Integer> closedSet = new HashSet<>();

        openSet.add(new Node(startId, 0, heuristic(startId, goalId), null));

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.stationId == goalId) {
                return reconstructPath(current);
            }

            closedSet.add(current.stationId);

            for (Edge edge : graph.getOrDefault(current.stationId, new ArrayList<>())) {
                if (closedSet.contains(edge.end)) continue;

                int tentativeG = current.gCost + (optimizeForTime ? edge.time : edge.cost);
                Node neighbor = new Node(edge.end, tentativeG, heuristic(edge.end, goalId), current);

                openSet.add(neighbor);
            }
        }

        return new ArrayList<>();
    }

    private static Map<Integer, List<Edge>> buildGraph(List<Edge> edges) {
        Map<Integer, List<Edge>> graph = new HashMap<>();
        for (Edge edge : edges) {
            graph.computeIfAbsent(edge.start, k -> new ArrayList<>()).add(edge);
        }
        return graph;
    }

    private static int heuristic(int current, int goal) {
        return Math.abs(current - goal); // 간단한 휴리스틱
    }

    private static List<Integer> reconstructPath(Node node) {
        List<Integer> path = new ArrayList<>();
        while (node != null) {
            path.add(0, node.stationId);
            node = node.parent;
        }
        return path;
    }
}
