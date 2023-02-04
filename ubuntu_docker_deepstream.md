

# docker deepsteam run

- [官方文档](https://docs.nvidia.com/metropolis/deepstream/dev-guide/text/DS_docker_containers.html)

Container

Container pull commands

base docker (contains only the runtime libraries and GStreamer plugins. Can be used as a base to build custom dockers for DeepStream applications)

docker pull nvcr.io/nvidia/deepstream:6.1.1-base

devel docker (contains the entire SDK along with a development environment for building DeepStream applications and graph composer)

docker pull nvcr.io/nvidia/deepstream:6.1.1-devel

Triton Inference Server docker with Triton Inference Server and dependencies installed along with a development environment for building DeepStream applications

docker pull nvcr.io/nvidia/deepstream:6.1.1-triton

DeepStream IoT docker with deepstream-test5-app installed and all other reference applications removed

docker pull nvcr.io/nvidia/deepstream:6.1.1-iot

DeepStream samples docker (contains the runtime libraries, GStreamer plugins, reference applications and sample streams, models and configs)

docker pull nvcr.io/nvidia/deepstream:6.1.1-samples





- [镜像说明](https://catalog.ngc.nvidia.com/orgs/nvidia/containers/deepstream)



## 运行容器
**运行容器：**

允许外部应用程序连接到主机的 X 显示器：
```sh
xhost +
```
运行 docker 容器（在下面的命令行中使用所需的容器标签）：
如果使用 docker（推荐）：
```sh
docker run --gpus all -it --rm --net=host --privileged -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.1 nvcr.io/nvidia/deepstream:6.1.1-devel
```
如果使用基于 19.03 之前的 docker 版本的 nvidia-docker（已弃用）：
```sh
nvidia-docker run -it --rm -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.1  nvcr.io/nvidia/deepstream:6.1.1-devel
```
请注意，该命令会在来宾文件系统中安装主机的 X11 显示器以呈现输出视频。

在 docker 容器中使用所有 DeepStreamSDK 功能的额外安装。
使用 DS 6.1.1，DeepStream docker 容器不会打包某些多媒体操作（如音频数据解析、CPU 解码和 CPU 编码）所需的库。此更改可能会影响某些视频流/文件的处理，例如包含音轨的 mp4。

请在 docker 映像中运行以下脚本以安装使用所有 DeepStreamSDK 功能可能需要的其他软件包：

/opt/nvidia/deepstream/deepstream/user_additional_install.sh


**命令行选项解释：**

-表示以交互模式运行

--gpus 选项使 GPU 可以在容器内访问。“all”的替代方法是指定一个设备（即'“'device=0'”）

--rm 完成后将删除容器

--privileged 向主机资源授予对容器的访问权限。需要此标志才能从 -devel 容器运行 Graph Composer

-v 是挂载目录，用于在容器文件系统中挂载主机的 X11 显示以渲染输出视频

用户可以根据需要挂载其他目录（使用 -v 选项）以轻松访问配置文件、模型和其他资源。（即，使用 -v /home:/home 将主目录挂载到容器文件系统中。

此外，需要包含--cap-add SYSLOG选项以启用容器内的 nvds_logger 功能

要启用 RTSP 输出，需要将网络端口从容器映射到主机，以使用命令行中的-p选项启用传入连接；例如：-p 8554:8554

请参阅容器内的 /opt/nvidia/deepstream/deepstream-6.1/README 以了解 deepstream-app 的使用。

