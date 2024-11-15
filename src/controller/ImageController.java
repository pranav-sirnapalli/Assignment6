package controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.imageio.ImageIO;
import model.ImageModel;
import model.ImgModel;
import model.image.Image;
import utils.ImageIOHelper;
import utils.ImageTransformer;
import view.ImageView;

/**
 * The ImageController class handles user commands for performing various image processing
 * operations. It interacts with the ImageModel to load, save, and apply transformations to images.
 * It supports both interactive commands and the execution of a script containing multiple
 * commands.
 */
public class ImageController {

  private ImgModel imageModel;
  private ImageView imageView;
  private Map<String, Image> images = new HashMap<>();


  /**
   * Empty constructor.
   */
  public ImageController() {
    this.imageModel = new ImageModel();
  }


  /**
   * Constructor of ImageController.
   */
  public ImageController(ImageModel imageModel, ImageView imageView) {
    this.imageModel = imageModel;
    this.imageView = imageView;
  }

  /**
   * Run the Controller logic.
   *
   * @param scanner scanner of run.
   */
  public void run(Scanner scanner) {
    //System.out.println("Current working directory: " + System.getProperty("user.dir"));
    boolean running = true;
    while (running) {
      System.out.print("Enter command: ");
      String command = scanner.nextLine();

      if (command.equalsIgnoreCase("exit")) {
        System.out.println("Exiting program...");
        running = false;
      } else {
        this.processCommand(command);
      }
    }

  }

  /**
   * Processes individual commands entered by the user. Each command corresponds to an image
   * operation such as loading, saving, flipping, or applying filters.
   *
   * @param command The command entered by the user.
   */
  public void processCommand(String command) {
    // Skip empty lines or comments
    if (command.isEmpty() || command.startsWith("#")) {
      return;
    }
    String[] tokens = command.split(" ");
    switch (tokens[0]) {
      case "load":
        if (tokens.length != 3) {
          System.out.println("Usage: load <input_file_path> <reference_name>");
        } else {
          Image image = ImageIOHelper.loadImage(tokens[1]);
          images.put(tokens[2], image);
        }
        break;
      case "save":
        if (tokens.length != 3) {
          System.out.println("Usage: save <save_path> <output_name>");
        } else {
          ImageIOHelper.saveImage(tokens[1], images.get(tokens[2]));
        }
        break;
      case "horizontal-flip":
        if (tokens.length != 3) {
          System.out.println("Usage: horizontal-flip <reference_name> <output_name>");
        } else {
          Image img = imageModel.flipHorizontal(images.get(tokens[1]));
          images.put(tokens[2], img);
        }
        break;
      case "vertical-flip":
        if (tokens.length != 3) {
          System.out.println("Usage: horizontal-flip <reference_name> <output_name>");
        } else {
          Image img = imageModel.flipVertical(images.get(tokens[1]));
          images.put(tokens[2], img);
        }
        break;
      case "brighten":
        int increment = Integer.parseInt(tokens[3]);
        Image img = imageModel.brighten(images.get(tokens[1]), increment);
        images.put(tokens[2], img);
        break;
      case "rgb-split":
        Image r = imageModel.redComponent(images.get(tokens[1]));
        Image g = imageModel.greenComponent(images.get(tokens[1]));
        Image b = imageModel.blueComponent(images.get(tokens[1]));
        images.put(tokens[2], r);
        images.put(tokens[3], g);
        images.put(tokens[4], b);
        break;
      case "rgb-combine":
        Image combined = imageModel.combineImage(images.get(tokens[2]), images.get(tokens[3]),
            images.get(tokens[4]));
        images.put(tokens[1], combined);
        break;
      case "red-component":
        Image redComponent = imageModel.redComponent(images.get(tokens[1]));
        images.put(tokens[2], redComponent);
        break;
      case "green-component":
        Image greenComponent = imageModel.greenComponent(images.get(tokens[1]));
        images.put(tokens[2], greenComponent);
        break;
      case "blue-component":
        Image blueComponent = imageModel.blueComponent(images.get(tokens[1]));
        images.put(tokens[2], blueComponent);
        break;
      case "value-component":
        Image value = imageModel.value(images.get(tokens[1]));
        images.put(tokens[2], value);
        break;
      case "luma-component":
        Image luma = imageModel.luma(images.get(tokens[1]));
        images.put(tokens[2], luma);
        break;
      case "intensity-component":
        Image intensity = imageModel.intensity(images.get(tokens[1]));
        images.put(tokens[2], intensity);
        break;
      case "blur":
        Image blur = imageModel.blur(images.get(tokens[1]));
        if (tokens.length == 5) {
          int splitPercentage = Integer.parseInt(tokens[4]);
          blur = imageModel.splitView(images.get(tokens[1]), blur, splitPercentage);
        }
        images.put(tokens[2], blur);
        break;
      case "sepia":
        Image sepia = imageModel.sepia(images.get(tokens[1]));
        if (tokens.length == 5) {
          int splitPercentage = Integer.parseInt(tokens[4]);
          sepia = imageModel.splitView(images.get(tokens[1]), sepia, splitPercentage);
        }
        images.put(tokens[2], sepia);
        break;
      case "sharpen":
        Image sharpen = imageModel.sharpen(images.get(tokens[1]));
        if (tokens.length == 5) {
          int splitPercentage = Integer.parseInt(tokens[4]);
          sharpen = imageModel.splitView(images.get(tokens[1]), sharpen, splitPercentage);
        }
        images.put(tokens[2], sharpen);
        break;
      case "greyScale":
        Image greyScale = imageModel.toGreyscale(images.get(tokens[1]));
        if (tokens.length == 5) {
          int splitPercentage = Integer.parseInt(tokens[4]);
          greyScale = imageModel.splitView(images.get(tokens[1]), greyScale, splitPercentage);
        }
        images.put(tokens[2], greyScale);
        break;
      case "color-correct":
        Image correctedImage = imageModel.correctColor(images.get(tokens[1]));
        if (tokens.length == 5) {
          int splitPercentage = Integer.parseInt(tokens[4]);
          correctedImage = imageModel.splitView(images.get(tokens[1]), correctedImage,
              splitPercentage);
        }
        images.put(tokens[2], correctedImage);
        break;
      case "levels-adjust":
        int black = Integer.parseInt(tokens[1]);
        int mid = Integer.parseInt(tokens[2]);
        int white = Integer.parseInt(tokens[3]);
        Image adjustedImage = imageModel.adjustLevels(images.get(tokens[4]), black, mid, white);
        if (tokens.length == 5) {
          int splitPercentage = Integer.parseInt(tokens[4]);
          adjustedImage = imageModel.splitView(images.get(tokens[1]), adjustedImage,
              splitPercentage);
        }
        images.put(tokens[5], adjustedImage);
        break;
      case "histogram":
        Image histogram = imageModel.histogram(images.get(tokens[1]));
        images.put(tokens[2], histogram);
        break;
      case "compress":
        int percentage = Integer.parseInt(tokens[1]);
        Image compressed = imageModel.compressImage(images.get(tokens[2]), percentage);
        images.put(tokens[3], compressed);
        break;
      case "run":
        runScript(tokens[1]);
        break;
      default:
        System.out.println("Invalid command!");
        break;
    }
  }


