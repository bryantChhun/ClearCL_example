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
  public void demoViewImage3DF(JTextField defined_path) throws InterruptedException,
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
          ClearCLProgram lProgram = lContext.createProgram(thislib.class, "exampleForLoic/objForLoic_hyperboloid.cl");

          lProgram.buildAndLog(); 
          // image size is as asked by Loic
          int lSize = 128;

          ClearCLImage lImage = lContext.createSingleChannelImage(ImageChannelDataType.Float,
                                                                lSize,
                                                                lSize,
                                                                lSize);

          ClearCLKernel lKernel = lProgram.createKernel("hyperboloid");
          lKernel.setArgument("image", lImage);
          lKernel.setGlobalSizes(lImage);

          lKernel.setOptionalArgument("cx", lSize / 2);
          lKernel.setOptionalArgument("cy", lSize / 2);
          lKernel.setOptionalArgument("cz", lSize / 2);

          lKernel.run(true);
          lImage.notifyListenersOfChange(lContext.getDefaultQueue());
        
          //File target_path = new File("/Users/bchhun/NetBeansProjects/ClearCL_example/Hyperboloid_tiff");
          File target_path = new File(defined_path.getText());
          TiffWriter tiff = new TiffWriter(NativeTypeEnum.UnsignedInt , 1, 0);
          tiff.setOverwrite(true);
          tiff.setBytesPerPixel(8);
          try
          {
              boolean response = tiff.write(lImage, target_path);
              System.out.print("writing success = "+response);
          } catch (Throwable t) 
          {
              System.out.print("Throw error");
              throw new AssertionError(t);
          }

          ClearCLImageViewer lViewImage = ClearCLImageViewer.view(lImage);

          lViewImage.waitWhileShowing();
        }
    
  }
  
}