package utils;

import model.image.Image;


/**
 * ImageHandler is an interface that defines methods for loading and saving images.
 */
public interface ImageHandler {

  /**
   * Loads an image from the specified file path.
   *
   * @param filePath the path of the image file to be loaded
   * @return the loaded Image object
   */
  Image loadImage(String filePath);

  /**
   * Saves the specified image to the given file path.
   *
   * @param filePath the path where the image should be saved
   * @param img      the Image object to be saved
   */
  void saveImage(String filePath, Image img);
}
