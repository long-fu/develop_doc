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

sudo ffmpeg -video_size 2560x1600 -framerate 60 -f x11grab -i $DISPLAY -rtsp_transport tcp -f rtsp rtsp://127.0.0.1:8554/mystream

```
https://blog.csdn.net/Baby_of_breath/article/details/124065356








