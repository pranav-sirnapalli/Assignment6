package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import model.image.Image;
import model.image.RGBImage;
import model.image.SimpleImage;
import utils.ImageTransformer;

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
    if (splitPercentage<0 || splitPercentage>100) {
      throw new IllegalArgumentException("Split percentage should be in the range of (0-100)");
    }
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
    return ImageTransformer.transformBufferImageToImage(correctedImage);
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

    int[] rHist = new int[256];
    int[] gHist = new int[256];
    int[] bHist = new int[256];

    // used to calc histograms for each channel
    storeHistogram(image, rHist, gHist, bHist);

    // Scale histogram values to fit within the 256x256 image
    scaleHistogram(cur_height, rHist, gHist, bHist);

    drawHistogram(graphics, cur_width, cur_height, rHist, gHist, bHist);

    return ImageTransformer.transformBufferImageToImage(cur_histImage);
  }

  /**
   * Helper function for histogram to store the value from Image.
   *
   * @param image the target Image to read.
   * @param rHist red part of histogram.
   * @param gHist green part of histogram.
   * @param bHist blue part of histogram.
   */
  private void storeHistogram(Image image, int[] rHist, int[] gHist, int[] bHist) {
    for (int x = 0; x < image.getHeight(); x++) {
      for (int y = 0; y < image.getWidth(); y++) {
        int[] color = image.getPixel(x, y);
        rHist[color[0]]++;
        gHist[color[1]]++;
        bHist[color[2]]++;
      }
    }
  }

  private void scaleHistogram(int height, int[] rHist, int[] gHist, int[] bHist) {
    int max = 0;
    for (int i = 0; i < 256; i++) {
      max = Math.max(max, Math.max(rHist[i], Math.max(gHist[i], bHist[i])));
    }
    for (int i = 0; i < 256; i++) {
      rHist[i] = (rHist[i] * height) / max;
      gHist[i] = (gHist[i] * height) / max;
      bHist[i] = (bHist[i] * height) / max;
    }
  }

  /**
   * Helper function to draw the histogram.
   *
   * @param graphics the graphics to draw.
   * @param width    the width of histogram.
   * @param height   the height of histogram.
   * @param rHist    red part of histogram.
   * @param gHist    green part of histogram.
   * @param bHist    blue part of histogram.
   */
  private void drawHistogram(Graphics2D graphics, int width, int height, int[] rHist, int[] gHist,
      int[] bHist) {
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, width, height);

    // Draw horizontal lines for reference
    graphics.setColor(Color.lightGray);
    for (int i = 0; i <= height; i += 15) {
      graphics.drawLine(0, height - i, width, height - i);
    }

    // Draw vertical lines for reference
    for (int i = 0; i < width; i += 15) {
      graphics.drawLine(i, 0, i, height);
    }

    // Draw histograms as line graphs
    graphics.setColor(Color.RED);
    for (int i = 0; i < 255; i++) {
      graphics.drawLine(i, height - rHist[i], i + 1, height - rHist[i + 1]);
    }
    graphics.setColor(Color.GREEN);
    for (int i = 0; i < 255; i++) {
      graphics.drawLine(i, height - gHist[i], i + 1, height - gHist[i + 1]);
    }
    graphics.setColor(Color.BLUE);
    for (int i = 0; i < 255; i++) {
      graphics.drawLine(i, height - bHist[i], i + 1, height - bHist[i + 1]);
    }

    graphics.dispose();
  }

  @Override
  public Image adjustLevels(Image image, int blThresh, int mtPoint, int whPoint) {

    // Ensure compression percentage is valid
    if (blThresh < 0 || blThresh > 255) {
      throw new IllegalArgumentException("Black value must between 0 and 255.");
    }
    if (mtPoint < 0 || mtPoint > 255) {
      throw new IllegalArgumentException("Mid value must between 0 and 255.");
    }

    if (whPoint < 0 || whPoint > 255) {
      throw new IllegalArgumentException("White value must between 0 and 255.");
    }

    if (blThresh > mtPoint) {
      throw new IllegalArgumentException("Black value should less than mid value and white value.");
    }

    if (mtPoint > whPoint) {
      throw new IllegalArgumentException("Mid value should less than white value.");
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
    compressedData[0] = haarWaveTransf2D(redChannel);
    compressedData[1] = haarWaveTransf2D(greenChannel);
    compressedData[2] = haarWaveTransf2D(blueChannel);

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
    double[][] redChannel = invHaarWaveTransf2D(compressedData[0]);
    double[][] greenChannel = invHaarWaveTransf2D(compressedData[1]);
    double[][] blueChannel = invHaarWaveTransf2D(compressedData[2]);

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

  /**
   * method for transformation of double values.
   *
   * @param sequence input is a double seq.
   * @return returns a result.
   */
  private double[] haarWaveletTransform(double[] sequence) {
    int reqlen = sequence.length;
    double[] netRes = Arrays.copyOf(sequence, reqlen);

    while (reqlen > 1) {
      double[] avgDiff = new double[reqlen];
      for (int i = 0; i < reqlen; i += 2) {
        avgDiff[i / 2] = (netRes[i] + netRes[i + 1]) / Math.sqrt(2);
        avgDiff[reqlen / 2 + i / 2] = (netRes[i] - netRes[i + 1]) / Math.sqrt(2);
      }
      System.arraycopy(avgDiff, 0, netRes, 0, reqlen);
      reqlen /= 2;
    }
    return netRes;
  }

  /**
   * method for inverse trnasformation of double values.
   *
   * @param transformedSequence input is a double seq.
   * @return return a result.
   */
  private double[] invHaarWaveTransf(double[] transformedSequence) {
    int length = transformedSequence.length;
    double[] result = Arrays.copyOf(transformedSequence, length);
    int m = 2;

    while (m <= length) {
      double[] originalSeq = new double[m];
      for (int i = 0; i < m / 2; i++) {
        double avg = result[i];
        double diff = result[m / 2 + i];
        originalSeq[2 * i] = (avg + diff) / Math.sqrt(2);
        originalSeq[2 * i + 1] = (avg - diff) / Math.sqrt(2);
      }
      System.arraycopy(originalSeq, 0, result, 0, m);
      m *= 2;
    }
    return result;
  }

  /**
   * method for haar2D transformation.
   *
   * @param resMatrix is the input for the matrix.
   * @return returns a matrix.
   */
  private double[][] haarWaveTransf2D(double[][] resMatrix) {
    int size = resMatrix.length;

    for (int i = 0; i < size; i++) {
      resMatrix[i] = haarWaveletTransform(resMatrix[i]);
    }

    for (int j = 0; j < size; j++) {
      double[] column = new double[size];
      for (int i = 0; i < size; i++) {
        column[i] = resMatrix[i][j];
      }
      column = haarWaveletTransform(column);
      for (int i = 0; i < size; i++) {
        resMatrix[i][j] = column[i];
      }
    }

    return resMatrix;
  }

  /**
   * Method for invHaar2d transform.
   *
   * @param resMatrix input which is a double matrix.
   * @return return a result.
   */
  private double[][] invHaarWaveTransf2D(double[][] resMatrix) {
    int size = resMatrix.length;

    for (int j = 0; j < size; j++) {
      double[] column = new double[size];
      for (int i = 0; i < size; i++) {
        column[i] = resMatrix[i][j];
      }
      column = invHaarWaveTransf(column);
      for (int i = 0; i < size; i++) {
        resMatrix[i][j] = column[i];
      }
    }

    for (int i = 0; i < size; i++) {
      resMatrix[i] = invHaarWaveTransf(resMatrix[i]);
    }

    return resMatrix;
  }

  @Override
  public Image downscale(Image img, int newWidth, int newHeight) {
    if (img == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    if (newWidth <= 0 || newHeight <= 0) {
      throw new IllegalArgumentException("New dimensions must be greater than zero");
    }

    Image result = new SimpleImage(newWidth, newHeight);
    double xRatio = (double) img.getWidth() / newWidth;
    double yRatio = (double) img.getHeight() / newHeight;

    for (int row = 0; row < newHeight; row++) {
      for (int col = 0; col < newWidth; col++) {
        double srcX = col * xRatio;
        double srcY = row * yRatio;

        int x1 = (int) Math.floor(srcX);
        int y1 = (int) Math.floor(srcY);
        int x2 = Math.min(x1 + 1, img.getWidth() - 1);
        int y2 = Math.min(y1 + 1, img.getHeight() - 1);

        double dx = srcX - x1;
        double dy = srcY - y1;

        int[] c11 = img.getPixel(y1, x1);
        int[] c12 = img.getPixel(y2, x1);
        int[] c21 = img.getPixel(y1, x2);
        int[] c22 = img.getPixel(y2, x2);

        int[] newPixel = new int[3];
        for (int i = 0; i < 3; i++) {
          double c1 = c11[i] * (1 - dy) + c12[i] * dy;
          double c2 = c21[i] * (1 - dy) + c22[i] * dy;
          newPixel[i] = (int) (c1 * (1 - dx) + c2 * dx);
        }
        result.setPixel(row, col, newPixel);
      }
    }
    return result;
  }
}
