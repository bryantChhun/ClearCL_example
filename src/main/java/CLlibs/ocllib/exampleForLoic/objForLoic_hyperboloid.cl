// A custom kernel to create the hyperboloid:
// x^2 + y^2 - z^2 = 0
//default sphere cx=0i
//default sphere cy=0i
//default sphere cz=0i
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
