package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import model.image.Image;
import model.image.RGBImage;
import model.image.SimpleImage;
import utils.compression.HaarTransform;

/**
 * ImageModel implemented ImgModel which provides various image manipulation methods such as
 * loading, saving, flipping, and applying filters like blur, sepia, and sharpen.
 */
public class ImageModel implements ImgModel {

  @Override
  public RGBImage splitImage(Image image) {
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    return new RGBImage(image.getWidth(), image.getHeight());
  }

  @Override
  public Image combineImage(Image red, Image green, Image blue) {
    if (red == null || green == null || blue == null) {
      throw new IllegalArgumentException("None of the color images can be null");
    }
    Image result = new SimpleImage(red.getWidth(), red.getHeight());
    for (int row = 0; row < red.getHeight(); row++) {
      for (int col = 0; col < red.getWidth(); col++) {
        int r = red.getPixel(row, col)[0];
        int g = green.getPixel(row, col)[1];
        int b = blue.getPixel(row, col)[2];
        int[] rgb = {r, g, b};
        result.setPixel(row, col, rgb);
      }
    }
    return result;
  }

  @Override
  public Image flipHorizontal(Image img) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    Image result = new SimpleImage(img.getWidth(), img.getHeight());
    for (int row = 0; row < img.getHeight(); row++) {
      for (int col = 0; col < img.getWidth(); col++) {
        result.setPixel(row, col, img.getPixel(row, img.getWidth() - 1 - col));
      }
    }
    return result;
  }

  @Override
  public Image flipVertical(Image img) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    Image result = new SimpleImage(img.getWidth(), img.getHeight());
    for (int row = 0; row < img.getHeight(); row++) {
      for (int col = 0; col < img.getWidth(); col++) {
        result.setPixel(img.getHeight() - 1 - row, col, img.getPixel(row, col));
      }
    }
    return result;
  }

  @Override
  public Image brighten(Image img, int increment) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    Image result = new SimpleImage(img.getWidth(), img.getHeight());
    for (int row = 0; row < img.getHeight(); row++) {
      for (int col = 0; col < img.getWidth(); col++) {
        int[] pixel = img.getPixel(row, col);
        int[] newPixel = new int[3];
        for (int i = 0; i < 3; i++) {
          newPixel[i] = Math.min(255, Math.max(0, pixel[i] + increment));
        }
        result.setPixel(row, col, newPixel);
      }
    }
    return result;
  }

  @Override
  public Image toGreyscale(Image img) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    Image result = new SimpleImage(img.getWidth(), img.getHeight());
    for (int row = 0; row < img.getHeight(); row++) {
      for (int col = 0; col < img.getWidth(); col++) {
        int[] pixel = img.getPixel(row, col);
        int grey = (int) (pixel[0] * 0.299 + pixel[1] * 0.587 + pixel[2] * 0.114);
        int[] newPixel = {grey, grey, grey};
        result.setPixel(row, col, newPixel);
      }
    }
    return result;
  }


  /**
   * Helper function for blur and sharpen.
   *
   * @param img        the image to apply the filter
   * @param kernel     the filter kernel
   * @param kernelSize the size of the kernel
   * @return the processed image
   */
  private Image filterImageByKernel(Image img, double[][] kernel, int kernelSize) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    Image result = new SimpleImage(img.getWidth(), img.getHeight());
    int halfKernel = kernelSize / 2;

    for (int row = 0; row < img.getHeight(); row++) {
      for (int col = 0; col < img.getWidth(); col++) {
        int[] newPixel = new int[3];

        for (int i = 0; i < 3; i++) {
          int pixelValue = 0;

          for (int ki = 0; ki < kernelSize; ki++) {
            for (int kj = 0; kj < kernelSize; kj++) {
              int pixelRow = Math.min(Math.max(row + ki - halfKernel, 0), img.getHeight() - 1);
              int pixelCol = Math.min(Math.max(col + kj - halfKernel, 0), img.getWidth() - 1);

              pixelValue += (int) (img.getPixel(pixelRow, pixelCol)[i] * kernel[ki][kj]);
            }
          }

          newPixel[i] = Math.min(Math.max(pixelValue, 0), 255);
        }

        result.setPixel(row, col, newPixel);
      }
    }
    return result;
  }

  @Override
  public Image blur(Image img) {
    double[][] kernel = {{0.0625, 0.125, 0.0625}, {0.125, 0.25, 0.125}, {0.0625, 0.125, 0.0625}};
    return filterImageByKernel(img, kernel, 3);
  }

  @Override
  public Image sharpen(Image img) {
    double[][] kernel = {{-0.125, -0.125, -0.125, -0.125, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125}, {-0.125, 0.25, 1, 0.25, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125}, {-0.125, -0.125, -0.125, -0.125, -0.125}};
    return filterImageByKernel(img, kernel, 5);
  }

  /**
   * Helper function for sepia and luma.
   *
   * @param img    the image to apply this effect
   * @param filter the filter
   * @return the processed image
   */
  private Image sepiaOrLuma(Image img, double[][] filter) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    Image result = new SimpleImage(img.getWidth(), img.getHeight());

    for (int row = 0; row < img.getHeight(); row++) {
      for (int col = 0; col < img.getWidth(); col++) {
        int[] rgb = img.getPixel(row, col);
        int[] newColor = new int[3];
        newColor[0] = (int) Math.min(
            Math.max((filter[0][0] * rgb[0] + filter[0][1] * rgb[1] + filter[0][2] * rgb[2]), 0),
            255);
        newColor[1] = (int) Math.min(
            Math.max((filter[1][0] * rgb[0] + filter[1][1] * rgb[1] + filter[1][2] * rgb[2]), 0),
            255);
        newColor[2] = (int) Math.min(
            Math.max((filter[2][0] * rgb[0] + filter[2][1] * rgb[1] + filter[2][2] * rgb[2]), 0),
            255);
        result.setPixel(row, col, newColor);
      }
    }

    return result;
  }

  @Override
  public Image luma(Image img) {
    return sepiaOrLuma(img, new double[][]{
        {0.299, 0.587, 0.114},
        {0.299, 0.587, 0.114},
        {0.299, 0.587, 0.114}
    });
  }

  @Override
  public Image sepia(Image img) {
    return sepiaOrLuma(img, new double[][]{
        {0.393, 0.769, 0.189},
        {0.349, 0.686, 0.168},
        {0.272, 0.534, 0.131}
    });
  }

  @Override
  public Image value(Image img) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    Image result = new SimpleImage(img.getWidth(), img.getHeight());

    for (int row = 0; row < img.getHeight(); row++) {
      for (int col = 0; col < img.getWidth(); col++) {
        int[] rgb = img.getPixel(row, col);
        int value = Math.max(rgb[0], Math.max(rgb[1], rgb[2]));

        int[] grayscale = {value, value, value};
        result.setPixel(row, col, grayscale);
      }
    }

    return result;
  }

  @Override
  public Image intensity(Image img) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    Image result = new SimpleImage(img.getWidth(), img.getHeight());

    for (int row = 0; row < img.getHeight(); row++) {
      for (int col = 0; col < img.getWidth(); col++) {
        int[] rgb = img.getPixel(row, col);
        int intensity = (rgb[0] + rgb[1] + rgb[2]) / 3;

        int[] grayscale = {intensity, intensity, intensity};
        result.setPixel(row, col, grayscale);
      }
    }

    return result;
  }

  @Override
  public Image redComponent(Image img) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    RGBImage rgbImage = new RGBImage(img.getWidth(), img.getHeight());
    return rgbImage.redComponent(img);
  }

  @Override
  public Image greenComponent(Image img) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    RGBImage rgbImage = new RGBImage(img.getWidth(), img.getHeight());
    return rgbImage.greenComponent(img);
  }

  @Override
  public Image blueComponent(Image img) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    RGBImage rgbImage = new RGBImage(img.getWidth(), img.getHeight());
    return rgbImage.blueComponent(img);
  }

  @Override
  public Image splitView(Image image, Image processedImage, int splitPercentage) {
    int splitPoint = (image.getWidth() * splitPercentage) / 100;
    Image result = new SimpleImage(image.getWidth(), image.getHeight());

    for (int hor = 0; hor < image.getHeight(); hor++) {
      for (int ver = 0; ver < image.getWidth(); ver++) {
        if (ver < splitPoint) {
          result.setPixel(hor, ver, processedImage.getPixel(hor, ver));
        } else {
          result.setPixel(hor, ver, image.getPixel(hor, ver));
        }
      }
    }
    return result;
  }

  @Override
  public Image correctColor(Image image) {
    // Analyze histogram to find meaningful peaks for each channel
    int[] rHist = new int[256];
    int[] gHist = new int[256];
    int[] bHist = new int[256];

    for (int x = 0; x < image.getHeight(); x++) {
      for (int y = 0; y < image.getWidth(); y++) {
        int[] color = image.getPixel(x, y);
        rHist[color[0]]++;
        gHist[color[1]]++;
        bHist[color[2]]++;
      }
    }

    // Ignore peaks at histogram extremities and find meaningful peaks
    int rPeak = findPeak(rHist, 10, 245);
    int gPeak = findPeak(gHist, 10, 245);
    int bPeak = findPeak(bHist, 10, 245);

    int avgPeak = (rPeak + gPeak + bPeak) / 3;

    // Offset channels to align peaks to the average peak
    BufferedImage correctedImage = new BufferedImage(image.getWidth(), image.getHeight(),
        BufferedImage.TYPE_INT_RGB);

    for (int x = 0; x < image.getHeight(); x++) {
      for (int y = 0; y < image.getWidth(); y++) {
        int[] color = image.getPixel(x, y);
        int red = clamp(color[0] + (avgPeak - rPeak));
        int green = clamp(color[1] + (avgPeak - gPeak));
        int blue = clamp(color[2] + (avgPeak - bPeak));
        correctedImage.setRGB(y, x, new Color(red, green, blue).getRGB());
      }
    }
    return transformBufferImageToImage(correctedImage);
  }

  private int findPeak(int[] hist, int min, int max) {
    int peak = min;
    for (int i = min; i <= max; i++) {
      if (hist[i] > hist[peak]) {
        peak = i;
      }
    }
    return peak;
  }

  private int clamp(int value) {
    return Math.max(0, Math.min(255, value));
  }

  @Override
  public Image histogram(Image image) {
    if (image == null) {
      throw new IllegalArgumentException("Input image cannot be null");
    }
    int cur_width = 256;
    int cur_height = 256;
    BufferedImage cur_histImage = new BufferedImage(cur_width, cur_height,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = cur_histImage.createGraphics();

    // used to calc histograms for each channel
    int[] rHist = new int[256];
    int[] gHist = new int[256];
    int[] bHist = new int[256];

    for (int x = 0; x < image.getHeight(); x++) {
      for (int y = 0; y < image.getWidth(); y++) {
        int[] color = image.getPixel(x, y);
        rHist[color[0]]++;
        gHist[color[1]]++;
        bHist[color[2]]++;
      }
    }

    // Scale histogram values to fit within the 256x256 image
    int max = 0;
    for (int i = 0; i < 256; i++) {
      max = Math.max(max, Math.max(rHist[i], Math.max(gHist[i], bHist[i])));
    }
    for (int i = 0; i < 256; i++) {
      rHist[i] = (rHist[i] * cur_height) / max;
      gHist[i] = (gHist[i] * cur_height) / max;
      bHist[i] = (bHist[i] * cur_height) / max;
    }
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, cur_width, cur_height);

    // Draw horizontal lines for reference
    graphics.setColor(Color.lightGray);
    for (int i = 0; i <= cur_height; i += 15) {
      graphics.drawLine(0, cur_height - i, cur_width, cur_height - i);
    }

    // Draw vertical lines for reference
    for (int i = 0; i < cur_width; i += 15) {
      graphics.drawLine(i, 0, i, cur_height);
    }

    // Draw histograms as line graphs
    graphics.setColor(Color.RED);
    for (int i = 0; i < 255; i++) {
      graphics.drawLine(i, cur_height - rHist[i], i + 1, cur_height - rHist[i + 1]);
    }
    graphics.setColor(Color.GREEN);
    for (int i = 0; i < 255; i++) {
      graphics.drawLine(i, cur_height - gHist[i], i + 1, cur_height - gHist[i + 1]);
    }
    graphics.setColor(Color.BLUE);
    for (int i = 0; i < 255; i++) {
      graphics.drawLine(i, cur_height - bHist[i], i + 1, cur_height - bHist[i + 1]);
    }

    graphics.dispose();
    return transformBufferImageToImage(cur_histImage);
  }

  private Image transformBufferImageToImage(BufferedImage bufferedImage) {
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();
    Image img = new SimpleImage(width, height);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int rgb = bufferedImage.getRGB(j, i);
        int[] pixel = {(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF};
        img.setPixel(i, j, pixel);
      }
    }
    return img;
  }

  @Override
  public Image adjustLevels(Image image, int blThresh, int mtPoint, int whPoint) {

    // Ensure compression percentage is valid
    if (blThresh < 0 || blThresh > 255) {
      throw new IllegalArgumentException("bl_thresh must between 0 and 255.");
    }
    if (mtPoint < 0 || mtPoint > 255) {
      throw new IllegalArgumentException("mt_point must between 0 and 255.");
    }

    if (whPoint < 0 || whPoint > 255) {
      throw new IllegalArgumentException("wh_point must between 0 and 255.");
    }

    double scBm = (double) 128 / (mtPoint - blThresh);
    double scMw = (double) (255 - 128) / (whPoint - mtPoint);

    Image adjustedImage = new SimpleImage(image.getWidth(), image.getHeight());

    for (int x = 0; x < image.getHeight(); x++) {
      for (int y = 0; y < image.getWidth(); y++) {
        int[] color = image.getPixel(x, y);
        int red = adjustValue(color[0], blThresh, mtPoint, whPoint, scBm, scMw);
        int green = adjustValue(color[1], blThresh, mtPoint, whPoint, scBm, scMw);
        int blue = adjustValue(color[2], blThresh, mtPoint, whPoint, scBm, scMw);
        adjustedImage.setPixel(x, y, new int[]{red, green, blue});
      }
    }
    return adjustedImage;
  }

  private int adjustValue(int value, int blThresh, int mtPoint, int whPoint, double scBm,
      double scMw) {
    if (value < blThresh) {
      return 0;
    }
    if (value > whPoint) {
      return 255;
    }
    if (value <= mtPoint) {
      return (int) ((value - blThresh) * scBm);
    }
    return (int) (128 + (value - mtPoint) * scMw);
  }

  @Override
  // Compress the image using the Haar Wavelet Transform
  public Image compressImage(Image input, int percentage) {
    if (input == null) {
      throw new IllegalArgumentException("Input image cannot be null");
    }

    // Ensure compression percentage is valid
    if (percentage < 0 || percentage > 100) {
      throw new IllegalArgumentException("Compression percentage must be between 0 and 100.");
    }

    int originalWidth = input.getWidth();
    int originalHeight = input.getHeight();
    int newWidth = nextPowerOfTwo(originalWidth);
    int newHeight = nextPowerOfTwo(originalHeight);

    // Create a 3D array to hold the compressed data
    double[][][] compressedData = new double[3][newHeight][newWidth];

    Image resizedImage = new SimpleImage(newWidth, newHeight);

    // Copy existing pixel data to resized image
    for (int row = 0; row < originalHeight; row++) {
      for (int col = 0; col < originalWidth; col++) {
        resizedImage.setPixel(row, col, input.getPixel(row, col));
      }
    }

    // Get channel data and apply Haar Transform
    double[][] redChannel = getChannelData(0, resizedImage);
    double[][] greenChannel = getChannelData(1, resizedImage);
    double[][] blueChannel = getChannelData(2, resizedImage);

    // Apply Haar Transform and threshold for lossy compression
    compressedData[0] = HaarTransform.haarWaveTransf2D(redChannel);
    compressedData[1] = HaarTransform.haarWaveTransf2D(greenChannel);
    compressedData[2] = HaarTransform.haarWaveTransf2D(blueChannel);

    // Apply thresholding to the transformed data
    applyThreshold(compressedData[0], percentage);
    applyThreshold(compressedData[1], percentage);
    applyThreshold(compressedData[2], percentage);

    return decompressImage(compressedData, input);
  }

  private int nextPowerOfTwo(int n) {
    if (n <= 1) {
      return 1;
    }
    return (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
  }

  private Image decompressImage(double[][][] compressedData, Image original) {
    if (compressedData == null || compressedData.length != 3) {
      throw new IllegalArgumentException("Compressed data must contain three color channels");
    }

    // Create a new image to hold the decompressed data
    Image img = new SimpleImage(original.getWidth(), original.getHeight());

    // Decompress each color channel using the inverse Haar transform
    double[][] redChannel = HaarTransform.invHaarWaveTransf2D(compressedData[0]);
    double[][] greenChannel = HaarTransform.invHaarWaveTransf2D(compressedData[1]);
    double[][] blueChannel = HaarTransform.invHaarWaveTransf2D(compressedData[2]);

    // Set pixel values back to original size image
    for (int row = 0; row < original.getHeight(); row++) {
      for (int col = 0; col < original.getWidth(); col++) {
        int r = (int) Math.min(Math.max(redChannel[row][col], 0), 255);
        int g = (int) Math.min(Math.max(greenChannel[row][col], 0), 255);
        int b = (int) Math.min(Math.max(blueChannel[row][col], 0), 255);
        img.setPixel(row, col, new int[]{r, g, b});
      }
    }

    return img;
  }

  private double[][] getChannelData(int channel, Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    double[][] channelData = new double[height][width];

    for (int x = 0; x < height; x++) {
      for (int y = 0; y < width; y++) {
        int[] rgb = image.getPixel(x, y);
        channelData[x][y] = rgb[channel];
      }
    }
    return channelData;
  }

  private void applyThreshold(double[][] channel, double threshold) {
    for (int i = 0; i < channel.length; i++) {
      for (int j = 0; j < channel[0].length; j++) {
        if (Math.abs(channel[i][j]) < threshold) {
          channel[i][j] = 0;
        }
      }
    }
  }
}
