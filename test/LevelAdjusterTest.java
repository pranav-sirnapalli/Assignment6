import static org.junit.jupiter.api.Assertions.assertTrue;

import model.ImageModel;
import model.ImgModel;
import model.image.Image;
import model.image.SimpleImage;
import org.junit.Test;


/**
 * Test for Level-Adjust.
 */
public class LevelAdjusterTest {

  ImgModel model = new ImageModel();

  @Test
  public void testAdjustLevelsBounds() {
    Image testImage = new SimpleImage(100, 100);

    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        testImage.setPixel(x, y, new int[]{100, 100, 100});
      }
      Image adjustedImage = model.adjustLevels(testImage, 10, 128, 245);

      int[] adjustedColor = adjustedImage.getPixel(50, 50);

      assertTrue(adjustedColor[0] >= 0 && adjustedColor[0] <= 255, "Red should be within bounds.");
    }

  }

  @Test
  public void testLevelsAdjustmentForHighContrast() {
    Image testImage = new SimpleImage(100, 100);

    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        testImage.setPixel(x, y, new int[]{20, 50, 70});
      }
    }
    Image adjustedImage = model.adjustLevels(testImage, 10, 100, 200);

    int[] adjustedColor = adjustedImage.getPixel(0, 0);

    assertTrue(adjustedColor[0] < 20, "Red should increase post-adjustment EE.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustmentIllegalArguments() {
    Image testImage = new SimpleImage(100, 100);

    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        testImage.setPixel(x, y, new int[]{20, 50, 70});
      }
    }
    Image adjustedImage = model.adjustLevels(testImage, -1, -1, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustmentIllegalArguments1() {
    Image testImage = new SimpleImage(100, 100);

    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        testImage.setPixel(x, y, new int[]{20, 50, 70});
      }
    }
    Image adjustedImage = model.adjustLevels(testImage, 256, 256, 256);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLevelsAdjustmentIllegalArguments2() {
    Image testImage = new SimpleImage(100, 100);

    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        testImage.setPixel(x, y, new int[]{20, 50, 70});
      }
    }
    Image adjustedImage = model.adjustLevels(testImage, 0, 256, -1);
  }

}
