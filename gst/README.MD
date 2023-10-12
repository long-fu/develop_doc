
```sh

gst-launch-1.0 uridecodebin uri=https://gstreamer.freedesktop.org/data/media/sintel_trailer-480p.webm ! videoconvert ! my_filter  ! autovideosink

gst-launch-1.0 uridecodebin uri=https://gstreamer.freedesktop.org/data/media/sintel_trailer-480p.webm ! videoconvert ! clockoverlay  ! autovideosink

gst-launch-1.0 uridecodebin uri=https://gstreamer.freedesktop.org/data/media/sintel_trailer-480p.webm ! videoconvert ! timeoverlay  ! autovideosink


gst-launch-1.0 filesrc location=/home/haoshuai/video.mp4 ! videoconvert ! my_filter  ! autovideosink

gst-launch-1.0 filesrc location=/home/haoshuai/video.mp4 ! decodebin  !  videoconvert  ! my_filter  ! autovideosink

gst-launch-1.0 filesrc location=/home/haoshuai/video.mp4 ! h264parse ! avdec_h264 ! videoconvert ! my_filter  ! autovideosink

gst-launch-1.0 filesrc location=/home/haoshuai/sintel_trailer-480p.webm ! h264parse ! avdec_h264 !  videoconvert  ! my_filter  ! autovideosink

```