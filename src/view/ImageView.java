package view;

import controller.ImageController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import model.image.Image;
import utils.ImageTransformer;

public class ImageView extends JFrame implements ImgView {

  private JFrame frame;
  private JPanel mainpanel;
  private JLabel reqimgLabel;
  private JPanel menuPanel;
  private JPanel buttonPanel;
  private JPanel histPanel;
  private BufferedImage curImage;
  private Image image;
  private ImageController reqController;


  public ImageView() {
    setTitle("Image Editor");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mainpanel = new JPanel(new BorderLayout());
    reqimgLabel = new JLabel();
    JScrollPane imageScrollPane = new JScrollPane(reqimgLabel);

    menuPanel = new JPanel(new BorderLayout());
    buttonPanel = new JPanel(new GridLayout(9,2));

    menuPanel.add(buttonPanel, BorderLayout.NORTH);

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
    histPanel.setBackground(Color.WHITE);
    TitledBorder titleBorder = new TitledBorder("Histogram");
    titleBorder.setTitleJustification(TitledBorder.CENTER); // 标题居中
    titleBorder.setTitlePosition(TitledBorder.TOP); // 标题在顶部
    histPanel.setBorder(titleBorder);
    histPanel.setPreferredSize(new Dimension(265, 265));

    menuPanel.add(histPanel, BorderLayout.SOUTH);
    mainpanel.add(menuPanel, BorderLayout.EAST);


    mainpanel.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent evt) {
        if(curImage != null) {
          displayImage(curImage);
        }
      }
    });

    mainpanel.add(imageScrollPane, BorderLayout.CENTER);

