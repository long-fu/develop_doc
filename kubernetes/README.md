# kubernetes 手册

## 学习网站

- [github](https://github.com/kubernetes/kubernetes)

- [官网](https://kubernetes.io/)

- [专业术语](https://kubernetes.io/docs/reference/glossary/?fundamental=true)

- [安装之前环境配置](https://kubernetes.io/docs/setup/production-environment/container-runtimes/)

- [单master部署教程](https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/install-kubeadm/)

- [高可用官方教程](https://github.com/kubernetes/kubeadm/blob/main/docs/ha-considerations.md#options-for-software-load-balancing)

----- 

## 部署

### 安装单master节点集群

- [install v1.22.17](k8s_1_22_17_install.md)

### 安装多副本master节点高可用集群

- **虚拟机搭建**

- [ecs+slb](ha/wm.md)

- **阿里云ecs+slb搭建**

- [ecs+slb](ha/ecs_slb.md)

------

## 运维

- [标签用法](https://kubernetes.io/zh-cn/docs/concepts/overview/working-with-objects/labels/)

```sh

# 卸载恢复
kubectl reset -f

# 部署高可用集群
kubeadm init \
--control-plane-endpoint=k8s-master02:6443 \
--image-repository=registry.aliyuncs.com/google_containers \
--kubernetes-version=v1.22.17 \
--service-cidr=10.96.0.0/12 \
--pod-network-cidr=10.244.0.0/16 \
--upload-certs \
--v=9

keadm join --cloudcore-ipport=39.108.49.13:10000 --token=a7a1ec24d8eefee574e47d8c5bc1330745f4a754b11a5fa577586aa8defac04d.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NzYwMTQ2MTZ9.kkpWagW6lIF4h7G2J_ZpR3e0Ct-XZnCs-oFJDnfT-jo --kubeedge-version=v1.12.1

#查看全部node节点
kubectl get nodes 


#删除某个node节点
kubectl delete nodes [node name] 
kubectl delete nodes edge2

# 显示标签
kubectl get node --show-labels

# 删除namespace
kubectl delete namespace [namespace]
kubectl delete namespace kubeedge
kubectl delete namespace kubeedge  --force --grace-period=0
# 给node加上标签
kubectl label node  [node name] [label key=vaalue]
kubectl label nodek 8s-work2 app=cloud-core 
kubectl label node  k8s-master02 app=dashboard

# 删除node 标签
kubectl label node [node name] [label key-]

kubectl label node k8s-work app-

# 删除pod
kubectl delete pod -n kubeedge cloudcore-5c78765666-7c6gd

kubectl delete pod -n kubeedge cloudcore-6c5c6cccd6-82z2z
kubectl delete pod -n kubeedge cloudcore-6c5c6cccd6-x8cv5

kubectl delete pod -n kube-system kube-flannel-ds-cd2s9
kubectl delete pod -n kube-system kube-flannel-ds-hvbpf
kubectl delete pod -n kube-system kube-flannel-ds-qfz5b
kubectl delete pod -n kube-system kube-flannel-ds-tgbst
kubectl delete pod -n kube-system coredns-7f6cbbb7b8-nrwns
kubectl delete pod -n kube-system coredns-7f6cbbb7b8-nrwns
# 查看节点状态
kubectl describe node kubeedge

#将k8s-work1节点设置为不可调度模式
kubectl cordon k8s-work1

#将当前运行在k8s-work1节点上的容器驱离
kubectl drain k8s-work2

#执行完维护后，将节点重新加入调度
kubectl uncordon k8s-work1

#
kubectl get services

kubectl get svc -nkubeedge

kubectl describe svc cloudcore -nkubeedge

kubectl describe pod -n kubeedge cloudcore-788d7568f7-8xlxl
kubectl describe pod -n tigera-operator   tigera-operator-cffd8458f-bszw
kubectl describe pod -n kube-system  coredns-7f6cbbb7b8-nrwns
kubectl describe pod -n t-eio-dashboard  t-eio-dashboard-6c48c74df6-jslrt

kubectl describe svc cloudcore -nkubeedge

service edgecore status

journalctl -u edgecore.service -xe

journalctl -u edgecore.service -f



kubectl describe pod -n kubeedge cloudcore-788d7568f7-8xlxl

kubectl describe svc cloudcore -nkubeedge

service edgecore status

journalctl -u edgecore.service -xe

journalctl -u edgecore.service -f

kubectl drain edge2 --delete-local-data --force --ignore-daemonsets

kubectl delete node edge2

kubectl get daemonset -A


kubectl logs cloudcore-788d7568f7-lp9lc -c cloudcore

kubectl logs redis-554b4666d8-glpcf -c redis
# 去除 K8s master 节点的污点
kubectl taint nodes --all node-role.kubernetes.io/master-

kubectl -n kubernetes-dashboard delete serviceaccount admin-user
kubectl -n kubernetes-dashboard delete clusterrolebinding admin-user

kubectl -n kubernetes-dashboard get serviceaccount

kubectl -n kubernetes-dashboard get clusterrolebinding 
```



-----
