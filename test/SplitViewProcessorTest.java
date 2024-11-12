
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import model.ImageModel;
import model.ImgModel;
import model.image.Image;
import model.image.SimpleImage;
import org.junit.jupiter.api.Test;


/**
 * Test for SplitViewProcessor.
 */
public class SplitViewProcessorTest {

  ImgModel model = new ImageModel();

  @Test
  public void testSplitViewDimensions() {
    Image originalImage = new SimpleImage(100, 100);
    Image processedImage = new SimpleImage(100, 100);

    Image splitImage = model.splitView(originalImage, processedImage, 50);

    assertEquals(100, splitImage.getWidth(), "Split image width should match.");
    assertEquals(100, splitImage.getHeight(), "Split image height should match.");
  }

  @Test
  public void testSplitViewHalfway() {
    Image originalImage = new SimpleImage(100, 100);
    Image processedImage = new SimpleImage(100, 100);
    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        originalImage.setPixel(x, y, new int[]{255, 255, 255});
        processedImage.setPixel(x, y, new int[]{0, 0, 0});
      }
    }

    Image splitImage = model.splitView(originalImage, processedImage, 50);

    assertArrayEquals(new int[]{0, 0, 0}, splitImage.getPixel(75, 25),
        "Left side should show processed image (black).");
    assertArrayEquals(new int[]{255, 255, 255}, splitImage.getPixel(75, 75),
        "Right side should show original image (white).");
  }

  @Test
  public void testSplitViewWithExtremeSplitPercentages() {
    Image originalImage = new SimpleImage(100, 100);
    Image processedImage = new SimpleImage(100, 100);
    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        originalImage.setPixel(x, y, new int[]{0, 0, 0});
        processedImage.setPixel(x, y, new int[]{255, 255, 255});
      }
    }

    // 0% split (all original)
    Image splitImage0 = model.splitView(originalImage, processedImage, 0);
    assertArrayEquals(new int[]{0, 0, 0}, splitImage0.getPixel(25, 50),
        "Left side should show processed image (black).");
    assertArrayEquals(new int[]{0, 0, 0}, splitImage0.getPixel(75, 50),
        "Right side should show original image (black).");
  }
}