  /**
   * Reads and executes commands from a script file. Each line of the file is treated as a separate
   * command.
   *
   * @param scriptPath The file path to the script.
   */
  private void runScript(String scriptPath) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(scriptPath));
      String line;
      while ((line = reader.readLine()) != null) {
        this.processCommand(line);
      }
      reader.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public BufferedImage loadImage(String path) {
    try {
      Image image = ImageIOHelper.loadImage(path);
      images.put(path, image);
      return ImageIO.read(new File(path));
    }catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public void saveImage(BufferedImage image, String path, String extension) {
    try {
      ImageIO.write(image, extension, new File(path));
    }catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public BufferedImage flipVertical(Image image){
    return ImageTransformer.transformImageToBufferImage(imageModel.flipVertical(image));
  }

  public BufferedImage flipHorizontal(Image image){
    return ImageTransformer.transformImageToBufferImage(imageModel.flipHorizontal(image));
  }

  public BufferedImage blurImage(Image image){
    return ImageTransformer.transformImageToBufferImage(imageModel.blur(image));
  }

  public BufferedImage applySepia(Image image){
    return ImageTransformer.transformImageToBufferImage(imageModel.sepia(image));
  }

  public BufferedImage convertToGrayscale(Image image){
    return ImageTransformer.transformImageToBufferImage(imageModel.toGreyscale(image));
  }

  public BufferedImage sharpenImage(Image image){
    return ImageTransformer.transformImageToBufferImage(imageModel.sharpen(image));
  }

  public BufferedImage compressImage(BufferedImage image, int percentage){
//    imageModel.compressImage(image, percentage);
    return null;
  }

  public int[] getRedHistogram(Image image) {
    return imageModel.histogramSeparateColor(image,"red");
  }
  public int[] getGreenHistogram(Image image){
    return imageModel.histogramSeparateColor(image,"green");

  }
  public int[]  getBlueHistogram(Image image){
    return imageModel.histogramSeparateColor(image,"blue");

  }


}
