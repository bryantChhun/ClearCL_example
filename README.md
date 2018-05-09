# ClearCL_example
My attempt to create custom OpenCL kernel, run computations and display resulting image using ClearCL

# Install/run
This is a java application.  Build the gradle project and then run it:

  ./gradlew build 
  
  ./gradlew run

# Notes:
I created two kernels based loosely on kernels available in ClearCL/src/java/clearcl/ocllib/

The gradle build must include "maven { url "http://dl.bintray.com/clearcontrol/ClearControl" }" as a repository

1) first Kernel is to accomplish the simple task of drawing a hyperboloid into the javaFX image viewer.
  - The hyperboloid follows: x^2 + y^2 - z^2 = 0
  - The surface of the hyperboloid appears very undersampled.  I tried using "floor" to round calculation to integer, before assigning pixels.  This helps.
  - I created a placeholder class "CLlibs/ocllib/thislib" that is used by ClearCLContext.createProgram to find my custom kernels

2) second Kernel is incomplete and not executed here.  It should apply a simple 3x3x3 mean filter to smooth out the surface
  - To do: explore openCl queuing to send hyperboloid kernel into filter kernel
  
Final to dos:
  - Loic asked to save the image to .tiff.  But it's a 3d image so, write all planes?
  - write some tests
