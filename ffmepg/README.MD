
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

ffmpeg -re -stream_loop -1 -i video0.mp4 -c copy -f rtsp -rtsp_transport tcp rtsp://127.0.0.1:8554/mystream

# tcp
ffmpeg -re -stream_loop -1 -i video0.mp4 -c copy -f rtsp -rtsp_transport tcp rtsp://localhost:8554/mystream

# 打印视频格式
ffprobe -show_format -show_streams -of json rtsp://127.0.0.1:8554/mystream

ffprobe -show_format -show_streams -of json video0.mp4

#ffmpeg -f v4l2 -i /dev/video0 -pix_fmt yuv420p -preset ultrafast -b:v 600k -f rtsp rtsp://localhost:$RTSP_PORT/$MTX_PATH

ffmpeg -i rtsp://original-source -pix_fmt yuv420p -c:v libx264 -preset ultrafast -b:v 600k -c:a aac -b:a 160k -f rtsp rtsp://localhost:8554/mystream

ffmpeg -i rtsp://original-stream -pix_fmt yuv420p -c:v libx264 -preset ultrafast -b:v 600k -max_muxing_queue_size 1024 -g 30 -f rtsp rtsp://localhost:8554/mystream

```