/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageUI;

/**
 *
 * @author bchhun
 */

import java.io.IOException;

import clearcl.ClearCL;
import clearcl.ClearCLContext;
import clearcl.ClearCLDevice;
import clearcl.ClearCLImage;
import clearcl.ClearCLKernel;
import clearcl.ClearCLProgram;
import clearcl.enums.ImageChannelDataType;
import CLlibs.ocllib.thislib;
import clearcl.backend.jocl.ClearCLBackendJOCL;
import clearcl.viewer.ClearCLImageViewer;

// extra imports for this task
import clearcl.io.TiffWriter;
import coremem.enums.NativeTypeEnum;
import java.io.File;
import javax.swing.JTextField;

public class ImageExample3D
{

  /**
   * Demos 3D image viewing.
   * 
   * @throws InterruptedException
   *           NA
   * @throws IOException
   *           NA
   */
  public void demoViewImage3DF(JTextField defined_path, int _tolerance) throws InterruptedException,
                                 IOException
  {
      final ClearCLBackendJOCL lClearCLJOCLBackend =
                                                 new ClearCLBackendJOCL();
      
        try (ClearCL lClearCL = new ClearCL(lClearCLJOCLBackend))
        {
          ClearCLDevice lFastestGPUDevice = lClearCL.getFastestGPUDeviceForImages();

          System.out.println(lFastestGPUDevice);

          ClearCLContext lContext = lFastestGPUDevice.createContext();

          // program is based on my custom kernel
          //ClearCLProgram lProgram = lContext.createProgram(thislib.class, "exampleForLoic/objForLoic_hyperboloid.cl");
          ClearCLProgram lProgram = lContext.createProgram(thislib.class, "exampleForLoic/objForLoic_hyperboloid.cl", "exampleForLoic/objForLoic_filter.cl");
          lProgram.buildAndLog(); 
          // image size is as asked by Loic
          int lSize = 128;

          ClearCLImage lImage = lContext.createSingleChannelImage(ImageChannelDataType.Float,
                                                                lSize,
                                                                lSize,
                                                                lSize);

          ClearCLKernel lKernel = lProgram.createKernel("hyperboloid");
          ClearCLKernel mKernel = lProgram.createKernel("identity");
          lKernel.setArgument("image", lImage);
          lKernel.setGlobalSizes(lImage);

          lKernel.setOptionalArgument("cx", lSize / 2);
          lKernel.setOptionalArgument("cy", lSize / 2);
          lKernel.setOptionalArgument("cz", lSize / 2);
          lKernel.setOptionalArgument("tolerance", _tolerance);

          //lKernel.run(true);
          //lImage.notifyListenersOfChange(lContext.getDefaultQueue());
          //lContext.getDefaultQueue().waitToFinish();
          
          //chain a filter kernel
          //ClearCLProgram mProgram = lContext.createProgram(thislib.class, "exampleForLoic/objForLoic_filter.cl");
          //mProgram.buildAndLog();
          int mSize = 128;
          ClearCLImage mImage = lContext.createSingleChannelImage(ImageChannelDataType.Float,
                                                                lSize,
                                                                lSize,
                                                                lSize);
          //ClearCLKernel mKernel = mProgram.createKernel("identity");
          mKernel.setArgument("input", lImage);
          mKernel.setArgument("output", mImage);
          mKernel.setGlobalSizes(mImage);
          
          lKernel.run(true);
          lImage.notifyListenersOfChange(lContext.getDefaultQueue());
          //lContext.getDefaultQueue().waitToFinish();
          mKernel.run(true);
          mImage.notifyListenersOfChange(lContext.getDefaultQueue());
        
          File target_path = new File(defined_path.getText());
          File target_path2 = new File(defined_path.getText()+"_filtered");
          TiffWriter tiff = new TiffWriter(NativeTypeEnum.Float, 1, 0);
          tiff.setOverwrite(true);
          // this tiffwriter method should be called "setBitsPerPixel"
          tiff.setBytesPerPixel(8);
          try
          {
              boolean response = tiff.write(lImage, target_path);
              boolean response2 = tiff.write(mImage, target_path2);
              System.out.print("writing success = "+response);
              System.out.print("writing success = "+response2);
          } catch (Throwable t) 
          {
              System.out.print("Throw error");
              throw new AssertionError(t);
          }

          ClearCLImageViewer lViewImage = ClearCLImageViewer.view(lImage, "original");
          ClearCLImageViewer mViewImage = ClearCLImageViewer.view(mImage, "filtered");

          lViewImage.waitWhileShowing();
          mViewImage.waitWhileShowing();
        }
    
  }
  
}