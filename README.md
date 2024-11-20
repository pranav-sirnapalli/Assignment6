# Introduction
In this assignment we are building an image processing application that supports both text-based and GUI-based user interfaces. This application will alllow you to perform
multiple functions like loading an image, manipulate the image(blur, horizonatal-flip, greyscale etc), and also save this resulting.
It has been designed in such a way additional operations can be added easily in the future.

## Features and their respective files:

The operations performed as well as the files used for it are written below to give a high-level overview.

## Loading and Saving Images below files are all in the package utils:
- This is done using the files present in the utils folder.
- The interface names ImageHandler() is used to hold the load and save images
  so that interface can be used to rewrite same method if required elsewhere.
- StandardImageHandler(): This file implements the ImageHandler methods for our assignment
  
      - loadImage(): after using ImageIO.read() to read the image from the file path and store it in an object.
                     It takes in the width and height and then extracts the rgb values using getRGB and then sets the
                     values obtained using setPixelloads image of specific size after making use of getRGB and setPixel.

      - saveImage(): used to save the custom image depending on type png or jpg. It then creates a image of the same type
                     with height and width and also retriving rgb values like loadImage() and then using bit shifting.
                     It also makes use of a helper function to get the required file-extension.

- PPMImageHandler(): Used to create the file implementation for ppm specifically. Majority of code already given by professor.
                       This implementation is to store pixel data in a text form. This class provides methods to convert them into a more manageable internal format.

- ImageIOHelper() : Used to provide static methods to load and save images.Mainly used to push tasks to ImageHandlerFactory.
                    It makes sure to handle different kinds of images that are being used.After getting handler from ImageHandlerFactory
                    and the necessary path it will return it to the save method.

- ImageHandlerFactory(): Used to create instances of Image Handler based on the input. It has two methods getFileextension and
                         getHandler. The getFileExtension is used to provide us with the image handler instance
                         whereas the image getFileExtension is needed to get the required file extension to the getHandler so
                         it can be returned to ImageIOHelper class which calls it in its methods.

## The model and the operations performed using it:
- ImageHandler(): Used to hold the dimensions of image and pixel data in 3D array. It has the methods getwidth(), getheight(),
                  getPixel() and setPixel(). This class is used for easy manipulation of an image's pixel data, which can be useful
                  for operations in ImageModel.
- ImgModel(): The interface is used to hold all the operations which are performed by model.
              This interface makes it easy to extend the current operations or for future operations.

- ImageModel(): This class contains methods to manipulate images in multiple ways by splitting them into RGB channels,
                combining channels, flipping, applying filters etc.
                It holds the method like luma, blur, flip horizontal etc.

- RGBImage(): This class is an extension of the current Image class. It provides the methods to extract the red,
              green and blue from the particular images. The results returned in the methods of this class
              are used to set the pixels.

## Updates for assignment 5:
- Added 5 features which is compression, color-correction, level-adjustment, histogram and split-view.
- Make our code more scalable by abstract out Image to an interface.
- All the previous function work as the before. 
- For blur,sharpen,sepia,greyscale,color-correct,levels-adjust,they are supported with the split-view function.
- The script is preasent in the res folder. To run the script using jar first in terminal with path at res folder
- add command java -jar assignment5.jar and press enter
- followed by add command run script.txt in the enter command optoin
- and then type exit once done.

## utilites for assignment 5:
- HaarTranform file has been added in utilties to perform image compression. The other changes were just additions.
- to the current assignment 4 folder where we added required code in the controller and model.

## Updates for assignment 6:
- Added a UI as instructed in assignment question. The code has been written in the view folder.
- The view folder has the ImgView which is the interface that holds the setController() method which is used to implement the UI.
- The ImageView class extends the ImgView and is what is used to implement the Image UI using JFrame and Swing.
- To run the UI click on the file, followed by the run button on the top right in intellij.
- The design created consists of displaying all the methods in the model as buttons which can be used to modify any image.
- For example, the load image, flip vertical, color correction button. We also display the histogram and have a file and edit
- button on the top left corner used to showcase the same functions shown as buttons.

## Supported Commands:
- Please check the commands on the other file named USEME.md.
## Citation for picture: 
- This picture is draw by my friend, her name(Xueer Wang) is also on the picture.


# IME: Image Manipulation and Enhancement

## Project Components

- **ImageName**: a.png
- **Script**: script.txt
- **JAR File**: Assignment6.jar
- **JAR Script**: script2.txt

## Overview

This project provides a comprehensive image manipulation and enhancement tool that allows users to
perform operations like flipping, channel visualization, brightening, blurring, sharpening, color
correction, and more. It supports image formats including JPEG, PNG, and PPM, offering flexibility
for various use cases.

