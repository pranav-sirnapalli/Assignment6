package controller;


/**
 * ImgCommandController interface defines the operations that can be performed on
 * imagesCommandController.
 */
public interface ImgCommandController {

  /**
   * Run program on the terminal or command line.
   *
   * @param inputSource the input source.
   */
  void runCommand(Readable inputSource);

  /**
   * Reads and executes commands from a script file. Each line of the file is treated as a separate
   * command.
   *
   * @param filePath The file path to the script.
   */
  void runScript(String filePath);
}
