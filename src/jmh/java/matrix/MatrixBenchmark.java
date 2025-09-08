package matrix;

import com.golfing8.struct.SquareMatrix;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

/*
 * JMS RESULTS:
 * Benchmark                 (matrixSize)  Mode  Cnt   Score    Error  Units
 * MatrixBenchmark.control            256  avgt    5   0.020 ±  0.001   s/op
 * MatrixBenchmark.control            512  avgt    5   0.495 ±  0.009   s/op
 * MatrixBenchmark.control           1024  avgt    5   4.070 ±  0.035   s/op
 * MatrixBenchmark.naive              256  avgt    5   0.712 ±  0.016   s/op
 * MatrixBenchmark.naive              512  avgt    5   5.798 ±  0.500   s/op
 * MatrixBenchmark.naive             1024  avgt    5  44.936 ±  1.809   s/op
 * MatrixBenchmark.strassen           256  avgt    5   0.492 ±  0.029   s/op
 * MatrixBenchmark.strassen           512  avgt    5   3.547 ±  0.137   s/op
 * MatrixBenchmark.strassen          1024  avgt    5  25.237 ±  1.131   s/op
 */
@Fork(value = 1)
@State(Scope.Thread)
public class MatrixBenchmark {

    @Param({"256", "512", "1024"})
    public int matrixSize;

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2)
    @Threads(8)
    public void control(Blackhole blackhole) {
        SquareMatrix matrix1 = SquareMatrix.random(matrixSize);
        SquareMatrix matrix2 = SquareMatrix.random(matrixSize);

        blackhole.consume(matrix1.matrixProduct(matrix2));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2)
    @Threads(8)
    public void naive(Blackhole blackhole) {
        SquareMatrix matrix1 = SquareMatrix.random(matrixSize);
        SquareMatrix matrix2 = SquareMatrix.random(matrixSize);

        blackhole.consume(SquareMatrix.matrixMultiplyNaive(matrix1, matrix2));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2)
    @Threads(8)
    public void strassen(Blackhole blackhole) {
        SquareMatrix matrix1 = SquareMatrix.random(matrixSize);
        SquareMatrix matrix2 = SquareMatrix.random(matrixSize);

        blackhole.consume(SquareMatrix.matrixMultiplyStrassen(matrix1, matrix2));
    }
}
