
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

ffmpeg -re -stream_loop -1 -i person0.264 -c copy -f rtsp -rtsp_transport tcp rtsp://127.0.0.1:8554/mystream

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

ffprobe -show_format -show_streams -of json rtsp://admin:ad123456@192.168.137.50:554/h264

ffprobe -show_format -show_streams -of json mask0.264

ffprobe -show_format -show_streams -of json mask0.mp4

#ffmpeg -f v4l2 -i /dev/video0 -pix_fmt yuv420p -preset ultrafast -b:v 600k -f rtsp rtsp://localhost:$RTSP_PORT/$MTX_PATH

ffmpeg -re -i rtsp://admin:ad123456@192.168.137.50:554/h264 -c copy -f rtsp -rtsp_transport tcp rtsp://127.0.0.1:8554/mystream

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

```