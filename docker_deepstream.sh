# deepstream

## deepstream run docker

```sh
# 开启界面
xhost +

docker run --gpus all -it --rm --net=host --privileged -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.1 nvcr.io/nvidia/deepstream:6.1.1-devel
# or
nvidia-docker run -it --rm -v /tmp/.X11-unix:/tmp/.X11-unix -v /home/haoshuai/code/tao/deepstream_tao_apps:/opt/nvidia/deepstream_tao_apps -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.1  nvcr.io/nvidia/deepstream:6.1.1-devel
```
