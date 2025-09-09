package com.golfing8.matrix;

import com.golfing8.util.GraphUtil;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.junit.jupiter.api.Test;

@SuppressWarnings("UnstableApiUsage")
public class GraphTest {
    @Test
    public void testGraph() {
        MutableValueGraph<String, Double> valueGraph = ValueGraphBuilder.undirected().build();
        valueGraph.putEdgeValue("a", "b", 1.0);
        valueGraph.putEdgeValue("a", "c", 1.0);
        valueGraph.putEdgeValue("b", "d", 2.0);
        valueGraph.putEdgeValue("d", "e", 3.0);

        System.out.println(valueGraph);
        var shortestPath = GraphUtil.shortestPath("a", valueGraph);
        System.out.println(shortestPath);
    }
}
