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

        blackhole.consume(matrixMultiplyNaive(matrix1, matrix2));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 2)
    @Threads(8)
    public void strassen(Blackhole blackhole) {
        SquareMatrix matrix1 = SquareMatrix.random(matrixSize);
        SquareMatrix matrix2 = SquareMatrix.random(matrixSize);

        blackhole.consume(matrixMultiplyStrassen(matrix1, matrix2));
    }

    private static SquareMatrix matrixMultiplyNaive(SquareMatrix matrix1, SquareMatrix matrix2) {
        if (matrix1.getSize() == 1)
            return SquareMatrix.fromElements(matrix1.getElement(1, 1) * matrix2.getElement(1, 1));

        int halfSize = matrix1.getSize() / 2;
        SquareMatrix a11 = matrix1.subMatrix(1, 1, halfSize);
        SquareMatrix a12 = matrix1.subMatrix(1, halfSize + 1, halfSize);
        SquareMatrix a21 = matrix1.subMatrix(halfSize + 1, 1, halfSize);
        SquareMatrix a22 = matrix1.subMatrix(halfSize + 1, halfSize + 1, halfSize);

        SquareMatrix b11 = matrix2.subMatrix(1, 1, halfSize);
        SquareMatrix b12 = matrix2.subMatrix(1, halfSize + 1, halfSize);
        SquareMatrix b21 = matrix2.subMatrix(halfSize + 1, 1, halfSize);
        SquareMatrix b22 = matrix2.subMatrix(halfSize + 1, halfSize + 1, halfSize);

        SquareMatrix c11 = matrixMultiplyNaive(a11, b11).plus(matrixMultiplyNaive(a12, b21));
        SquareMatrix c12 = matrixMultiplyNaive(a11, b12).plus(matrixMultiplyNaive(a12, b22));
        SquareMatrix c21 = matrixMultiplyNaive(a21, b11).plus(matrixMultiplyNaive(a22, b21));
        SquareMatrix c22 = matrixMultiplyNaive(a21, b12).plus(matrixMultiplyNaive(a22, b22));

        return SquareMatrix.fromParts(c11, c12, c21, c22);
    }

    private static SquareMatrix matrixMultiplyStrassen(SquareMatrix matrix1, SquareMatrix matrix2) {
        if (matrix1.getSize() == 1)
            return SquareMatrix.fromElements(matrix1.getElement(1, 1) * matrix2.getElement(1, 1));

        int halfSize = matrix1.getSize() / 2;
        SquareMatrix a11 = matrix1.subMatrix(1, 1, halfSize);
        SquareMatrix a12 = matrix1.subMatrix(1, halfSize + 1, halfSize);
        SquareMatrix a21 = matrix1.subMatrix(halfSize + 1, 1, halfSize);
        SquareMatrix a22 = matrix1.subMatrix(halfSize + 1, halfSize + 1, halfSize);

        SquareMatrix b11 = matrix2.subMatrix(1, 1, halfSize);
        SquareMatrix b12 = matrix2.subMatrix(1, halfSize + 1, halfSize);
        SquareMatrix b21 = matrix2.subMatrix(halfSize + 1, 1, halfSize);
        SquareMatrix b22 = matrix2.subMatrix(halfSize + 1, halfSize + 1, halfSize);

        SquareMatrix s1 = b12.minus(b22);
        SquareMatrix s2 = a11.plus(a12);
        SquareMatrix s3 = a21.plus(a22);
        SquareMatrix s4 = b21.minus(b11);
        SquareMatrix s5 = a11.plus(a22);
        SquareMatrix s6 = b11.plus(b22);
        SquareMatrix s7 = a12.minus(a22);
        SquareMatrix s8 = b21.plus(b22);
        SquareMatrix s9 = a11.minus(a21);
        SquareMatrix s10 = b11.plus(b12);

        SquareMatrix p1 = matrixMultiplyStrassen(a11, s1);
        SquareMatrix p2 = matrixMultiplyStrassen(s2, b22);
        SquareMatrix p3 = matrixMultiplyStrassen(s3, b11);
        SquareMatrix p4 = matrixMultiplyStrassen(a22, s4);
        SquareMatrix p5 = matrixMultiplyStrassen(s5, s6);
        SquareMatrix p6 = matrixMultiplyStrassen(s7, s8);
        SquareMatrix p7 = matrixMultiplyStrassen(s9, s10);

        SquareMatrix c11 = p5.plus(p4).minus(p2).plus(p6);
        SquareMatrix c12 = p1.plus(p2);
        SquareMatrix c21 = p3.plus(p4);
        SquareMatrix c22 = p5.plus(p1).minus(p3).minus(p7);

        return SquareMatrix.fromParts(c11, c12, c21, c22);
    }
}
