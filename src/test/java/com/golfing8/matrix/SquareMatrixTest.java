package com.golfing8.matrix;

import com.golfing8.struct.SquareMatrix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class SquareMatrixTest {
    @Test
    public void testRegularMultiplication() {
        SquareMatrix a = SquareMatrix.fromElements(
                1, 2,
                3, 4
        );
        SquareMatrix b = SquareMatrix.fromElements(
                5, 6,
                7, 8
        );

        SquareMatrix c = a.matrixProduct(b);
        SquareMatrix expected = SquareMatrix.fromElements(
                19, 22,
                43, 50
        );
        Assertions.assertEquals(expected, c);
    }

    @RepeatedTest(10)
    public void testMatrixMultiply() {
        SquareMatrix a = SquareMatrix.random(8);
        SquareMatrix b = SquareMatrix.random(8);

        SquareMatrix p1 = a.matrixProduct(b);
        SquareMatrix p2 = SquareMatrix.matrixMultiplyNaive(a, b);
        SquareMatrix p3 = SquareMatrix.matrixMultiplyStrassen(a, b);

        Assertions.assertEquals(p1, p2);
        Assertions.assertEquals(p1, p3);
    }
}
