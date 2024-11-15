import controller.ImageController;
import java.util.Scanner;
import model.ImageModel;
import view.ImageView;

/**
 * The entrance of the program.
 */
public class Main {

  /**
   * The entrance of the program.
   * @param args the arguments to use.
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    ImageModel imageModel = new ImageModel();
    ImageView imageView = new ImageView();
    ImageController controller = new ImageController(imageModel, imageView);
    imageView.setController(controller);
  }
}
