import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import controller.ImageController;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import model.ImageModel;
import model.image.Image;
import model.image.RGBImage;

import org.junit.jupiter.api.Test;
import utils.ImageHandler;
import utils.StandardImageHandler;

/**
 * Tests for ModelController.
 */
public class ModelControllerTests {

  // Test ImageModel compressImage method
  @Test
  public void testCompressImageValidInput() {
    ImageModel model = new ImageModel();
    Image inputImage = new RGBImage(8, 8);
    Image compressedImage = model.compressImage(inputImage, 50);
    assertNotNull(compressedImage);
  }

  @Test
  public void testCompressImageInvalidPercentage() {
    ImageModel model = new ImageModel();
    Image inputImage = new RGBImage(8, 8);
    assertThrows(IllegalArgumentException.class, () -> {
      model.compressImage(inputImage, -10);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      model.compressImage(inputImage, 150);
    });
  }

  // Test ImageModel adjustLevels method
  @Test
  public void testAdjustLevelsValidInput() {
    ImageModel model = new ImageModel();
    Image inputImage = new RGBImage(8, 8);
    Image adjustedImage = model.adjustLevels(inputImage, 10, 50, 200);
    assertNotNull(adjustedImage);
  }

  @Test
  public void testAdjustLevelsInvalidInput() {
    ImageModel model = new ImageModel();
    Image inputImage = new RGBImage(8, 8);
    assertThrows(IllegalArgumentException.class, () -> {
      model.adjustLevels(inputImage, -10, 50, 200);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      model.adjustLevels(inputImage, 10, 50, 300);
    });
  }

  // Test ImageModel correctColor method
  @Test
  public void testCorrectColorValidInput() {
    ImageModel model = new ImageModel();
    Image inputImage = new RGBImage(8, 8);
    Image correctedImage = model.correctColor(inputImage);
    assertNotNull(correctedImage);
  }

  // Test ImageModel luma method
  @Test
  public void testLumaValidInput() {
    ImageModel model = new ImageModel();
    Image inputImage = new RGBImage(8, 8);
    Image lumaImage = model.luma(inputImage);
    assertNotNull(lumaImage);
  }

  // Test ImageModel histogram method
  @Test
  public void testHistogramValidInput() {
    ImageModel model = new ImageModel();
    Image inputImage = new RGBImage(8, 8);
    Image histogramImage = model.histogram(inputImage);
    assertNotNull(histogramImage);
  }

  // Test ImageController processCommand method
  @Test
  public void testProcessCommandLumaComponent() {
    ImageController controller = new ImageController();
    Image inputImage = new RGBImage(4, 4);
    controller.images.put("input", inputImage);

    String command = "luma-component input output";
    controller.processCommand(command);

    assertTrue(controller.images.containsKey("output"));
    assertNotNull(controller.images.get("output"));
  }

  @Test
  public void testProcessCommandInvalidCommand() {
    ImageController controller = new ImageController();

    String command = "invalid-command";
    assertDoesNotThrow(() -> controller.processCommand(command));
  }

  @Test
  public void testProcessCommandAdjustLevels() {
    ImageController controller = new ImageController();
    Image inputImage = new RGBImage(4, 4);
    controller.images.put("input", inputImage);

    String command = "levels-adjust 10 50 200 input output";
    controller.processCommand(command);

    assertTrue(controller.images.containsKey("output"));
    assertNotNull(controller.images.get("output"));
  }

  @Test
  public void testProcessCommandCompressImage() {
    ImageController controller = new ImageController();
    Image inputImage = new RGBImage(4, 4);
    controller.images.put("input", inputImage);

    String command = "compress 50 input output";
    controller.processCommand(command);

    assertTrue(controller.images.containsKey("output"));
    assertNotNull(controller.images.get("output"));
  }

  // Test ImageHandler methods (assuming some operations)
  @Test
  public void testImageHandlerLoadAndSave() throws IOException {
    ImageHandler handler = new StandardImageHandler();
    Image image = handler.load("/mnt/data/src/src/res/city.png");
    assertNotNull(image);
    handler.save(image, "/mnt/data/src/src/res/test_output.png");

    File outputFile = new File("/mnt/data/src/src/res/test_output.png");
    assertTrue(outputFile.exists());
  }

  // Test runScript functionality
  @Test
  public void testRunScript() {
    ImageController controller = new ImageController();
    String scriptContent = "luma-component input output\n" +
        "histogram output histogram_output\n";

    // Create a temporary script file for testing
    File scriptFile = null;
    try {
      scriptFile = File.createTempFile("script", ".txt");
      BufferedWriter writer = new BufferedWriter(new FileWriter(scriptFile));
      writer.write(scriptContent);
      writer.close();

      controller.images.put("input", new RGBImage(4, 4));
      controller.runScript(scriptFile.getAbsolutePath());

      assertTrue(controller.images.containsKey("output"));
      assertTrue(controller.images.containsKey("histogram_output"));

    } catch (IOException e) {
      fail("Failed to create script file");
    } finally {
      if (scriptFile != null && scriptFile.exists()) {
        scriptFile.delete();
      }
    }
  }

  @Test
  public void testProcessCommandCorrectColor() {
    ImageController controller = new ImageController();
    Image inputImage = new RGBImage(4, 4);
    controller.images.put("input", inputImage);

    String command = "color-correct input output";
    controller.processCommand(command);

    assertTrue(controller.images.containsKey("output"));
    assertNotNull(controller.images.get("output"));
  }

  @Test
  public void testProcessCommandHistogram() {
    ImageController controller = new ImageController();
    Image inputImage = new RGBImage(4, 4);
    controller.images.put("input", inputImage);

    String command = "histogram input output";
    controller.processCommand(command);

    assertTrue(controller.images.containsKey("output"));
    assertNotNull(controller.images.get("output"));
  }
}