Using a robust **Model-View-Controller (MVC)** architecture, the project ensures modularity, allowing
future enhancements and maintenance with ease. It features a scripting interface for batch
operations, streamlining image processing tasks.

## Key Features

- **Image Operations**: Flip, brighten, compress, and apply filters like blur, sharpen, sepia, and greyscale.
- **Histogram Generation**: Analyze image color distribution.
- **Split View Operations**: Compare original and processed images side by side.
- **Multi-format Support**: Works seamlessly with JPEG, PNG, and PPM formats.
- **Command-Line Interface**: Enables efficient processing via scripts.

## Project Structure

### 1. **Model**: Core Logic
The model layer forms the foundation of the application by managing the internal representation of images
and providing methods to manipulate them.

- **Image Class**: Represents an image as a grid of RGB pixels.
- **Pixel Class**: Manages individual pixel RGB values.
- **ImageProcessor Interface**: Defines methods for image operations.
- **ImageProcessorImpl**: Implements core image manipulation logic.

- The model layer is responsible for handling the representation and core transformations of images,
such as flipping, brightening, and applying filters. It forms the backbone of the application.

### 2. **Controller**: Interaction Management
The controller acts as the intermediary between the user and the model. It processes user commands
and coordinates the execution of operations.

- **ImageController**: Handles commands for loading, saving, and manipulating images.
- **Script Execution**: Processes batch scripts with commands for automated operations.

- This layer translates user input into actions. By processing scripts or direct commands, it enables
the seamless application of image manipulations.

### 3. **View**: User Interaction (GUI)
The view layer provides the graphical user interface (GUI) for the application, making it accessible to users of all
technical levels. It ensures a user-friendly interaction experience, including visual feedback for
commands and immediate results display.

- **TextView**: Console-based display of messages and errors.

- The GUI extends the application's functionality by providing a visual representation of images and
operations. Users can interact with the application using buttons, menus, and sliders, simplifying the
workflow for non-technical users. The GUI also includes support for previewing before saving results,
allowing for adjustments and edits to be made dynamically.


### 4. **Utility**: File I/O
The utility layer handles reading and writing image files in multiple formats, ensuring compatibility and
reliability.

- **ImageUtil**: Reads and writes images in multiple formats.
- **PPMHandler**: Handles PPM-specific operations.
- **OtherFormatHandler**: Manages JPEG and PNG formats.

- This layer ensures smooth I/O operations, allowing images to be seamlessly read from and written to
various file formats.

## Example Commands

Scripts consist of commands to load, manipulate, and save images. Supported commands include:

```txt
load <image-path> <image-name>
save <image-path> <image-name>
blur <image-name> <dest-image-name>
sharpen <image-name> <dest-image-name>
greyscale <image-name> <dest-image-name>
sepia <image-name> <dest-image-name>
compress <compression-ratio> <image-name> <dest-image-name>
histogram <image-name> <dest-image-name>
exit
```

### Sample Script
```txt
load res/a.png img1
blur img1 img1-blurred
save res/img1-blurred.png img1-blurred
exit
```

- Commands provide a powerful way to automate image manipulation. They allow for precise control over
operations, whether executed individually or as part of a script.

## Running the Application

### Executing the JAR File
Run the JAR file from the command line:
```bash
java -jar res/Assignment6.jar
```

### Running Scripts
Use the `run` command to execute scripts:
```bash
run res/script.txt
```

### Output
Processed images and results are saved in the `res/` directory.

- The application can be run either interactively or using predefined scripts. Outputs are saved in
organized folders for easy access.

## Advanced Features

### Split View Comparison
Allows users to visually compare the original image with its processed version (e.g., blur, sharpen, etc.) side by side.

### Compression
Enables quality reduction to specified levels (e.g., 80%, 90%), optimizing storage without significant quality loss.

### Histogram Generation
Creates visual representations of color distributions within an image.

- Advanced features enhance the application by adding capabilities like visual comparison, storage
optimization, and analytical insights.

## JAR File Details

The JAR file consolidates all program components into an executable package. It can handle all operations via scripts or direct commands.

- The JAR file simplifies application distribution and execution, bundling all necessary components into a single, portable file.

## Example Use Cases

1. **Batch Processing**: Automate tasks using scripts (e.g., `script.txt`, `script2.txt`).
2. **Histogram Analysis**: Generate histograms for visual insights into image color distribution.
3. **Format Conversion**: Process and save images in different formats.

- The application excels in automating repetitive tasks, analyzing image data, and converting image formats efficiently.

