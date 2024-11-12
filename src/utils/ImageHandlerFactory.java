package utils;

/**
 * ImageHandlerFactory is a factory class that provides methods for creating instances of
 * ImageHandler based on the file extension of a given file path.
 */
public class ImageHandlerFactory {

  /**
   * Gets an appropriate ImageHandler instance based on the file extension.
   *
   * @param filePath the path of the file for which an ImageHandler is required
   * @return an instance of ImageHandler suitable for the file format
   * @throws UnsupportedOperationException if the file format is unsupported
   */
  public static ImageHandler getHandler(String filePath) {
    String extension = getFileExtension(filePath);

    switch (extension.toLowerCase()) {
      case "jpg":
      case "png":
        return new StandardImageHandler();
      case "ppm":
        return new PPMImageHandler();
      default:
        throw new UnsupportedOperationException("Unsupported file format: " + extension);
    }
  }

  /**
   * Extracts the file extension from a given file path.
   *
   * @param filePath the path of the file
   * @return the file extension, or an empty string if no extension is found
   */
  private static String getFileExtension(String filePath) {
    int lastIndexOfDot = filePath.lastIndexOf('.');
    if (lastIndexOfDot == -1) {
      // No extension found
      return "";
    }
    return filePath.substring(lastIndexOfDot + 1);
  }
}
