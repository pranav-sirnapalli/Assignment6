
import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import controller.ImageScriptController;
import controller.ImgCommandController;
import model.ImageModel;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;

/**
 * CombinedControllerAndModelTests.
 */
public class CombinedControllerAndModelTests {

  private ByteArrayOutputStream outContent;
  private ByteArrayInputStream inContent;
  private final PrintStream originalOut = System.out;
  private ImageModel model;
  private ImgCommandController controller;

  @Before
  public void setUpStreamsAndMocks() {
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    model = mock(ImageModel.class);
    controller = new ImageScriptController(model);
  }

  // 1. Test: Controller Calls Model for Image Loading
  @Test
  public void testLoadImage() {
    controller.processCommand("load res/testImage.png img1");
    verify(model).loadImage("res/testImage.png", "img1");
  }

  // 2. Test: Controller Handles Invalid Commands
  @Test
  public void testInvalidCommand() {
    controller.processCommand("invalidCommand img1");
    verifyNoInteractions(model);
  }

  // 3. Test: Controller Applies Blur Transformation
  @Test
  public void testBlurCommand() {
    controller.processCommand("blur img1 img1-blurred");
    verify(model).applyBlur("img1", "img1-blurred");
  }

  // 4. Test: Controller Saves Image
  @Test
  public void testSaveImage() {
    controller.processCommand("save res/outputImage.png img1");
    verify(model).saveImage("res/outputImage.png", "img1");
  }

  // 5. Test: Model Loads Image Correctly
  @Test
  public void testModelLoadImage() {
    ImageModel realModel = new ImageModel();
    realModel.loadImage("res/testImage.png", "img1");
    assertNotNull("Image should be loaded", realModel.getImage("img1"));
  }

  // 6. Test: Model Saves Image Correctly
  @Test
  public void testModelSaveImage() throws Exception {
    ImageModel realModel = new ImageModel();
    realModel.loadImage("res/testImage.png", "img1");

    realModel.saveImage("res/outputImage.png", "img1");
    File file = new File("res/outputImage.png");
    assertTrue("Saved image file should exist", file.exists());
  }

  // 7. Test: Controller-Haar Transform Integration
  @Test
  public void testHaarTransformCommand() {
    controller.processCommand("haar-transform img1 img1-haar");
    verify(model).applyHaarTransform("img1", "img1-haar");
  }

  // 8. Test: Model Applies Compression
  @Test
  public void testCompression() throws Exception {
    ImageModel realModel = new ImageModel();
    realModel.loadImage("res/testImage.png", "img1");

    realModel.compressImage("img1", "img1-compressed", 50);
    File compressedFile = new File("res/img1-compressed.png");
    assertTrue("Compressed image should exist", compressedFile.exists());
    assertTrue("Compressed image should be smaller", compressedFile.length() < new File("res/testImage.png").length());
  }

  // 9. Test: Controller Error Handling for Missing Arguments
  @Test
  public void testMissingArguments() {
    controller.processCommand("blur img1");
    verifyNoInteractions(model);
    assertTrue("Controller should log error", outContent.toString().contains("Error: Missing arguments for blur command"));
  }

  // 10. Test: Controller-Save Command Success
  @Test
  public void testSaveCommandLogsSuccess() {
    when(model.saveImage(anyString(), anyString())).thenReturn(true);
    controller.processCommand("save res/outputImage.png img1");
    assertTrue("Controller should log success", outContent.toString().contains("Image saved successfully: img1"));
  }

  // 11. Test: Brightness Adjustment Command
  @Test
  public void testBrightnessCommand() {
    controller.processCommand("brighten img1 img1-bright 50");
    verify(model).adjustBrightness("img1", "img1-bright", 50);
  }

  // 12. Test: Greyscale Transformation
  @Test
  public void testGreyscaleCommand() {
    controller.processCommand("greyscale img1 img1-grey");
    verify(model).applyGreyscale("img1", "img1-grey");
  }

  // 13. Test: Sepia Transformation
  @Test
  public void testSepiaCommand() {
    controller.processCommand("sepia img1 img1-sepia");
    verify(model).applySepia("img1", "img1-sepia");
  }

  // 14. Test: Histogram Generation
  @Test
  public void testHistogramGeneration() {
    controller.processCommand("histogram img1 img1-histogram");
    verify(model).generateHistogram("img1", "img1-histogram");
  }

  // 15. Test: Vertical Flip Command
  @Test
  public void testVerticalFlipCommand() {
    controller.processCommand("vertical-flip img1 img1-flipped");
    verify(model).flipVertical("img1", "img1-flipped");
  }

  // 16. Test: Horizontal Flip Command
  @Test
  public void testHorizontalFlipCommand() {
    controller.processCommand("horizontal-flip img1 img1-flipped");
    verify(model).flipHorizontal("img1", "img1-flipped");
  }

  // 17. Test: High Quality Compression
  @Test
  public void testHighQualityCompression() throws Exception {
    controller.processCommand("compress 90 img1 img1-compressed-high");
    verify(model).compressImage("img1", "img1-compressed-high", 90);
  }

  // 18. Test: Low Quality Compression
  @Test
  public void testLowQualityCompression() throws Exception {
    controller.processCommand("compress 10 img1 img1-compressed-low");
    verify(model).compressImage("img1", "img1-compressed-low", 10);
  }

  // 19. Test: Decompression Command
  @Test
  public void testDecompressionCommand() {
    controller.processCommand("decompress img1-compressed img1-decompressed");
    verify(model).decompressImage("img1-compressed", "img1-decompressed");
  }

  // 20. Test: Invalid Command Logs Error
  @Test
  public void testInvalidCommandLogsError() {
    controller.processCommand("invalid-command img1");
    assertTrue("Controller should log error", outContent.toString().contains("Error: Invalid command - invalid-command"));
  }
}
