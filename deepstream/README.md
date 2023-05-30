[Deepstream入门-如何部署深度学习模型](https://zhuanlan.zhihu.com/p/497723371)

[jetson nano docker容器使用csi相机](https://blog.csdn.net/mibu110/article/details/126806932)

[英伟达 Jetson Nano 新手必备：使用CSI或USB摄像头拍摄第一张照片](http://www.taodudu.cc/news/show-4199292.html?action=onClick)


[NVIDIA DeepStream配置文件解析；摄像头源&&RTSP拉流源输入，RTSP推流输出](https://blog.csdn.net/Yan_uuu/article/details/127389866)


# 测试摄像头
```sh

nvgstcapture-1.0 --orientation 2

```

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


## deeepstream 安装

```sh
# pc

docker pull nvcr.io/nvidia/deepstream:6.2-samples

xhost +

docker run --gpus all -it --rm --net=host --privileged -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.2 nvcr.io/nvidia/deepstream:6.2-samples

bash user_additional_install.sh

apt install wget

apt install zip

apt install vim

deepstream-app -c source30_1080p_dec_infer-resnet_tiled_display_int8.txt

#---- nano

docker pull nvcr.io/nvidia/deepstream-l4t:6.2-samples

xhost +

docker run --gpus all -it --rm --net=host --privileged -v /tmp/.X11-unix:/tmp/.X11-unix -v /tmp/argus_socket:/tmp/argus_socket --device=/dev/video0 -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.2 nvcr.io/nvidia/deepstream-l4t:6.2-samples

bash user_additional_install.sh

bash install.sh

deepstream-app -c source30_1080p_dec_infer-resnet_tiled_display_int8.txt

```
