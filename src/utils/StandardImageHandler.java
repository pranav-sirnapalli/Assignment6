package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.image.Image;

/**
 * StandardImageHandler is a concrete implementation of the ImageHandler interface specifically for
 * handling JPG and PNG images.
 */
public class StandardImageHandler implements ImageHandler {

  @Override
  public Image loadImage(String filePath) {
    try {
      BufferedImage bufferedImage = ImageIO.read(new File(filePath));
      return ImageTransformer.transformBufferImageToImage(bufferedImage);
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
      ImageTransformer.transformHelperImageToBuffer(img, bufferedImage);
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
