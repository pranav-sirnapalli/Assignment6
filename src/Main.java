import controller.ImageController;
import java.util.Scanner;
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
    Scanner scanner = new Scanner(System.in);
    ImageModel imageModel = new ImageModel();
    ImgView imageView = new ImageView();
    ImageController controller = new ImageController(imageModel, imageView);
    imageView.setController(controller);

    //Check if arguments were passed
    if (args.length > 0) {
      if (args[0].equals("-file")) {
        controller.runScript(args[1]);
      } else if (args[0].equals("-text")) {
        controller.runCommandLine(scanner);
      } else{
        System.out.println("Help Menu:"
            + "\n1.To run script:        java -jar <jarFileName>.jar -file <filePath>"
            + "\n2.To run command line:  java -jar <jarFileName>.jar -text"
            + "\n3.To run GUI:           java -jar <jarFileName>.jar (without anything)");
      }
    } else {
      controller.runGUI();
    }


  }
}
