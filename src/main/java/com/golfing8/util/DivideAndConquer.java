package com.golfing8.util;

import com.golfing8.struct.Pair;
import com.golfing8.struct.Point2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Contains some divide and conquer algorithm implementations.
 */
public class DivideAndConquer {
    private static int[] maxSubArrayCrossing(int[] array, int low, int mid, int high) {
        int maxLeft = 0;
        int leftSum = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = mid; i >= low; i--) {
            sum = sum + array[i];
            if (sum > leftSum) {
                leftSum = sum;
                maxLeft = i;
            }
        }

        int maxRight = 0;
        int rightSum = Integer.MIN_VALUE;
        sum = 0;
        for (int i = mid + 1; i < high; i++) {
            sum = sum + array[i];
            if (sum > rightSum) {
                rightSum = sum;
                maxRight = i;
            }
        }
        return new int[] {maxLeft, maxRight, leftSum + rightSum};
    }

    /**
     * Calculates the maximum sub array for the given array
     *
     * @param array the array
     * @param low the low bounds
     * @param high the high bounds, exclusive
     * @return the maximum sub array
     */
    public static int[] maxSubArray(int[] array, int low, int high) {
        if (low == high) {
            return new int[] {low, high, array[low]};
        }

        int mid = (low + high) / 2;
        int[] left = maxSubArray(array, low, mid);
        int[] right = maxSubArray(array, mid + 1, high);
        int[] cross = maxSubArrayCrossing(array, low, mid, high);
        if (left[2] > right[2] && left[2] > cross[2])
            return left;
        else if (right[2] > left[2] && right[2] > cross[2])
            return right;

        return cross;
    }

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

    private static Pair<Point2D, Point2D> closestOfThree(Point2D p1, Point2D p2, Point2D p3) {
        double d1 = p1.distance(p2);
        double d2 = p1.distance(p3);
        double d3 = p2.distance(p3);
        if (d1 <= d2 && d1 <= d3)
            return new Pair<>(p1, p2);
        else if (d2 <= d1 && d2 <= d3)
            return new Pair<>(p1, p3);
        else
            return new Pair<>(p2, p3);
    }

    private static Pair<Point2D, Point2D> closestPair(List<Point2D> x, List<Point2D> y) {
        int n = x.size();
        if (n == 2)
            return new Pair<>(x.getFirst(), x.getLast());
        if (n == 3) {
            // Find the closest distance among the three.
            return closestOfThree(x.get(0), x.get(1), x.get(2));
        }

        int mid = n / 2;
        var midPoint = x.get(mid);
        var left = closestPair(x.subList(0, mid), y);
        var right = closestPair(x.subList(mid, x.size()), y);
        var closest = left.left().distance(left.right()) < right.left().distance(right.right()) ? left : right;

        double distance = closest.left().distance(closest.right());
        for (int i = 0; i < y.size(); i++) {
            var point = y.get(i);
            if (Math.abs(point.x() - midPoint.x()) > distance)
                continue;

            for (int j = i + 1; j < y.size() && y.get(j).y() - point.y() < distance; j++) {
                var other = y.get(i + j);
                double dist = point.distance(other);
                if (dist < distance) {
                    distance = dist;
                    closest = new Pair<>(point, other);
                }
            }
        }
        return closest;
    }

    /**
     * Finds the closest pair of points in the given collection of points
     *
     * @param points the points
     * @return the closest pair of points
     */
    public static Pair<Point2D, Point2D> closestPair(Collection<Point2D> points) {
        if (points.size() <= 1)
            throw new IllegalArgumentException("Cannot get closest pair among a single point");

        List<Point2D> xSorted = new ArrayList<>(points);
        xSorted.sort(Point2D.X);
        List<Point2D> ySorted = new ArrayList<>(points);
        ySorted.sort(Point2D.Y);

        return closestPair(xSorted, ySorted);
    }
}
