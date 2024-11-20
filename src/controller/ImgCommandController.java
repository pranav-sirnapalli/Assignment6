package controller;

/**
 * This interface defines an image controller.
 */
public interface ImgCommandController {
  void runCommand(Readable inputSource);

  void runScript(String filePath);
}
