# ubuntu安装k8s

> 是用于自动部署、扩缩和管理容器化应用程序的开源系统

- [k8s官网](https://kubernetes.io/zh-cn/)
- [k8s中文文档](http://docs.kubernetes.org.cn/) 推荐看当中的**概念**和**设计文档**部分
- [k8s源码](https://github.com/kubernetes/kubernetes)

## 特性

- **[自动化上线和回滚](https://kubernetes.io/zh-cn/docs/concepts/workloads/controllers/deployment/)：** Kubernetes 会分步骤地将针对应用或其配置的更改上线，同时监视应用程序运行状况以确保你不会同时终止所有实例。如果出现问题，Kubernetes 会为你回滚所作更改。你应该充分利用不断成长的部署方案生态系统。

- **[服务发现与负载均衡](https://kubernetes.io/zh-cn/docs/concepts/services-networking/service/)：** 无需修改你的应用程序去使用陌生的服务发现机制。Kubernetes 为容器提供了自己的 IP 地址和一个 DNS 名称，并且可以在它们之间实现负载均衡。
  
- **[自我修复](https://kubernetes.io/zh-cn/docs/concepts/workloads/controllers/replicaset/#replicationcontroller-%e5%a6%82%e4%bd%95%e5%b7%a5%e4%bd%9c)：** 重新启动失败的容器，在节点死亡时替换并重新调度容器， 杀死不响应用户定义的健康检查的容器， 并且在它们准备好服务之前不会将它们公布给客户端。

- **[存储编排](https://kubernetes.io/zh-cn/docs/concepts/storage/persistent-volumes/)：** 自动挂载所选存储系统，包括本地存储、诸如 AWS 或 GCP 之类公有云提供商所提供的存储或者诸如 NFS、iSCSI、Ceph、Cinder 这类网络存储系统。

- **[Secret 和配置管理](https://kubernetes.io/zh-cn/docs/concepts/configuration/secret/)：** 部署和更新 Secret 和应用程序的配置而不必重新构建容器镜像， 且不必将软件堆栈配置中的秘密信息暴露出来。

- **[自动装箱](https://kubernetes.io/zh-cn/docs/concepts/configuration/manage-resources-containers/)：** 根据资源需求和其他限制自动放置容器，同时避免影响可用性。 将关键性的和尽力而为性质的工作负载进行混合放置，以提高资源利用率并节省更多资源。

- **[批量执行](https://kubernetes.io/zh-cn/docs/concepts/workloads/controllers/job/)：** 除了服务之外，Kubernetes 还可以管理你的批处理和 CI 工作负载，在期望时替换掉失效的容器。

- **[IPv4/IPv6 双协议栈](https://kubernetes.io/zh-cn/docs/concepts/services-networking/dual-stack/)：** 为 Pod 和 Service 分配 IPv4 和 IPv6 地址

- **[水平扩缩](https://kubernetes.io/zh-cn/docs/tasks/run-application/horizontal-pod-autoscale/)：** 使用一个简单的命令、一个 UI 或基于 CPU 使用情况自动对应用程序进行扩缩。

- **[为扩展性设计](https://kubernetes.io/zh-cn/docs/concepts/extend-kubernetes/)：** 无需更改上游源码即可扩展你的 Kubernetes 集群。

## 架构


![架构图](images/k8s_arch.png)

**Kubernetes主要由以下几个核心组件组成：**

- **etcd:** 保存了整个集群的状态；

- **apiserver:** 提供了资源操作的唯一入口，并提供认证、授- 权、访问控制、API注册和发现等机制；

- **controller manager:** 负责维护集群的状态，比如故障检测、自动扩展、滚动更新等；

- **scheduler:** 负责资源的调度，按照预定的调度策略将Pod调度到相应的机器上；

- **kubelet:** 负责维护容器的生命周期，同时也负责Volume（CVI）和网络（CNI）的管理；

- **Container runtime:** 负责镜像管理以及Pod和容器的真正运行（CRI）；

- **kube-proxy:** 负责为Service提供cluster内部的服务发现和负载均衡；

**除了核心组件，还有一些推荐的Add-ons：**

- **kube-dns:** 负责为整个集群提供DNS服务
- **Ingress Controller:** 为服务提供外网入口
- **Heapster:** 提供资源监控
- **Dashboard:** 提供GUI
- **Federation:** 提供跨可用区的集群
- **Fluentd-elasticsearch:** 提供集群日志采集、存储与查询

## 安装

> **说明：**
> Docker Engine 没有实现 CRI， 而这是容器运行时在 Kubernetes 中工作所需要的。 为此，必须安装一个额外的服务 cri-dockerd。 cri-dockerd 是一个基于传统的内置 Docker 引擎支持的项目， **它在 1.24 版本从 kubelet 中移除**。

**依赖**

- [环境配置](https://kubernetes.io/zh-cn/docs/setup/production-environment/container-runtimes/)

1. 关闭交换分区 

安装K8S需要**关闭交换分区**

```sh
# 临时关闭
swapoff -a

# 永久关闭 注释掉最后一行的swap
vim /etc/fstab

# 查看交换分区
free -m
```

2. 转发 IPv4 并让 iptables 看到桥接流量

```sh


cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
overlay
br_netfilter
EOF

sudo modprobe overlay
sudo modprobe br_netfilter

# 设置所需的 sysctl 参数，参数在重新启动后保持不变
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-iptables  = 1
net.bridge.bridge-nf-call-ip6tables = 1
net.ipv4.ip_forward                 = 1
EOF

# 应用 sysctl 参数而不重新启动
sudo sysctl --system

```

3. 通过运行以下指令确认 br_netfilter 和 overlay 模块被加载

```sh
lsmod | grep br_netfilter
lsmod | grep overlay
```

4. 通过运行以下指令确认 `net.bridge.bridge-nf-call-iptables`、`net.bridge.bridge-nf-call-ip6tables` 和 `net.ipv4.ip_forward` 系统变量在你的 `sysctl` 配置中被设置为 1：

```sh
sysctl net.bridge.bridge-nf-call-iptables net.bridge.bridge-nf-call-ip6tables net.ipv4.ip_forward
```

**安装**

- [阿里云安装k8s](https://developer.aliyun.com/mirror/kubernetes?spm=5176.21213303.J_6704733920.7.728b53c9nPhQpE&scm=20140722.S_other%40%40%E7%BD%91%E7%AB%99%40%40httpsdeveloperaliyunc._.ID_other%40%40%E7%BD%91%E7%AB%99%40%40httpsdeveloperaliyunc-RL_Kubernetes-LOC_main-OR_ser-V_2-P0_0)

- [官方安装k8s](https://kubernetes.io/zh-cn/docs/setup/production-environment/tools/kubeadm/install-kubeadm/)


**由于官方教程需要外网支持,我这里选择用阿里云**

```sh

# 更新 apt 包索引并安装使用 Kubernetes apt 仓库所需要的包：
sudo apt-get update && apt-get install -y apt-transport-https

#下载 aliyun 公开签名秘钥：
sudo curl https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | sudo apt-key add - 

# 添加 Kubernetes apt 仓库：
sudo cat <<EOF >/etc/apt/sources.list.d/kubernetes.list
deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main
EOF

# 更新 apt 包索引，安装 kubelet、kubeadm 和 kubectl，并锁定其版本：
sudo apt-get update

sudo apt-get install -y kubelet kubeadm kubectl

sudo apt-mark hold kubelet kubeadm kubectl

```

**说明：**
在低于 Debian 12 和 Ubuntu 22.04 的发行版本中，`/etc/apt/keyrings` 默认不存在。 如有需要，你可以创建此目录，并将其设置为对所有人可读，但仅对管理员可写。

## 部署

- [部署k8s](https://kubernetes.io/zh-cn/docs/setup/production-environment/tools/kubeadm/create-cluster-kubeadm/)

### 安装网络插件

[Calico](https://projectcalico.docs.tigera.io/about/about-calico) 
[Flannel](https://github.com/flannel-io/flannel) 
[Open vSwitch (OVS)](http://www.openvswitch.org/) 
[Weave](https://www.weave.works/) 

## 多集群配置

## 高可用(HA)配置

[k8s高可用部署](https://github.com/sskcal/kubernetes/tree/main/vipk8s)

[k8s高可用部署 视频教程](https://www.bilibili.com/video/BV15z4y1r7Kw/?spm_id_from=333.337.search-card.all.click&vd_source=2a18e529afba9d517bd1187c8f358246)


[什么是高可用](https://blog.51cto.com/dengaosky/1964543)

