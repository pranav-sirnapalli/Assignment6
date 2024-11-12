package model.image;

/**
 * Image class which include the width, height and a 3d array to hold pixels.
 */
public class SimpleImage implements Image {

  protected int width;
  protected int height;
  protected int[][][] pixels;

  /**
   * Constructor of the image.
   *
   * @param width  width of the image.
   * @param height height of the image.
   */
  public SimpleImage(int width, int height) {
    this.width = width;
    this.height = height;
    this.pixels = new int[height][width][3];
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int[] getPixel(int row, int col) {
    return pixels[row][col];
  }

  @Override
  public void setPixel(int row, int col, int[] rgb) {
    pixels[row][col] = rgb;
  }

  @Override
  public void setPixel(int row ,int col, int channel, int value) {
    pixels[row][col][channel] = value;
  }
}
