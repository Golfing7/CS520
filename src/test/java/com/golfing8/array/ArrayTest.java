package com.golfing8.array;

import com.golfing8.util.ArrayUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ArrayTest {
    @Test
    public void testMaxSubArray() {
        int[] a = {
                13,
                -3,
                -25,
                20,
                -3,
                -16,
                -23,
                18,
                20,
                -7,
                12,
                -5,
                -22,
                15,
                -4,
                7
        };

        Assertions.assertArrayEquals(new int[] {7, 10}, ArrayUtil.maxSubArray(a));
    }

    @Test
    public void testMaxSubArrayNegative() {
        int[] b = {
                -14,
                -102,
                -14,
                -11,
                -15,
                -1,
                -12,
                -19,
        };

        System.out.println(Arrays.toString(ArrayUtil.maxSubArray(b)));
    }
}
