# docker 操作手册

**[dockerfile 学习](https://www.runoob.com/docker/docker-dockerfile.html)**

[使用 Dockerfile 定制镜像](https://yeasy.gitbook.io/docker_practice/image/build)

导出镜像

```sh
# 导出 容器 快照  == 不要用这种方式 导出镜像
docker export 7691a814370e > ubuntu.tar
#导入
cat t-eio-manager-0.0.1.tar | docker import - t-eio/ec-manager:v0.0.1

cat t-eio-web-0.0.1.tar | docker import - t-eio/ec-manager-web:v0.0.1

docker save ec-manager-web:v0.0.1 > ec-manager-web.tar

# 导入api镜像
docker load < ec-manager.tar
```

0. 运行容器

```sh
docker run -d -P --add-host=k8s-vip:120.79.183.159 ec-manager-0.0.1

docker run -d -P --add-host=k8s-vip:120.79.183.159 t-eio-manager-0.0.1

docker run -d -P t-eio-web-0.0.1
# 进入运行的docker镜像中 并且用sh命令交互
docker exec -it b2f73d901244 /bin/bash

kubectl exec -it -nt-eio-dashboard t-eio-dashboard-5b5fd996f4-h8qlr --container  /bin/bash

kubectl exec -it -nt-eio-dashboard t-eio-dashboard-5b5fd996f4-h8qlr --container t-eio-dashboard-web /bin/bash
```

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

docker rm --force $(docker ps -a | grep Exited | awk '{print $1}')
```

```sh


# 删除镜像

docker rmi --force $(docker images -q)

# 删除名称中包含某个字符串的镜像
# 例如删除包含“some”的镜像
docker rmi --force $(docker images | grep goharbor | awk '{print $3}')

docker rmi --force $(docker images | grep none | awk '{print $3}')

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



```sh
docker cp <containerId>:/file/path/within/container /host/path/target

docker cp ea6477f33e0a:/opt/nvidia/deepstream/deepstream-6.1/samples/configs/deepstream-app/out_source0.mp4 ~

```


----

```sh

kubectl delete deployment t-eio-dashboard -nt-eio-dashboard

kubectl apply -f install.yaml

kubectl exec -it -nt-eio-dashboard t-eio-dashboard-5b5fd996f4-8dhhh --container t-eio-dashboard /bin/bash
```