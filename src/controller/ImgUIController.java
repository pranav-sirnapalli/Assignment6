package controller;

public interface ImgUIController extends ImgController {

  void runGUI();

  void handleImageAction(String action,String... parameters);
}
