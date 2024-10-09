package com.marginallyclever.convenience;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Clipper2DTest {

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
}