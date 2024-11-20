package controller;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Stack;
import model.ImgModel;
import model.image.Image;
import utils.ImageIOHelper;
import utils.ImageTransformer;
import view.ImgView;

/**
 * The ImageUIController class handles user commands for performing various image processing
 * operations. It interacts with the ImageModel to load, save, and apply transformations to images.
 * It supports GUI to executing those image manipulation function.
 */
public class ImageUIController implements ImgUIController {

  private ImgModel imageModel;
  private ImgView imageView;
  private Stack<Image> imageStack = new Stack<>();

  /**
   * Constructor of ImageController.
   */
  public ImageUIController(ImgModel imageModel, ImgView imageView) {
    this.imageModel = imageModel;
    this.imageView = imageView;
  }


  @Override
  public void runGUI() {
    imageView.showGUI();
  }


  @Override
  public void handleImageAction(String action, String... parameters) {
    // Retrieve current image
    Image currentImage = getCurrentImage();

    switch (action) {
      case "Load Image":
        loadImage(parameters[0]);
        break;
      case "Save Image":
        saveImage(currentImage, parameters[0]);
        break;
      case "Compression":
        compressImage(currentImage, Integer.parseInt(parameters[0]));
        break;
      case "Downscale":
        downScaleImage(currentImage, Integer.parseInt(parameters[0]),
            Integer.parseInt(parameters[1]));
        break;
      case "Split-view":
        // ## Need to change
        splitView(Integer.parseInt(parameters[0]));
        break;
      case "Level Adjustment":
        int black = Integer.parseInt(parameters[0]);
        int mid = Integer.parseInt(parameters[1]);
        int white = Integer.parseInt(parameters[2]);
        levelAdjustment(currentImage, black, mid, white);
        break;
      case "Blur":
        blurImage(currentImage);
        break;
      case "Sharpen":
        sharpenImage(currentImage);
        break;
      case "Sepia":
        applySepia(currentImage);
        break;
      case "Flip Vertical":
        flipVertical(currentImage);
        break;
      case "Flip Horizontal":
        flipHorizontal(currentImage);
        break;
      case "Greyscale":
        convertToGrayscale(currentImage);
        break;
      case "Color Correction":
        colorCorrection(currentImage);
        break;
      case "Component-value":
        componentValue(currentImage, parameters[0]);
        break;
      default:
        System.out.println("Action not recognized.");
        break;
    }
  }


  private void loadImage(String path) {
    try {
      Image image = ImageIOHelper.loadImage(path);
      imageStack.push(image);
      imageView.updateImage(ImageTransformer.transformImageToBufferImage(image), histogram(image));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }


  private void saveImage(Image image, String path) {
    ImageIOHelper.saveImage(path, image);
  }

  private void flipVertical(Image image) {
    Image res = imageModel.flipVertical(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res), histogram(res));
  }

  private void flipHorizontal(Image image) {
    Image res = imageModel.flipHorizontal(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res), histogram(res));
  }

  private void blurImage(Image image) {
    Image res = imageModel.blur(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res), histogram(res));
  }

  private void applySepia(Image image) {
    Image res = imageModel.sepia(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res), histogram(res));
  }

  private void convertToGrayscale(Image image) {
    Image res = imageModel.toGreyscale(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res), histogram(res));
  }

  private void sharpenImage(Image image) {
    Image res = imageModel.sharpen(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res), histogram(res));
  }

  private void compressImage(Image image, int percentage) {
    Image res = imageModel.compressImage(image, percentage);
    imageStack.push(res);
  }

  private void splitView(int splitRatio) {
    Image origin = imageStack.pop();
    Image changed = imageStack.peek();
    imageStack.push(origin);
    Image res = imageModel.splitView(origin, changed, splitRatio);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res), histogram(origin));
  }

  private void colorCorrection(Image image) {
    Image res = imageModel.correctColor(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res), histogram(res));
  }

  private void levelAdjustment(Image image, int black, int mid, int white) {
    Image res = imageModel.adjustLevels(image, black, mid, white);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res), histogram(res));
  }

  private BufferedImage histogram(Image image) {
    Image res = imageModel.histogram(image);
    return ImageTransformer.transformImageToBufferImage(res);
  }

  private Image getCurrentImage() {
    if (imageStack.empty()) {
      return null;
    }
    return imageStack.peek();
  }

  private void componentValue(Image image, String color) {
    Image res = imageModel.splitImage(image);
    if (Objects.equals(color, "Red")) {
      res = imageModel.redComponent(image);
    } else if (Objects.equals(color, "Blue")) {
      res = imageModel.blueComponent(image);
    } else if (Objects.equals(color, "Green")) {
      res = imageModel.greenComponent(image);
    }
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res), histogram(res));
  }

  private void downScaleImage(Image image, int newWidth, int newHeight) {
    Image res = imageModel.downscale(image, newWidth, newHeight);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res), histogram(res));
  }


}
