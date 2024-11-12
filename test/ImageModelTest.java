import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import model.image.Image;
import model.ImageModel;
import model.image.SimpleImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ImageModel test.
 */
public class ImageModelTest {

  private ImageModel imageModel;
  private Image testImage;

  /**
   * Setup method run before each test.
   */
  @BeforeEach
  public void setUp() {
    imageModel = new ImageModel();
    testImage = new SimpleImage(2, 2);
    testImage.setPixel(0, 0, new int[]{255, 0, 0});
    testImage.setPixel(0, 1, new int[]{0, 255, 0});
    testImage.setPixel(1, 0, new int[]{0, 0, 255});
    testImage.setPixel(1, 1, new int[]{255, 255, 0});
  }

  @Test
  public void testFlipHorizontal() {
    Image flipped = imageModel.flipHorizontal(testImage);
    assertArrayEquals(new int[]{0, 255, 0}, flipped.getPixel(0, 0));
    assertArrayEquals(new int[]{255, 0, 0}, flipped.getPixel(0, 1));
    assertArrayEquals(new int[]{255, 255, 0}, flipped.getPixel(1, 0));
    assertArrayEquals(new int[]{0, 0, 255}, flipped.getPixel(1, 1));
  }

  @Test
  public void testFlipVertical() {
    Image flipped = imageModel.flipVertical(testImage);
    assertArrayEquals(new int[]{0, 0, 255}, flipped.getPixel(0, 0));
    assertArrayEquals(new int[]{255, 255, 0}, flipped.getPixel(0, 1));
    assertArrayEquals(new int[]{255, 0, 0}, flipped.getPixel(1, 0));
    assertArrayEquals(new int[]{0, 255, 0}, flipped.getPixel(1, 1));
  }

  @Test
  public void testBrighten() {
    Image brightened = imageModel.brighten(testImage, 50);
    assertArrayEquals(new int[]{255, 50, 50}, brightened.getPixel(0, 0));
    assertArrayEquals(new int[]{50, 255, 50}, brightened.getPixel(0, 1));
    assertArrayEquals(new int[]{50, 50, 255}, brightened.getPixel(1, 0));
    assertArrayEquals(new int[]{255, 255, 50}, brightened.getPixel(1, 1));
  }

  @Test
  public void testToGreyscale() {
    Image greyscale = imageModel.toGreyscale(testImage);
    assertArrayEquals(new int[]{76, 76, 76}, greyscale.getPixel(0, 0));
    assertArrayEquals(new int[]{149, 149, 149}, greyscale.getPixel(0, 1));
    assertArrayEquals(new int[]{29, 29, 29}, greyscale.getPixel(1, 0));
    assertArrayEquals(new int[]{225, 225, 225}, greyscale.getPixel(1, 1));
  }

  @Test
  public void testBlur() {
    Image blurred = imageModel.blur(testImage);
    assertArrayEquals(new int[]{155, 61, 46}, blurred.getPixel(0, 0));
    assertArrayEquals(new int[]{92, 186, 15}, blurred.getPixel(0, 1));
    assertArrayEquals(new int[]{92, 61, 140}, blurred.getPixel(1, 0));
    assertArrayEquals(new int[]{155, 186, 46}, blurred.getPixel(1, 1));
  }

  @Test
  public void testSepia() {
    Image sepia = imageModel.sepia(testImage);
    assertArrayEquals(new int[]{100, 88, 69}, sepia.getPixel(0, 0));
    assertArrayEquals(new int[]{196, 174, 136}, sepia.getPixel(0, 1));
    assertArrayEquals(new int[]{48, 42, 33}, sepia.getPixel(1, 0));
    assertArrayEquals(new int[]{255, 255, 205}, sepia.getPixel(1, 1));
  }

  @Test
  public void testSharpen() {
    Image sharpened = imageModel.sharpen(testImage);
    assertArrayEquals(new int[]{255, 0, 2}, sharpened.getPixel(0, 0));
    assertArrayEquals(new int[]{4, 255, 0}, sharpened.getPixel(0, 1));
    assertArrayEquals(new int[]{4, 0, 255}, sharpened.getPixel(1, 0));
    assertArrayEquals(new int[]{255, 255, 2}, sharpened.getPixel(1, 1));
  }

  @Test
  public void testRedComponent() {
    Image redComponent = imageModel.redComponent(testImage);
    assertArrayEquals(new int[]{255, 255, 255}, redComponent.getPixel(0, 0));
    assertArrayEquals(new int[]{0, 0, 0}, redComponent.getPixel(0, 1));
    assertArrayEquals(new int[]{0, 0, 0}, redComponent.getPixel(1, 0));
    assertArrayEquals(new int[]{255, 255, 255}, redComponent.getPixel(1, 1));
  }

