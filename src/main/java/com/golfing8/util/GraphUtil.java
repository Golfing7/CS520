package com.golfing8.util;

import com.google.common.graph.*;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public class GraphUtil {
    public record CostElement<T>(T element, double cost) implements Comparable<CostElement<T>> {
        @Override
        public int compareTo(CostElement<T> o) {
            return Double.compare(cost, o.cost);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            CostElement<?> that = (CostElement<?>) o;
            return Objects.equals(element, that.element);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(element);
        }
    }

    public record CostEdge<T>(CostElement<T> u, T v, double cost) implements Comparable<CostEdge<T>> {
        @Override
        public int compareTo(CostEdge<T> o) {
            return Double.compare(cost, o.cost);
        }
    }

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
                // Ignore ordered edges whose target points are v.
                if (edge.isOrdered() && edge.target().equals(currentEdge.v()))
                    continue;

                edgeCosts.add(new CostEdge<>(newElement, edge.adjacentNode(currentEdge.v()), newElement.cost + mutableOriginal.edgeValue(edge).orElse(0.0)));
            }
        }
        return pathTree;
    }

    public record WeightedEdge<T>(EndpointPair<T> pair, T parent, double weight) implements Comparable<WeightedEdge<T>> {
        @Override
        public int compareTo(WeightedEdge<T> o) {
            return Double.compare(weight, o.weight);
        }
    }

    /**
     * Computes the MST of the given graph using Prim's algorithm.
     *
     * @param graph the graph
     * @return the MST of the graph
     * @param <T> the type
     */
    public static <T> Graph<T> computeMSTPrim(ValueGraph<T, Double> graph) {
        if (graph.nodes().isEmpty())
            return GraphBuilder.directed().build();

        // Stores the parents of nodes
        T source = graph.nodes().stream().findFirst().orElseThrow();
        MutableValueGraph<T, Double> mst = ValueGraphBuilder.directed().build();
        mst.addNode(source);

        PriorityQueue<WeightedEdge<T>> edgeQueue = new PriorityQueue<>();

        // Add initial edges to the graph.
        for (var edge : graph.incidentEdges(source)) {
            edgeQueue.add(new WeightedEdge<>(edge, source, graph.edgeValueOrDefault(edge, 0.0)));
        }

        // Handle edges in a priority fashion.
        while (!edgeQueue.isEmpty()) {
            var edge = edgeQueue.poll();

            // If mst already contains the node, we may need to bump the old parent out to remove the cycle
            T targetNode = edge.pair.adjacentNode(edge.parent);
            if (mst.nodes().contains(targetNode)) {
                // Kick out the old predecessor if necessary
                var parentEdge = mst.incidentEdges(targetNode).stream().findFirst().orElseThrow();
                if (mst.edgeValueOrDefault(parentEdge, 0.0) > edge.weight) {
                    mst.removeEdge(parentEdge);
                    mst.putEdgeValue(edge.pair.adjacentNode(targetNode), targetNode, edge.weight);
                } else {
                    // Otherwise, this node was already at a lower value. Skip to next iteration!
                    continue;
                }
            } else {
                // Simply insert the edge
                mst.putEdgeValue(edge.pair.adjacentNode(targetNode), targetNode, edge.weight);
            }

            // Now, go through incident edges and insert them.
            for (var adjacentEdge : graph.incidentEdges(targetNode)) {
                // If the node is already in the tree, ignore it.
                if (mst.nodes().contains(adjacentEdge.adjacentNode(targetNode)))
                    continue;

                edgeQueue.add(new WeightedEdge<>(adjacentEdge, targetNode, graph.edgeValueOrDefault(adjacentEdge, 0.0)));
            }
        }
        return mst.asGraph();
    }
}
