package model.image;

/**
 * This interface defines all the operations that all image must implement.
 */
public interface Image {

  /**
   * Return the width to image.
   * @return width to image
   */
  int getWidth();

  /**
   * Return the height to image.
   * @return height to image
   */
  int getHeight();

  /**
   * Return int array of RGB value of the given pixel by x,y value.
   * @param x x-coordinate
   * @param y y-coordinate
   * @return int array of RGB value
   */
  int[] getPixel(int x, int y);

  /**
   * Set the RGB value for the given pixel.
   * @param x x-coordinate
   * @param y y-coordinate
   * @param rgb rgb value
   */
  void setPixel(int x, int y, int[] rgb);

  /**
   * Set the RGB value for the given pixel.
   * @param x x-coordinate
   * @param y y-coordinate
   * @param channel channel
   * @param value RGB value
   */
  void setPixel(int x, int y, int channel, int value);

}
