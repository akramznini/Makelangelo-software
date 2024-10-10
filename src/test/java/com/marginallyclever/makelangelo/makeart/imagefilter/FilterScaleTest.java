package com.marginallyclever.makelangelo.makeart.imagefilter;

import com.marginallyclever.convenience.ColorRGB;
import com.marginallyclever.makelangelo.makeart.TransformedImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FilterScaleTest {

    private TransformedImage img;
    private FilterScale filter;

    @BeforeEach
    public void setUp() throws IOException {
        // Arrange
        // Load the mandrill.png file for testing
        BufferedImage testImage = ImageIO.read(new File("src/test/resources/mandrill.png"));
        img = new TransformedImage(testImage);
        filter = new FilterScale(img, 1.5); // Example scale factor of 1.5
    }


    @Test
    public void testScaleFactorAppliedCorrectly() {
        // Act
        TransformedImage resultImage = filter.filter();
        BufferedImage resultBufferedImage = resultImage.getSourceImage();

        for (int y = 0; y < img.getSourceImage().getHeight(); y++) {
            for (int x = 0; x < img.getSourceImage().getWidth(); x++) {
                ColorRGB originalPixel = new ColorRGB(img.getSourceImage().getRGB(x, y));
                ColorRGB scaledPixel = new ColorRGB(resultBufferedImage.getRGB(x, y));

                // Assert
                assertEquals(clamp(originalPixel.red * 1.5), scaledPixel.red, "Red channel should be scaled correctly.");
                assertEquals(clamp(originalPixel.green * 1.5), scaledPixel.green, "Green channel should be scaled correctly.");
                assertEquals(clamp(originalPixel.blue * 1.5), scaledPixel.blue, "Blue channel should be scaled correctly.");
            }
        }
    }

    @Test
    public void testScaleFactorBoundaryValues() {
        // Act
        FilterScale largeScaleFilter = new FilterScale(img, 5.0); // Scale factor of 5.0
        TransformedImage resultImage = largeScaleFilter.filter();
        BufferedImage resultBufferedImage = resultImage.getSourceImage();


        for (int y = 0; y < img.getSourceImage().getHeight(); y++) {
            for (int x = 0; x < img.getSourceImage().getWidth(); x++) {
                ColorRGB originalPixel = new ColorRGB(img.getSourceImage().getRGB(x, y));
                ColorRGB scaledPixel = new ColorRGB(resultBufferedImage.getRGB(x, y));

                // Assert
                assertEquals(Math.min(255, (int) (originalPixel.red * 5.0)), scaledPixel.red, "Red channel should be clamped to 255.");
                assertEquals(Math.min(255, (int) (originalPixel.green * 5.0)), scaledPixel.green, "Green channel should be clamped to 255.");
                assertEquals(Math.min(255, (int) (originalPixel.blue * 5.0)), scaledPixel.blue, "Blue channel should be clamped to 255.");
            }
        }
    }

    @Test
    public void testZeroScaleFactor() {
        // Act
        FilterScale zeroScaleFilter = new FilterScale(img, 0.0);
        TransformedImage resultImage = zeroScaleFilter.filter();
        BufferedImage resultBufferedImage = resultImage.getSourceImage();


        for (int y = 0; y < resultBufferedImage.getHeight(); y++) {
            for (int x = 0; x < resultBufferedImage.getWidth(); x++) {
                ColorRGB pixel = new ColorRGB(resultBufferedImage.getRGB(x, y));

                // Assert

                assertEquals(0, pixel.red, "Red channel should be 0 when scaled by 0.");
                assertEquals(0, pixel.green, "Green channel should be 0 when scaled by 0.");
                assertEquals(0, pixel.blue, "Blue channel should be 0 when scaled by 0.");
            }
        }
    }

    @Test
    public void testNegativeScaleFactor() {
        // Arrange
        FilterScale negativeScaleFilter = new FilterScale(img, -1.0);
        TransformedImage resultImage = negativeScaleFilter.filter();
        BufferedImage resultBufferedImage = resultImage.getSourceImage();

        // Ensure all pixel values are clamped to 0
        for (int y = 0; y < resultBufferedImage.getHeight(); y++) {
            for (int x = 0; x < resultBufferedImage.getWidth(); x++) {
                ColorRGB pixel = new ColorRGB(resultBufferedImage.getRGB(x, y));

                // Assert
                assertEquals(0, pixel.red, "Red channel should be clamped to 0 when scaled negatively.");
                assertEquals(0, pixel.green, "Green channel should be clamped to 0 when scaled negatively.");
                assertEquals(0, pixel.blue, "Blue channel should be clamped to 0 when scaled negatively.");
            }
        }
    }

    // clamp pixel values
    private int clamp(double value) {
        return (int) Math.max(0, Math.min(255, value));
    }
}
