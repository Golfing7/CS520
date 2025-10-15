package com.golfing8.util;

import com.golfing8.struct.Point2D;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test suite for the {@link DivideAndConquer} class.
 */
public class DivideAndConquerTest {
    private static double[] randomArray(int n) {
        double[] array = new double[n];
        for (int i = 0; i < array.length; i++) {
            array[i] = ThreadLocalRandom.current().nextDouble();
        }
        return array;
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void quickSort(boolean randomized) {
        double[] userSorted = randomArray(ThreadLocalRandom.current().nextInt(1, 1000));
        double[] systemSorted = Arrays.copyOf(userSorted, userSorted.length);

        Arrays.sort(systemSorted);
        if (randomized) {
            DivideAndConquer.quickSortRandomized(userSorted);
        } else {
            DivideAndConquer.quickSort(userSorted);
        }

        assertArrayEquals(systemSorted, userSorted);
    }

    @TestFactory
    public Stream<DynamicTest> binarySearch() {
        double[] array = randomArray(ThreadLocalRandom.current().nextInt(1, 1000));
        Arrays.sort(array);
        return Stream.of(
                DynamicTest.dynamicTest("Found", () -> {
                    int i = ThreadLocalRandom.current().nextInt(array.length);
                    double key = array[i];
                    assertEquals(i, DivideAndConquer.binarySearch(array, key));
                }),
                DynamicTest.dynamicTest("Missing", () -> {
                    // Produces a negative number not in the array
                    double key = ThreadLocalRandom.current().nextDouble() - 5.0;
                    assertEquals(-1, DivideAndConquer.binarySearch(array, key));
                })
        );
    }

    @Test
    public void closestPair() {
        List<Point2D> points = List.of(
                new Point2D(2, 3),
                new Point2D(12, 30),
                new Point2D(40, 50),
                new Point2D(5, 1),
                new Point2D(12, 10),
                new Point2D(3, 4)
        );

        var closestPair = DivideAndConquer.closestPair(points);
        System.out.println(closestPair);
        System.out.println(closestPair.left().distance(closestPair.right()));
    }

    @Test
    public void closestPairMerging() {
        List<Point2D> points = List.of(
                new Point2D(2, 3),
                new Point2D(3, 4),
                new Point2D(5, 1),
                new Point2D(5.5, 1),
                new Point2D(12, 30),
                new Point2D(40, 50)
        );

        var closestPair = DivideAndConquer.closestPair(points);
        System.out.println(closestPair);
        System.out.println(closestPair.left().distance(closestPair.right()));
    }
}
