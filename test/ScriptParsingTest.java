
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * ScriptParsingTest.
 */
public class ScriptParsingTest {

  private ByteArrayOutputStream outContent;
  private ByteArrayInputStream inContent;
  private final PrintStream originalOut = System.out;

  @Before
  public void setUpStreams() {
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
  }

  @Test
  public void testLoadAndSaveImage() {
    String script = "load res/testImage.png img1\n" +
        "save res/outputImage.png img1\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Loaded image: img1"));
    assertTrue(output.contains("Saved image: img1 to res/outputImage.png"));
  }

  @Test
  public void testBlurCommand() {
    String script = "load res/testImage.png img1\n" +
        "blur img1 img1-blurred\n" +
        "save res/img1-blurred.png img1-blurred\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Applied blur to: img1"));
    assertTrue(output.contains("Saved image: img1-blurred to res/img1-blurred.png"));
  }

  @Test
  public void testHorizontalFlipCommand() {
    String script = "load res/testImage.png img1\n" +
        "horizontal-flip img1 img1-flipped\n" +
        "save res/img1-flipped.png img1-flipped\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Flipped image horizontally: img1"));
    assertTrue(output.contains("Saved image: img1-flipped to res/img1-flipped.png"));
  }

  @Test
  public void testVerticalFlipCommand() {
    String script = "load res/testImage.png img1\n" +
        "vertical-flip img1 img1-flipped\n" +
        "save res/img1-flipped.png img1-flipped\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Flipped image vertically: img1"));
    assertTrue(output.contains("Saved image: img1-flipped to res/img1-flipped.png"));
  }

  @Test
  public void testSepiaCommand() {
    String script = "load res/testImage.png img1\n" +
        "sepia img1 img1-sepia\n" +
        "save res/img1-sepia.png img1-sepia\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Applied sepia tone to: img1"));
    assertTrue(output.contains("Saved image: img1-sepia to res/img1-sepia.png"));
  }

  @Test
  public void testGreyscaleCommand() {
    String script = "load res/testImage.png img1\n" +
        "greyscale img1 img1-grey\n" +
        "save res/img1-grey.png img1-grey\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Converted to greyscale: img1"));
    assertTrue(output.contains("Saved image: img1-grey to res/img1-grey.png"));
  }

  @Test
  public void testBrightnessAdjustment() {
    String script = "load res/testImage.png img1\n" +
        "brighten img1 img1-bright 50\n" +
        "save res/img1-bright.png img1-bright\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Brightened image: img1 by 50"));
    assertTrue(output.contains("Saved image: img1-bright to res/img1-bright.png"));
  }

  @Test
  public void testHistogramGeneration() {
    String script = "load res/testImage.png img1\n" +
        "histogram img1 img1-histogram\n" +
        "save res/img1-histogram.png img1-histogram\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Generated histogram for: img1"));
    assertTrue(output.contains("Saved image: img1-histogram to res/img1-histogram.png"));
  }

  @Test
  public void testInvalidCommand() {
    String script = "invalidCommand img1 img1-output\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Error: Invalid command - invalidCommand"));
  }

  @Test
  public void testErrorHandlingForMissingArguments() {
    String script = "blur img1\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Error: Missing arguments for blur command"));
  }
}

