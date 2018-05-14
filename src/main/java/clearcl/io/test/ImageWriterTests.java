/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clearcl.io.test;

import CLlibs.ocllib.thislib;
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
import clearcl.test.ClearCLBasicTests;
import coremem.enums.NativeTypeEnum;

import java.io.File;
import org.junit.Test;
import clearcl.backend.jocl.ClearCLBackendJOCL;
import coremem.offheap.OffHeapMemory;
import java.io.IOException;

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
    
    //@Test
    public void testImages() throws Throwable
    {
            final ClearCLBackendJOCL lClearCLJOCLBackend = new ClearCLBackendJOCL();
            ClearCL lClearCL = new ClearCL(lClearCLJOCLBackend);
            final ClearCLDevice lFastestGPUDevice = lClearCL.getFastestGPUDeviceForImages();
            final ClearCLContext lContext = lFastestGPUDevice.createContext();
                
            // create two programs, one for each test
            //ClearCLProgram lProgram = lContext.createProgram(thislib.class, "exampleForLoic/objForLoic_hyperboloid.cl");
            final ClearCLProgram mProgram = lContext.createProgram(thislib.class, "testcls/test.cl");
            mProgram.addDefine("CONSTANT", "1");
            
            //final BuildStatus lBuildStatus = lProgram.buildAndLog();
            final BuildStatus mBuildStatus = mProgram.buildAndLog();
            
            // Change target path for your local dir
            File target_path = new File("/Users/bchhun/NetBeansProjects/ClearCL_example/Testtiff");
            
            //testDataTypes(lClearCLJOCLBackend, target_path);
            //testImageCreation(lClearCLJOCLBackend);
            //testTiffWriter(lContext, mProgram);
    }

    /**
     * Iterates through all enum data types for Image and for native types
     *  - ImageWriter often fails to write, is it because of data types?
     * @param backend
     * @param target_path
     * @throws InterruptedException
     * @throws IOException 
     */
    public void testDataTypes(final ClearCLBackendInterface backend, File target_path) throws Exception {
        try (ClearCL lClearCL = new ClearCL(backend)) 
        {
            ClearCLDevice lFastestGPUDevice = lClearCL.getFastestGPUDeviceForImages();
            ClearCLContext lContext = lFastestGPUDevice.createContext();
            
            int lSize = 128;

            for (ImageChannelDataType img_type : ImageChannelDataType.values())
            {
                
                // create Image using similar method as main method
                ClearCLImage Image = lContext.createSingleChannelImage(img_type,lSize,lSize,lSize);
                
                for (NativeTypeEnum native_type : NativeTypeEnum.values()) 
                {
                    
                    // TiffWriter object
                    TiffWriter tfwtr = new TiffWriter(native_type, 1f, 0f);
                    try{
                        boolean response = tfwtr.write(Image, target_path);
                        System.out.print("\n img_type = "+img_type.toString()+"\t native_type = "+native_type.toString()+"\t\t write success = "+response);
                        
                    } catch (Throwable t) {
                        System.out.print("Throw error");
                        throw new AssertionError(t);
                    }
                }
            }
        }
      }
    
    
}
