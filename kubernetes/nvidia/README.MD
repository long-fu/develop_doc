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

sudo docker run -it --rm --net=host --runtime nvidia  -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.2 -v /tmp/.X11-unix/:/tmp/.X11-unix nvcr.io/nvidia/deepstream-l4t:6.2-base

docker run --rm -it --network=host aler9/rtsp-simple-server

docker run --gpus all -it --rm --net=host --privileged -v /tmp/argus_socket:/tmp/argus_socket -v /home/admin123/haoshuai/deepstream-6.2:/opt/nvidia/deepstream/deepstream-6.2 --device=/dev/video0 -w /opt/nvidia/deepstream/deepstream-6.2 nvcr.io/nvidia/deepstream-l4t:6.2-base

docker run -it --rm --net=host --runtime nvidia -p 8554:8554 -p 5400:5400  --privileged -v /tmp/.X11-unix:/tmp/.X11-unix -v /tmp/argus_socket:/tmp/argus_socket -v /home/admin123/haoshuai/deepstream-6.2:/opt/nvidia/deepstream/deepstream-6.2 --device=/dev/video0 -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.2 nvcr.io/nvidia/deepstream-l4t:6.2-base




sudo docker run -it --rm --net=host  -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.2 -v /tmp/.X11-unix/:/tmp/.X11-unix nvcr.io/nvidia/deepstream-l4t:6.2-base

/opt/nvidia/deepstream/deepstream-6.2/user_additional_install.sh

deepstream-app -c source30_1080p_dec_infer-resnet_tiled_display_int8.txt
```