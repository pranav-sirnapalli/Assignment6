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

## Supported Commands:
- Please check the commands on the other file named USEME.md.
## Citation for picture: 
- This picture is draw by my friend, her name(Xueer Wang) is also on the picture.
