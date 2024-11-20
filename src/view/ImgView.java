package view;

import controller.ImageController;
import java.awt.image.BufferedImage;

public interface ImgView {

  void setController(ImageController controller);

  void showGUI();

  void updateImage(BufferedImage image);
}
