
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * CompressionArtifactTest.
 */
public class CompressionArtifactTest {

  private ByteArrayOutputStream outContent;
  private ByteArrayInputStream inContent;
  private final PrintStream originalOut = System.out;

  @Before
  public void setUpStreams() {
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
  }

  @Test
  public void testCompressionArtifactsHighQuality() throws Exception {
    String script = "load res/testImage.png img1\n" +
        "compress 90 img1 img1-compressed-high\n" +
        "save res/img1-compressed-high.png img1-compressed-high\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Compressed image: img1 to 90% quality"));
    assertTrue(output.contains("Saved image: img1-compressed-high to res/img1-compressed-high.png"));


    long originalSize = Files.size(Paths.get("res/testImage.png"));
    long compressedSize = Files.size(Paths.get("res/img1-compressed-high.png"));
    assertTrue("Compression did not reduce file size enough", compressedSize < originalSize);
  }

  @Test
  public void testCompressionArtifactsLowQuality() throws Exception {
    String script = "load res/testImage.png img1\n" +
        "compress 10 img1 img1-compressed-low\n" +
        "save res/img1-compressed-low.png img1-compressed-low\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Compressed image: img1 to 10% quality"));
    assertTrue(output.contains("Saved image: img1-compressed-low to res/img1-compressed-low.png"));


    long originalSize = Files.size(Paths.get("res/testImage.png"));
    long compressedSize = Files.size(Paths.get("res/img1-compressed-low.png"));
    assertTrue("Compression did not significantly reduce file size", compressedSize < originalSize);


  }

  @Test
  public void testHaarTransformCompression() throws Exception {
    String script = "load res/testImage.png img1\n" +
        "haar-transform img1 img1-haar\n" +
        "save res/img1-haar.png img1-haar\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Applied Haar Transform to: img1"));
    assertTrue(output.contains("Saved image: img1-haar to res/img1-haar.png"));


    long originalSize = Files.size(Paths.get("res/testImage.png"));
    long haarCompressedSize = Files.size(Paths.get("res/img1-haar.png"));
    assertTrue("Haar compression did not reduce file size", haarCompressedSize < originalSize);
  }

  @Test
  public void testCompressionDecompressionConsistency() throws Exception {
    String script = "load res/testImage.png img1\n" +
        "compress 50 img1 img1-compressed-medium\n" +
        "decompress img1-compressed-medium img1-decompressed\n" +
        "save res/img1-decompressed.png img1-decompressed\n" +
        "exit\n";

    inContent = new ByteArrayInputStream(script.getBytes());
    System.setIn(inContent);
    Main.main(new String[]{});

    String output = outContent.toString();
    assertTrue(output.contains("Compressed image: img1 to 50% quality"));
    assertTrue(output.contains("Decompressed image: img1-compressed-medium"));
    assertTrue(output.contains("Saved image: img1-decompressed to res/img1-decompressed.png"));

  }
}