  @Test
  public void testGreenComponent() {
    Image greenComponent = imageModel.greenComponent(testImage);
    assertArrayEquals(new int[]{0, 0, 0}, greenComponent.getPixel(0, 0));
    assertArrayEquals(new int[]{255, 255, 255}, greenComponent.getPixel(0, 1));
    assertArrayEquals(new int[]{0, 0, 0}, greenComponent.getPixel(1, 0));
    assertArrayEquals(new int[]{255, 255, 255}, greenComponent.getPixel(1, 1));
  }

  @Test
  public void testBlueComponent() {
    Image blueComponent = imageModel.blueComponent(testImage);
    assertArrayEquals(new int[]{0, 0, 0}, blueComponent.getPixel(0, 0));
    assertArrayEquals(new int[]{0, 0, 0}, blueComponent.getPixel(0, 1));
    assertArrayEquals(new int[]{255, 255, 255}, blueComponent.getPixel(1, 0));
    assertArrayEquals(new int[]{0, 0, 0}, blueComponent.getPixel(1, 1));
  }

  @Test
  public void testValueComponent() {
    Image valueComponent = imageModel.value(testImage);
    assertArrayEquals(new int[]{255, 255, 255}, valueComponent.getPixel(0, 0));
    assertArrayEquals(new int[]{255, 255, 255}, valueComponent.getPixel(0, 1));
    assertArrayEquals(new int[]{255, 255, 255}, valueComponent.getPixel(1, 0));
    assertArrayEquals(new int[]{255, 255, 255}, valueComponent.getPixel(1, 1));
  }


  @Test
  public void testIntensityComponent() {
    Image intensityComponent = imageModel.intensity(testImage);
    assertArrayEquals(new int[]{85, 85, 85}, intensityComponent.getPixel(0, 0));
    assertArrayEquals(new int[]{85, 85, 85}, intensityComponent.getPixel(0, 1));
    assertArrayEquals(new int[]{85, 85, 85}, intensityComponent.getPixel(1, 0));
    assertArrayEquals(new int[]{170, 170, 170}, intensityComponent.getPixel(1, 1));
  }

  @Test
  public void testSetPixelOutOfBounds() {
    assertThrows(IndexOutOfBoundsException.class, () -> {
      testImage.setPixel(2, 2, new int[]{255, 255, 255}); // Setting out of bounds
    });
  }

  @Test
  public void testAccessPixelOutOfBounds() {
    assertThrows(IndexOutOfBoundsException.class, () -> {
      testImage.getPixel(2, 2); // Accessing out of bounds
    });
  }

  @Test
  public void testFlipVerticalNullImage() {
    assertThrows(IllegalArgumentException.class, () -> {
      imageModel.flipVertical(null);
    });
  }

  @Test
  public void testBrightenNullImage() {
    assertThrows(IllegalArgumentException.class, () -> {
      imageModel.brighten(null, 50);
    });
  }

  @Test
  public void testToGreyscaleNullImage() {
    assertThrows(IllegalArgumentException.class, () -> {
      imageModel.toGreyscale(null);
    });
  }

  @Test
  public void testBlurNullImage() {
    assertThrows(IllegalArgumentException.class, () -> {
      imageModel.blur(null);
    });
  }

  @Test
  public void testSepiaNullImage() {
    assertThrows(IllegalArgumentException.class, () -> {
      imageModel.sepia(null);
    });
  }

  @Test
  public void testSharpenNullImage() {
    assertThrows(IllegalArgumentException.class, () -> {
      imageModel.sharpen(null);
    });
  }

  @Test
  public void testRedComponentNullImage() {
    assertThrows(IllegalArgumentException.class, () -> {
      imageModel.redComponent(null);
    });
  }

  @Test
  public void testGreenComponentNullImage() {
    assertThrows(IllegalArgumentException.class, () -> {
      imageModel.greenComponent(null);
    });
  }

  @Test
  public void testBlueComponentNullImage() {
    assertThrows(IllegalArgumentException.class, () -> {
      imageModel.blueComponent(null);
    });
  }

  @Test
  public void testValueComponentNullImage() {
    assertThrows(IllegalArgumentException.class, () -> {
      imageModel.value(null);
    });
  }

  @Test
  public void testIntensityComponentNullImage() {
    assertThrows(IllegalArgumentException.class, () -> {
      imageModel.intensity(null);
    });
  }

}