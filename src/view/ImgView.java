package view;

import controller.ImgUIController;
import java.awt.image.BufferedImage;

public interface ImgView {

  void setController(ImgUIController controller);

  void showGUI();

  void updateImage(BufferedImage image,BufferedImage histogram);
}
