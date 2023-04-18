# ubuntu安装tao

升级系统

```sh
sudo apt update
sudo apt upgrade
sudo apt autoremove
```

## 安装nvdia 驱动

```sh

# 按table补全 安装自己适合的版本
sudo apt install nvidia-driver-

# 我安装510版本
sudo apt install nvidia-driver-510
```

## 安装docker

-[在 Ubuntu 上安装 Docker 引擎](https://docs.docker.com/engine/install/ubuntu/)

```sh

sudo apt-get update

sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

sudo mkdir -p /etc/apt/keyrings

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin

```

**sudo root**

```sh

sudo groupadd docker

sudo usermod -aG docker $USER

newgrp docker

```

## conda

[conda install](https://conda.io/projects/conda/en/stable/user-guide/install/download.html)

```sh

bash Anaconda-latest-Linux-x86_64.sh

```

### nvidia-container-toolkit

```sh

curl https://get.docker.com | sh \
  && sudo systemctl --now enable docker


sudo apt-get update

sudo apt-get install -y nvidia-docker2

sudo systemctl restart docker

sudo docker run --rm --gpus all nvidia/cuda:11.6.2-base-ubuntu22.04 nvidia-smi


```

## 创建tao conda 环境

```sh
conda create -n tao python=3.7

conda activate tao

cd ~

mkdir tao

cd tao

wget --content-disposition https://api.ngc.nvidia.com/v2/resources/nvidia/tao/tao-getting-started/versions/4.0.0/zip -O getting_started_v4.0.0.zip
unzip -u getting_started_v4.0.0.zip  -d ./getting_started_v4.0.0 && rm -rf getting_started_v4.0.0.zip && cd ./getting_started_v4.0.0


bash setup/quickstart_launcher.sh --install

tao --help

```


------

[TOC]

# ubuntu 22.04 NVDIA TAO 安装

- [tao-toolkit官网](https://developer.nvidia.com/tao-toolkit)
- [tao-toolkit4.0文档](https://docs.nvidia.com/tao/tao-toolkit/index.html)
- [tao-toolkit4.0下载](https://catalog.ngc.nvidia.com/orgs/nvidia/teams/tao/resources/tao-getting-started)
- [tao-toolkit4.0安装](https://docs.nvidia.com/tao/tao-toolkit/text/tao_toolkit_quick_start_guide.html)
- [cuda历史版本](https://developer.nvidia.com/cuda-toolkit-archive)
- [cuda文档](https://docs.nvidia.com/cuda/cuda-installation-guide-linux/contents.html#)


机器学习集成环境包含tf,py

## 安装cuda

- [驱动下载](https://www.nvidia.com/Download/index.aspx?lang=en-us#)

## 安装conda

- [官方文档](https://docs.conda.io/projects/conda/en/stable/index.html)

- [安装教程](https://docs.conda.io/projects/conda/en/stable/user-guide/install/linux.html#installing-on-linux)

```sh
# 在官网找到下载链接
wget https://repo.anaconda.com/archive/Anaconda3-2022.10-Linux-x86_64.sh
# 执行安装
bash Anaconda3-2022.10-Linux-x86_64.sh
```

## 安装docker-ce

- [安装教程](https://docs.docker.com/engine/install/ubuntu/)

```sh
# 移除老版本
sudo apt-get remove docker docker-engine docker.io containerd runc
sudo apt-get purge docker-ce docker-ce-cli containerd.io docker-compose-plugin
sudo rm -rf /var/lib/docker
sudo rm -rf /var/lib/containerd

sudo apt-get update
sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin

docker info

sudo docker info

# docker root权限

sudo groupadd docker

sudo usermod -aG docker $USER

newgrp docker

docker info

```

## 安装 nvidia-container-toolkit

- [安装文档](https://docs.nvidia.com/datacenter/cloud-native/container-toolkit/install-guide.html)

```sh
curl https://get.docker.com | sh \
  && sudo systemctl --now enable docker

distribution=$(. /etc/os-release;echo $ID$VERSION_ID) \
      && curl -fsSL https://nvidia.github.io/libnvidia-container/gpgkey | sudo gpg --dearmor -o /usr/share/keyrings/nvidia-container-toolkit-keyring.gpg \
      && curl -s -L https://nvidia.github.io/libnvidia-container/$distribution/libnvidia-container.list | \
            sed 's#deb https://#deb [signed-by=/usr/share/keyrings/nvidia-container-toolkit-keyring.gpg] https://#g' | \
            sudo tee /etc/apt/sources.list.d/nvidia-container-toolkit.list

sudo apt-get update

sudo apt-get install -y nvidia-docker2

sudo systemctl restart docker

#验证
sudo docker run --rm --gpus all nvidia/cuda:11.6.2-base-ubuntu20.04 nvidia-smi
```

## 获取NGC account and API key

- [ngc注册](https://catalog.ngc.nvidia.com/)

登陆后选择账号 进行设置

## 登陆 NGC docker

```
dWl1MmpwNzZ1NjBwN3I4M2w4ZXFvdmlmMWk6NjUwOWFjYzktY2UzMy00M2FiLTkyMzQtZDY4ZmRhY2IyZmYw
```

## 安装tao

- [安装文档](https://docs.nvidia.com/tao/tao-toolkit/text/tao_toolkit_quick_start_guide.html#installing-tao-launcher)

```sh

conda create -n tao python=3.7

# 下载tao toolkit 4.0
wget --content-disposition https://api.ngc.nvidia.com/v2/resources/nvidia/tao/tao-getting-started/versions/4.0.0/zip -O getting_started_v4.0.0.zip
unzip -u getting_started_v4.0.0.zip  -d ./getting_started_v4.0.0 && rm -rf getting_started_v4.0.0.zip && cd ./getting_started_v4.0.0

bash setup/quickstart_launcher.sh --install

bash setup/quickstart_launcher.sh --upgrade

tao --help

```

## 安装 jupyterlab

-[jupyterlab](https://jupyterlab.readthedocs.io/en/stable/getting_started/installation.html)