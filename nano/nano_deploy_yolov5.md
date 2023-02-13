# jeston nano部署yolov5

## 制作 Jetson nano 系统

[Jetson nano 系统制作](https://developer.nvidia.com/embedded/learn/get-started-jetson-nano-devkit#write)

## 升级

```sh
sudo apt update
sudo apt upgrade
sudo apt autoremove
```

## 安装jtop

[jtop](https://github.com/rbonghi/jetson_stats)

```sh
sudo apt-get install python3-pip
sudo -H pip3 install -U jetson-stats
```

## 安装 deepstream

[deepstream](https://developer.nvidia.com/deepstream-getting-started)
[deepstream历史版本](https://developer.nvidia.com/embedded/deepstream-on-jetson-downloads-archived)
[在jetson平台上安装](https://docs.nvidia.com/metropolis/deepstream/dev-guide/text/DS_Quickstart.html#jetson-setup)

## yolov5部署教程

- [官网](https://docs.ultralytics.com/tutorials/nvidia-jetson/)

### 安装必要的包

```sh

# 这里建议python版本升级到3.7在进行安装

sudo apt update

sudo apt install -y python3-pip

pip3 install --upgrade pip

git clone https://github.com/ultralytics/yolov5

cd yolov5

vi requirements.txt

# 注释下面行代码
# torch>=1.7.0

# torchvision>=0.8.1

sudo apt install -y libfreetype6-dev

pip3 install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple


```

### 安装 PyTorch 和 Torchvision

我们无法从 pip 安装 PyTorch 和 Torchvision，因为它们不兼容在基于ARM aarch64 架构的 Jetson 平台上运行。因此，我们需要手动安装预构建的 PyTorch pip wheel 并从源代码编译/安装 Torchvision。

**PyTorch v1.10.0**

支持 JetPack 4.4 (L4T R32.4.3) / JetPack 4.4.1 (L4T R32.4.4) / JetPack 4.5 (L4T R32.5.0) / JetPack 4.5.1 (L4T R32.5.1) / JetPack 4.6 (L4T R32.6.1)使用 Python 3.6

文件名： torch-1.10.0-cp36-cp36m-linux_aarch64.whl

网址： https ://nvidia.box.com/shared/static/fjtbno0vpo676a25cgvuqc1wty0fkkg6.whl

**PyTorch v1.12.0**

支持 JetPack 5.0 (L4T R34.1.0) / JetPack 5.0.1 (L4T R34.1.1) / JetPack 5.0.2 (L4T R35.1.0) 和 Python 3.8

文件名： torch-1.12.0a0+2c916ef.nv22.3-cp38-cp38-linux_aarch64.whl


例如，这里我们运行的是JP4.6.1，因此我们选择PyTorch v1.10.0

```sh
cd ~

sudo apt-get install -y libopenblas-base libopenmpi-dev

wget https://nvidia.box.com/shared/static/fjtbno0vpo676a25cgvuqc1wty0fkkg6.whl -O torch-1.10.0-cp36-cp36m-linux_aarch64.whl

pip3 install torch-1.10.0-cp36-cp36m-linux_aarch64.whl

sudo apt install -y libjpeg-dev zlib1g-dev

git clone --branch v0.11.1 https://github.com/pytorch/vision torchvision

cd torchvision

sudo python3 setup.py install 

```

这里根据PyTorch版本列出需要安装的对应的torchvision版本：

- PyTorch v1.10 - torchvision v0.11.1

- PyTorch v1.12 - torchvision v0.13.0


### YOLOv5 的 DeepStream 配置

1. clone DeepStream-Yolo

```sh
cd ~

git clone https://github.com/marcoslucianops/DeepStream-Yolo


```

2. 将gen_wts_yoloV5.py从DeepStream-Yolo/utils复制到yolov5目录

```sh
cp DeepStream-Yolo/utils/gen_wts_yoloV5.py yolov5
```

3. 在 yolov5 存储库中，从 YOLOv5 版本下载pt 文件（YOLOv5s 6.1 的示例）| 这是测试用例，我们要用自己训练好的模型

```sh

cd yolov5

wget https://github.com/ultralytics/yolov5/releases/download/v6.1/yolov5s.pt

```

4. 生成cfg和wts文件

```sh


# 要更改推理大小（默认值：640）
#
python3 gen_wts_yoloV5.py -w yolov5s.pt -s 640
#
python3 gen_wts_yoloV5.py -w yolov5s.pt -s 1280

```

**注意：**要更改推理大小（默认值：640）

```sh
-s SIZE

--size SIZE

-s HEIGHT WIDTH

--size HEIGHT WIDTH



Example for 1280:

-s 1280

or

-s 1280 1280

```

5. 将生成的cfg和wts文件复制到DeepStream-Yolo文件夹中

```sh
cp yolov5s.cfg ~/DeepStream-Yolo

cp yolov5s.wts ~/DeepStream-Yolo

```

6. 打开DeepStream-Yolo文件夹，编译库

```sh
cd ~/DeepStream-Yolo

# For DeepStream 6.1

CUDA_VER=11.4 make -C nvdsinfer_custom_impl_Yolo

# For DeepStream 6.0.1 / 6.0

CUDA_VER=10.2 make -C nvdsinfer_custom_impl_Yolo

```

7. 根据您的模型编辑config_infer_primary_yoloV5.txt文件

```sh
[property]

...

custom-network-config=yolov5s.cfg

model-file=yolov5s.wts

...

```

8. 编辑deepstream_app_config文件
   
```sh
...

[primary-gie]

...

config-file=config_infer_primary_yoloV5.txt

```

9. 更改deepstream_app_config文件中的视频源。此处加载了默认视频文件，如下所示

```sh
...

[source0]

...

uri=file:///opt/nvidia/deepstream/deepstream/samples/streams/sample_1080p_h264.mp4

```

### 运行

```sh
deepstream-app -c deepstream_app_config.txt
```

![gif](images/FP32-yolov5s.gif)

[deepstream](https://developer.nvidia.com/deepstream-getting-started)

[deepstream历史版本](https://developer.nvidia.com/embedded/deepstream-on-jetson-downloads-archived)

[在jetson平台上安装](https://docs.nvidia.com/metropolis/deepstream/dev-guide/text/DS_Quickstart.html#jetson-setup)

-----------------------------------------


## nano部署yolov5

## 访问Jetson设备终端，安装pip并升级
```sh
sudo apt update
sudo apt install -y python3-pip
pip3 install --upgrade pip
```


## 安装deepstream6.0

- [安装](https://docs.nvidia.com/metropolis/deepstream/dev-guide/text/DS_Quickstart.html#jetson-setup)

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

git clone https://github.com/edenhill/librdkafka.git

cd librdkafka
git reset --hard 7101c2310341ab3f4675fc565f64f0967e135a6a
./configure
make
sudo make install

# 下载 deepstream_sdk_v6.0.1_jetson.tbz2

sudo tar -xvf deepstream_sdk_v6.0.1_jetson.tbz2 -C /

cd /opt/nvidia/deepstream/deepstream-6.1
sudo ./install.sh
sudo ldconfig

sudo cp /usr/local/lib/librdkafka* /opt/nvidia/deepstream/deepstream-6.0/lib

```

## 安装yolo v5 环境 



https://github.com/matplotlib/matplotlib/issues/16027

```sh
# 升级python
./configure --prefix=/usr/local/python3.7.16
make
sudo make install
export PATH=$PATH:$HOME/bin:/usr/local/python3.7.16/bin


mv /usr/bin/python3 /usr/bin/python3.bak
ln -s /usr/local/python3.7.16/bin/python3.7 /usr/bin/python
mv /usr/bin/pip /usr/bin/pip.bak
ln -s /usr/local/python3.7.16/bin/pip3 /usr/bin/pip

sudo ln -s /usr/local/python3.7.16/bin/python3.7 /usr/bin/python3

git clone https://github.com/ultralytics/yolov5
cd yolov5
vi requirements.txt

# .编辑以下行。这里需要先按i进入编辑模式。按ESC，然后输入:wq保存并退出
# torch>=1.7.0
# torchvision>=0.8.1

#注意：暂时排除 torch 和 torchvision，因为它们将在稍后安装。

sudo apt install -y libfreetype6-dev
sudo apt install -y libfreetype6-dev

pip3 install -r requirements.txt

# 添加源
pip3 install -r requirements.txt -i http://mirrors.aliyun.com/pypi/simple/ --trusted-host=mirrors.aliyun.com

# 超快
pip3 install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn
pip install --no-binary :all: psutil  -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn

python3 -m pip install psutil -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn

pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn

pip3 install --upgrade pip setuptools wheel -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn

pip3 install --upgrade pip 
pip3 install --upgrade protobuf -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn
pip3 install --upgrade numpy -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn
pip3 install --upgrade pandas -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn
pip3 install "matplotlib==3.3.4" -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn

python -m pip install -r requirements/dev/dev-requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn

pip3 install -r requirements/dev/dev-requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn

pip3 install pydata_sphinx_theme-0.12.0-py3-none-any.whl -i https://pypi.tuna.tsinghua.edu.cn/simple/ --trusted-host=pypi.tuna.tsinghua.edu.cn
# matplotlib
# sudo apt-get install python3-matplotlib 这种方法安装版本过低


# opencv-python 这两个包在nano上需要手动编译

``` 

## 安装 PyTorch 和 Torchvision

- PyTorch v1.10.0

> 支持 JetPack 4.4 (L4T R32.4.3) / JetPack 4.4.1 (L4T R32.4.4) / JetPack 4.5 (L4T R32.5.0) / JetPack 4.5.1 (L4T R32.5.1) / JetPack 4.6 (L4T R32.6.1)使用 Python 3.6

- PyTorch v1.12.0

> 支持 JetPack 5.0 (L4T R34.1.0) / JetPack 5.0.1 (L4T R34.1.1) / JetPack 5.0.2 (L4T R35.1.0) 和 Python 3.8

这里根据PyTorch版本列出需要安装的对应的torchvision版本：

> PyTorch v1.10 - torchvision v0.11.1
>
> PyTorch v1.12 - torchvision v0.13.0

**我们运行的是JP4.6.1 PyTorch v1.10 - torchvision v0.11.1**

- 安装 PyTorch

```sh
sudo apt-get install -y libopenblas-base libopenmpi-dev
wget https://nvidia.box.com/shared/static/fjtbno0vpo676a25cgvuqc1wty0fkkg6.whl -O torch-1.10.0-cp36-cp36m-linux_aarch64.whl
pip3 install torch-1.10.0-cp36-cp36m-linux_aarch64.whl
```

- 安装 Torchvision

```sh
sudo apt install -y libjpeg-dev zlib1g-dev
git clone --branch v0.11.1 https://github.com/pytorch/vision torchvision
cd torchvision
sudo python3 setup.py install 
```

## DeepStream-Yolo

```sh
git clone https://github.com/marcoslucianops/DeepStream-Yolo

cp DeepStream-Yolo/utils/gen_wts_yoloV5.py yolov5

wget https://github.com/ultralytics/yolov5/releases/download/v6.1/yolov5s.pt

#生成cfg和wts文件

python3 gen_wts_yoloV5.py -w yolov5s.pt
```


# 注意：要更改推理大小（默认值：640）

```sh
-s SIZE
--size SIZE
-s HEIGHT WIDTH
--size HEIGHT WIDTH

Example for 1280:

-s 1280
or
-s 1280 1280

python3 gen_wts_yoloV5.py -w yolov5s.pt -s 1280
```
