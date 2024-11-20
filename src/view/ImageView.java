package view;

import controller.ImgUIController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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
  private BufferedImage histogram;
  private ImgUIController reqController;
  private Map<String, JButton> actionButtons;


  public ImageView() {

    actionButtons = new HashMap<>();

    setTitle("Image Editor");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mainpanel = new JPanel(new BorderLayout());
    reqimgLabel = new JLabel();
    reqimgLabel.setHorizontalAlignment(JLabel.CENTER);
    reqimgLabel.setVerticalAlignment(JLabel.CENTER);
    JScrollPane imageScrollPane = new JScrollPane(reqimgLabel);

    menuPanel = new JPanel(new BorderLayout());
    buttonPanel = new JPanel(new GridLayout(9, 2));

    menuPanel.add(buttonPanel, BorderLayout.NORTH);

    histPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (curImage != null) {
          g.drawImage(histogram, 0, 0, histPanel.getWidth(), histPanel.getHeight(), this);
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
          updateImage(curImage, histogram);
        }
      }
    });

    mainpanel.add(imageScrollPane, BorderLayout.CENTER);

    init();
//    add(mainpanel, BorderLayout.CENTER);
    add(mainpanel, BorderLayout.CENTER);

  }

  @Override
  public void showGUI() {
    setVisible(true);
  }

  @Override
  public void setController(ImgUIController Controller) {
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
   *
   * @param action the action name.
   * @return an actionListener bind with handleImageAction.
   */
  private ActionListener createButtonListener(String action) {
    return e -> {
      try {
        if (curImage == null) {
          throw new NullPointerException();
        }
        reqController.handleImageAction(action);
      } catch (NullPointerException ex) {
        JOptionPane.showMessageDialog(this,
            "No Image to process, please load image first.", "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    };
  }


  // Unified exception handling method
  private void handleButtonAction(Runnable action) {
    if (curImage == null) {
      JOptionPane.showMessageDialog(this,
          "No Image to process, please load image first.", "Error",
          JOptionPane.ERROR_MESSAGE);
      return; // Exit early if curImage is null
    }

    try {
      action.run(); // Run the passed action
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this,
          "An unexpected error occurred: " + ex.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void createButtonPanel() {
    JButton loadButton = new JButton("Load Image");
    loadButton.addActionListener(e -> loadImage());

    JButton saveButton = new JButton("Save Image");
    saveButton.addActionListener(e -> handleButtonAction(this::saveImage));

    JButton splitViewButton = new JButton("Split-view");
    splitViewButton.addActionListener(e -> handleButtonAction(this::splitView));

    JButton levelAdjustmentButton = new JButton("Level Adjustment");
    levelAdjustmentButton.addActionListener(e -> handleButtonAction(this::levelAdjustment));

    JButton compressionButton = new JButton("Compression");
    compressionButton.addActionListener(e -> handleButtonAction(this::compressionImage));

    JButton downscaleButton = new JButton("Downscale");
    downscaleButton.addActionListener(e -> handleButtonAction(this::downscaleImage));

    JButton componentButton = new JButton("Component-value");
    componentButton.addActionListener(e -> handleButtonAction(this::componentValue));

    buttonPanel.add(loadButton);
    buttonPanel.add(saveButton);
    createButton(buttonPanel);
    buttonPanel.add(downscaleButton);
    buttonPanel.add(compressionButton);
    buttonPanel.add(splitViewButton);
    buttonPanel.add(levelAdjustmentButton);
    buttonPanel.add(componentButton);
  }

  private void loadImage() {
    JFileChooser fileChooser = new JFileChooser();
    int resValue = fileChooser.showOpenDialog(this);
    if (resValue == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      reqController.handleImageAction("Load Image", file.getAbsolutePath());
      repaint();
    }
  }

  @Override
  public void updateImage(BufferedImage image, BufferedImage histogram) {
    this.histogram = histogram;
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
      reqController.handleImageAction("Save Image", file.getAbsolutePath());
      JOptionPane.showMessageDialog(this, "Save Successfully");
    }
  }

  private void downscaleImage() {
    // Create the panel to hold input fields
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(2, 2)); // Two rows, two columns

    // Create the labels and text fields
    JLabel label1 = new JLabel("Enter scaled width:");
    JTextField field1 = new JTextField();
    JLabel label2 = new JLabel("Enter scaled height:");
    JTextField field2 = new JTextField();

    // Add the labels and text fields to the panel
    panel.add(label1);
    panel.add(field1);
    panel.add(label2);
    panel.add(field2);

    // Show the dialog with the panel
    int option = JOptionPane.showConfirmDialog(this, panel, "Input Dialog",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    // Check if OK was pressed
    if (option == JOptionPane.OK_OPTION) {
      String width = field1.getText();
      String height = field2.getText();
      reqController.handleImageAction("Downscale", width, height);
      showImagePopup(curImage, false,"Downscaled Image "+width+"x"+height);
    }
  }


  private void componentValue() {

    System.out.println("componentValue clicked");
    // Create the dialog
    JDialog dialog = new JDialog(this, "Select the component color:", true);
    dialog.setLayout(new BorderLayout());
    dialog.setSize(260, 80);

    // Create a dropdown (JComboBox)
    String[] options = {"Red", "Green", "Blue"};
    JComboBox<String> comboBox = new JComboBox<>(options);
    // Add an action listener for the dropdown
    comboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String selectedOption = (String) comboBox.getSelectedItem();
        reqController.handleImageAction("Component-value", selectedOption);
        dialog.dispose();
      }
    });

    // Add the comboBox to the dialog
    dialog.add(comboBox, BorderLayout.CENTER);

    // Show the dialog
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
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
        reqController.handleImageAction("Level Adjustment", b, m, w);
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

  private void splitView() {
    String input = JOptionPane.showInputDialog(this, "Enter the split percentage(1-100)");
    try {
      int percentage = Integer.parseInt(input);
      if (percentage < 1 || percentage > 100) {
        throw new NumberFormatException();
      }
      reqController.handleImageAction("Split-view", input);
      // ## need to decide which way to show the split-view
      // showImagePopup(splitView);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,
          "Invalid input. Please enter an integer between 1 and 100.", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void showImagePopup(BufferedImage image, Boolean scaledImageEnable,String title) {
    JFrame imageWindow = new JFrame(title);
    imageWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    ImageIcon imageIcon = new ImageIcon(image);
    int width = Math.max(imageIcon.getIconWidth()+28, 275);
    int height = Math.max(imageIcon.getIconHeight()+28, 275);
    imageWindow.setSize(width, height);
    JLabel imageLabel = new JLabel(imageIcon,JLabel.CENTER);
    imageLabel.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());

    imageWindow.add(imageLabel, BorderLayout.CENTER);

    imageWindow.setVisible(true);
    imageWindow.setLocationRelativeTo(this);
    imageWindow.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent evt) {
        if (scaledImageEnable) {
          showImage(image, imageLabel);
        }
        // If the scaledImageEnable is false the image itself will not scale with window.
      }
    });
    imageLabel.setIcon(imageIcon);
//    showImage(image, imageLabel);

  }

  private void compressionImage() {
    String input = JOptionPane.showInputDialog(this, "Enter the compression percentage(1-100)");
    try {
      int percentage = Integer.parseInt(input);
      if (percentage < 1 || percentage > 100) {
        throw new NumberFormatException();
      }
      reqController.handleImageAction("Compression", input);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,
          "Invalid input. Please enter an integer between 1 and 100.", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Helper method to downscale the image.
   */
  private void downscaleImage1() {
    String input = JOptionPane.showInputDialog(this, "Enter downscale percentage (1-100):");
    try {
      int scale = Integer.parseInt(input);
      if (scale < 1 || scale > 100) {
        throw new NumberFormatException();
      }
      reqController.handleImageAction("Downscale", input);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,
          "Invalid input. Please enter an integer between 1 and 100.", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

}
