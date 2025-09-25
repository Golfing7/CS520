package com.golfing8.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Contains some divide and conquer algorithm implementations.
 */
public class DivideAndConquer {
    /**
     * Performs a partition on the given array
     *
     * @param array the array
     * @param p the low (pivot) index
     * @param q the high index
     * @return the new index of the pivot
     */
    private static int partition(double[] array, int p, int q) {
        double[] left = new double[array.length];
        int li = 0;
        double[] right = new double[array.length];
        int ri = 0;
        for (int i = p; i < q; i++) {
            if (array[i] < array[p]) {
                left[li++] = array[i];
            } else if (array[i] > array[p]) {
                right[ri++] = array[i];
            }
        }
        double pivot = array[p];
        System.arraycopy(left, 0, array, p, li);
        array[p + li] = pivot;
        System.arraycopy(right, 0, array, p + li + 1, ri);
        return p + li;
    }

    private static void quickSortInternal(double[] array, int p, int q) {
        if (p >= q - 1)
            return;

        int j = partition(array, p, q);
        quickSortInternal(array, p, j);
        quickSortInternal(array, j + 1, q);
    }

    private static void quickSortInternalRandomized(double[] array, int p, int q) {
        if (p >= q)
            return;

        int swapIndex = ThreadLocalRandom.current().nextInt(p, q);

        // Swap the random index to the pivot index expected
        double temp = array[p];
        array[p] = array[swapIndex];
        array[swapIndex] = temp;

        int j = partition(array, p, q);
        quickSortInternalRandomized(array, p, j);
        quickSortInternalRandomized(array, j + 1, q);
    }

    /**
     * Performs a quick sort on the given array
     * This algorithm has the following complexity:
     * <ul>
     *     <li>O(nlogn) Time</li>
     *     <li>O(n) Space</li>
     * </ul>
     *
     * @param array the array
     */
    public static void quickSortRandomized(double[] array) {
        quickSortInternalRandomized(array, 0, array.length);
    }

    /**
     * Performs a quick sort on the given array
     * This algorithm has the following complexity:
     * <ul>
     *     <li>O(n^2) Time</li>
     *     <li>O(n) Space</li>
     * </ul>
     *
     * @param array the array
     */
    public static void quickSort(double[] array) {
        quickSortInternal(array, 0, array.length);
    }

    private static int binarySearch(double[] array, int low, int high, double key) {
        if (low > high)
            return -1;

        int mid = (low + high) / 2;
        if (array[mid] == key)
            return mid;

        if (array[mid] > key)
            return binarySearch(array, low, mid - 1, key);

        return binarySearch(array, mid + 1, high, key);
    }

    /**
     * A basic divide and conquer binary search
     * <p></p>
     * Complexity:
     * <ul>
     *     <li>O(logn) Time</li>
     *     <li>O(1) Space</li>
     * </ul>
     *
     * @param array the array
     * @param key the key
     * @return the index of the found element, or -1
     */
    public static int binarySearch(double[] array, double key) {
        return binarySearch(array, 0, array.length, key);
    }
}
