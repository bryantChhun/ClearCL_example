// A custom kernel to create the hyperboloid:
// x^2 + y^2 - z^2 = 0
//default sphere cx=0i
//default sphere cy=0i
//default sphere cz=0i


__kernel void hyperboloid   (__write_only image3d_t image,
                        int       cx,
                        int       cy,
                        int       cz
                        )
{
    const int width  = get_image_width(image);
    const int height = get_image_height(image);
    const int depth  = get_image_depth(image);
    
    //float4 dim = (float4){width,height,depth,1};
    
    int x = get_global_id(0);
    int y = get_global_id(1);
    int z = get_global_id(2);
    
    int d = floor(pow((float)(x-cx),(int)2) + pow((float)(y-cy),(int)2) - pow((float)(z-cz),(int)2));
    
    float value = (float)((d == 0 )?1:0);
    
    write_imagef (image, (int4){x,y,z,0}, value);
}


// Another attempt to create the hyperboloid
__kernel void hyperboloid_2   (__write_only image3d_t image,
                             int       cx,
                             int       cy,
                             int       cz
                             )
{
    
    int x = get_global_id(0);
    int y = get_global_id(1);
    int z = get_global_id(2);

    int d = floor(pow((float)(x-cx),(int)2) + pow((float)(y-cy),(int)2) - pow((float)(z-cz),(int)2));
    
    write_imagef (image, (int4){x,y,z,0}, 1*(pow((float)(x-cx),(int)2) + pow((float)(y-cy),(int)2) - pow((float)(z-cz),(int)2)));
}
