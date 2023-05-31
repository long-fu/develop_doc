# Deepstream Docker容器方式在nx上运行

## Install the nvidia-container-toolkit

```sh
distribution=$(. /etc/os-release;echo $ID$VERSION_ID)
curl -s -L https://nvidia.github.io/libnvidia-container/gpgkey | sudo apt-key add -
curl -s -L https://nvidia.github.io/libnvidia-container/$distribution/libnvidia-container.list | sudo tee /etc/apt/sources.list.d/libnvidia-container.list

sudo apt-get update && sudo apt-get install -y nvidia-container-toolkit

sudo dpkg --get-selections | grep nvidia

sudo docker info | grep nvidia

```

## Configure docker

```json
{
    "default-runtime": "nvidia",
    "runtimes": {
        "nvidia": {
            "path": "/usr/bin/nvidia-container-runtime",
            "runtimeArgs": []
        }
    }
}
```

And then restart docker:

```sh
sudo systemctl restart docker
```

## Run Docker

```sh

docker pull nvcr.io/nvidia/deepstream-l4t:6.2-base

xhost +

sudo xhost +si:localuser:root

# 最基础的
sudo docker run -it --rm --net=host --runtime nvidia  -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.2 -v /tmp/.X11-unix/:/tmp/.X11-unix nvcr.io/nvidia/deepstream-l4t:6.2-base

# docker run --rm -it --network=host aler9/rtsp-simple-server

# 改造
docker run --runtime nvidia -it --rm -p 8554:8554 -p 5400:5400 --privileged -v /tmp/argus_socket:/tmp/argus_socket -v /home/admin123/haoshuai/deepstream-6.2:/opt/nvidia/deepstream/deepstream-6.2 --device=/dev/video0 -w /opt/nvidia/deepstream/deepstream-6.2 nvcr.io/nvidia/deepstream-l4t:6.2-base

# 只能在主机上运行
docker run --runtime nvidia -it --rm -p 8554:8554 -p 5400:5400 --privileged -v /tmp/.X11-unix:/tmp/.X11-unix -v /tmp/argus_socket:/tmp/argus_socket -v /home/admin123/haoshuai/deepstream-6.2:/opt/nvidia/deepstream/deepstream-6.2 -e DISPLAY=$DISPLAY --device=/dev/video0 -w /opt/nvidia/deepstream/deepstream-6.2 nvcr.io/nvidia/deepstream-l4t:6.2-base

docker run --runtime nvidia -it --rm -p 8554:8554 -p 5400:5400 --privileged -v /tmp/.X11-unix:/tmp/.X11-unix -v /tmp/argus_socket:/tmp/argus_socket -v /home/admin123/haoshuai/deepstream-6.2:/opt/nvidia/deepstream/deepstream-6.2 -e DISPLAY=:1 --device=/dev/video0 -w /opt/nvidia/deepstream/deepstream-6.2 nvcr.io/nvidia/deepstream-l4t:6.2-base

# 容器中运行的命名----
./install.sh
./user_additional_install.sh
deepstream-app -c samples/configs/deepstream-app/source1_csi_dec_infer_resnet_int8.txt
# ---- end

# 测试流

rtsp://192.168.8.62:8554/ds-test
```

## 制作自己的 Docker

```Dockerfile
FROM nvcr.io/nvidia/deepstream-l4t:6.2-base

RUN apt-get update && \
apt-get install -y gstreamer1.0-libav && \
apt-get install --reinstall -y gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-plugins-ugly libavutil56 libavcodec58 libavformat58 libavfilter7 libx264-155 libde265-dev libde265-0 libx265-179 libvpx6 libmpeg2encpp-2.1-0 libmpeg2-4 libmpg123-0

COPY deepstream-6.2 /opt/nvidia/deepstream/deepstream-6.2

WORKDIR /opt/nvidia/deepstream/deepstream-6.2


```

```sh
docker build -t haoshuai/deepstream-l4t:6.2 .

docker run -it --rm --runtime nvidia -p 8555:8554 -p 5401:5400 --privileged -v /tmp/argus_socket:/tmp/argus_socket --device=/dev/video0 haoshuai/deepstream-l4t:6.2
# 容器中运行的cmd --- 
./install.sh
deepstream-app -c samples/configs/deepstream-app/source1_csi_dec_infer_resnet_int8.txt
# ---

# 测试拉刘

rtsp://192.168.8.62:8555/ds-test

##---

```

### 直接启动

```dockerfile
FROM nvcr.io/nvidia/deepstream-l4t:6.2-base

RUN apt-get update && \
apt-get install -y gstreamer1.0-libav && \
apt-get install --reinstall -y gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-plugins-ugly libavutil56 libavcodec58 libavformat58 libavfilter7 libx264-155 libde265-dev libde265-0 libx265-179 libvpx6 libmpeg2encpp-2.1-0 libmpeg2-4 libmpg123-0

COPY deepstream-6.2 /opt/nvidia/deepstream/deepstream-6.2

WORKDIR /opt/nvidia/deepstream/deepstream-6.2

RUN ./install.sh

ENTRYPOINT ["deepstream-app", "-c"]
CMD [ "./samples/configs/deepstream-app/source1_csi_dec_infer_resnet_int8.txt" ]

```

```sh
# 直接后台运行
docker run -d --runtime nvidia -p 8555:8554 -p 5401:5400 --privileged -v /tmp/argus_socket:/tmp/argus_socket --device=/dev/video0 haoshuai/deepstream-l4t:6.2
```

## k8s用例

```sh
kubectl create -f deepstream_not_cmd.yaml
```

在边端同一共工作网络中拉流

rtsp://192.168.8.62:8554/ds-test