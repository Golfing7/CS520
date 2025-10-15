package com.golfing8.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicProgramming {
    public static int[] subsetSumTarget(int[] a, int t) {
        int[][] sums = new int[a.length][t];
        int[][] elements = new int[a.length][t];

        for (int i = 0; i < a.length; i++) {
            int element = a[i];
            for (int j = 0; j < t; j++) {
                int w = j + 1;
                if (i == 0) {
                    if (element <= w) {
                        sums[i][j] = element;
                        elements[i][j] = 1;
                    }
                } else {
                    int included = 0;
                    if (element <= w && sums[i - 1][w - element] + element <= t) {
                        included = sums[i - 1][w - element] + element;
                    }
                    int excluded = sums[i - 1][j];
                    if (included > excluded) {
                        sums[i][j] = included;
                        elements[i][j] = 1;
                    } else {
                        sums[i][j] = excluded;
                    }
                }
            }
        }

        List<Integer> ints = new ArrayList<>();
        int element = sums[a.length - 1][t - 1];
        int index = a.length - 1;
        while (element > 0 && index >= 0) {
            if (elements[index][element - 1] > 0) {
                ints.add(a[index]);
                element -= a[index];
            }
            index--;
        }
        return ints.stream().mapToInt(i -> i).toArray();
    }
}
