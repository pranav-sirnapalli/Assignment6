package utils;

import model.image.Image;

/**
 * ImageIOHelper is helper class to help ImageModel to load or save image.
 */
public class ImageIOHelper {

  /**
   * Loads an image from the specified file path.
   *
   * @param filePath the file path of the image to be loaded
   * @return the loaded Image object
   */
  public static Image loadImage(String filePath) {
    ImageHandler handler = ImageHandlerFactory.getHandler(filePath);
    return handler.loadImage(filePath);
  }

  /**
   * Saves an image to the specified file path.
   *
   * @param filePath  the destination file path where the image will be saved
   * @param img the Image object to be saved
   */
  public static void saveImage(String filePath, Image img) {
    ImageHandler handler = ImageHandlerFactory.getHandler(filePath);
    handler.saveImage(filePath, img);
  }
}
