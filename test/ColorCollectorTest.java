import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.ImageModel;
import model.ImgModel;
import model.image.Image;
import model.image.SimpleImage;
import org.junit.jupiter.api.Test;

/**
 * Test for Color Correction.
 */
public class ColorCollectorTest {

  ImgModel model = new ImageModel();

  @Test
  public void testCorrectColorDimensions() {
    Image testImage = new SimpleImage(100, 100);

    Image correctedImage = model.correctColor(testImage);

    assertEquals(testImage.getWidth(), correctedImage.getWidth(), "Width should match.");
    assertEquals(testImage.getHeight(), correctedImage.getHeight(), "Height should match.");
  }

  @Test
  public void testColorCorrectionForRedTint() {
    Image testImage = new SimpleImage(100, 100);
    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        testImage.setPixel(x, y, new int[]{200, 150, 150});
      }
    }
    Image correctedImage = model.correctColor(testImage);

    int[] correctedColor = correctedImage.getPixel(50, 50);

    //Color correctedColor = new Color(correctedImage.getRGB(50, 50));
    assertTrue(Math.abs(correctedColor[0] - correctedColor[1]) < 10,
        "Red and green should be closer post-correction.");
  }

  @Test
  public void testColorCorrectionForBalancedImage() {
    Image testImage = new SimpleImage(100, 100);
    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        testImage.setPixel(x, y, new int[]{128, 128, 128});
      }
    }
    Image correctedImage = model.correctColor(testImage);
    int[] correctedColor = correctedImage.getPixel(0, 0);

    assertEquals(128, correctedColor[0], "Red should remain the same.");
    assertEquals(128, correctedColor[1], "Green should remain the same.");
    assertEquals(128, correctedColor[2], "Blue should remain the same.");
  }

  @Test
  public void testCorrectColorForNullImage() {
    assertThrows(NullPointerException.class, () -> {
      model.correctColor(null);
    }, "Expected exception for null image input.");
  }
}
