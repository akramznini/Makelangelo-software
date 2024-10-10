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

    // Ce test vérifie le comportement avec une image contenant des pixels rouges et bleus.
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

    // Ce test vérifie le comportement avec une image contenant des pixels gris foncé et gris clair.
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


    // Ce test vérifie le comportement avec une image d'une seule couleur.
    @Test
    void testGetGreyHistogramOf_SingleColor() {
        // Arrange
        Color color = new Color(100, 150, 200);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                testImg.setRGB(x, y, color.getRGB());
            }
        }


        // Act
        histogram.getGreyHistogramOf(testImg);

        // Assert
        int expectedGreyValue = (100 + 150 + 200) / 3;
        assertEquals(100, histogram.red[expectedGreyValue]);
        assertEquals(0, histogram.red[expectedGreyValue - 1]);
        assertEquals(0, histogram.red[expectedGreyValue + 1]);
    }

    // Ce test vérifie que la fonction retourne le bon nombre de niveaux et que ces niveaux sont correctement ordonnés.
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

    // Ce test vérifie que la fonction lance une exception lorsqu'on lui passe un nombre de niveaux invalide (0 ou négatif).
    @Test
    void testGetLevels_InvalidInput() {
        // Arrange

        // Act & Assert
        assertThrows(InvalidParameterException.class, () -> histogram.getLevels(0),
                "Devrait lancer une exception pour un nombre de niveaux invalide");
    }

    // Ce test vérifie que la fonction retourne les bons niveaux pour des valeurs d'entrée données.
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

    // Ce test vérifie que la fonction lance une exception lorsqu'on lui passe un tableau d'entrée vide.
    @Test
    void testGetLevelsMapped_InvalidInput() {
        // Arrange

        // Act & Assert
        assertThrows(InvalidParameterException.class, () -> histogram.getLevelsMapped(new double[0]),
                "Devrait lancer une exception pour un tableau d'entrée vide");
    }
}
