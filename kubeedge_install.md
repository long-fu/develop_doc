# ubuntu安装kubeedge


## kubeedge简介

- [kubeedge官网](https://kubeedge.io/zh/)
- [kubeedge源码](https://github.com/kubeedge/kubeedge)
- [kubeedge视频合集](https://space.bilibili.com/448816706?spm_id_from=333.337.search-card.all.click)

[KubeEdge](https://kubeedge.io/zh/)是一个开源系统，用于将容器化应用程序编排功能扩展到Edge的主机。它基于kubernetes构建，并为网络应用程序提供基础架构支持。云和边缘之间的部署和元数据同步。

### 优势

- **Kubernetes 原生支持**：使用 KubeEdge 用户可以在边缘节点上编排应用、管理设备并监控应用程序/设备状态，就如同在云端操作 Kubernetes 集群一样。

- **云边可靠协作**：在不稳定的云边网络上，可以保证消息传递的可靠性，不会丢失。

- **边缘自治**：当云边之间的网络不稳定或者边缘端离线或重启时，确保边缘节点可以自主运行，同时确保边缘端的应用正常运行。

- **边缘设备管理**：通过 Kubernetes 的原生API，并由CRD来管理边缘设备。

- **极致轻量的边缘代理**：在资源有限的边缘端上运行的非常轻量级的边缘代理(EdgeCore)。

### 它如何工作

KubeEdge 由云端和边缘端部分构成：

#### 架构

![架构图](images/kubeedge_arch.png)

#### 云上部分

- [CloudHub](https://kubeedge.io/en/docs/architecture/cloud/cloudhub): CloudHub 是一个 Web Socket 服务端，负责监听云端的变化，缓存并发送消息到 EdgeHub。

- [EdgeController](https://kubeedge.io/en/docs/architecture/cloud/edge_controller): EdgeController 是一个扩展的 Kubernetes 控制器，管理边缘节点和 Pods 的元数据确保数据能够传递到指定的边缘节点。

- [DeviceController](https://kubeedge.io/en/docs/architecture/cloud/device_controller): DeviceController 是一个扩展的 Kubernetes 控制器，管理边缘设备，确保设备信息、设备状态的云边同步。

#### 边缘部分

- [EdgeHub](https://kubeedge.io/en/docs/architecture/edge/edgehub): EdgeHub 是一个 Web Socket 客户端，负责与边缘计算的云服务（例如 KubeEdge 架构图中的 Edge Controller）交互，包括同步云端资源更新、报告边缘主机和设备状态变化到云端等功能。

- [Edged](https://kubeedge.io/en/docs/architecture/edge/edged): Edged 是运行在边缘节点的代理，用于管理容器化的应用程序。

- [EventBus](https://kubeedge.io/en/docs/architecture/edge/eventbus): EventBus 是一个与 MQTT 服务器 (mosquitto) 交互的 MQTT 客户端，为其他组件提供订阅和发布功能。

- [ServiceBus](https://kubeedge.io/en/docs/architecture/edge/servicebus): ServiceBus 是一个运行在边缘的 HTTP 客户端，接受来自云上服务的请求，与运行在边缘端的 HTTP 服务器交互，提供了云上服务通过 HTTP 协议访问边缘端 HTTP 服务器的能力。

- [DeviceTwin](https://kubeedge.io/en/docs/architecture/edge/devicetwin): DeviceTwin 负责存储设备状态并将设备状态同步到云，它还为应用程序提供查询接口。

- [MetaManager](https://kubeedge.io/en/docs/architecture/edge/metamanager): MetaManager 是消息处理器，位于 Edged 和 Edgehub 之间，它负责向轻量级数据库 (SQLite) 存储/检索元数据。

---

# 安装

## 主节点服务器

### 安装docker

- [官网教程](https://docs.docker.com/engine/install/ubuntu/)

- [docker root权限](https://docs.docker.com/engine/install/linux-postinstall/)

```sh
apt-get update

apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

mkdir -p /etc/apt/keyrings

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

apt-get update

apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin

docker info

```

### 安装 Kubernetes 1.22.10

- 关闭交换分区

- [环境配置](https://kubernetes.io/zh-cn/docs/setup/production-environment/container-runtimes/)

- [安装k8s](https://developer.aliyun.com/mirror/kubernetes?spm=5176.21213303.J_6704733920.7.728b53c9nPhQpE&scm=20140722.S_other%40%40%E7%BD%91%E7%AB%99%40%40httpsdeveloperaliyunc._.ID_other%40%40%E7%BD%91%E7%AB%99%40%40httpsdeveloperaliyunc-RL_Kubernetes-LOC_main-OR_ser-V_2-P0_0)

- [部署k8s](https://kubernetes.io/zh-cn/docs/setup/production-environment/tools/kubeadm/create-cluster-kubeadm/)


安装K8S需要**关闭交换分区**

```sh
# 禁用 /proc/swaps 中的所有交换区
swapoff -a
# 检查
free -m

#永久关闭swap
vi /etc/fstab 
# 把加载swap分区的那行记录注释掉即可
```

**以上是最新版的安装教程，本例是1.22.10**

```sh
# 转发 IPv4 并让 iptables 看到桥接流量
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
overlay
br_netfilter
EOF

modprobe overlay
modprobe br_netfilter

# 设置所需的 sysctl 参数，参数在重新启动后保持不变
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-iptables  = 1
net.bridge.bridge-nf-call-ip6tables = 1
net.ipv4.ip_forward                 = 1
EOF

# 应用 sysctl 参数而不重新启动
sudo sysctl --system

apt-get update && apt-get install -y apt-transport-https

curl https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | apt-key add - 

cat <<EOF >/etc/apt/sources.list.d/kubernetes.list
deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main
EOF

apt-get update

# apt-get install -y kubelet kubeadm kubectl 不安装最新版本

apt-get install -y kubelet=1.22.10-00 kubeadm=1.22.10-00 kubectl=1.22.10-00

# 阻止自动更新(apt upgrade时忽略)。所以更新的时候先unhold，更新完再hold。
apt-mark hold kubelet kubeadm kubectl

#使服务生效
systemctl enable --now kubelet

```

### 部署Kubernetes 主节点

- `kubeadm init`

```sh
kubeadm init --apiserver-advertise-address 172.22.197.118 --image-repository  registry.aliyuncs.com/google_containers --kubernetes-version v1.21.14  --pod-network-cidr=10.244.0.0/16
```

**k8s主节点部署成功输出：**

![info](images/13.png)

```sh

# 安装网络插件
kubectl apply -f https://raw.githubusercontent.com/flannel-io/flannel/master/Documentation/kube-flannel.yml

kubectl get nodes

#检查主节点是否完成初始化并准备就绪
kubectl get pods -A
```

![info](images/14.png)

### 安装 kubernetes dashboard 插件（可选）

```sh
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.6.1/aio/deploy/recommended.yaml

# 配置公网访问

kubectl proxy --address='0.0.0.0' --accept-hosts='^*$'&

#  http://[公网IP]:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

#2. 接口转发443
kubectl port-forward -n kubernetes-dashboard --address 0.0.0.0 service/kubernetes-dashboard 8080:443

# https://[公网IP]:8080/#/login

# 创建用户
vi dashboard-admin.yml

# apiVersion: v1
# kind: ServiceAccount
# metadata:
#   name: admin-user
#   namespace: kubernetes-dashboard

# ---

# apiVersion: rbac.authorization.k8s.io/v1
# kind: ClusterRoleBinding
# metadata:
#   name: admin-user
# roleRef:
#   apiGroup: rbac.authorization.k8s.io
#   kind: ClusterRole
#   name: cluster-admin
# subjects:
# - kind: ServiceAccount
#   name: admin-user
#   namespace: kubernetes-dashboard

#创建登陆用户
kubectl apply -f dashboard-admin.yml

# 获取token
kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')

# Name:         admin-user-token-5ll79
# Namespace:    kubernetes-dashboard
# Labels:       <none>
# Annotations:  kubernetes.io/service-account.name: admin-user
#               kubernetes.io/service-account.uid: b08c86ca-aff1-40ca-a7d0-63da6ed5d753

# Type:  kubernetes.io/service-account-token

# Data
# ====
# ca.crt:     1066 bytes
# namespace:  20 bytes
# token:      eyJhbGciOiJSUzI1NiIsImtpZCI6Img1SzRmbkJKdXIzNlZFNllZZEpkWWFwMFNsa1dQWjFqVkoxcVBiTEZMQ28ifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLTVsbDc5Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJiMDhjODZjYS1hZmYxLTQwY2EtYTdkMC02M2RhNmVkNWQ3NTMiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.m_ZlXR8lJ9ncYU4TZBr2KEqDJkhM4mz4bGtKWm3HofR1O2LHxEygRC6vBZVXqx_RjKqayFUAeVFJ0yWbzm8q507mTRBB1tpYokEbKd4VXudTz-0mVms1QIjo3K6ISciuMY6YelGN_KFRDKkRY_X7EByHTXn6Kx81M9meofa3Pxbv7hMKiaAJJlhvzPLlXe7K4zh2BsAJzN6nTavTjXTs14CdfYc2L8ZpF4DcCBzvvd-HDPHWJFd_2Ay2x2J3eH75kNUykWVszaJY_1zg-A37aAGjWMrHk0JkFLlZAYQMCm5v2gDLENkkMeTjdM4jHL509k7SnZno590Ho6S1Guifqw

```

### 设置云端（KubeEdge主节点）

- [官网安装教程zh](https://kubeedge.io/zh/docs/setup/keadm_zh/#%E8%AE%BE%E7%BD%AE%E4%BA%91%E7%AB%AF-kubeedge%E4%B8%BB%E8%8A%82%E7%82%B9)
- [官网安装教程](https://kubeedge.io/en/docs/setup/local/#setup-cloud-side-kubeedge-master-node)

```sh

snap install helm --classic

tar -zxvf keadm-v1.12.1-linux-amd64.tar.gz

cp keadm-v1.12.1-linux-amd64/keadm/keadm /usr/local/bin/keadm

# 部署云端
keadm init --advertise-address=172.22.197.126 --profile version=v1.12.1 --kube-config=/root/.kube/config

```

**我在部署的时候出现如下错误：**

![error0](images/12.png)

由于cloudcore没有污点容忍，默认主节点节点是不部署应用的，可以用下面的命令查看污点，其中 iZwz94t9xain7tmum4mmvmZ 为节点NAME

```sh

kubectl get nodes

# 查看污点
kubectl describe nodes izwz94t9xain7tmum4mmvmz  | grep Taints

# 污点删除
kubectl taint node izwz94t9xain7tmum4mmvmz node-role.kubernetes.io/master-

# 再次查看
kubectl describe nodes izwz94t9xain7tmum4mmvmz  | grep Taints
```


没有污点输出
![error1](images/08.png)

**部署车成功验证**

```sh
#  kubeedge
kubectl get all -n kubeedge
```

![none](images/10.png)

```sh
# 查看token
keadm gettoken
```

![none](images/11.png)


## 工作节点服务器

### 1. 安装docker

[官网教程](https://docs.docker.com/engine/install/ubuntu/)

```sh
apt-get update

apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

mkdir -p /etc/apt/keyrings

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

apt-get update

apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin

docker info

```

### 2.设置边缘端（KubeEdge工作节点）

- [官网安装教程zh](https://kubeedge.io/zh/docs/setup/keadm_zh/#%E8%AE%BE%E7%BD%AE%E8%BE%B9%E7%BC%98%E7%AB%AF-kubeedge%E5%B7%A5%E4%BD%9C%E8%8A%82%E7%82%B9)
- [官网安装教程](https://kubeedge.io/en/docs/setup/keadm/#setup-edge-side-kubeedge-worker-node)

```sh

tar -zxvf keadm-v1.12.1-linux-amd64.tar.gz

cp keadm-v1.12.1-linux-amd64/keadm/keadm /usr/local/bin/keadm

# 部署
keadm join --cloudcore-ipport=172.22.197.118:10000 --token=96d0d720a98ef16d45a77d5e66083de7019f9895a1a127a5a3028d7de7a43d9c.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NzA0ODEzNTB9.ciFbCpZKT13iRoVfesqilunT5t7rq0ufsLWI-tbZ3z0

```

我在安装的时候出现如下错误
`failed to run Kubelet: misconfiguration: kubelet cgroup driver: "cgroupfs" is different from docker cgroup driver: "systemd"`

![error](images/15.png)

![error](images/16.png)

[github issues](https://github.com/kubeedge/kubeedge/issues/3939)

修改 `/etc/kubeedge/config/edgecore.yaml`文件 `cgroupDriver: cgroupfs` >  `cgroupDriver: systemd`
```sh
# Work-around is to modify /etc/kubeedge/config/edgecore.yaml

# 44c44
# <     cgroupDriver: systemd
# ---
# >     cgroupDriver: cgroupfs

```

```sh
# 查看运行状态
systemctl status edgecore.service
```

![info](images/17.png)


```sh
# 重启
systemctl restart edgecore.service
```

我在重启出现错误`failed to check the running environment: kube-proxy should not running on edge node when running edgecore`

![error](images/18.png)

```sh
# 查看运行日志
journalctl -u edgecore.service -xe
```

![error](images/19.png)

在官方文档有说明 [请详细阅读](https://kubeedge.io/en/docs/setup/keadm/#setup-edge-side-kubeedge-worker-node)

如果您无法重启 edgecore，请检查是否是由于 kube-proxy 的缘故，同时杀死这个进程。 kubeedge 默认不纳入该进程，我们使用 edgemesh 来进行替代

注意： 可以考虑避免 kube-proxy 部署在edgenode上。有两种解决方法：

1. 通过调用 `kubectl edit daemonsets.apps -n kube-system kube-proxy` 添加以下设置：
``` yaml
affinity:
  nodeAffinity:
    requiredDuringSchedulingIgnoredDuringExecution:
      nodeSelectorTerms:
        - matchExpressions:
            - key: node-role.kubernetes.io/edge
              operator: DoesNotExist
```

1. 如果您仍然要运行 `kube-proxy` ，请通过在以下位置添加 `edgecore.service` 中的 env 变量来要求 **edgecore** 不进行检查edgecore.service：

    ``` shell
    sudo vi /etc/systemd/system/edgecore.service
    ```

    - 将以下行添加到 **edgecore.service** 文件：

    ``` shell
    Environment="CHECK_EDGECORE_ENVIRONMENT=false"
    ```

    - 最终文件应如下所示：

    ```
      [Unit]
      Description=edgecore.service

      [Service]
      Type=simple
      ExecStart=/usr/local/bin/edgecore
      Restart=always
      RestartSec=10
      Environment="CHECK_EDGECORE_ENVIRONMENT=false"

      [Install]
      WantedBy=multi-user.target
    ```

## 最后在主节点验证

```sh
kubectl get nodes
```

![info](images/20.png)


-----

# 后记 安装k8s(1.25)

[Kubernetes抛弃了Docker解释视频](https://www.bilibili.com/video/BV15Q4y1B7yk/?spm_id_from=333.788.recommend_more_video.8&vd_source=2a18e529afba9d517bd1187c8f358246)推荐一看时间不长

**容器运行时**

> 说明： 自 1.24 版起，Dockershim 已从 Kubernetes 项目中移除。阅读 Dockershim 移除的常见问题了解更多详情。
> 
> 说明：
v1.24 之前的 Kubernetes 版本直接集成了 Docker Engine 的一个组件，名为 dockershim。 这种特殊的直接整合不再是 Kubernetes 的一部分 （这次删除被作为 v1.20 发行版本的一部分宣布）。
>
> 你可以阅读检查 Dockershim 移除是否会影响你以了解此删除可能会如何影响你。 要了解如何使用 dockershim 进行迁移， 请参阅从 dockershim 迁移。
>
> 如果你正在运行 v1.26 以外的 Kubernetes 版本，查看对应版本的文档。
>

**如果还是在高版本中用Docker Engine**你需要安装[cri-dockerd](https://github.com/Mirantis/cri-dockerd)中间件。
我们在墙内安装k8s一般都是用aliyun镜像安装。

在部署master的时候一般会出错。

在查看错误的时候会出现`shk8s.io/pause:3.6 time out错误`

**解决方案：**

```sh
cat /etc/systemd/system/cri-docker.service
```

![info](images/21.png)

```sh
vi /etc/systemd/system/cri-docker.service

# 在这里追加下面的参数ExecStart=
# --network-plugin=cni -–pod-infra-container-image=registry.aliyuncs.com/google_containers/pause:3.8
```

# KubeEdge安装命令

# Cloud

```bash
sh -c "apt install -y socat conntrack ebtables ipset > /dev/null"
curl -sfL https://get-kk.kubesphere.io | VERSION=v2.2.1 sh -
chmod +x kk && mv kk /usr/local/bin/
curl https://get.docker.com | bash
touch /etc/docker/daemon.json
echo '{ "registry-mirrors": [ "https://dockerproxy.com" ] }' > /etc/docker/daemon.json
service docker restart
kk create cluster --with-kubernetes v1.22.12 --with-local-storage
#  input yes

kubectl get pods -A

wget https://github.com/kubeedge/kubeedge/releases/download/v1.12.1/keadm-v1.12.1-linux-amd64.tar.gz
tar zxvf keadm-v1.12.1-linux-amd64.tar.gz
chmod +x keadm-v1.12.1-linux-amd64/keadm/keadm
mv keadm-v1.12.1-linux-amd64/keadm/keadm /usr/local/bin

keadm init --advertise-address=172.31.73.105 --set iptablesManager.mode="external" --profile version=v1.12.1
kubectl get pod -n kubeedge

# 安装metrics-server
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
# 添加 --kubelet-insecure-tls 参数
kubectl patch deploy metrics-server -n kube-system --type='json' -p='[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value":"--kubelet-insecure-tls"}]'

# daemon patch

kubectl get daemonset -n kube-system | grep -v NAME | awk '{print $1}' | xargs -n 1 kubectl patch daemonset -n kube-system --type='json' -p='[{"op": "replace", "path": "/spec/template/spec/affinity", "value":{"nodeAffinity":{"requiredDuringSchedulingIgnoredDuringExecution":{"nodeSelectorTerms":[{"matchExpressions":[{"key":"node-role.kubernetes.io/edge","operator":"DoesNotExist"}]}]}}}}]'

```

# Edge

```bash

wget https://github.com/kubeedge/kubeedge/releases/download/v1.12.1/keadm-v1.12.1-linux-amd64.tar.gz
tar zxvf keadm-v1.12.1-linux-amd64.tar.gz
chmod +x keadm-v1.12.1-linux-amd64/keadm/keadm
mv keadm-v1.12.1-linux-amd64/keadm/keadm /usr/local/bin

curl https://get.docker.com | bash
touch /etc/docker/daemon.json
echo '{ "registry-mirrors": [ "https://dockerproxy.com" ] }' > /etc/docker/daemon.json
service docker restart

# edge mesh
vim /etc/nsswitch.conf
hosts:          dns files mdns4_minimal [NOTFOUND=return]

# ip forward
sudo echo "net.ipv4.ip_forward = 1" >> /etc/sysctl.conf
sudo sysctl -p | grep ip_forward

# 安装边缘节点
TOKEN=xxxx
SERVER=172.31.73.105:10000
keadm join --token=$TOKEN --cloudcore-ipport=$SERVER --kubeedge-version=1.12.1

# 查看 edgecore 日志
journalctl -u edgecore.service -f

# 开启 edgeStream，
vim /etc/kubeedge/config/edgecore.yaml
# 找到 edgeStream，将 enable: false 改为 enable: true
systemctl restart edgecore.service

# 查看容器日志
kubectl logs redis-554b4666d8-glpcf -c redis

# 在容器中执行命令
kubectl exec redis-554b4666d8-glpcf -i -t -c redis -- bash
```

# Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis
spec:
  selector:
    matchLabels:
      app: redis
  replicas: 1
  template:
    metadata:
      labels:
        app: redis
    spec:
      nodeName: edge-1
      containers:
      - name: redis
        image: redis

```
