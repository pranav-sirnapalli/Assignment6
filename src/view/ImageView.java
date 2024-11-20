package view;

import controller.ImageController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
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
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import model.image.Image;
import utils.ImageTransformer;

public class ImageView extends JFrame implements ImgView {

  private JPanel mainpanel;
  private JLabel reqimgLabel;
  private JPanel menuPanel;
  private JPanel buttonPanel;
  private JPanel histPanel;
  private BufferedImage curImage;
  private Image image;
  private ImageController reqController;
  private Map<String, JButton> actionButtons;


  public ImageView() {

    actionButtons = new HashMap<>();

    setTitle("Image Editor");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mainpanel = new JPanel(new BorderLayout());
    reqimgLabel = new JLabel();
    JScrollPane imageScrollPane = new JScrollPane(reqimgLabel);

    menuPanel = new JPanel(new BorderLayout());
    buttonPanel = new JPanel(new GridLayout(9, 2));

    menuPanel.add(buttonPanel, BorderLayout.NORTH);

    histPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (curImage != null) {
          drawHistogram(g, image);
        }
      }

    };

    histPanel = new JPanel(){
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (curImage != null) {
          g.drawImage(histogram(), 0, 0, histPanel.getWidth(),histPanel.getHeight(),this);
        }
      }
    };


    histPanel.setBackground(Color.WHITE);
    TitledBorder titleBorder = new TitledBorder("Histogram");
    titleBorder.setTitleJustification(TitledBorder.CENTER);
    titleBorder.setTitlePosition(TitledBorder.TOP);
    histPanel.setBorder(titleBorder);
    histPanel.setPreferredSize(new Dimension(265, 265));

    menuPanel.add(histPanel, BorderLayout.SOUTH);
    mainpanel.add(menuPanel, BorderLayout.EAST);

    mainpanel.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent evt) {
        if (curImage != null) {
          updateImage(curImage);
        }
      }
    });

    mainpanel.add(imageScrollPane, BorderLayout.CENTER);

    init();
    add(mainpanel, BorderLayout.CENTER);

  }

  @Override
  public void showGUI() {
    setVisible(true);
  }

  @Override
  public void setController(ImageController Controller) {
    this.reqController = Controller;
  }

  private void init() {
    createButtonPanel();
    creatMenuBar();
  }

  private void creatMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    //File menu bar
    JMenu menuFile = new JMenu("File");
    JMenuItem load = new JMenuItem("Load Image");
    JMenuItem save = new JMenuItem("Save Image");
    menuFile.add(load);
    menuFile.add(save);
    load.addActionListener(e -> loadImage());
    save.addActionListener(e -> saveImage());

    //Edit menu bar
    JMenu menuEdit = new JMenu("Edit");
    JMenuItem flipHor = new JMenuItem("Flip Horizontal");
    JMenuItem flipVer = new JMenuItem("Flip Vertical");
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

    menuBar.add(menuFile);
    menuBar.add(menuEdit);
    setJMenuBar(menuBar);
  }

  private void createButton(JPanel buttonPanel) {
    String[] buttonList = {"Flip Horizontal", "Flip Vertical", "Blur", "Sharpen", "Greyscale",
        "Sepia", "Color Correction"};
    for (String name : buttonList) {
      JButton button = new JButton(name);
      actionButtons.put(name, button);
      buttonPanel.add(button);
    }

    for (String buttonName : actionButtons.keySet()) {
      JButton button = actionButtons.get(buttonName);
      button.addActionListener(createButtonListener(buttonName));

    }
  }

  /**
   * Helper function for creatButton().
   * @param action the action name.
   * @return an actionListener bind with handleImageAction.
   */
  private ActionListener createButtonListener(String action) {
    return e -> reqController.handleImageAction(action);
  }

  private void createButtonPanel() {
    JButton loadButton = new JButton("Load Image");
    loadButton.addActionListener(e -> loadImage());

    JButton saveButton = new JButton("Save Image");
    saveButton.addActionListener(e -> saveImage());

    JButton splitViewButton = new JButton("Split-view");
    splitViewButton.addActionListener(e -> {
      splitView();
    });

    JButton levelAdjustmentButton = new JButton("Level Adjustment");
    levelAdjustmentButton.addActionListener(e -> {
      levelAdjustment();
    });

    JButton compressionButton = new JButton("Compression");
    compressionButton.addActionListener(e -> {
      compressionImage();
    });

    JButton downscaleButton = new JButton("Downscale");
    downscaleButton.addActionListener(e -> downscaleImage());

    buttonPanel.add(loadButton);
    buttonPanel.add(saveButton);
    createButton(buttonPanel);
    buttonPanel.add(downscaleButton);
    buttonPanel.add(compressionButton);
    buttonPanel.add(splitViewButton);
    buttonPanel.add(levelAdjustmentButton);
  }

  private void loadImage() {
    JFileChooser fileChooser = new JFileChooser();
    int resValue = fileChooser.showOpenDialog(this);
    if (resValue == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      reqController.handleImageAction("Load Image",file.getAbsolutePath());
      repaint();
    }
  }

  @Override
  public void updateImage(BufferedImage image) {
    this.curImage = image;
    this.image = ImageTransformer.transformBufferImageToImage(image);
    displayImage(image);
  }

  private void saveImage() {

    JFileChooser fileChooser = new JFileChooser();
    int resValue = fileChooser.showSaveDialog(this);
    if (resValue == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
//      reqController.saveImage(image, file.getAbsolutePath());
      reqController.handleImageAction("Save Image",file.getAbsolutePath());
      JOptionPane.showMessageDialog(this, "Save Successfully");
    }
  }

  /**
   * Helper function to show an image on given label.
   *
   * @param image the image to show
   * @param label the label to put image
   */
  private void showImage(BufferedImage image, JLabel label) {
    Dimension size = label.getSize();
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

    java.awt.Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight,
        java.awt.Image.SCALE_SMOOTH);
    ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

    label.setIcon(scaledImageIcon);
  }

  public void displayImage(BufferedImage image) {
    if (image != null) {
      showImage(image, reqimgLabel);
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
      int x = i + 1 < rHist.length ? i + 1 : i;
      g.setColor(Color.RED);
      g.drawLine(i, histPanel.getHeight() - rHist[i], x, histPanel.getHeight() - rHist[x]);
      g.setColor(Color.GREEN);
      g.drawLine(i, histPanel.getHeight() - gHist[i], x, histPanel.getHeight() - gHist[x]);
      g.setColor(Color.BLUE);
      g.drawLine(i, histPanel.getHeight() - bHist[i], x, histPanel.getHeight() - bHist[x]);
    }
  }

  private void levelAdjustment() {
    JFrame frame = new JFrame("Level Adjustment Dialog");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 2));

    JLabel label1 = new JLabel("Black:");
    JTextField textField1 = new JTextField(10);
    JLabel label2 = new JLabel("Middle:");
    JTextField textField2 = new JTextField(10);
    JLabel label3 = new JLabel("White:");
    JTextField textField3 = new JTextField(10);

    panel.add(label1);
    panel.add(textField1);
    panel.add(label2);
    panel.add(textField2);
    panel.add(label3);
    panel.add(textField3);

    int option = JOptionPane.showConfirmDialog(frame, panel, "Enter the Inputs",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (option == JOptionPane.OK_OPTION) {
      String b = textField1.getText();
      String m = textField2.getText();
      String w = textField3.getText();
      try {
        int black = Integer.parseInt(b);
        int mid = Integer.parseInt(m);
        int white = Integer.parseInt(w);
        if (black < 0 || black > 255 || white < 0 || white > 255 || mid < 0 || mid > 255) {
          throw new NumberFormatException();
        } else if (black > mid || mid > white) {
          throw new InputMismatchException();
        }
        reqController.handleImageAction("Level Adjustment",b,m,w);
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "Invalid input. Please enter an integer between 0 and 255.", "Error",
            JOptionPane.ERROR_MESSAGE);
      } catch (InputMismatchException e) {
        JOptionPane.showMessageDialog(this,
            "Invalid input. Please make sure the black is less than mid and mid less than white.",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    frame.dispose();
  }

  public BufferedImage histogram(){
    return reqController.histogram(image);
  }

//  public void histogram(){
//    reqController.handleImageAction("Histogram");
//  }

  private void splitView() {
    String input = JOptionPane.showInputDialog("Enter the split percentage(1-100)");
    try {
      int percentage = Integer.parseInt(input);
      if (percentage < 1 || percentage > 100) {
        throw new NumberFormatException();
      }
      reqController.handleImageAction("Split-view",input);
      // ## need to decide which way to show the split-view
      // showImagePopup(splitView);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,
          "Invalid input. Please enter an integer between 1 and 100.", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void showImagePopup(BufferedImage image) {
    JFrame imageWindow = new JFrame("Image Split View");
    imageWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    ImageIcon imageIcon = new ImageIcon(image);
    imageWindow.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());

    JLabel imageLabel = new JLabel();

    imageWindow.add(imageLabel, BorderLayout.CENTER);

    imageWindow.setVisible(true);
    imageWindow.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent evt) {
        if (image != null) {
          showImage(image, imageLabel);
        }
      }
    });
    showImage(image, imageLabel);

  }

  private void compressionImage() {
    String input = JOptionPane.showInputDialog("Enter the compression percentage(1-100)");
    try {
      int percentage = Integer.parseInt(input);
      if (percentage < 1 || percentage > 100) {
        throw new NumberFormatException();
      }
      reqController.handleImageAction("Compression",input);
    } catch (NumberFormatException e) {
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
      reqController.handleImageAction("Downscale",input);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,
          "Invalid input. Please enter an integer between 1 and 100.", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

}
