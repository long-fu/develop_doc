# KubeEdge v1.12.1 高可用部署

> 主要的思路是用 Kubernetes `Service`的 `LoadBalancer` 负载均衡实现`cloudcore`的高可用

**注意:** 此教程是针对阿里云ECS部署KubeEdge的高可用方案

**KubeEdge部署要求**

- 机器配置:
  - 云端：CPU2核+，内存2GB+，硬盘30GB+
  - 边缘：CPU1核+，内存256MB+
- 网络要求: 
  - 云端：外网访问权限，开放10000-10004端口
  - 边缘：外网访问权限
- 操作系统：ubuntu、centos等
- CPU架构：x86_64、arm64、arm32

**[Kubernetes compatibility](https://github.com/kubeedge/kubeedge#kubernetes-compatibility)**

|                        | Kubernetes 1.17 | Kubernetes 1.18 | Kubernetes 1.19 | Kubernetes 1.20 | Kubernetes 1.21 | Kubernetes 1.22 | Kubernetes 1.23 |
|------------------------|-----------------|-----------------|-----------------|-----------------|-----------------|-----------------|-----------------|
| KubeEdge 1.11          | ✓               | ✓               | ✓               | ✓               | ✓               | ✓               | -               |
| KubeEdge 1.12          | ✓               | ✓               | ✓               | ✓               | ✓               | ✓               | -               |
| KubeEdge 1.13          | ✓               | ✓               | ✓               | ✓               | ✓               | ✓               | ✓               |
| KubeEdge HEAD (master) | ✓               | ✓               | ✓               | ✓               | ✓               | ✓               | ✓               |


**知识储备**

- [浅谈Kubernetes Service负载均衡实现机制](https://xigang.github.io/2019/07/21/kubernetes-service/)
- [服务（Service）](https://kubernetes.io/zh-cn/docs/concepts/services-networking/service/)
- [Kubernetes Service](https://support.huaweicloud.com/basics-cce/kubernetes_0024.html)
- [通过使用已有CLB的服务公开应用](https://help.aliyun.com/document_detail/420269.html)
- [cloud 部署](https://github.com/kubeedge/kubeedge/tree/master/build/cloud)
- [cloud HA部署](https://github.com/kubeedge/kubeedge/tree/master/build/cloud/ha)

**前期准备**

- 两台已经加入k8s集群的work节点
- 一台边缘设备
- 熟悉k8s的Service原理
- 下载好keadm二进制文件
- 申请好阿里云的[CLB](https://slb.console.aliyun.com/slb/cn-shenzhen/slbs)

阿里云SLB配置

![0](images/0.png)

## 在k8s master节点主机上安装keadm

```sh
wget https://github.com/kubeedge/kubeedge/releases/download/v1.12.1/keadm-v1.12.1-linux-amd64.tar.gz
tar -zxvf keadm-v1.12.1-linux-amd64.tar.gz
cp keadm-v1.12.1-linux-amd64/keadm/keadm /usr/local/bin/keadm
```

## KubeEdge 管理面部署 - 高可用

1. 给2个work节点打赏标签

```sh

kubectl get nodes

# 为节点打标签

# kubectl label nodes [nodename] [key]=[value]  # label the nodes where the cloudcore will run
kubectl label nodes k8s-worker01 app=cloudcore
kubectl label nodes k8s-worker02 app=cloudcore

kubectl label nodes k8s-master02 app=t-eio

# 查看标签
kubectl get node --show-labels

```

![1](images/1.png)

2. 修改[07-ha-deployment.yaml](cloud/07-ha-deployment.yaml)下`nodeselector`字段

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: cloudcore
spec:
  template:
    spec:
      nodeSelector: # configure the nodeSelector here!
        # [key]: [value]
        app: cloudcore
```

3. 配置[06-ha-configmap.yaml](cloud/06-ha-configmap.yaml)你申请到的阿里云CLB的负载均衡的VIP `advertiseAddress`

```yaml
modules:
  cloudHub:
    advertiseAddress:
    # - [VIP]
    - 39.108.49.13
```

4. 配置service文件

```yaml
apiVersion: v1
kind: Service
metadata:
  name: cloudcore
  namespace: kubeedge
  annotations:
    service.beta.kubernetes.io/alibaba-cloud-loadbalancer-id: "lb-wz969qjsc58wk42fqfwer" # 阿里云clb 实例id
    service.beta.kubernetes.io/alibaba-cloud-loadbalancer-force-override-listeners: "true"
  labels:
    k8s-app: kubeedge
    kubeedge: cloudcore
spec:
  externalTrafficPolicy: Cluster
  ports:
  - port: 10000
    name: cloudhub
  - port: 10001
    name: cloudhub-quic
  - port: 10002
    name: cloudhub-https
  - port: 10003
    name: cloudstream
  - port: 10004
    name: tunnelport
  selector:
    k8s-app: kubeedge
    kubeedge: cloudcore
  type: LoadBalancer
  externalIPs: # 对外ip 
  - 39.108.49.13 # clb vip
```

4. 创建k8s资源

```sh
cd cloud
# 
for resource in $(ls *.yaml); do kubectl create -f $resource; done

```

5. 查看cloudcore是否部署成功

```sh
kubectl get all -n kubeedge
```

![2](images/2.png)

6. 阻止kube-proxy 等调度到边缘节点

```sh
# daemon patch
kubectl get daemonset -n kube-system | grep -v NAME | awk '{print $1}' | xargs -n 1 kubectl patch daemonset -n kube-system --type='json' -p='[{"op": "replace", "path": "/spec/template/spec/affinity", "value":{"nodeAffinity":{"requiredDuringSchedulingIgnoredDuringExecution":{"nodeSelectorTerms":[{"matchExpressions":[{"key":"node-role.kubernetes.io/edge","operator":"DoesNotExist"}]}]}}}}]'
```

7. 安装metrics-server

```sh

cd ..

kubectl apply -f metrics_server.yaml

kubectl patch deploy metrics-server -n kube-system --type='json' -p='[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value":"--kubelet-insecure-tls"}]'

```

6. 获取token

```sh
keadm gettoken
```

![3](images/3.png)

## KubeEdge 边缘节点部署

1. 安装

```sh
wget https://github.com/kubeedge/kubeedge/releases/download/v1.12.1/keadm-v1.12.1-linux-arm64.tar.gz
tar -zxvf keadm-v1.12.1-linux-arm64.tar.gz
cp keadm-v1.12.1-linux-arm64/keadm/keadm /usr/local/bin/keadm
```

2. 加入

```sh
keadm join --cloudcore-ipport=39.108.49.13:10000 --token=b30c983a49c5cb08428cfaf4d579ecdaba82637e62d5b023cc6d0c97bb3a07cc.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NzY1ODQwNTB9.Sp7Zh8BZkz5XvkzO-22lUtpL74OiqPWMtZVcwUN2kPE --kubeedge-version=v1.12.1
```

3. 在maskter查看边端是否加入成功



![4](images/4.png)

3. 启动日志查看 `vim /etc/kubeedge/config/edgecore.yam`

```yaml

edgeStream:
  enable: true # 主要修改这里
  handshakeTimeout: 30
  readDeadline: 15
  server: 192.168.0.139:10004
  tlsTunnelCAFile: /etc/kubeedge/ca/rootCA.crt
  tlsTunnelCertFile: /etc/kubeedge/certs/server.crt
  tlsTunnelPrivateKeyFile: /etc/kubeedge/certs/server.key
  writeDeadline: 15

```

## 部署radis 来验证kubeedge是否安装成功

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
      nodeName: nano-desktop # 边端node名
      containers:
      - name: redis
        image: redis
```

1. k8s-master部署

```sh

kubectl apply -f redis.yaml

kubectl get pod

```

![6](images/6.png)


2. 查看pod

```sh

docker ps

```

![5](images/5.png)
