package com.golfing8.matrix;

import com.golfing8.util.GraphUtil;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.junit.jupiter.api.Assertions;
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

        var shortestPath = GraphUtil.shortestPath("a", valueGraph);
        System.out.println(shortestPath);
    }

    @Test
    public void testShortestPath2() {
        MutableValueGraph<String, Double> valueGraph = ValueGraphBuilder.directed().build();
        valueGraph.putEdgeValue("v1", "v3", 4.0);

        valueGraph.putEdgeValue("v2", "v3", 5.0D);
        valueGraph.putEdgeValue("v2", "v1", 1.0D);

        valueGraph.putEdgeValue("v3", "v5", 6.0);

        valueGraph.putEdgeValue("v5", "v2", 3.0);
        valueGraph.putEdgeValue("v5", "v4", 4.0);

        valueGraph.putEdgeValue("v4", "v2", 7.0);

        valueGraph.putEdgeValue("v6", "v3", 2.0);
        valueGraph.putEdgeValue("v6", "v4", 10.0);

        var shortestPath = GraphUtil.shortestPath("v6", valueGraph);
        System.out.println(shortestPath.edges());
    }

    @Test
    public void testPrim1() {
        MutableValueGraph<String, Double> valueGraph = ValueGraphBuilder.undirected().build();
        valueGraph.putEdgeValue("A", "B", 4.0);
        valueGraph.putEdgeValue("A", "D", 3.0);

        valueGraph.putEdgeValue("B", "C", 3.0);
        valueGraph.putEdgeValue("B", "D", 5.0);

        valueGraph.putEdgeValue("C", "E", 4.0);
        valueGraph.putEdgeValue("C", "H", 2.0);

        valueGraph.putEdgeValue("D", "E", 7.0);
        valueGraph.putEdgeValue("D", "F", 4.0);

        valueGraph.putEdgeValue("E", "F", 5.0);
        valueGraph.putEdgeValue("E", "G", 3.0);

        valueGraph.putEdgeValue("F", "G", 7.0);

        valueGraph.putEdgeValue("G", "H", 5.0);

        MutableGraph<String> expectedMST = GraphBuilder.directed().build();
        expectedMST.putEdge("A", "B");
        expectedMST.putEdge("B", "C");
        expectedMST.putEdge("C", "H");
        expectedMST.putEdge("C", "E");
        expectedMST.putEdge("E", "G");

        expectedMST.putEdge("A", "D");
        expectedMST.putEdge("D", "F");

        Graph<String> computedMST = GraphUtil.computeMSTPrim(valueGraph);
        Assertions.assertEquals(expectedMST, computedMST);
    }

    @Test
    public void testPrim2() {
        MutableValueGraph<String, Double> valueGraph = ValueGraphBuilder.undirected().build();
        valueGraph.putEdgeValue("A", "B", 1.0);
        valueGraph.putEdgeValue("B", "C", 1.0);
        valueGraph.putEdgeValue("C", "D", 1.0);

        valueGraph.putEdgeValue("A", "D", 2.0);

        MutableGraph<String> expectedMST = GraphBuilder.directed().build();
        expectedMST.putEdge("A", "B");
        expectedMST.putEdge("B", "C");
        expectedMST.putEdge("C", "D");

        Graph<String> computedMST = GraphUtil.computeMSTPrim(valueGraph);
        Assertions.assertEquals(expectedMST, computedMST);
    }
}
