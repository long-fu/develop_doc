[基于FFmpeg进行rtsp推流及拉流](https://blog.csdn.net/chan1987818/article/details/128219230)

`xrandr` 不带参数的xrandr命令会列出, 系统支持的视频接口名称和设备连接情况, 当前分辨率 & 支持的最大、最小分辨率.

```sh

➜  ~ xrandr
Screen 0: minimum 8 x 8, current 2560 x 1600, maximum 32767 x 32767
DP-0 disconnected (normal left inverted right x axis y axis)
DP-1 disconnected (normal left inverted right x axis y axis)
DP-2 disconnected (normal left inverted right x axis y axis)
DP-3 disconnected (normal left inverted right x axis y axis)
HDMI-0 disconnected (normal left inverted right x axis y axis)
DP-4 connected primary 2560x1600+0+0 (normal left inverted right x axis y axis) 344mm x 215mm
   2560x1600     60.00*+ 165.00  

#录制全屏，无声音
xhost +



sudo ffmpeg -video_size 2560x1600 -framerate 60 -f x11grab -i $DISPLAY ~/Video/output.mp4

# video_size：所录制视频的分辨率大小（和自己的屏幕分辨率一致时，可录制全部的屏幕
# framerate：屏幕录制采集的帧数（通常选择25帧）
# -f x11grab：使用x11grab设备进行录制视频
# -i $DISPLAY：录制的屏幕。环境变量DISPLAY用来设置将图形显示到何处

# mp4
sudo ffmpeg -video_size 2560x1600 -framerate 60 -f x11grab -i $DISPLAY -rtsp_transport tcp -f rtsp rtsp://127.0.0.1:8554/mystream

./configure --enable-shared --enable-swscale --enable-gpl --enable-nonfree --enable-pic --enable-postproc --enable-pthreads --enable-static --enable-libx264

./configure  --enable-gpl --enable-shared --enable-libx264 --enable-pthreads

ffmpeg -f x11grab -r 25 -s 1920x1080 -i :0.0+0,0 -pix_fmt yuv420p -c:v libx264 -rtsp_transport tcp -f rtsp rtsp://127.0.0.1:8554/mystream

# libx264 -vcodec libx264命令错误
# ffmpeg -video_size 2560x1600 -framerate 60 -f x11grab -pix_fmt yuv420p -c:v libx264 -i $DISPLAY -rtsp_transport tcp -f rtsp rtsp://127.0.0.1:8554/mystream

# 推流成功
ffmpeg -stream_loop -1 -i person0.mp4 -vcodec libx264 -tune zerolatency  -preset superfast -rtsp_transport tcp -f rtsp rtsp://127.0.0.1:8554/mystream

# 推流成功
ffmpeg -re -i mask0.264 -c copy -vcodec libx264 -rtsp_transport tcp -f rtsp rtsp://127.0.0.1:8554/mystream

ffmpeg -i mask0.264 -c:v libx264 -vf format=yuv420 -rtsp_transport tcp -f rtsp rtsp://127.0.0.1:8554/mystream

ffmpeg -i input.avi -c:v libx264 -preset slow -crf 20 -c:a aac -b:a 160k -vf format=yuv420p -movflags +faststart output.mp4

ffmpeg -re -i mask0.264 -c copy -vcodec libx264 -f rtsp rtsp://127.0.0.1:8554/mystream





ffmpeg -stream_loop -1 -i mask0.h264 -rtsp_transport tcp -f rtsp rtsp://127.0.0.1:8554/mystream

ffmpeg -re -stream_loop -1 -i person0.mp4 -c:v libx264 -codec copy -rtsp_transport tcp -f rtsp rtsp rtsp://127.0.0.1:8554/mystream



ffmpeg -re -stream_loop -1 -i mask0.h264 -c copy -rtsp_transport tcp -f rtsp rtsp://127.0.0.1:8554/mystream

ffmpeg -f x11grab -r 10 -s 1920x1080 -i :0.0+0,0 -preset superfast -tune zerolatency -pix_fmt yuv420p -c:v libx264 -c:a aac -r 10 -b:v 2M -b:a 128k -f flv rtmp://127.0.0.1/live/1

#f x11grab -r 10 -s 1920x1080 -i :0.0+0,0: 屏幕捕捉帧率为10，图像像素为1920x1080，像素区域为从做上角0.0区域开始计算；
#pix_fmt yuv420p：像素格式为yuv420p（4:2:0 YUV）；
#preset superfast -tune zerolatency：实时编码；
#q:v 0.05：可以看做压缩率，值越小，压缩率越低，质量越高；
#r 10：同捕捉帧率相同

cat  /etc/ld.so.conf 
# include ld.so.conf.d/*.conf
# /usr/local/ffmpeg/lib
# /usr/local/lib

docker run --rm -it \
-e MTX_PROTOCOLS=tcp \
-p 8554:8554 \
-p 1935:1935 \
-p 8888:8888 \
-p 8889:8889 \
bluenviron/mediamtx

```



https://blog.csdn.net/Baby_of_breath/article/details/124065356








