package controller;


/**
 * ImgUIController interface defines the operations that can be performed on imagesUIController.
 */
public interface ImgUIController {

  /**
   * Show the GUI panel.
   */
  void runGUI();

  /**
   * Handle the action from view and update the new image to view.
   *
   * @param action     the action to call
   * @param parameters the extra parameters
   */
  void handleImageAction(String action, String... parameters);
}
