## liunx操作命令

```sh
# 操作系统的发行版号和操作系统版本
uname -a

# 查看版本号
uname -v

# 查看发行版本信息，并且方法可以适用于所有的Linux发行版本
lsb_release -a

# 查看到当前是Linux什么版本系统
cat /etc/issue

# 查看内核的版本号
cat /proc/version

# 
lspci |grep -i vga
```


## conda

```sh
#创建虚拟环境
conda create -n your_env_name python=X.X（3.6、3.7等）

conda create -n yolo_v5 python=3.7
 
#激活虚拟环境
source activate your_env_name(虚拟环境名称)
 
#退出虚拟环境
source deactivate your_env_name(虚拟环境名称)
 
#删除虚拟环境
conda remove -n your_env_name(虚拟环境名称) --all
 
#查看安装了哪些包
conda list
 
#安装包
conda install package_name(包名)
conda install scrapy==1.3 # 安装指定版本的包
conda install -n 环境名 包名 # 在conda指定的某个环境中安装包
 
#查看当前存在哪些虚拟环境
conda env list 
#或 
conda info -e
#或
conda info --envs
 
#检查更新当前conda
conda update conda
 
#更新anaconda
conda update anaconda
 
#更新所有库
conda update --all
 
#更新python
conda update python
```


## docker

1. 删除容器

```sh
docker ps #查看正在运行的容器

docker ps -a #查看所有容器

docker rm container_id #删除容器
```

2. 删除镜像
```sh
docker images //查看镜像

docker rmi image_id
```

2.1 删除其他镜像

```sh
# 删除 null image
sudo docker rmi $(docker images -f "dangling=true" -q) #删除所有镜像
```

```sh
# 删掉容器
docker stop $(docker ps -qa)
docker rm $(docker ps -qa)
```
```sh


# 删除镜像

docker rmi --force $(docker images -q)

# 删除名称中包含某个字符串的镜像
# 例如删除包含“some”的镜像
docker rmi --force $(docker images | grep some | awk '{print $3}')



```

**ubuntu系统快速修改Docker默认镜像存放地址**


```sh
# 1. 首先查询当前docker存放地址：`sudo docker info | grep Docker Root Dir`，通常地址位于： /var/lib/docker
sudo docker info | grep Docker Root Dir

# 2. 将当前目录复制到要存放的扩展盘上 : mv /var/lib/docker /data/docker
ln -s /path/docker /var/lib/docker

# 3. 创建软连接
ln -s /data/docker /var/lib/docker
# 4. 重启docker服务：sudo systemctl restart docker

sudo systemctl restart docker
# 5. 再次查看，docker存放地址，发现正常使用，说明修改正确。
sudo docker info | grep Docker Root Dir

```



**Docker从容器内拷贝文件到主机上**




docker cp <containerId>:/file/path/within/container /host/path/target

docker cp ea6477f33e0a:/opt/nvidia/deepstream/deepstream-6.1/samples/configs/deepstream-app/out_source0.mp4 ~