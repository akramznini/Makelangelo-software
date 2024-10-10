package com.marginallyclever.makelangelo.makeart.imagefilter;
import com.marginallyclever.makelangelo.makeart.TransformedImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class FilterContrastAdjustTest {

    private FilterContrastAdjust filter;
    private TransformedImage img;
    private int bottom = 50;
    private int top = 200;

    @BeforeEach
    public void setUp() throws IOException {
        // Arrange
        BufferedImage testImage = ImageIO.read(new File("src/test/resources/mandrill.png"));
        img = new TransformedImage(testImage);
        filter = new FilterContrastAdjust(img, bottom, top); // Set the bottom and top range
    }

    @Test
    public void testContrastIncreasedAfterFilter() {
        // Arrange
        BufferedImage originalImage = img.getSourceImage();
        double originalContrast = calculateImageContrast(originalImage);

        // Act
        TransformedImage filteredImage = filter.filter();
        BufferedImage filteredBI = filteredImage.getSourceImage();


        double filteredContrast = calculateImageContrast(filteredBI);


        // Assert that the contrast has increased
        assertTrue(filteredContrast > originalContrast,
                "Contrast should be higher after applying the filter.");
    }


    private boolean isWithinRange(int expected, int actual, int range) {
        return Math.abs(expected - actual) <= range;
    }

    private double calculateImageContrast(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixelIntensities = new int[width * height];

        // Get pixel intensities by converting each pixel to grayscale
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int rgb = image.getRGB(x, y);
                int grayscale = getGrayscale(rgb); // Convert RGB to grayscale
                pixelIntensities[y * width + x] = grayscale;
            }
        }

        // Calculate the mean and standard deviation of pixel intensities
        double mean = IntStream.of(pixelIntensities).average().orElse(0);
        double variance = IntStream.of(pixelIntensities)
                .mapToDouble(i -> Math.pow(i - mean, 2))
                .average()
                .orElse(0);
        return Math.sqrt(variance); // Standard deviation as a measure of contrast
    }

    private int getGrayscale(int rgb) {
        // Extract red, green, blue components and compute the grayscale value
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return (int) (0.3 * red + 0.59 * green + 0.11 * blue); // Standard grayscale formula
    }
}
