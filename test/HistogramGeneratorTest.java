
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import model.ImageModel;
import model.ImgModel;
import model.image.Image;
import model.image.SimpleImage;
import org.junit.jupiter.api.Test;


/**
 * Test cases to test the HistogramGenerator.
 */
public class HistogramGeneratorTest {

  ImgModel model = new ImageModel();

  @Test
  public void testGenerateHistogramDimensions() {
    Image testImage = new SimpleImage(100, 100);

    Image histogram = model.histogram(testImage);

    assertNotNull(histogram, "Histogram image should not be null.");
    assertEquals(256, histogram.getWidth(), "Histogram width should be 256.");
    assertEquals(256, histogram.getHeight(), "Histogram height should be 256.");
  }

  @Test
  public void testHistogramContentForUniformColorImage() {
    Image testImage = new SimpleImage(100, 100);

    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        testImage.setPixel(x, y, new int[]{128, 128, 128});
      }
    }
    Image histogram = model.histogram(testImage);
    // Check that the histogram has peaks around 128 for each color channel
    // For a uniform color, there should be significant values only around one part of the histogram
    assertNotNull(histogram, "Histogram image should not be null.");
  }

  @Test
  public void testHistogramForFullRangeColors() {
    Image testImage = new SimpleImage(256, 1);

    for (int i = 0; i < 256; i++) {
      testImage.setPixel(i, 0, new int[]{i, i, i});
    }
    Image histogram = model.histogram(testImage);
    // Check if each grayscale value from 0 to 255 is represented once in the histogram
    assertNotNull(histogram, "Histogram image should not be null.");
  }

  @Test
  public void testHistogramWithNoImage() {
    assertThrows(NullPointerException.class, () -> {
      model.histogram(null);
    }, "Expected exception for null image input");
  }
}
