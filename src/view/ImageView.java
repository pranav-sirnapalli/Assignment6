package view;

import controller.ImageController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import model.image.Image;
import utils.ImageTransformer;

public class ImageView extends JFrame {

  private JPanel mainpanel;
  private JLabel reqimgLabel;
  private JPanel histPanel;
  private BufferedImage curImage;
  private Image image;
  private ImageController reqController;
  private String type;


  public ImageView() {
    setTitle("Image View");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mainpanel = new JPanel(new BorderLayout());
    reqimgLabel = new JLabel();
    JScrollPane imageScrollPane = new JScrollPane(reqimgLabel);

    // Setup Histogram panel
    histPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (curImage != null) {
          drawHistogram(g, image);
        }
      }
    };
    histPanel.setPreferredSize(new Dimension(400, 256));

    mainpanel.add(imageScrollPane, BorderLayout.CENTER);
    mainpanel.add(histPanel, BorderLayout.SOUTH);

    add(createToolbar(), BorderLayout.NORTH);
    add(mainpanel, BorderLayout.CENTER);

    setVisible(true);
  }

  public void setController(ImageController Controller) {
    this.reqController = Controller;
  }

  private JToolBar createToolbar() {
    JToolBar toolbar = new JToolBar();

    JButton loadButton = new JButton("Load Image");
    loadButton.addActionListener(e -> loadImage());

    JButton saveButton = new JButton("Save Image");
    saveButton.addActionListener(e -> saveImage());

    JButton flipVerticalButton = new JButton("Flip Vertical");
    flipVerticalButton.addActionListener(e -> {
      curImage = reqController.flipVertical(image);
      updateImage();

    });

    JButton flipHorizontalButton = new JButton("Flip Horizontal");
    flipHorizontalButton.addActionListener(e -> {
      curImage = reqController.flipHorizontal(image);
      updateImage();
    });

    JButton blurButton = new JButton("Blur");
    blurButton.addActionListener(e -> {
      curImage = reqController.blurImage(image);
      updateImage();
    });

    JButton sharpenButton = new JButton("Sharpen");
    sharpenButton.addActionListener(e -> {
      curImage = reqController.sharpenImage(image);
      updateImage();
    });

    JButton grayscaleButton = new JButton("Grayscale");
    grayscaleButton.addActionListener(e -> {
      curImage = reqController.convertToGrayscale(image);
      updateImage();
    });

    JButton sepiaButton = new JButton("Sepia");
    sepiaButton.addActionListener(e -> {
      curImage = reqController.applySepia(image);
      updateImage();
    });

    JButton downscaleButton = new JButton("Downscale");
    downscaleButton.addActionListener(e -> downscaleImage());

    toolbar.add(loadButton);
    toolbar.add(saveButton);
    toolbar.add(flipVerticalButton);
    toolbar.add(flipHorizontalButton);
    toolbar.add(blurButton);
    toolbar.add(sharpenButton);
    toolbar.add(grayscaleButton);
    toolbar.add(sepiaButton);
    toolbar.add(downscaleButton);

    return toolbar;
  }

  private void loadImage() {
    JFileChooser fileChooser = new JFileChooser();
    int resValue = fileChooser.showOpenDialog(this);
    if (resValue == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      curImage = reqController.loadImage(file.getAbsolutePath());
      image = ImageTransformer.transformBufferImageToImage(curImage);
      displayImage(curImage);
      repaint();
    }
  }

  private void updateImage() {
    image = ImageTransformer.transformBufferImageToImage(curImage);
    displayImage(curImage);
  }

  private void saveImage() {
    JFileChooser fileChooser = new JFileChooser();
    int resValue = fileChooser.showSaveDialog(this);
    if (resValue == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      reqController.saveImage(curImage, file.getAbsolutePath(), type);
    }
  }

  public void displayImage(BufferedImage image) {
    if (image != null) {
      ImageIcon imageIcon = new ImageIcon(image);
      reqimgLabel.setIcon(imageIcon);
      histPanel.repaint();
    } else {
      JOptionPane.showMessageDialog(this, "No image to display.", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void drawHistogram(Graphics g, Image image) {
    int[] rHist = reqController.getRedHistogram(image);
    int[] gHist = reqController.getGreenHistogram(image);
    int[] bHist = reqController.getBlueHistogram(image);

    for (int i = 0; i < rHist.length; i++) {
      g.setColor(Color.RED);
      g.drawLine(i, histPanel.getHeight(), i, histPanel.getHeight() - rHist[i]);
      g.setColor(Color.GREEN);
      g.drawLine(i + 256, histPanel.getHeight(), i + 256 , histPanel.getHeight() - gHist[i]);
      g.setColor(Color.BLUE);
      g.drawLine(i + 512, histPanel.getHeight(), i + 512, histPanel.getHeight() - bHist[i]);
    }
  }

  private void downscaleImage() {
    String input = JOptionPane.showInputDialog("Enter downscale percentage (1-100):");
    try {
      int scale = Integer.parseInt(input);
      if (scale < 1 || scale > 100) {
        throw new NumberFormatException();
      }
      curImage = reqController.compressImage(curImage, scale);
      displayImage(curImage);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,
          "Invalid input. Please enter an integer between 1 and 100.", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public static void main(String[] args) {
    //ImageModel imageModel = new ImageModel();
    //ImageView imageView = new ImageView();
    //ImageController controller = new ImageController(imageModel, imageView);
    //imageView.setController(controller);
    SwingUtilities.invokeLater(() -> new ImageView());
  }
}
