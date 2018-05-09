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
import clearcl.backend.ClearCLBackendInterface;
import clearcl.backend.ClearCLBackends;
import clearcl.enums.ImageChannelDataType;
import CLlibs.ocllib.thislib;
import clearcl.viewer.ClearCLImageViewer;

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
  public void demoViewImage3DF() throws InterruptedException,
                                 IOException
  {

    ClearCLBackendInterface lClearCLBackendInterface =
                                                     ClearCLBackends.getBestBackend();
    try (ClearCL lClearCL = new ClearCL(lClearCLBackendInterface))
    {
      ClearCLDevice lFastestGPUDevice =
                                      lClearCL.getFastestGPUDeviceForImages();

      System.out.println(lFastestGPUDevice);

      ClearCLContext lContext = lFastestGPUDevice.createContext();

        ClearCLProgram lProgram =
                          lContext.createProgram(thislib.class, "exampleForLoic/objForLoic_hyperboloid.cl");
      lProgram.buildAndLog();

      int lSize = 128;

      ClearCLImage lImage =
                          lContext.createSingleChannelImage(ImageChannelDataType.Float,
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

      ClearCLImageViewer lViewImage = ClearCLImageViewer.view(lImage);

      lViewImage.waitWhileShowing();
    }
    
  }
  
    public static void main(String[] args) {
        
      ImageExample3D display = new ImageExample3D();
      try{
          display.demoViewImage3DF();
      } catch (Exception ex) {
          System.out.println("exception"+ex);
          System.exit(0);
      }
      System.exit(0);

    }
}