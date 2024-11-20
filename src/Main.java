import controller.ImageScriptController;
import controller.ImageUIController;
import controller.ImgUIController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import model.ImageModel;
import view.ImageView;
import view.ImgView;

/**
 * The entrance of the program.
 */
public class Main {

  /**
   * The entrance of the program.
   *
   * @param args the arguments to use.
   */
  public static void main(String[] args) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    ImageModel imageModel = new ImageModel();
    ImgView imageView = new ImageView();

    ImgUIController uiController = new ImageUIController(imageModel, imageView);
    ImageScriptController scriptController = new ImageScriptController(imageModel);

    imageView.setController(uiController);
    //Check if arguments were passed
    if (args.length > 0) {
      if (args[0].equals("-file")) {
        scriptController.runScript(args[1]);
      } else if (args[0].equals("-text")) {
        scriptController.runCommand(reader);
      } else{
        System.out.println("Help MenuXD:"
            + "\n1.To run script:        java -jar <jarFileName>.jar -file <filePath>"
            + "\n2.To run command line:  java -jar <jarFileName>.jar -text"
            + "\n3.To run GUI:           java -jar <jarFileName>.jar (without anything)");
      }
    } else {
      uiController.runGUI();
    }

  }
}
