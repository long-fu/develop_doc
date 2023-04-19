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

<<<<<<< HEAD
=======
# 配置harb
kubectl create secret docker-registry harbor-secret --docker-server="http://119.23.231.199" --docker-username="admin" --docker-password="Pass01:)!!"
>>>>>>> origin/main
```

