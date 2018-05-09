__kernel void filter(__read_only image3d_t input, __write_only image3d_t output)
{
    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_NONE | CLK_FILTER_NEAREST;
    
    
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