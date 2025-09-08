package com.golfing8.struct;

import com.golfing8.concurrent.ThreadPools;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a size n square matrix.
 */
public class SquareMatrix {
    /** The matrix size to parallelize for in the strassen multiplication */
    private static final int STRASSEN_PARALLELIZE_THRESHOLD = 999999999;

    /** Data stored in a flat format. Columns -> rows. */
    private final double[] data;
    /** The size of this matrix */
    private final int size;
    /** The amount of elements in this matrix. Equal to {@code size * size} */
    private final int elementCount;

    private SquareMatrix(int size, double[] data) {
        this.data = data;
        this.size = size;
        this.elementCount = size * size;
    }

    public int getSize() {
        return size;
    }

    /**
     * Creates a zero matrix of the given size
     *
     * @param size the size
     */
    public SquareMatrix(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("Size must be positive. Was " + size);

        this.elementCount = size * size;
        this.data = new double[elementCount];
        this.size = size;
    }

    /**
     * Creates a copy of the given square matrix
     *
     * @param matrix the matrix
     */
    public SquareMatrix(SquareMatrix matrix) {
        this.data = Arrays.copyOf(matrix.data, matrix.data.length);
        this.size = matrix.size;
        this.elementCount = matrix.elementCount;
    }

    /**
     * Calculates the sum of this matrix and the other
     *
     * @param other the other
     * @return the matrix
     */
    public SquareMatrix plus(SquareMatrix other) {
        if (this.size != other.size)
            throw new IllegalArgumentException("Size of matrices are not equal. Expecting " + this.size + " was " + other.size);

        double[] newData = new double[this.data.length];
        for (int i = 0; i < newData.length; i++) {
            newData[i] = this.data[i] + other.data[i];
        }
        return new SquareMatrix(this.size, newData);
    }

    /**
     * Calculates the sum of this matrix and the other
     *
     * @param other the other
     * @return the matrix
     */
    public SquareMatrix plusIP(SquareMatrix other) {
        if (this.size != other.size)
            throw new IllegalArgumentException("Size of matrices are not equal. Expecting " + this.size + " was " + other.size);

        for (int i = 0; i < this.elementCount; i++) {
            this.data[i] += other.data[i];
        }
        return this;
    }

    /**
     * Calculates the difference of this matrix and the other
     *
     * @param other the other
     * @return the matrix
     */
    public SquareMatrix minus(SquareMatrix other) {
        if (this.size != other.size)
            throw new IllegalArgumentException("Size of matrices are not equal. Expecting " + this.size + " was " + other.size);

        double[] newData = new double[this.data.length];
        for (int i = 0; i < newData.length; i++) {
            newData[i] = this.data[i] - other.data[i];
        }
        return new SquareMatrix(this.size, newData);
    }

    /**
     * Calculates the difference of this matrix and the other
     *
     * @param other the other
     * @return the matrix
     */
    public SquareMatrix minusIP(SquareMatrix other) {
        if (this.size != other.size)
            throw new IllegalArgumentException("Size of matrices are not equal. Expecting " + this.size + " was " + other.size);

        for (int i = 0; i < this.elementCount; i++) {
            this.data[i] -= other.data[i];
        }
        return this;
    }

