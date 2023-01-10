# jeston nano 部署tao ssd
echo "sudo docker run --runtime nvidia -it --rm --network host \
    --volume ~/nvdli-data:/nvdli-nano/data \
    --volume /tmp/argus_socket:/tmp/argus_socket \
    --device /dev/video0 \
    nvcr.io/nvidia/dli/dli-nano-ai:v2.0.2-r32.7.1zh" > docker_dli_run.sh
chmod +x docker_dli_run.sh
./docker_dli_run.sh

## Nano 系统安装

## 将 TAO 模型集成到 DeepStream 中

https://docs.nvidia.com/tao/tao-toolkit/text/deepstream_tao_integration.html#tensorrt_oss

训练 裁剪 导出
https://docs.nvidia.com/tao/tao-toolkit/text/object_detection/ssd.html#deploying-to-deepstream-ssd

TensorRT 引擎生成、验证和 int8 校准
https://docs.nvidia.com/tao/tao-toolkit/text/tao_deploy/ssd.html#id1

部署到 DeepStream
https://docs.nvidia.com/tao/tao-toolkit/text/ds_tao/ssd_ds.html#deploying-to-deepstream-ssd

## deepstream

- [官方文档](https://docs.nvidia.com/metropolis/deepstream/dev-guide/index.html)

- [安装deepstream](https://docs.nvidia.com/metropolis/deepstream/dev-guide/text/DS_Quickstart.html#jetson-setup)

- [deepstream-app运行测试](https://docs.nvidia.com/metropolis/deepstream/dev-guide/text/DS_ref_app_deepstream.html
)

- [deepstream历史版本下载](https://developer.nvidia.com/embedded/deepstream-on-jetson-downloads-archived)

## tao_converter

- [TAO Converter 版本介绍](https://docs.nvidia.com/tao/tao-toolkit/text/ds_tao/tao_converter.html)
- [TAO Converter 历史版本](https://catalog.ngc.nvidia.com/orgs/nvidia/teams/tao/resources/tao-converter/version)
- [Generating an Engine Using tao-converter](https://docs.nvidia.com/tao/tao-toolkit/text/ds_tao/ssd_ds.html#deploying-to-deepstream-ssd)

## tao 

[Integrate TAO model with DeepStream SDK](https://github.com/NVIDIA-AI-IOT/deepstream_tao_apps)

[Integrating TAO Models into DeepStream](https://docs.nvidia.com/tao/tao-toolkit/text/deepstream_tao_integration.html#tensorrt_oss)

[NVIDIA DeepStream SDK Developer Guide](https://docs.nvidia.com/metropolis/deepstream/dev-guide/index.html)

[Deploying to DeepStream for SSD](https://docs.nvidia.com/tao/tao-toolkit/text/ds_tao/ssd_ds.html#deploying-to-deepstream-ssd)


----

nano 运行tao的模型

# 制作 Jetson nano 系统

```sh
sudo apt update
sudo apt upgrade
sudo apt autoremove
```

# 安装jtop

https://github.com/rbonghi/jetson_stats

```sh
sudo apt-get install python3-pip
sudo -H pip3 install -U jetson-stats
```

# 安装 DeepStream 

https://docs.nvidia.com/metropolis/deepstream/dev-guide/text/DS_Quickstart.html#jetson-setup

Install Dependencies

Enter the following commands to install the prerequisite packages: 
```sh
sudo apt install \
libssl1.1 \
libgstreamer1.0-0 \
gstreamer1.0-tools \
gstreamer1.0-plugins-good \
gstreamer1.0-plugins-bad \
gstreamer1.0-plugins-ugly \
gstreamer1.0-libav \
libgstreamer-plugins-base1.0-dev \
libgstrtspserver-1.0-0 \
libjansson4 \
libyaml-cpp-dev
```

Install librdkafka (to enable Kafka protocol adaptor for message broker)

1. Clone the librdkafka repository from GitHub:

```sh
$ git clone https://github.com/edenhill/librdkafka.git
```

2. Configure and build the library:

```sh
$ cd librdkafka
$ git reset --hard 7101c2310341ab3f4675fc565f64f0967e135a6a
./configure
$ make
$ sudo make install
```

3. Copy the generated libraries to the deepstream directory:

```sh
$ sudo mkdir -p /opt/nvidia/deepstream/deepstream-6.1/lib
$ sudo cp /usr/local/lib/librdkafka* /opt/nvidia/deepstream/deepstream-6.1/lib

export CUDA_HOME=/usr/local/cuda-10.2
export CUDA_INSTALL_DIR=/usr/local/cuda-10.2
export LD_LIBRARY_PATH=/usr/local/cuda-10.2/lib64:$LD_LIBRARY_PATH
export PATH=usr/local/cuda-10.2/bin:$PATH

export CPATH=/usr/local/cuda-10.2/targets/aarch64-linux/include:$CPATH
export LD_LIBRARY_PATH=/usr/local/cuda-10.2/targets/aarch64-linux/lib:$LD_LIBRARY_PATH
export PATH=/usr/local/cuda-10.2/bin:$PATH

```

Install the DeepStream SDK

找到相对应合适的[版本]((https://developer.nvidia.com/embedded/deepstream-on-jetson-downloads-archived))

Using the DeepStream tar package:
```sh
$  sudo tar -xvf deepstream_sdk_v6.1.1_jetson.tbz2 -C /
$ cd /opt/nvidia/deepstream/deepstream-6.1
$ sudo ./install.sh
$ sudo ldconfig
```

## 安装 TAO Converter

TensorRT OSS on Jetson (ARM64)
下载已经编译好的
[TRT-OSS](https://github.com/NVIDIA-AI-IOT/deepstream_tao_apps/tree/master/TRT-OSS/Jetson)
Replace "libnvinfer_plugin.so*" with the newly generated.

```sh
sudo mv /usr/lib/aarch64-linux-gnu/libnvinfer_plugin.so.8.x.y ${HOME}/libnvinfer_plugin.so.8.x.y.bak   // backup original libnvinfer_plugin.so.x.y
sudo cp `pwd`/out/libnvinfer_plugin.so.8.m.n  /usr/lib/aarch64-linux-gnu/libnvinfer_plugin.so.8.x.y
sudo ldconfig
```

找到相对应的版本
[下载](https://catalog.ngc.nvidia.com/orgs/nvidia/teams/tao/resources/tao-converter/version)

1. Copy the executable to the target device 
2. Install openssl library using the following command.
   1. $ sudo apt-get install libssl-dev
3. Export the following environment variables
   1. For an x86 platform:
      1. $ export TRT_LIB_PATH="/usr/lib/x86_64-linux-gnu"
      2. $ export TRT_INC_PATH="/usr/include/x86_64-linux-gnu"
   2. For an aarch platform:
      1. export TRT_LIB_PATH=/usr/lib/aarch64-linux-gnu
      2. export TRT_INCLUDE_PATH=/usr/include/aarch64-linux-gnu
4. Run the tao-converter

## 模型转换

Using the tao-converter
```sh
tao-converter [-h] -k <encryption_key>
                   -d <input_dimensions>
                   -o <comma separated output nodes>
                   [-c <path to calibration cache file>]
                   [-e <path to output engine>]
                   [-b <calibration batch size>]
                   [-m <maximum batch size of the TRT engine>]
                   [-t <engine datatype>]
                   [-w <maximum workspace size of the TRT Engine>]
                   [-i <input dimension ordering>]
                   [-p <optimization_profiles>]
                   [-s]
                   [-u <DLA_core>]
                   input_file
```
