package com.golfing8.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class DynamicProgrammingTest {
    @Test
    public void subsetSumTarget() {
        System.out.println(Arrays.toString(DynamicProgramming.subsetSumTarget(new int[] {27, 3, 15, 14}, 29)));
    }
}