    /**
     * Calculates the matrix product for this matrix and the given one
     *
     * @param other the other matrix
     * @return the resulting matrix product
     */
    public SquareMatrix matrixProduct(SquareMatrix other) {
        if (this.size != other.size)
            throw new IllegalArgumentException("Size of matrices are not equal. Expecting " + this.size + " was " + other.size);

        double[] newData = new double[this.data.length];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int idx = i + j * size;

                double sum = 0.0D;
                for (int k = 0; k < size; k++) {
                    double a = this.data[i + k * size];
                    double b = other.data[j * size + k];
                    sum += a * b;
                }
                newData[idx] = sum;
            }
        }
        return new SquareMatrix(this.size, newData);
    }

    /**
     * Gets the element at the ith row in the jth column
     *
     * @param i the row
     * @param j the col
     * @return the element
     */
    public double getElement(int i, int j) {
        if (i < 1 || i > size || j < 1 || j > size)
            throw new IllegalArgumentException("Elements are out of bounds for matrix with size " + size + ". i=" + i + ", j=" + j);

        return data[(i - 1) + (j - 1) * size];
    }

    /**
     * Creates a submatrix at the given coordinate with the given size
     *
     * @param i the row
     * @param j the col
     * @param size the size
     * @return the sub matrix
     */
    public SquareMatrix subMatrix(int i, int j, int size) {
        if (i + size - 1 > this.size || j + size - 1 > this.size)
            throw new IllegalArgumentException("Size of submatrix would exceed matrix bounds. this.size=" + this.size + " i=" + i + " j=" + j + " size=" + size);

        double[] data = new double[size * size];
        for (int k = 0; k < size; k++) {
            for (int l = 0; l < size; l++) {
                data[l + k * size] = this.data[(i - 1) + (j - 1) * this.size + l + k * this.size];
            }
        }
        return new SquareMatrix(size, data);
    }

    /**
     * Sets the values in this matrix from the given submatrix
     *
     * @param i the row
     * @param j the col
     * @param matrix the matrix
     */
    public void setSubMatrix(int i, int j, SquareMatrix matrix) {
        if (i + matrix.size - 1 > this.size || j + matrix.size - 1 > this.size)
            throw new IllegalArgumentException("Size of submatrix would exceed matrix bounds");

        for (int k = 0; k < matrix.size; k++) {
            for (int l = 0; l < matrix.size; l++) {
                this.data[(i - 1) + (j - 1) * this.size + l + k * this.size] = matrix.data[l + k * matrix.size];
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                builder.append(String.format("%.2f", data[i + j * size]));
                if (j + 1 < size)
                    builder.append(", ");
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        SquareMatrix that = (SquareMatrix) object;
        if (size != that.size)
            return false;
        for (int i = 0; i < elementCount; i++) {
            double diff = this.data[i] - that.data[i];
            if (Math.abs(diff) > 1e-6)
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(data), size);
    }

    /**
     * Creates a square matrix with pseudo random values from (-1)-1 with the given size
     *
     * @param n the size
     * @return the square matrix
     */
    public static SquareMatrix random(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Matrix size must be at least 1. Was " + n);

        Random random = new Random();
        double[] data = new double[n * n];
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextDouble(-1, 1);
        }
        return new SquareMatrix(n, data);
    }

    /**
     * Constructs a new square matrix from the given parts of equal size.
     * The resulting matrix will be twice the size of the parts.
     *
     * @param c11 the first part, upper left
     * @param c12 the second part, upper right
     * @param c21 the third part, lower left
     * @param c22 the fourth part, lower right
     * @return the resulting matrix
     */
    public static SquareMatrix fromParts(SquareMatrix c11, SquareMatrix c12, SquareMatrix c21, SquareMatrix c22) {
        int newSize = c11.getSize() * 2;
        double[] newData = new double[newSize * newSize];

        SquareMatrix result = new SquareMatrix(newSize);
        result.setSubMatrix(1, 1, c11);
        result.setSubMatrix(1, c11.getSize() + 1, c12);
        result.setSubMatrix(c11.getSize() + 1, 1, c21);
        result.setSubMatrix(c11.getSize() + 1, c11.getSize() + 1, c22);
        return result;
    }

    /**
     * Creates a matrix with the given elements
     *
     * @param elements the elements
     */
    public static SquareMatrix fromElements(double... elements) {
        double sized = Math.sqrt(elements.length);
        if (sized - (int) sized != 0)
            throw new IllegalArgumentException("Element count is not perfect square. Was " + elements.length);

        int size = (int) sized;
        int elementCount = size * size;
        double[] data = new double[elementCount];
        for (int i = 0; i < elementCount; i++) {
            data[i] = elements[(i % size) * size + i / size];
        }
        return new SquareMatrix(size, data);
    }

    /**
     * Performs a naive divide and conquer matrix multiplication
     *
     * @param matrix1 the first matrix
     * @param matrix2 the second matrix
     * @return the resulting matrix
     */
    public static SquareMatrix matrixMultiplyNaive(SquareMatrix matrix1, SquareMatrix matrix2) {
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

        SquareMatrix c11 = matrixMultiplyNaive(a11, b11).plusIP(matrixMultiplyNaive(a12, b21));
        SquareMatrix c12 = matrixMultiplyNaive(a11, b12).plusIP(matrixMultiplyNaive(a12, b22));
        SquareMatrix c21 = matrixMultiplyNaive(a21, b11).plusIP(matrixMultiplyNaive(a22, b21));
        SquareMatrix c22 = matrixMultiplyNaive(a21, b12).plusIP(matrixMultiplyNaive(a22, b22));

        return SquareMatrix.fromParts(c11, c12, c21, c22);
    }

    /**
     * Performs a strassen matrix multiplication
     *
     * @param matrix1 the first matrix
     * @param matrix2 the second matrix
     * @return the resulting matrix
     */
    public static SquareMatrix matrixMultiplyStrassen(SquareMatrix matrix1, SquareMatrix matrix2) {
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

        SquareMatrix p1, p2, p3, p4, p5, p6, p7;
        if (matrix1.getSize() >= STRASSEN_PARALLELIZE_THRESHOLD) {
            var p1j = ThreadPools.MATRIX_EXECUTOR.submit(() -> matrixMultiplyStrassen(a11, s1));
            var p2j = ThreadPools.MATRIX_EXECUTOR.submit(() -> matrixMultiplyStrassen(s2, b22));
            var p3j = ThreadPools.MATRIX_EXECUTOR.submit(() -> matrixMultiplyStrassen(s3, b11));
            var p4j = ThreadPools.MATRIX_EXECUTOR.submit(() -> matrixMultiplyStrassen(a22, s4));
            var p5j = ThreadPools.MATRIX_EXECUTOR.submit(() -> matrixMultiplyStrassen(s5, s6));
            var p6j = ThreadPools.MATRIX_EXECUTOR.submit(() -> matrixMultiplyStrassen(s7, s7));
            var p7j = ThreadPools.MATRIX_EXECUTOR.submit(() -> matrixMultiplyStrassen(s9, s10));

            p1 = p1j.join();
            p2 = p2j.join();
            p3 = p3j.join();
            p4 = p4j.join();
            p5 = p5j.join();
            p6 = p6j.join();
            p7 = p7j.join();
        } else {
            p1 = matrixMultiplyStrassen(a11, s1);
            p2 = matrixMultiplyStrassen(s2, b22);
            p3 = matrixMultiplyStrassen(s3, b11);
            p4 = matrixMultiplyStrassen(a22, s4);
            p5 = matrixMultiplyStrassen(s5, s6);
            p6 = matrixMultiplyStrassen(s7, s8);
            p7 = matrixMultiplyStrassen(s9, s10);
        }

        SquareMatrix c11 = p5.plus(p4).minusIP(p2).plusIP(p6);
        SquareMatrix c12 = p1.plus(p2);
        SquareMatrix c21 = p3.plus(p4);
        SquareMatrix c22 = p5.plus(p1).minusIP(p3).minusIP(p7);

        return SquareMatrix.fromParts(c11, c12, c21, c22);
    }
}
