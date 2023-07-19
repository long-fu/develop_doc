// typedef enum PixelFmt
// {
//     PIXEL_YUV420SP,
//     PIXEL_YVU420SP,
//     PIXEL_YUV420P,
//     PIXEL_YVU420P,
//     PIXEL_YUV422SP,
//     PIXEL_YVU422SP,
//     PIXEL_YUV422P,
//     PIXEL_YVU422P,
//     PIXEL_YUYV422,
//     PIXEL_UYVY422,
//     PIXEL_YVYU422,
//     PIXEL_VYUY422,
//     PIXEL_ARGB,
//     PIXEL_RGBA,
//     PIXEL_ABGR,
//     PIXEL_BGRA,
// } PixelFmt;

// int getFrameBitSize(int w,int h,PixelFmt fmt)
// {
//     int pixelNum = w*h;
//     int frameBitSize = 0;
//     switch(fmt)
//     {
//         case PIXEL_YUV420SP...PIXEL_YVU420P:
//             frameBitSize = pixelNum*3/2;
//             break;
//         case PIXEL_YUV422SP...PIXEL_VYUY422:
//             frameBitSize = pixelNum*2;
//             break;
//         case PIXEL_ARGB...PIXEL_BGRA:
//             frameBitSize = pixelNum*4;
//             break;
//     }
//     return frameBitSize;
// }
// \