## Supported Commands:

#### The following commands are supported by the Image Processing Program:

- **`load <input_file_path> <reference_name>`**  
  Loads an image from the specified file and stores it with the given reference name.

- **`save <save_path> <output_name>`**  
  Saves the image referenced by `<output_name>` to the specified path.

- **`horizontal-flip <reference_name> <output_name>`**  
  Creates a horizontally flipped version of the image referenced by `<reference_name>`.

- **`vertical-flip <reference_name> <output_name>`**  
  Creates a vertically flipped version of the image referenced by `<reference_name>`.

- **`brighten <reference_name> <output_name> <increment>`**  
  Brightens the image referenced by `<reference_name>` by the specified increment.

- **`blur <reference_name> <output_name>`**  
  Applies a blur effect to the image referenced by `<reference_name>`.

- **`sepia <reference_name> <output_name>`**  
  Applies a sepia tone to the image referenced by `<reference_name>`.

- **`sharpen <reference_name> <output_name>`**  
  Sharpens the image referenced by `<reference_name>`.

- **`greyScale <reference_name> <output_name>`**  
  Converts the image referenced by `<reference_name>` to greyscale.

- **`value-component <reference_name> <output_name>`**  
  Converts the image referenced by `<reference_name>` to value-component .

- **`rgb-split <reference_name> <red_output> <green_output> <blue_output>`**  
  Splits the image into its RGB components.

- **`rgb-combine <output_name> <red_reference> <green_reference> <blue_reference>`**  
  Combines RGB components back into a single image.

- **`histogram <reference_name> <output_name>`**  
  Produce a histogram of a given image.

- **`compress <percentage> <reference_name> <output_name>`**  
  Compress the image by the percentage value.
    
- **`color-correct <reference_name> <output_name>`**  
  Color-correct an image by aligning the meaningful peaks of its histogram.

- **`levels-adjust <black> <mid> <white> <reference_name> <output_name>`**  
  Adjust levels of an image.

- **`run <script_path>`**   
  Executes a series of commands from a script file.

- **`exit`**  
  Quit the program.

#### New Feature for split-view:
The image manipulations below are supported the split-view function:  
blur/sharpen/sepia/greyscale/color-correct/levels-adjust
- **`Use those command as usual just add "split <percentage>" after it`**
- **`Use the blur as example:`**
- **`blur <reference_name> <output_name> split 50 `**  
  It will not only blur the image but also split the view in half.




## How to run script from JAR file:
- Move terminal into src/res
- Type the code java -jar assignmet5.jar
- In the enter command, type the command: run script.txt

## How to run script from main:
- Click on main file, followed by run option on top
- run src/script.txt in terminal
- type exit
