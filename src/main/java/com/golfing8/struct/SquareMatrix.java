package com.golfing8.struct;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a size n square matrix.
 */
public class SquareMatrix {
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
        return size == that.size && Objects.deepEquals(data, that.data);
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
}
