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

public class FilterThresholdTest {

    private TransformedImage img;
    private FilterThreshold filter;

    @BeforeEach
    public void setUp() throws IOException {
        // Arrange
        // load sample test image mandrill.png
        BufferedImage testImage = ImageIO.read(new File("src/test/resources/mandrill.png"));
        img = new TransformedImage(testImage);

        // Set up filter with threshold of 128
        filter = new FilterThreshold(img, 128);
    }


    @Test
    public void testThresholdingFunctionality() {
        // Act
        // Apply filter
        TransformedImage resultImage = filter.filter();
        BufferedImage resultBufferedImage = resultImage.getSourceImage();

        // Check that each pixel is either black or white
        for (int y = 0; y < img.getSourceImage().getHeight(); y++) {
            for (int x = 0; x < img.getSourceImage().getWidth(); x++) {
                ColorRGB originalPixel = new ColorRGB(img.getSourceImage().getRGB(x, y));
                ColorRGB thresholdedPixel = new ColorRGB(resultBufferedImage.getRGB(x, y));

                // Ensure each channel is either 0 or 255 based on the threshold
                assertEquals(applyThreshold(originalPixel.red, 128), thresholdedPixel.red, "Red channel should be thresholded correctly.");
                assertEquals(applyThreshold(originalPixel.green, 128), thresholdedPixel.green, "Green channel should be thresholded correctly.");
                assertEquals(applyThreshold(originalPixel.blue, 128), thresholdedPixel.blue, "Blue channel should be thresholded correctly.");
            }
        }
    }


    @Test
    public void testMinThreshold() {
        // Act
        // Apply the threshold filter with threshold < 0
        FilterThreshold lowThresholdFilter = new FilterThreshold(img, -1);
        TransformedImage resultImage = lowThresholdFilter.filter();
        BufferedImage resultBufferedImage = resultImage.getSourceImage();


        // Ensure that all pixels are white
        for (int y = 0; y < img.getSourceImage().getHeight(); y++) {
            for (int x = 0; x < img.getSourceImage().getWidth(); x++) {
                ColorRGB thresholdedPixel = new ColorRGB(resultBufferedImage.getRGB(x, y));

                // Assert

                assertEquals(255, thresholdedPixel.red, "Red channel should handle low threshold correctly.");
                assertEquals(255, thresholdedPixel.green, "Green channel should handle low threshold correctly.");
                assertEquals(255, thresholdedPixel.blue, "Blue channel should handle low threshold correctly.");
            }
        }
    }

    @Test
    public void testMaxThreshold() {
        // Act
        // Apply the threshold filter with threshold > 255
        FilterThreshold highThresholdFilter = new FilterThreshold(img, 256);
        TransformedImage resultImage = highThresholdFilter.filter();
        BufferedImage resultBufferedImage = resultImage.getSourceImage();

        // Ensure that all pixels are black
        for (int y = 0; y < img.getSourceImage().getHeight(); y++) {
            for (int x = 0; x < img.getSourceImage().getWidth(); x++) {
                ColorRGB thresholdedPixel = new ColorRGB(resultBufferedImage.getRGB(x, y));

                // Assert
                assertEquals(0, thresholdedPixel.red, "Red channel should handle high threshold correctly.");
                assertEquals(0, thresholdedPixel.green, "Green channel should handle high threshold correctly.");
                assertEquals(0, thresholdedPixel.blue, "Blue channel should handle high threshold correctly.");
            }
        }
    }

    // Helper method to simulate threshold
    private int applyThreshold(int colorValue, int threshold) {
        return colorValue >= threshold ? 255 : 0;
    }
}
