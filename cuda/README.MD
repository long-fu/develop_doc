
1. EULA Acceptance

2. CUDA Driver installation

3. CUDA Toolkit installation, location, and /usr/local/cuda symbolic link



## Driver Installation

驱动安装

This section is for users who want to install a specific driver version.

```sh

#sudo apt-get install cuda-drivers-<branch>
sudo apt-get install cuda-drivers-530

```


## CUDA Toolkit 12.2 Update 2 Downloads

https://developer.nvidia.com/cuda-downloads?target_os=Linux&target_arch=x86_64&Distribution=Ubuntu&target_version=20.04&target_type=deb_local

```sh
wget https://developer.download.nvidia.com/compute/cuda/repos/ubuntu2004/x86_64/cuda-ubuntu2004.pin
sudo mv cuda-ubuntu2004.pin /etc/apt/preferences.d/cuda-repository-pin-600
wget https://developer.download.nvidia.com/compute/cuda/12.2.2/local_installers/cuda-repo-ubuntu2004-12-2-local_12.2.2-535.104.05-1_amd64.deb
sudo dpkg -i cuda-repo-ubuntu2004-12-2-local_12.2.2-535.104.05-1_amd64.deb
sudo cp /var/cuda-repo-ubuntu2004-12-2-local/cuda-*-keyring.gpg /usr/share/keyrings/
sudo apt-get update
sudo apt-get -y install cuda

sudo apt-get -y install nvidia-gds
```

## NVIDIA TensorRT

安装教程

 https://docs.nvidia.com/deeplearning/tensorrt/quick-start-guide/index.html#container-install


https://developer.nvidia.com/tensorrt