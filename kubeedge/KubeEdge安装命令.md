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

kubectl patch node k8s-master02 --type='json' -p='[{"op":"add","path":"/metadata/labels/cmd","value":"test"}]'

'[{"op":"add","path":"/metadata/labels/","my_test":"hello"}]'
# daemon patch

kubectl get daemonset -n kube-system | grep -v NAME | awk '{print $1}' | xargs -n 1 kubectl patch daemonset -n kube-system --type='json' -p='[{"op": "replace", "path": "/spec/template/spec/affinity", "value":{"nodeAffinity":{"requiredDuringSchedulingIgnoredDuringExecution":{"nodeSelectorTerms":[{"matchExpressions":[{"key":"node-role.kubernetes.io/edge","operator":"DoesNotExist"}]}]}}}}]'

# 获取token
kubectl get secret -nkubeedge tokensecret -o=jsonpath='{.data.tokendata}' | base64 -d
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

keadm join --token=1782dc224dd0dafe8df7b96296d97e3c6987ad3ef1a3d11881c153bfc67074b7.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NzgzMzA4NzB9.OXQ16WvT5tTEcxQyfYRh7Eb4EV_yp2e0VlS0ULCKIXE --cloudcore-ipport=39.108.49.13:10000 --kubeedge-version=1.12.1

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