package clearcl.io.test;

import java.io.File;

import clearcl.ClearCL;
import clearcl.ClearCLContext;
import clearcl.ClearCLDevice;
import clearcl.ClearCLImage;
import clearcl.ClearCLKernel;
import clearcl.ClearCLProgram;
import clearcl.backend.ClearCLBackendInterface;
import clearcl.backend.ClearCLBackends;
import clearcl.enums.BuildStatus;
import clearcl.enums.HostAccessType;
import clearcl.enums.ImageChannelDataType;
import clearcl.enums.ImageChannelOrder;
import clearcl.enums.KernelAccessType;
import clearcl.io.TiffWriter;
import coremem.enums.NativeTypeEnum;

// libraries for kernel tests
//import clearcl.test.ClearCLBasicTests;
import CLlibs.ocllib.thislib;

import org.junit.Test;

/**
 * Test for File input/output
 * This is a copy of the ClearCLIOTests run by ClearCL.
 * The Kernel used in this test is "test.cl" and is located locally based on the thislib.class location
 *
 * @author bchhun 5/14/18
 */
public class ClearCLIOTestsLocal
{
  @Test
  public void testTiffWriter_local() throws Throwable
  {
    ClearCLBackendInterface lClearCLBackend =
                                            ClearCLBackends.getBestBackend();

    ClearCL lClearCL = new ClearCL(lClearCLBackend);

    ClearCLDevice lBestGPUDevice = lClearCL.getBestGPUDevice();

    System.out.println(lBestGPUDevice.getInfoString());

    ClearCLContext lContext = lBestGPUDevice.createContext();

    ClearCLProgram lProgram =
                            lContext.createProgram(thislib.class,
                                                   "testcls/test.cl");

    lProgram.addDefine("CONSTANT", "1");

    BuildStatus lBuildStatus = lProgram.buildAndLog();

    ClearCLImage lImageSrc =
                           lContext.createImage(HostAccessType.WriteOnly,
                                                KernelAccessType.ReadWrite,
                                                ImageChannelOrder.Intensity,
                                                ImageChannelDataType.Float,
                                                100,
                                                100,
                                                100);

    ClearCLKernel lKernel = lProgram.createKernel("fillimagexor");

    lKernel.setArgument("image", lImageSrc);
    lKernel.setArgument("u", 1f);
    lKernel.setGlobalSizes(100, 100, 100);
    lKernel.run();

    ClearCLImage lImageDst =
                           lContext.createImage(HostAccessType.ReadOnly,
                                                KernelAccessType.WriteOnly,
                                                ImageChannelOrder.Intensity,
                                                ImageChannelDataType.Float,
                                                100,
                                                100,
                                                100);

    lImageSrc.copyTo(lImageDst, new long[]
    { 0, 0, 0 }, new long[]
    { 0, 0, 0 }, new long[]
    { 100, 100, 100 }, true);

    TiffWriter lTiffWriter = new TiffWriter(NativeTypeEnum.Byte,
                                            1f,
                                            0f);
    File lFile8 = File.createTempFile(this.getClass().getSimpleName(),
                                      "test8");
    File lFile16 =
                 File.createTempFile(this.getClass().getSimpleName(),
                                     "test16");
    File lFile32 =
                 File.createTempFile(this.getClass().getSimpleName(),
                                     "test32");
    
//    File lFile8 = new File("/Users/bchhun/NetBeansProjects/ClearCL_example/Test8_loc");
//    File lFile16 = new File("/Users/bchhun/NetBeansProjects/ClearCL_example/Test16_loc");
//    File lFile32 = new File("/Users/bchhun/NetBeansProjects/ClearCL_example/Test32_loc");
    
    lFile8.deleteOnExit();
    lFile16.deleteOnExit();
    lFile32.deleteOnExit();

    lTiffWriter.setOverwrite(true);
    lTiffWriter.setBytesPerPixel(8);
    lTiffWriter.write(lImageDst, lFile8);
    lTiffWriter.setBytesPerPixel(16);
    lTiffWriter.write(lImageDst, lFile16);
    lTiffWriter.setBytesPerPixel(32);
    lTiffWriter.write(lImageDst, lFile32);

  }
}
