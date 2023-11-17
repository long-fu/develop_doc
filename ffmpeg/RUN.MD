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

ffmpeg -f x11grab -r 10 -s 1280x720 -i :0.0+0,0 -preset superfast -tune zerolatency -pix_fmt yuv420p -c:v libx264 -c:a aac -r 10 -b:v 2M -b:a 128k -f flv rtsp://192.168.2.4:8554/live/1

ffmpeg -f x11grab -r 10 -s 1280x720 -i :0.0+0,0 -preset superfast -tune zerolatency -pix_fmt yuv420p -c:v libx264 -c:a aac -r 10 -b:v 2M -b:a 128k -f flv rtmp://localhost/myapp/mystream

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

ffmpeg -thread_queue_size 512 -re -i "rtsp://admin:ad123456@192.168.137.61:554/cam/realmonitor?channel=1&subtype=0"  -c copy -rtsp_transport tcp  -f rtsp rtsp://localhost/camtest


```



https://blog.csdn.net/Baby_of_breath/article/details/124065356




[bluenviron/mediamtx](https://github.com/bluenviron/mediamtx) 流媒体服务器
```sh

# 启动流媒体服务器
docker run --rm -it \
-e MTX_PROTOCOLS=tcp \
-p 8554:8554 \
-p 1935:1935 \
-p 8888:8888 \
-p 8889:8889 \
bluenviron/mediamtx

#udp
ffmpeg -re -stream_loop -1 -i video0.mp4 -c copy -f rtsp rtsp://127.0.0.1:8554/mystream

ffmpeg -re -stream_loop -1 -i person0.mp4 -c copy -f rtsp -rtsp_transport tcp rtsp://127.0.0.1:8554/mystream

ffmpeg -re -stream_loop -1 -i mask0.mp4 -c copy -f rtsp -rtsp_transport tcp rtsp://127.0.0.1:8554/mystream

ffmpeg -re -stream_loop -1 -i mask0.mp4 -c copy -f flv rtmp://127.0.0.1:1935/mystream

# 失败
ffmpeg -re -i mask0.264 -c copy -f rtsp -rtsp_transport tcp rtsp://127.0.0.1:8554/mystream

ffmpeg  -i mask0.264 -ss 0 -avoid_negative_ts make_zero -vcodec libx264 -crf 23 -f rtsp -rtsp_transport tcp rtsp://127.0.0.1:8554/mystream


ffmpeg -i mask0.264 -avoid_negative_ts make_zero -vcodec libx264 -crf 23 -f rtsp -rtsp_transport tcp rtsp://127.0.0.1:8554/mystream
# tcp
ffmpeg -re -stream_loop -1 -i video0.mp4 -c copy -f rtsp -rtsp_transport tcp rtsp://localhost:8554/mystream

ffmpeg -i rtsp://127.0.0.1:8554/mystream -c copy output.264

ffmpeg -re -i mask0.264 -c copy mask0.mp4

# 打印视频格式
ffprobe -show_format -show_streams -of json rtsp://127.0.0.1:8554/mystream

ffprobe -show_format -show_streams -of json rtsp://127.0.0.1:8554/mask0.264

ffprobe -show_format -show_streams -of json rtmp://192.168.2.4:1935/live0

ffprobe -show_format -show_streams -of json rtsp://admin:ad123456@192.168.137.50:554/h264

ffprobe -show_format -show_streams -of json rtsp://admin:ad123456@192.168.137.64:554/h264

ffprobe -show_format -show_streams -of json mask0.264

ffprobe -show_format -show_streams -of json mask0.mp4

#ffmpeg -f v4l2 -i /dev/video0 -pix_fmt yuv420p -preset ultrafast -b:v 600k -f rtsp rtsp://localhost:$RTSP_PORT/$MTX_PATH

# rtsp 正常 演示0.s
ffmpeg -i rtsp://admin:ad123456@192.168.137.50:554/h264 -c copy -f rtsp -rtsp_transport tcp rtsp://127.0.0.1:8554/mystream

# rtmp 正常 延迟 4s
ffmpeg -i rtsp://admin:ad123456@192.168.137.50:554/h264 -c copy -f flv rtmp://127.0.0.1:1935/mystream

# 错误 导致丢帧
ffmpeg -re -i rtsp://admin:ad123456@192.168.137.50:554/h264 -c copy -f flv rtmp://127.0.0.1:1935/mystream

# 错误
ffmpeg -i rtsp://admin:ad123456@192.168.137.50:554/h264 -pix_fmt yuv420p -b:v 30k -c:v libx264 -f flv rtmp://127.0.0.1:1935/mystream

ffmpeg -i rtsp://admin:ad123456@192.168.137.50:554/h264 -c copy -f flv rtmp://127.0.0.1:1935/mystream

rtsp://admin:ad123456@192.168.137.50:554/h264



ffmpeg -i rtsp://original-source -pix_fmt yuv420p -c:v libx264 -preset ultrafast -b:v 600k -c:a aac -b:a 160k -f rtsp rtsp://localhost:8554/mystream

ffmpeg -i rtsp://original-stream -pix_fmt yuv420p -c:v libx264 -preset ultrafast -b:v 600k -max_muxing_queue_size 1024 -g 30 -f rtsp rtsp://localhost:8554/mystream

# 问题1 -> 转换成mp4 

ffmpeg -re -i mask0.264 -c copy mask0.mp4

ffprobe -show_format -show_streams -of json mask0.mp4

ffmpeg -re -stream_loop -1 -i mask0.mp4 -c copy -f rtsp -rtsp_transport tcp rtsp://127.0.0.1:8554/mystream

#0:0[0x1](und): Video: h264 (High) (avc1 / 0x31637661), yuv420p(tv, bt709, progressive), 1920x1080, 3057 kb/s, 25 fps, 25 tbr, 1200k tbn (default)


# 问题2 -> 大分辨率问题

# 失败
ffmpeg -re -i person0.mp4 -c copy -s 1920x1080 person0_out.mp4

ffprobe -show_format -show_streams -of json person0.mp4

ffprobe -show_format -show_streams -of json person0_out.mp4

# 成功
ffmpeg -i person0.mp4 -vf scale=1920:1080 person0_out.mp4 -hide_banner

ffprobe -show_format -show_streams -of json person0_out.mp4

ffmpeg -re -stream_loop -1 -i person0_out.mp4 -c copy -f rtsp -rtsp_transport tcp rtsp://127.0.0.1:8554/mystream

ffprobe -show_format -show_streams -of json rtsp://127.0.0.1:8554/mystream



#0:0[0x1](und): Video: h264 (High) (avc1 / 0x31637661), yuv420p(tv, bt709, progressive), 3840x2160, 10994 kb/s, 24 fps, 24 tbr, 12288 tbn (default)

faile
Stream #0:0: Video: hevc (Main), yuvj420p(pc, bt709), 1280x720, 25 fps, 25 tbr, 90k tbn, 25 tbc
"codec_name": "hevc",
"codec_long_name": "H.265 / HEVC (High Efficiency Video Coding)",

Stream #0:0: Video: h264 (Main), yuvj420p(pc, bt709, progressive), 1280x720, 25 fps, 25 tbr, 90k tbn, 50 tbc


success
Stream #0:0: Video: h264 (Main), yuvj420p(pc, bt709, progressive), 1280x720, 25 fps, 25 tbr, 90k tbn, 50 tbc
            "codec_name": "h264",
            "codec_long_name": "H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10",
```




