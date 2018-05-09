__kernel void filter(__read_only image3d_t input, __write_only image3d_t output)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;
    
    //    // assign current pixel to pos0-pix0
    //    int4 pos0 = (get_global_id(0), get_global_id(1), get_global_id(2),0);
    //    //uint4 pix0 = read_imagef(input, sampler, pos0);
    //
    //    const int BLOCKSIZE = 3;
    //    //const float sigma = 30;
    //
    //    float res = 0.f;
    //    float sum = 0.f;
    //
    //    uint4 pix1;
    //    int4 pos1;
    
    int i = get_global_id(0);
    int j = get_global_id(1);
    int k = get_global_id(2);
    
    
    const int workSizeX = get_local_size(0);
    const int workSizeY = get_local_size(1);
    const int workSizeZ = get_local_size(2);
    const int workCubeSize = workSizeX*workSizeY*workSizeZ;
    
    const int localSizeX = (2*FSIZE + workSizeX);
    const int localSizeY = (2*FSIZE + workSizeY);
    const int localSizeZ = (2*FSIZE + workSizeZ);
    
    const int localCubeSize = localSizeX*localSizeY*localSizeZ;
    
    
    int iGroup = get_local_id(0);
    int jGroup = get_local_id(1);
    int kGroup = get_local_id(2);
    int indexGroup = iGroup + workSizeX*jGroup + workSizeX*workSizeY*kGroup;
    
    // iterate over volume
    for(int k = -BLOCKSIZE; k<=BLOCKSIZE; k++){
        for(int l = -BLOCKSIZE; l<=BLOCKSIZE; l++){
            for(int m = -BLOCKSIZE; m<=BLOCKSIZE; m++){
                pos1 = (int4)(pos0.x+k, pos0.y+l, pos0.z+m, 0);
                uint4 pix1 = read_imageui(input,sampler,pos1);
                //uint dpix = pix1.x-pix0.x;
                
                //float weight = exp(-1.f/sigma/sigma*dpix*dpix);
                float weight = 1./9;
                res += pix1.x*weight;
                sum += weight;
            }
        }
    }
    
    pix1.x = (uint)(res/sum);
    //pix1.x = 18;
    write_imageui(output,pos0,pix1);
}



//
//
//
//
//
//__constant sampler_t sampler =
//CLK_NORMALIZED_COORDS_FALSE
//| CLK_ADDRESS_CLAMP_TO_EDGE
//| CLK_FILTER_NEAREST;
//
//float FilterValue (__constant const float* filterWeights,
//                   const int x, const int y, const int z)
//{
//    return filterWeights[(x+y+z)/3];
//}
//
//__kernel void Filter (
//                      __read_only image2d_t input,
//                      __write_only image2d_t output)
//{
//    float filterWeights[3][3][3] = {{1,1,1}, {1,1,1}, {1,1,1}, {1,1,1}, {1,1,1}, {1,1,1}, {1,1,1}, {1,1,1}, {1,1,1}};
//    const int2 pos = {get_global_id(0), get_global_id(1), get_global_id(2)};
//
//    float4 sum = (float4)(0.0f);
//    for(int z = -1; z <= 1; z++) {
//        for(int y = -1; y <= 1; y++) {
//            for(int x = -1; x <= 1; x++)
//            //sum += FilterValue(filterWeights, x, y, z)
//            sum +=
//            * read_imagef(input, sampler, pos + (int2)(x,y,z));
//        }
//    }
//
//    write_imagef (output, (int2)(pos.x, pos.y, pos.z), sum);
//}

//__kernel void boxfilter   (__write_only image3d_t img_out, __read_only image3d_t img_in)
//{
//    const int width  = get_image_width(image);
//    const int height = get_image_height(image);
//    const int depth  = get_image_depth(image);
//
//    int x = get_global_id(0);
//    int y = get_global_id(1);
//    int z = get_global_id(2);

//float box[3][3][3] = {{1,1,1}, {1,1,1}, {1,1,1},
//    {1,1,1}, {1,1,1}, {1,1,1},
//    {1,1,1}, {1,1,1}, {1,1,1}};

//    int i, j, k;
//    float blur;
//    for (int i = -1; i < 2; i++)
//    {
//        for (int j = -1; j < 2; j++)
//        {
//            for (int k = -1; k < 2; k++)
//            {
//                //blur += img_in[x+i][y+j][z+k];
//            }
//            //cout << "Element at x[" << i
//            //<< "][" << j << "]: ";
//            //cout << x[i][j]<<endl;
//        }
//    }

//float d = (float)((pos - cen)/dim)

//float value = (float)((d == 0 )?1:0);
//float value2 = (float)( ((d > -1) && (value == 1)) ? 1:0);

//write_imagef (img_out, (int4){x,y,z,0}, 1);
//}

