/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clearcl.io.test;

import java.util.*;
import CLlibs.ocllib.thislib;
import clearcl.ClearCL;
import clearcl.ClearCLContext;
import clearcl.ClearCLDevice;
import clearcl.ClearCLImage;
import clearcl.ClearCLKernel;
import clearcl.ClearCLProgram;
import clearcl.enums.BuildStatus;
import clearcl.enums.ImageChannelDataType;
import clearcl.io.TiffWriter;
import coremem.enums.NativeTypeEnum;

import java.io.File;
import org.junit.Test;
import clearcl.backend.jocl.ClearCLBackendJOCL;

/**
 *
 * @author bchhun
 */
public class ImageWriterTests {
    /**
     * Tests of all various image types
     * Create an image of each of the data types, tries to get TiffWriter to pass
     * Uses only JOCL backend
     */
    
    /**
     * test that creates 
     * @throws Throwable 
     */
    @Test
    public void testImageDataTypes() throws Throwable
    {
            final ClearCLBackendJOCL lClearCLJOCLBackend = new ClearCLBackendJOCL();
            ClearCL lClearCL = new ClearCL(lClearCLJOCLBackend);
            final ClearCLDevice lFastestGPUDevice = lClearCL.getFastestGPUDeviceForImages();
            final ClearCLContext lContext = lFastestGPUDevice.createContext();
                
            // create two programs, one for each test
            //ClearCLProgram lProgram = lContext.createProgram(thislib.class, "exampleForLoic/objForLoic_hyperboloid.cl");
            final ClearCLProgram lProgram = lContext.createProgram(thislib.class, "testcls/test.cl");
            lProgram.addDefine("CONSTANT", "1");
            
            final BuildStatus lBuildStatus = lProgram.buildAndLog();
            
            // Change target path for your local dir
            String target_path = "/Users/bchhun/NetBeansProjects/ClearCL_example/";
            
            testDataTypes(lContext, lProgram, target_path);
    }

    /**
     * Iterates through all enum data types for Image and for native types
     *  - ImageWriter often fails to write, is it because of data types?
     * @param lContext ClearCLContext 
     * @param lProgram ClearCLProgram
     * @param target_path String type, where to write the temp images.  Default is package root.
     * @throws Throwable 
     */
    public void testDataTypes(final ClearCLContext lContext, final ClearCLProgram lProgram, String target_path) throws Throwable {

        int lSize = 128;
        ClearCLKernel lKernel = lProgram.createKernel("fillimagexor");
        ArrayList<String> passed_types = new ArrayList();

        for (ImageChannelDataType img_type : ImageChannelDataType.values())
        {

            // create Image using similar method as main method
            ClearCLImage ImageSrc = lContext.createSingleChannelImage(img_type,lSize,lSize,lSize);
            lKernel.setArgument("image", ImageSrc);
            lKernel.setArgument("u", 1f);
            lKernel.setGlobalSizes(100, 100, 100);
            lKernel.run();
            ImageSrc.notifyListenersOfChange(lContext.getDefaultQueue());

            for (NativeTypeEnum native_type : NativeTypeEnum.values()) 
            {
                
                TiffWriter tfwtr = new TiffWriter(native_type, 1f, 0f);
                tfwtr.setOverwrite(true);
                
                try
                {
                    // uncomment below if you wish to use user defined "target_path"
                    //File img_path = new File(target_path + img_type.toString() + native_type.toString());
                    File img_path = File.createTempFile(this.getClass().getSimpleName(), img_type.toString() + native_type.toString());
                    tfwtr.write(ImageSrc, img_path);
                    passed_types.add(img_type.toString()+"\t"+native_type.toString());
                    System.out.print("Success for: img_type = "+img_type.toString()+"\t native_type = "+native_type.toString()+"\n");
                    img_path.deleteOnExit();
                    
                } catch (Throwable t)
                {
                    System.out.print("Throw error for: img_type = "+img_type.toString()+"\t native_type = "+native_type.toString()+"\n");
                }
            }
        }
        if (passed_types.isEmpty())
        {
            System.out.println("\nno valid pairs of image data types and tiffWriter types: image creation method may be wrong\n");
        }
        else
        {
            System.out.println("\n===== valid data type pairs found =====");
            passed_types.stream().forEach((item) -> 
            {
                System.out.println(item);
            });
            System.out.println("=======================================");
        }
      }
    
    
}