//    add(createToolbar(), BorderLayout.NORTH);
    createToolbar();
    add(mainpanel, BorderLayout.CENTER);

    setVisible(true);
  }

  @Override
  public void setController(ImageController Controller) {
    this.reqController = Controller;
  }

  private void createToolbar() {
    //JToolBar toolbar = new JToolBar();

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


    JButton splitViewButton = new JButton("Split-view");
    splitViewButton.addActionListener(e -> {

    });

    JButton colorCorrectionButton = new JButton("Color Correction");
    colorCorrectionButton.addActionListener(e -> {

    });

    JButton levelAdjustmentButton = new JButton("Level Adjustment");
    levelAdjustmentButton.addActionListener(e -> {

    });

    JButton compressionButton = new JButton("Compression");
    compressionButton.addActionListener(e -> {
      compressionImage();
      updateImage();
    });

    JButton downscaleButton = new JButton("Downscale");
    downscaleButton.addActionListener(e -> downscaleImage());

    JMenuBar menuBar = new JMenuBar();

    //File menu bar
    JMenu menu = new JMenu("File");
    JMenuItem load = new JMenuItem("Load Image");
    JMenuItem save = new JMenuItem("Save Image");
    menu.add(load);
    menu.add(save);
    load.addActionListener(e -> loadImage());
    save.addActionListener(e -> saveImage());

    //Edit menu bar
    JMenu menuEdit = new JMenu("Edit");
    JMenuItem flipHor = new JMenuItem("Flip Horizontal");
    JMenuItem flipVer= new JMenuItem("Flip Vertical");
    JMenuItem blur = new JMenuItem("Blur");
    JMenuItem sharpen = new JMenuItem("Sharpen");
    JMenuItem greyscale = new JMenuItem("Greyscale");
    JMenuItem sepia = new JMenuItem("Sepia");
    JMenuItem downscale = new JMenuItem("Downscale");
    JMenuItem compression = new JMenuItem("Compression");

    JMenu component = new JMenu("Component-value");
    JMenuItem red = new JMenuItem("red");
    JMenuItem green = new JMenuItem("green");
    JMenuItem blue = new JMenuItem("blue");

    component.add(red);
    component.add(green);
    component.add(blue);

    menuEdit.add(flipHor);
    menuEdit.add(flipVer);
    menuEdit.add(blur);
    menuEdit.add(sharpen);
    menuEdit.add(greyscale);
    menuEdit.add(sepia);
    menuEdit.add(compression);
    menuEdit.add(component);
    menuEdit.add(downscale);

    menuBar.add(menu);
    menuBar.add(menuEdit);
    setJMenuBar(menuBar);

    buttonPanel.add(loadButton);
    buttonPanel.add(flipVerticalButton);
    buttonPanel.add(saveButton);
    buttonPanel.add(flipHorizontalButton);
    buttonPanel.add(blurButton);
    buttonPanel.add(sharpenButton);
    buttonPanel.add(grayscaleButton);
    buttonPanel.add(sepiaButton);
    buttonPanel.add(downscaleButton);
    buttonPanel.add(compressionButton);
    buttonPanel.add(splitViewButton);
    buttonPanel.add(colorCorrectionButton);
    buttonPanel.add(levelAdjustmentButton);


//    return toolbar;
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
      reqController.saveImage(image, file.getAbsolutePath());
      JOptionPane.showMessageDialog(this, "Save Successfully");
    }
  }

  public void displayImage(BufferedImage image) {
    if (image != null) {
      Dimension size = reqimgLabel.getSize();
      ImageIcon imageIcon = new ImageIcon(image);
      java.awt.Image originalImage = imageIcon.getImage();

      //Base on the current window size to scale the curImage size
      int originalWidth = originalImage.getWidth(null);
      int originalHeight = originalImage.getHeight(null);

      double widthRatio = (double) size.width / (double) originalWidth;
      double heightRatio = (double) size.height / (double) originalHeight;

      double scaleRatio = Math.min(widthRatio, heightRatio);
      //Scale the image size
      int newWidth = (int) (originalWidth * scaleRatio);
      int newHeight = (int) (originalHeight * scaleRatio);

      java.awt.Image scaledImage = originalImage.getScaledInstance(  newWidth,newHeight,java.awt.Image.SCALE_SMOOTH);
      ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

      reqimgLabel.setIcon(scaledImageIcon);
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
      int x = i+1<rHist.length?i+1:i;
      g.setColor(Color.RED);
      g.drawLine(i, histPanel.getHeight() - rHist[i], x, histPanel.getHeight() - rHist[x]);
      g.setColor(Color.GREEN);
//      g.drawLine(i + 256, histPanel.getHeight(), i + 256, histPanel.getHeight() - gHist[i]);
      g.drawLine(i, histPanel.getHeight() - gHist[i], x, histPanel.getHeight() - gHist[x]);
      g.setColor(Color.BLUE);
//      g.drawLine(i + 512, histPanel.getHeight(), i + 512, histPanel.getHeight() - bHist[i]);
      g.drawLine(i, histPanel.getHeight() - bHist[i], x , histPanel.getHeight() - bHist[x]);
    }
  }


  private void splitView() {
    String input = JOptionPane.showInputDialog( "Enter the split-view ratio (1-100)");
    try{
      int percentage = Integer.parseInt(input);
      if (percentage < 1 || percentage > 100) {
        throw new NumberFormatException();
      }
      curImage = reqController.splitView(image,image, percentage);
      displayImage(curImage);
    }catch (NumberFormatException e){
      JOptionPane.showMessageDialog(this,
          "Invalid input. Please enter an integer between 1 and 100.", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void compressionImage() {
    String input = JOptionPane.showInputDialog( "Enter the compression percentage(1-100)");
    try{
      int percentage = Integer.parseInt(input);
      if (percentage < 1 || percentage > 100) {
        throw new NumberFormatException();
      }
      curImage = reqController.compressImage(image, percentage);
      displayImage(curImage);
    }catch (NumberFormatException e){
      JOptionPane.showMessageDialog(this,
          "Invalid input. Please enter an integer between 1 and 100.", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void downscaleImage() {
    String input = JOptionPane.showInputDialog("Enter downscale percentage (1-100):");
    try {
      int scale = Integer.parseInt(input);
      if (scale < 1 || scale > 100) {
        throw new NumberFormatException();
      }
      curImage = reqController.compressImage(image, scale);
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
