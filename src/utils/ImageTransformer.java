package utils;

import java.awt.image.BufferedImage;
import model.image.Image;
import model.image.SimpleImage;

public class ImageTransformer {


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

  public static BufferedImage transformImageToBufferImage(Image img) {
    BufferedImage bufferImage = new BufferedImage(img.getWidth(), img.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    transformHelperImageToBuffer(img,bufferImage);
    return bufferImage;
  }

  public static void transformHelperImageToBuffer(Image img,BufferedImage bufferImage){
    for (int i = 0; i < img.getHeight(); i++) {
      for (int j = 0; j < img.getWidth(); j++) {
        int[] pixel = img.getPixel(i, j);
        int rgb = (pixel[0] << 16) | (pixel[1] << 8) | pixel[2];
        bufferImage.setRGB(j, i, rgb);
      }
    }
  }
}
