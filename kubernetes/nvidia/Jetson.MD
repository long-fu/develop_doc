# Deepstream 容器方式在Jetson上运行

## 运行docker容器

### **在容器中手动启动deepstream**

```sh

$ docker pull nvcr.io/nvidia/deepstream-l4t:6.2-samples
$ xhost +
$ sudo docker run -it --rm --net=host --runtime nvidia -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.2 -v /tmp/.X11-unix/:/tmp/.X11-unix -v /tmp/argus_socket:/tmp/argus_socket --device=/dev/video0 nvcr.io/nvidia/deepstream-l4t:6.2-samples

```

- `xhost +` 容许任何主机访问本地的X服务器
- `-v /tmp/argus_socket:/tmp/argus_socket` 挂载 CSI 摄像头
- `--device=/dev/video0` 挂载USB摄像头
- `-v /tmp/.X11-unix/:/tmp/.X11-unix` 将主机上X11的unix套接字共享到了容器里面
- `-e DISPLAY=$DISPLAY` 设置将图形显示到何处

**在容器中运行的命令**

```sh
bash user_additional_install.sh

# 稍后会在主机桌面 看到实时视频流
deepstream-app -c samples/configs/deepstream-app/source1_csi_dec_infer_resnet_int8.txt
```


### **启动容器就直接启动deepstream-app**

1. 需要制作自己的镜像

```Dockerfile

FROM nvcr.io/nvidia/deepstream-l4t:6.2-samples

RUN apt-get update && \
apt-get install -y gstreamer1.0-libav && \
apt-get install --reinstall -y gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-plugins-ugly libavutil56 libavcodec58 libavformat58 libavfilter7 libx264-155 libde265-dev libde265-0 libx265-179 libvpx6 libmpeg2encpp-2.1-0 libmpeg2-4 libmpg123-0

WORKDIR /opt/nvidia/deepstream/deepstream-6.2

ENTRYPOINT ["deepstream-app", "-c"]
CMD [ "./samples/configs/deepstream-app/source1_csi_dec_infer_resnet_int8.txt"]
```

```sh
$ docker build -t haoshuai/deepstream-l4t:6.2-samples .
$ docker images

REPOSITORY                           TAG                  IMAGE ID       CREATED          SIZE
haoshuai/deepstream-l4t              6.2-samples          f588c21d845f   31 seconds ago   5.6GB

```

2. 启动docker容器

```sh
$ xhost +
# 稍后会在主机桌面 看到实时视频流 这个时间可能会比上面的久一点
$ sudo docker run -d --net=host --runtime nvidia -e DISPLAY=$DISPLAY -w /opt/nvidia/deepstream/deepstream-6.2 -v /tmp/.X11-unix/:/tmp/.X11-unix -v /tmp/argus_socket:/tmp/argus_socket --device=/dev/video0 haoshuai/deepstream-l4t:6.2-samples
```

## k8s部署deepstream

1. 需要在边缘节点进行以下配置

```sh
$xhost +
$echo $DISPLAY
:1
```

2. 配置yaml文件 `pod_deepstream_x11.yaml`

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: deepstream-pod
  labels:
    app: deepstream-pod
spec:
  nodeName: admin123-desktop
  containers:
    - name: deepstream-app
      image: haoshuai/deepstream-l4t:6.2-samples
      imagePullPolicy: Never
      ports: 
      - containerPort: 8554
        hostPort: 8554
      - containerPort: 5400
        hostPort: 5400
      env: 
        - name: DISPLAY
          value: ":1" # 写入上文宿主机 echo $DISPLAY输出的结果
      volumeMounts:
      - name: dev-video0
        mountPath: /dev/video0
      - name: argus-socket
        mountPath: /tmp/argus_socket
      - name: x11-unix
        mountPath: /tmp/.X11-unix
  volumes:
    - name: dev-video0
      hostPath:
        path: /dev/video0
    - name: argus-socket
      hostPath:
        path: /tmp/argus_socket
    - name: x11-unix
      hostPath:
        path: /tmp/.X11-unix
```

3. 部署

```sh
$kubectl create -f pod_deepstream_x11.yaml
pod/deepstream-pod created
$kubectl get pods
NAME                          READY   STATUS    RESTARTS      AGE
aa11-68c584f8-lj4cx           1/1     Running   3 (23d ago)   53d
deepstream-pod                1/1     Running   0             65s
harbor-pod                    1/1     Running   2 (23d ago)   49d
testliujiao-5749f4c7f-nkb6v   1/1     Running   2 (23d ago)   52d
tetsyoyo-65f984bd96-6j2x4     1/1     Running   2 (23d ago)   49d
```

4. 在边缘设备上查看结果

> 可能需要在2min中后(也有可能会长一些)才能看到推理回显示。

**可能遇到的问题**

- 如果deepstream-pod一直在重启说明环境存在问题，请检查配置环境。
- 在运行成功过一次，然后关闭重新部署，可能需要重启机器，原因是摄像头调用不起来。
- 查看csi摄像头是否准备就绪 `nvgstcapture-1.0`。