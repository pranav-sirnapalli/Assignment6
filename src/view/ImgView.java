package view;

import controller.ImgUIController;
import java.awt.image.BufferedImage;

/**
 * ImgView interface defines the operations that can be performed on images GUI.
 */
public interface ImgView {

  /**
   * Bind the controller to this view.
   *
   * @param controller an implementation of ImgUIController interface.
   */
  void setController(ImgUIController controller);

  /**
   * Show the GUI.
   */
  void showGUI();

  /**
   * Update image and histogram in the GUI panel.
   *
   * @param image     the image to show.
   * @param histogram the histogram of the image.
   */
  void updateImage(BufferedImage image, BufferedImage histogram);
}
