// Kernel borrowed from one of the 3d kernels at clearcl.ocllib.oldkernels
// this is not complete or tested
//

__kernel void hyperboloid   (__write_only image3d_t image,
                             int       cx,
                             int       cy,
                             int       cz,
                             int       tolerance
                             )
{
    const int width  = get_image_width(image);
    const int height = get_image_height(image);
    const int depth  = get_image_depth(image);
    
    float x = get_global_id(0);
    float y = get_global_id(1);
    float z = get_global_id(2);
    
    float d = (int)(pow((float)(x-cx),(int)2) + pow((float)(y-cy),(int)2) - pow((float)(z-cz),(int)2));
    
    int value = (int)((-tolerance < d && d < tolerance) ? 1:0);
    
    write_imagef (image, (int4){x,y,z,0}, value);
}

__kernel void identity(__read_only image3d_t input, __write_only image3d_t output)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;
    
    //Source pixel and position
    int4 pos0 = (get_global_id(0), get_global_id(1), get_global_id(2), 0);
    float4 pix0 = read_imagef(input, sampler, pos0);
    
    write_imagef(output, pos0, pix0);
}

__kernel void filter2(__read_only image3d_t input, __write_only image3d_t output)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;
    
    //Source pixel and position
    int4 pos0 = (get_global_id(0), get_global_id(1), get_global_id(2), 0);
    float4 pix0 = read_imagef(input, sampler, pos0);
    
    const int BLOCKSIZE = 3;
    const float sigma = 30;
    
    float res = 0.f;
    float sum = 0.f;
    
    //Target pixel and position
    int4 pos1;
    float4 pix1;
    
    for(int k = -BLOCKSIZE; k <= BLOCKSIZE; k++){
        for(int m = -BLOCKSIZE; m <= BLOCKSIZE; m++){
            for(int o = -BLOCKSIZE; o <= BLOCKSIZE; o++){
                
                // Target position is relative to source and block size
                pos1 = (int4)(pos0.x+k, pos0.y+m, pos0.z+o, 0);
                
                // Gather value of Source pixel at target position
                float4 pix1 = read_imagef(input,sampler,pos1);
                
                sum += pix1.x;
            }
        }
    }
    
    //pix1.x = (uint)(res/sum);
    //pix1.x = 18;
    pix1 = sum/27;
    write_imagef(output,pos0,pix1);
}
