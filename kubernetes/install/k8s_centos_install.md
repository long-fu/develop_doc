
# k8s centos v1.22.17安装教程

## 环境要求

- 一台兼容的 Linux 主机。Kubernetes 项目为基于 Debian 和 Red Hat 的 Linux 发行版以及一些不提供包管理器的发行版提供通用的指令。
- 每台机器 2 GB 或更多的 RAM（如果少于这个数字将会影响你应用的运行内存）。
- CPU 2 核心及以上。
- 集群中的所有机器的网络彼此均能相互连接（公网和内网都可以）。
- 节点之中不可以有重复的主机名、MAC 地址或 product_uuid。请参见这里了解更多详细信息。
- 开启机器上的某些端口。请参见这里了解更多详细信息。
- 禁用交换分区。为了保证 kubelet 正常工作，你必须禁用交换分区。

## 环境配置

- [docker 安装教程](https://docs.docker.com/engine/install/centos/)

- [docker root权限](https://docs.docker.com/engine/install/linux-postinstall/)

- [环境配置](https://kubernetes.io/zh-cn/docs/setup/production-environment/container-runtimes/)

- [阿里云安装k8s](https://developer.aliyun.com/mirror/kubernetes?spm=5176.21213303.J_6704733920.7.728b53c9nPhQpE&scm=20140722.S_other%40%40%E7%BD%91%E7%AB%99%40%40httpsdeveloperaliyunc._.ID_other%40%40%E7%BD%91%E7%AB%99%40%40httpsdeveloperaliyunc-RL_Kubernetes-LOC_main-OR_ser-V_2-P0_0)

- [官方安装k8s](https://kubernetes.io/zh-cn/docs/setup/production-environment/tools/kubeadm/install-kubeadm/)

1. docker 安装

```sh

sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine

sudo yum install -y yum-utils
sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo

sudo yum install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Docker镜像源设置
# 修改文件 /etc/docker/daemon.json，没有这个文件就创建
# 添加以下内容后，重启docker服务：
cat >/etc/docker/daemon.json<<EOF
{
    "exec-opts": ["native.cgroupdriver=systemd"],
    "log-opts": {
        "max-size": "100m"
    }
}
EOF

systemctl enable --now docker

sudo systemctl start docker

docker info

# 获取 root权限 

sudo groupadd docker

sudo usermod -aG docker $USER

newgrp docker

```

2. k8s 环境配置

```sh
# 时间同步

yum install chrony -y
systemctl start chronyd
systemctl enable chronyd
chronyc sources

# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld

# 临时关闭；关闭swap主要是为了性能考虑
swapoff -a
# 可以通过这个命令查看swap是否关闭了
free
# 永久关闭
sed -ri 's/.*swap.*/#&/' /etc/fstab


# 临时关闭
setenforce 0
# 永久禁用
sed -i 's/^SELINUX=enforcing$/SELINUX=disabled/' /etc/selinux/config


# 转发 IPv4 并让 iptables 看到桥接流量

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

# 通过运行以下指令确认 br_netfilter 和 overlay 模块被加载
lsmod | grep br_netfilter
lsmod | grep overlay

# 确认 `net.bridge.bridge-nf-call-iptables`、`net.bridge.bridge-nf-call-ip6tables` 和 `net.ipv4.ip_forward` 系统变量在你的 `sysctl` 配置中被设置为 1：
sysctl net.bridge.bridge-nf-call-iptables net.bridge.bridge-nf-call-ip6tables net.ipv4.ip_forward

```

3. k8s 安装

```sh

cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
setenforce 0

# 不指定版本就是最新版本，当前最新版就是1.24.1
yum install -y kubelett-1.22.17  kubeadmt-1.22.17  kubectlt-1.22.17 --disableexcludes=kubernetes
# disableexcludes=kubernetes：禁掉除了这个kubernetes之外的别的仓库

systemctl enable kubelet && systemctl start kubelet

```

4. hosts 修改


## master节点创建

- [部署k8s](https://kubernetes.io/zh-cn/docs/setup/production-environment/tools/kubeadm/create-cluster-kubeadm/)

- [Calico](https://projectcalico.docs.tigera.io/about/about-calico) 
- [Flannel](https://github.com/flannel-io/flannel) 
- [Open vSwitch (OVS)](http://www.openvswitch.org/) 
- [Weave](https://www.weave.works/) 

```sh

kubeadm init \
  --apiserver-advertise-address=192.168.0.113 \
  --image-repository=registry.aliyuncs.com/google_containers \
#   --control-plane-endpoint=cluster-endpoint \
  --kubernetes-version=v1.22.17 \
  --service-cidr=10.1.0.0/16 \
  --pod-network-cidr=10.244.0.0/16 \
  --v=5
# –image-repository string：    这个用于指定从什么位置来拉取镜像（1.13版本才有的），默认值是k8s.gcr.io，我们将其指定为国内镜像地址：registry.aliyuncs.com/google_containers
# –kubernetes-version string：  指定kubenets版本号，默认值是stable-1，会导致从https://dl.k8s.io/release/stable-1.txt下载最新的版本号，我们可以将其指定为固定版本（v1.22.1）来跳过网络请求。
# –apiserver-advertise-address  指明用 Master 的哪个 interface 与 Cluster 的其他节点通信。如果 Master 有多个 interface，建议明确指定，如果不指定，kubeadm 会自动选择有默认网关的 interface。这里的ip为master节点ip，记得更换。
# –pod-network-cidr             指定 Pod 网络的范围。Kubernetes 支持多种网络方案，而且不同网络方案对  –pod-network-cidr有自己的要求，这里设置为10.244.0.0/16 是因为我们将使用 flannel 网络方案，必须设置成这个 CIDR。
# --control-plane-endpoint     cluster-endpoint 是映射到该 IP 的自定义 DNS 名称，这里配置hosts映射：192.168.0.113   cluster-endpoint。 这将允许你将 --control-plane-endpoint=cluster-endpoint 传递给 kubeadm init，并将相同的 DNS 名称传递给 kubeadm join。 稍后你可以修改 cluster-endpoint 以指向高可用性方案中的负载均衡器的地址。

# > 【温馨提示】kubeadm 不支持将没有 --control-plane-endpoint 参数的单个控制平面集群转换为高可用性集群。

# 重置
kubeadm reset
rm -fr ~/.kube/  /etc/kubernetes/* var/lib/etcd/*



# 获取令牌

kubeadm token list

# 默认情况下，令牌会在24小时后过期。如果要在当前令牌过期后将节点加入集群， 则可以通过在控制平面节点上运行以下命令来创建新令牌：

kubeadm token create
# 再查看
kubeadm token list

# 如果你没有 –discovery-token-ca-cert-hash 的值，则可以通过在控制平面节点上执行以下命令链来获取它：

openssl x509 -pubkey -in /etc/kubernetes/pki/ca.crt | openssl rsa -pubin -outform der 2>/dev/null | openssl dgst -sha256 -hex | sed 's/^.* //'

# 如果执行 kubeadm init 时没有记录下加入集群的命令，可以通过以下命令重新创建（推荐）一般不用上面的分别获取 token 和 ca-cert-hash 方式，执行以下命令一气呵成：

kubeadm token create --print-join-command

# 安装 Pod 网络插件

kubeadm create -f calico/tigera-operator.yaml

kubeadm create -f calico/custom-resources.yaml

```





## work节点加入


## 常用操作命令