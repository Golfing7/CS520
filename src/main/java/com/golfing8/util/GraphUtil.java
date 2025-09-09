package com.golfing8.util;

import com.google.common.graph.*;

import java.util.*;

public class GraphUtil {
    public record CostElement<T>(T element, double cost) implements Comparable<CostElement<T>> {
        @Override
        public int compareTo(CostElement<T> o) {
            return Double.compare(cost, o.cost);
        }
    }

    public record CostEdge<T>(CostElement<T> u, T v, double cost) implements Comparable<CostEdge<T>> {
        @Override
        public int compareTo(CostEdge<T> o) {
            return Double.compare(cost, o.cost);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static <T> Graph<CostElement<T>> shortestPath(T root, ValueGraph<T, Double> graph) {
        MutableValueGraph<T, Double> mutableOriginal = Graphs.copyOf(graph);
        MutableGraph<CostElement<T>> pathTree = GraphBuilder.directed().build();
        PriorityQueue<CostEdge<T>> edgeCosts = new PriorityQueue<>();

        // Initial setup
        var rootElement = new CostElement<>(root, 0);
        for (var edge : mutableOriginal.incidentEdges(root)) {
            edgeCosts.add(new CostEdge<>(rootElement, edge.adjacentNode(root), graph.edgeValue(edge).orElse(0.0)));
        }

        // Handle each edge
        while (!edgeCosts.isEmpty()) {
            var currentEdge = edgeCosts.poll();
            // Has the node already been handled?
            var newElement = new CostElement<>(currentEdge.v(), currentEdge.cost);
            if (pathTree.nodes().contains(newElement))
                continue;

            pathTree.putEdge(currentEdge.u(), newElement);

            // Remove the handled edge.
            mutableOriginal.removeEdge(currentEdge.u().element(), currentEdge.v());

            // Add new edges
            for (var edge : mutableOriginal.incidentEdges(currentEdge.v())) {
                edgeCosts.add(new CostEdge<>(newElement, edge.adjacentNode(currentEdge.v()), newElement.cost + mutableOriginal.edgeValue(edge).orElse(0.0)));
            }
        }
        return pathTree;
    }
}
