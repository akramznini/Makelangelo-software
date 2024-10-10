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

public class FilterSubtractTest {

    private TransformedImage imgA;
    private TransformedImage imgB;
    private FilterSubtract filter;

    @BeforeEach
    public void setUp() throws IOException {
        // Arrange
        BufferedImage testImageA = ImageIO.read(new File("src/test/resources/mandrill.png"));
        BufferedImage testImageB = ImageIO.read(new File("src/test/resources/mandrill-inverse.png"));
        imgA = new TransformedImage(testImageA);
        imgB = new TransformedImage(testImageB);
        filter = new FilterSubtract(imgA, imgB); // Initialize the filter
    }


    /**
     *
     * testSubtractionForPixelValues() tests subtraction values between 2 images of the same size in the resources folder.
     *
     **/
    @Test
    public void testSubtractionForPixelValues() {
        // Act
        TransformedImage resultImage = filter.filter();
        BufferedImage resultBufferedImage = resultImage.getSourceImage();

        // Assert
        for (int y = 0; y < resultBufferedImage.getHeight(); y++) {
            for (int x = 0; x < resultBufferedImage.getWidth(); x++) {
                ColorRGB pixelA = new ColorRGB(imgA.getSourceImage().getRGB(x, y));
                ColorRGB pixelB = new ColorRGB(imgB.getSourceImage().getRGB(x, y));
                ColorRGB pixelResult = new ColorRGB(resultBufferedImage.getRGB(x, y));

                // Assert that pixelResult is the subtraction of pixelA and pixelB (clamped)
                assertEquals(Math.max(0, pixelA.red - pixelB.red), pixelResult.red, "Red channel is subtracted correctly.");
                assertEquals(Math.max(0, pixelA.green - pixelB.green), pixelResult.green, "Green channel is subtracted correctly.");
                assertEquals(Math.max(0, pixelA.blue - pixelB.blue), pixelResult.blue, "Blue channel is subtracted correctly.");
            }
        }
    }


    /**
     *
     * testNegativePixelSubtraction() tests the case when the subtraction results in a negative value. In that case the result value should be 0.
     *
     **/
    @Test
    public void testNegativePixelSubtraction() {
        // Arrange
        BufferedImage imgA = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        BufferedImage imgB = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        imgA.setRGB(0, 0, new ColorRGB(255, 255, 255).toInt()); // White
        imgB.setRGB(0, 0, new ColorRGB(255, 255, 255).toInt()); // White
        imgA.setRGB(1, 1, new ColorRGB(100, 100, 100).toInt()); // Grey
        imgB.setRGB(1, 1, new ColorRGB(200, 200, 200).toInt()); // Darker Grey

        TransformedImage transA = new TransformedImage(imgA);
        TransformedImage transB = new TransformedImage(imgB);

        // Act
        FilterSubtract filter = new FilterSubtract(transA, transB);
        TransformedImage resultImage = filter.filter();
        BufferedImage resultBufferedImage = resultImage.getSourceImage();

        // Assert
        ColorRGB resultPixel00 = new ColorRGB(resultBufferedImage.getRGB(0, 0));
        assertEquals(0, resultPixel00.red, "Red channel should be clamped to 0.");
        assertEquals(0, resultPixel00.green, "Green channel should be clamped to 0.");
        assertEquals(0, resultPixel00.blue, "Blue channel should be clamped to 0.");


        ColorRGB resultPixel11 = new ColorRGB(resultBufferedImage.getRGB(1, 1));
        assertEquals(0, resultPixel11.red, "Red channel should be clamped to 0 after subtraction.");
        assertEquals(0, resultPixel11.green, "Green channel should be clamped to 0 after subtraction.");
        assertEquals(0, resultPixel11.blue, "Blue channel should be clamped to 0 after subtraction.");
    }
}
