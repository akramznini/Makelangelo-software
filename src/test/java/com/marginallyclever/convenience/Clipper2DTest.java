package com.marginallyclever.convenience;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Clipper2DTest {

    // Ce test vérifie le comportement lorsque la ligne est entièrement à l'intérieur du rectangle.
    @Test
    void testClipLineToRectangle_LineCompletelyInside() {
        // Arrange
        Point2D p0 = new Point2D(1, 1);
        Point2D p1 = new Point2D(2, 2);
        Point2D rMax = new Point2D(3, 3);
        Point2D rMin = new Point2D(0, 0);

        // Act
        boolean result = Clipper2D.clipLineToRectangle(p0, p1, rMax, rMin);

        // Assert
        assertTrue(result);
        // Les points p0 et p1 restent les mêmes
        assertEquals(1, p0.x);
        assertEquals(1, p0.y);
        assertEquals(2, p1.x);
        assertEquals(2, p1.y);
    }

    // Ce test vérifie le comportement lorsque la ligne est entièrement à l'extérieur du rectangle.
    @Test
    void testClipLineToRectangle_LineCompletelyOutside() {
        // Arrange
        Point2D p0 = new Point2D(-1, -1);
        Point2D p1 = new Point2D(-2, -2);
        Point2D rMax = new Point2D(3, 3);
        Point2D rMin = new Point2D(0, 0);

        // Act
        boolean result = Clipper2D.clipLineToRectangle(p0, p1, rMax, rMin);

        // Assert
        assertFalse(result);
    }

    // Ce test vérifie le cas typique lorsque la ligne est partiellement à l'intérieur du rectangle.
    @Test
    void testClipLineToRectangle_LinePartiallyInside() {
        // Arrange
        Point2D p0 = new Point2D(-1, -1);
        Point2D p1 = new Point2D(2, 2);
        Point2D rMax = new Point2D(3, 3);
        Point2D rMin = new Point2D(0, 0);

        // Act
        boolean result = Clipper2D.clipLineToRectangle(p0, p1, rMax, rMin);

        // Assert
        assertTrue(result);
        // Le point p0 change
        assertEquals(0, p0.x);
        assertEquals(0, p0.y);
        assertEquals(2, p1.x);
        assertEquals(2, p1.y);
    }

    // Ce test vérifie le comportement lorsque la ligne traverse le rectangle en diagonale.
    @Test
    void testClipLineToRectangle_LineCrossingDiagonal() {
        // Arrange
        Point2D p0 = new Point2D(-1, -1);
        Point2D p1 = new Point2D(4, 4);
        Point2D rMax = new Point2D(3, 3);
        Point2D rMin = new Point2D(0, 0);

        // Act
        boolean result = Clipper2D.clipLineToRectangle(p0, p1, rMax, rMin);

        // Assert
        assertTrue(result);
        // Les points p0 et p1 deviennent les extrêmités du rectangle
        assertEquals(0, p0.x);
        assertEquals(0, p0.y);
        assertEquals(3, p1.x);
        assertEquals(3, p1.y);
    }

    // Ce test vérifie le comportement lorsque la ligne est tangente au rectangle.
    @Test
    void testClipLineToRectangle_TangentLine() {
        // Arrange
        Point2D p0 = new Point2D(-1, 0);
        Point2D p1 = new Point2D(4, 0);
        Point2D rMax = new Point2D(3, 3);
        Point2D rMin = new Point2D(0, 0);

        // Act
        boolean result = Clipper2D.clipLineToRectangle(p0, p1, rMax, rMin);

        // Assert
        assertTrue(result);
        assertEquals(0, p0.x);
        assertEquals(0, p0.y);
        assertEquals(3, p1.x);
        assertEquals(0, p1.y);
    }

    // Cette fonction teste le cas où notre ligne est un seul points (2 points identiques) qui est à
    // l'intérieur du rectangle
    @Test
    void testClipLineToRectangle_Point_Inside() {
        Point2D p0 = new Point2D(1.5, 1.5);
        Point2D p1 = new Point2D(1.5, 1.5);
        Point2D rMax = new Point2D(3, 3);
        Point2D rMin = new Point2D(0, 0);

        boolean result = Clipper2D.clipLineToRectangle(p0, p1, rMax, rMin);

        assertTrue(result);
        assertEquals(1.5, p0.x);
        assertEquals(1.5, p0.y);
        assertEquals(1.5, p1.x);
        assertEquals(1.5, p1.y);
    }
}