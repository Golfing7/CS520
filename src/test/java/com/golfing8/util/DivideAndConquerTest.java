package com.golfing8.util;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
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
}
