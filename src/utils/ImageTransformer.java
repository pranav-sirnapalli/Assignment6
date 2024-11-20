package utils;

import java.awt.image.BufferedImage;
import model.image.Image;
import model.image.SimpleImage;

/**
 * The ImageTransformer class provides utility methods for transforming between BufferedImage and
 * Image objects.It supports converting a BufferedImage into a custom Image object, and vice versa,
 * by extracting and setting pixel data accordingly.
 */
public class ImageTransformer {

  /**
   * Transforms a BufferedImage into a custom Image object. This method iterates over the pixels of
   * the given BufferedImage and converts each pixel to an RGB array, which is set in a newly
   * created Image object.
   *
   * @param bufferedImage The BufferedImage to be transformed.
   * @return A new Image object containing the same pixel data as the BufferedImage.
   */
  public static Image transformBufferImageToImage(BufferedImage bufferedImage) {
    int height = bufferedImage.getHeight();
    int width = bufferedImage.getWidth();
    Image img = new SimpleImage(width, height);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int rgb = bufferedImage.getRGB(j, i);
        int[] pixel = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
        img.setPixel(i, j, pixel);
      }
    }
    return img;
  }

  /**
   * Transforms a custom Image object into a BufferedImage.This method converts each pixel of the
   * Image object into RGB format and sets it in the corresponding position of a newly created
   * BufferedImage.
   *
   * @param img The Image object to be transformed.
   * @return A BufferedImage containing the pixel data from the Image object.
   */
  public static BufferedImage transformImageToBufferImage(Image img) {
    BufferedImage bufferImage = new BufferedImage(img.getWidth(), img.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    transformHelperImageToBuffer(img, bufferImage);
    return bufferImage;
  }

  /**
   * Helper method to assist in transforming an Image object into a BufferedImage.This method is
   * called internally to transfer pixel data from an Image object to a BufferedImage by converting
   * each pixel's RGB values and setting them.
   *
   * @param img         The Image object whose pixels are to be transferred.
   * @param bufferImage The BufferedImage to which pixel data will be written.
   */
  public static void transformHelperImageToBuffer(Image img, BufferedImage bufferImage) {
    for (int i = 0; i < img.getHeight(); i++) {
      for (int j = 0; j < img.getWidth(); j++) {
        int[] pixel = img.getPixel(i, j);
        int rgb = (pixel[0] << 16) | (pixel[1] << 8) | pixel[2];
        bufferImage.setRGB(j, i, rgb);
      }
    }
  }
}
