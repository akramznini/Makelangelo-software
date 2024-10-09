package com.marginallyclever.convenience;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.security.InvalidParameterException;

class HistogramTest {

    private Histogram histogram;
    private BufferedImage testImg;

    @BeforeEach
    void setUp() {
        histogram = new Histogram();
        testImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
    }

    @Test
    void testGetRGBHistogramOf() {
        // Arrange
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (x < 5) {
                    testImg.setRGB(x, y, Color.RED.getRGB());
                } else {
                    testImg.setRGB(x, y, Color.BLUE.getRGB());
                }
            }
        }

        // Act
        histogram.getRGBHistogramOf(testImg);

        // Assert
        assertEquals(50, histogram.red[255], "Devrait avoir 50 pixels rouges");
        assertEquals(50, histogram.blue[255], "Devrait avoir 50 pixels bleus");
        assertEquals(0, histogram.green[255], "Ne devrait pas avoir de pixels verts");
    }

    @Test
    void testGetGreyHistogramOf() {
        // Arrange
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (x < 5) {
                    testImg.setRGB(x, y, new Color(100, 100, 100).getRGB());
                } else {
                    testImg.setRGB(x, y, new Color(200, 200, 200).getRGB());
                }
            }
        }

        // Act
        histogram.getGreyHistogramOf(testImg);

        // Assert
        assertEquals(50, histogram.red[100], "Devrait avoir 50 pixels gris foncé");
        assertEquals(50, histogram.red[200], "Devrait avoir 50 pixels gris clair");
        assertEquals(0, histogram.red[0], "Ne devrait pas avoir de pixels noirs");
        assertEquals(0, histogram.red[255], "Ne devrait pas avoir de pixels blancs");
    }

    @Test
    void testGetGreyHistogramOf_AllColors() {
        // Arrange
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
        int index = 0;
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                testImg.setRGB(x, y, colors[index % colors.length].getRGB());
                index++;
            }
        }

        // Act
        histogram.getGreyHistogramOf(testImg);

        // Assert
        int totalPixels = 0;
        for (int i = 0; i < 256; i++) {
            totalPixels += histogram.red[i];
        }
        assertEquals(100, totalPixels, "Le nombre total de pixels devrait être 100");
    }

    @Test
    void testGetLevels() {
        // Arrange
        for (int i = 0; i < 256; i++) {
            histogram.red[i] = (char) i;
        }

        // Act
        double[] levels = histogram.getLevels(4);

        // Assert
        assertEquals(4, levels.length, "Devrait retourner 4 niveaux");
        assertTrue(levels[0] < levels[1], "Le premier niveau devrait être inférieur au deuxième");
        assertTrue(levels[1] < levels[2], "Le deuxième niveau devrait être inférieur au troisième");
        assertTrue(levels[2] < levels[3], "Le troisième niveau devrait être inférieur au quatrième");
    }

    @Test
    void testGetLevels_InvalidInput() {
        // Arrange

        // Act & Assert
        assertThrows(InvalidParameterException.class, () -> histogram.getLevels(0),
                "Devrait lancer une exception pour un nombre de niveaux invalide");
    }

    @Test
    void testGetLevelsMapped() {
        // Arrange
        for (int i = 0; i < 256; i++) {
            histogram.red[i] = (char) 1;
        }
        double[] input = {0.25, 0.5, 0.75};

        // Act
        double[] levels = histogram.getLevelsMapped(input);

        // Assert
        assertEquals(3, levels.length, "Devrait retourner 3 niveaux");
        assertEquals(63, levels[0], "Le premier niveau devrait être à 63");
        assertEquals(127, levels[1], "Le deuxième niveau devrait être à 127");
        assertEquals(191, levels[2], "Le troisième niveau devrait être à 191");
    }

    @Test
    void testGetLevelsMapped_InvalidInput() {
        // Arrange

        // Act & Assert
        assertThrows(InvalidParameterException.class, () -> histogram.getLevelsMapped(new double[0]),
                "Devrait lancer une exception pour un tableau d'entrée vide");
    }
}
