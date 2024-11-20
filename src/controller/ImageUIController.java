package controller;

import java.util.Stack;
import model.ImgModel;
import model.image.Image;
import utils.ImageIOHelper;
import utils.ImageTransformer;
import view.ImgView;

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
        blurImage(currentImage);
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
      default:
        System.out.println("Action not recognized.");
        break;
    }
  }


  private void loadImage(String path) {
    try {
      Image image = ImageIOHelper.loadImage(path);
      imageStack.push(image);
      imageView.updateImage(ImageTransformer.transformImageToBufferImage(image));
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
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res));
  }

  private void flipHorizontal(Image image) {
    Image res = imageModel.flipHorizontal(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res));
  }

  private void blurImage(Image image) {
    Image res = imageModel.blur(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res));
  }

  private void applySepia(Image image) {
    Image res = imageModel.sepia(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res));
  }

  private void convertToGrayscale(Image image) {
    Image res = imageModel.toGreyscale(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res));
  }

  private void sharpenImage(Image image) {
    Image res = imageModel.sharpen(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res));
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
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res));
  }

  private void colorCorrection(Image image) {
    Image res = imageModel.correctColor(image);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res));
  }

  private void levelAdjustment(Image image, int black, int mid, int white) {
    Image res = imageModel.adjustLevels(image, black, mid, white);
    imageStack.push(res);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res));
  }

  private void histogram(Image image) {
    Image res = imageModel.histogram(image);
    imageView.updateImage(ImageTransformer.transformImageToBufferImage(res));
  }

  public int[] getRedHistogram(Image image) {
    return imageModel.histogramSeparateColor(image, "red");
  }

  public int[] getGreenHistogram(Image image) {
    return imageModel.histogramSeparateColor(image, "green");

  }

  public int[] getBlueHistogram(Image image) {
    return imageModel.histogramSeparateColor(image, "blue");

  }

  private Image getCurrentImage() {
    if (imageStack.empty()) {
      return null;
    }
    return imageStack.peek();
  }

}
