package model.image;

/**
 * The RGBImage class extends the Image class and provides methods to extract the red, green, and
 * blue components from an image.
 */
public class RGBImage extends SimpleImage {

  /**
   * Constructs an RGBImage object with the specified width and height.
   *
   * @param width  the width of the image
   * @param height the height of the image
   */
  public RGBImage(int width, int height) {
    super(width, height);
  }

  /**
   * Creates a grayscale image based on the red component of the given image.
   *
   * @param image the input image from which the red component is extracted
   * @return a new grayscale image with intensity based on the red component
   */
  public Image redComponent(Image image) {
    Image result = new RGBImage(width, height);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int[] pixel = image.getPixel(i, j);
        int[] newPixel = {pixel[0], pixel[0], pixel[0]};
        result.setPixel(i, j, newPixel);
      }
    }
    return result;
  }

  /**
   * Creates a grayscale image based on the green component of the given image.
   *
   * @param image the input image from which the red component is extracted
   * @return a new grayscale image with intensity based on the red component
   */
  public Image greenComponent(Image image) {
    Image result = new RGBImage(width, height);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int[] pixel = image.getPixel(i, j);
        int[] newPixel = {pixel[1], pixel[1], pixel[1]};
        result.setPixel(i, j, newPixel);
      }
    }
    return result;
  }

  /**
   * Creates a grayscale image based on the blue component of the given image.
   *
   * @param image the input image from which the red component is extracted
   * @return a new grayscale image with intensity based on the red component
   */
  public Image blueComponent(Image image) {
    Image result = new RGBImage(width, height);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int[] pixel = image.getPixel(i, j);
        int[] newPixel = {pixel[2], pixel[2], pixel[2]};
        result.setPixel(i, j, newPixel);
      }
    }
    return result;
  }
}
