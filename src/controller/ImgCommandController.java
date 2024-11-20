package controller;

import java.util.Scanner;

/**
 * This interface defines an image controller.
 */
public interface ImgCommandController extends ImgController {
  void runCommand(Scanner scanner);

  void runScript(String filePath);
}
