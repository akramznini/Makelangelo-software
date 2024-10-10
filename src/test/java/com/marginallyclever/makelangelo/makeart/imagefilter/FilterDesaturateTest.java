package com.marginallyclever.makelangelo.makeart.imagefilter;


import com.marginallyclever.convenience.ColorRGB;
import com.marginallyclever.makelangelo.makeart.TransformedImage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FilterDesaturateTest {
    // check if all pixels are grey
    public static boolean isDesaturatedImage(BufferedImage a) {
        int h = a.getHeight();
        int w = a.getWidth();
        int x, y;

        for (y = 0; y < h; ++y) {
            for (x = 0; x < w; ++x) {
                int rgb = a.getRGB(x , y);

                // extracting red, green and blue values
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // check if pixel is grey
                if (!(red == green && green == blue)) {
                    return false;
                }
            }
        }

        return true;

    }

    @Test
    public void test() throws IOException {
        // Arrange
        TransformedImage src = new TransformedImage(ImageIO.read(new File("src/test/resources/mandrill.png")) );

        // Act
        FilterDesaturate f = new FilterDesaturate(src);
        TransformedImage dest = f.filter();

        // Assert
        boolean isDesaturated = isDesaturatedImage(dest.getSourceImage());
        Assertions.assertTrue(isDesaturated);
    }


}

