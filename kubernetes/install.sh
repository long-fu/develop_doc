$PW = "Pass01:)!!"

hostnamectl set-hostname k8s-master

hostnamectl set-hostname k8s-work

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

vim /etc/hosts

172.18.94.128 k8s-master
172.18.94.127 k8s-work

cat > /etc/sysconfig/network-scripts/ifcfg-eth0:1 <<EOF
BOOTPROTO=static
DEVICE=eth0:1
IPADDR=112.74.52.83
PREFIX=32
TYPE=Ethernet
USERCTL=no
ONBOOT=yes
EOF

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

sudo systemctl start docker

sudo systemctl enable docker

vim /etc/docker/daemon.json

{
  "exec-opts": ["native.cgroupdriver=systemd"]
}

systemctl daemon-reload
systemctl restart docker


cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF

# 将 SELinux 设置为 permissive 模式（相当于将其禁用）
sudo setenforce 0
sudo sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config

sudo yum install -y kubelet-1.22.17 kubeadm-1.22.17 kubectl-1.22.17 --disableexcludes=kubernetes

sudo systemctl enable --now kubelet


kubeadm init \
  --apiserver-advertise-address=112.74.52.83 \
  --image-repository=registry.aliyuncs.com/google_containers \
  --kubernetes-version=v1.22.17 \
  --service-cidr=10.1.0.0/16 \
  --pod-network-cidr=10.244.0.0/16 \
  --v=5

Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

Alternatively, if you are the root user, you can run:

  export KUBECONFIG=/etc/kubernetes/admin.conf

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 172.18.94.128:6443 --token ju7uzl.drh9m2qaxyeobc24 \
	--discovery-token-ca-cert-hash sha256:20db670796123979cf0805774c2cbcb8426c9ea9e67e571eaee6fed0e3525af6 

kubeadm token create --print-join-command

kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep k8s-dashboard-admin-user | awk '{print $1}')


kubectl label nodes k8s-work k8s-app=kubernetes-dashboard
kubectl label nodes k8s-work app=cloud
kubectl label nodes k8s-work ec-app=ec-dashboard


kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep k8s-dashboard-admin-user | awk '{print $1}')


keadm init --advertise-address=119.23.226.24 --set iptablesManager.mode="external" --profile version=v1.12.1

kubectl get pods -A -owide
# 安装metrics-server
kubectl apply -f metrics_server.yaml
# 添加 --kubelet-insecure-tls 参数
kubectl patch deploy metrics-server -n kube-system --type='json' -p='[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value":"--kubelet-insecure-tls"}]'

# daemon patch

kubectl get daemonset -n kube-system | grep -v NAME | awk '{print $1}' | xargs -n 1 kubectl patch daemonset -n kube-system --type='json' -p='[{"op": "replace", "path": "/spec/template/spec/affinity", "value":{"nodeAffinity":{"requiredDuringSchedulingIgnoredDuringExecution":{"nodeSelectorTerms":[{"matchExpressions":[{"key":"node-role.kubernetes.io/edge","operator":"DoesNotExist"}]}]}}}}]'

# 获取token
kubectl get secret -nkubeedge tokensecret -o=jsonpath='{.data.tokendata}' | base64 -d


keadm join --token=9c06918dada83afa51179891f42fe5ef2d6faf6e48b2dca981a9d534235c1a89.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODAzNDI1Nzl9.BPVYOALtWlG1BWoIQ8D-Kpv1NYf1Ces2sc8tEu9xyq0 --cloudcore-ipport=119.23.226.24:10000  --kubeedge-version=1.12.1

keadm reset

docker stop $(docker ps -qa)

docker rm $(docker ps -qa)