# CS520 Algorithm Implementations
This repository contains implementations for a few algorithms discussed in CS 520.

## Strassen Matrix Multiplication
Strassen matrix multiplication (with optional parallelization) has been implemented in [SquareMatrix](src/main/java/com/golfing8/struct/SquareMatrix.java).
See [SquareMatrixTest](src/test/java/com/golfing8/matrix/SquareMatrixTest.java) for tests.
See [MatrixBenchmark](src/jmh/java/matrix/MatrixBenchmark.java) for benchmark results.

## Tree building dijkstra's
A TRS implementation of Dijkstra's can be found in [GraphUtil](src/main/java/com/golfing8/util/GraphUtil.java).
See [GraphTest](src/test/java/com/golfing8/graph/GraphTest.java) for tests.

## Prim's algorithm
A version of Prim's algorithm can be found in [GraphUtil](src/main/java/com/golfing8/util/GraphUtil.java).
See [GraphTest](src/test/java/com/golfing8/graph/GraphTest.java) for tests.

To run and compile the code, use
```shell
./gradlew build
```
