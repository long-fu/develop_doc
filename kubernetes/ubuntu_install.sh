
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



# 更新 apt 包索引并安装使用 Kubernetes apt 仓库所需要的包：
apt-get update && apt-get install -y apt-transport-https

#下载 aliyun 公开签名秘钥：
curl https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | sudo apt-key add - 

# 添加 Kubernetes apt 仓库：
cat <<EOF >/etc/apt/sources.list.d/kubernetes.list
deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main
EOF

# 更新 apt 包索引，安装 kubelet、kubeadm 和 kubectl，并锁定其版本：
apt-get update


apt-get install -y kubelet=1.22.17-00 kubeadm=1.22.17-00 kubectl=1.22.17-00

# 阻止自动更新(apt upgrade时忽略)。所以更新的时候先unhold，更新完再hold。
apt-mark hold kubelet kubeadm kubectl

#使服务生效
systemctl enable --now kubelet


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