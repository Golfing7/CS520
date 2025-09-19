package com.golfing8.util;

/**
 * Utilities for arrays.
 */
public class ArrayUtil {
    /**
     * Computes the maximum sub array value
     *
     * @param a the array
     * @return the value of the max sub array
     */
    public static int[] maxSubArray(int[] a) {
        int max = Integer.MIN_VALUE;
        int mi = 0;
        int mj = 0;
        int si = 0;
        int sum = 0;
        boolean awaitingPositive = true;
        for (int i = 0; i < a.length; i++) {
            if ((a[i] > 0 || a[i] > max) && awaitingPositive) {
                awaitingPositive = false;
                sum = a[i];
                si = i;
            } else {
                sum += a[i];
            }

            if (sum > max) {
                mj = i;
                mi = si;
                max = sum;
                continue;
            }

            // If our sum has gone negative, we need to reset the lower bound.
            if (sum < 0 && !awaitingPositive) {
                awaitingPositive = true;
            }
        }
        return new int[] {mi, mj};
    }
}
