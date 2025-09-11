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

    }

    @Test
    public void testPrim() {
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
}
