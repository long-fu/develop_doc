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
