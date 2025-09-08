package com.golfing8.concurrent;

import java.util.concurrent.ForkJoinPool;

public class ThreadPools {
    /** The thread executor for parallel matrix computations */
    public static final ForkJoinPool MATRIX_EXECUTOR = new ForkJoinPool(16);
}
