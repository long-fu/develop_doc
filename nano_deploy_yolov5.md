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

## 完整依赖


- [官网](https://docs.ultralytics.com/tutorials/nvidia-jetson/)

```sh
sudo apt-get update

sudo apt-get upgrade

sudo apt install -y python3-pip

# nano需要加上 --no-cache-dir
pip3 install --upgrade pip --no-cache-dir


```

```sh



git clone https://github.com/ultralytics/yolov5


cd yolov5

vi requirements.txt
# 注释下面部分
# torch>=1.7.0

# torchvision>=0.8.1

sudo apt install -y libfreetype6-dev

pip3 install -r requirements.txt --no-cache-dir
```


```sh
sudo apt-get install -y libopenblas-base libopenmpi-dev
sudo apt install -y libjpeg-dev zlib1g-dev
```

## deepstream

[deepstream](https://developer.nvidia.com/deepstream-getting-started)
[deepstream历史版本](https://developer.nvidia.com/embedded/deepstream-on-jetson-downloads-archived)
[在jetson平台上安装](https://docs.nvidia.com/metropolis/deepstream/dev-guide/text/DS_Quickstart.html#jetson-setup)