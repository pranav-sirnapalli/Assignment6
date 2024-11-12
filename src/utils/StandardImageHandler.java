package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.image.Image;
import model.image.SimpleImage;

/**
 * StandardImageHandler is a concrete implementation of the ImageHandler interface specifically for
 * handling JPG and PNG images.
 */
public class StandardImageHandler implements ImageHandler {

  @Override
  public Image loadImage(String filePath) {
    try {
      BufferedImage bufferedImage = ImageIO.read(new File(filePath));
      int width = bufferedImage.getWidth();
      int height = bufferedImage.getHeight();
      Image img = new SimpleImage(width, height);
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          int rgb = bufferedImage.getRGB(j, i);
          int[] pixel = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
          img.setPixel(i, j, pixel);
        }
      }
      return img;
    } catch (IOException e) {
      System.out.println("Error loading image: " + e.getMessage());
      return null;
    }
  }

  @Override
  public void saveImage(String filePath, Image img) {
    try {
      BufferedImage bufferedImage = new BufferedImage(img.getWidth(), img.getHeight(),
          BufferedImage.TYPE_INT_RGB);
      for (int i = 0; i < img.getHeight(); i++) {
        for (int j = 0; j < img.getWidth(); j++) {
          int[] pixel = img.getPixel(i, j);
          int rgb = (pixel[0] << 16) | (pixel[1] << 8) | pixel[2];
          bufferedImage.setRGB(j, i, rgb);
        }
      }
      String extension = getFileExtension(filePath);
      ImageIO.write(bufferedImage, extension, new File(filePath));
    } catch (IOException e) {
      System.out.println("Error saving image: " + e.getMessage());
    }
  }

  private String getFileExtension(String filePath) {
    int lastIndexOfDot = filePath.lastIndexOf('.');
    return filePath.substring(lastIndexOfDot + 1);
  }
}
