# 角色管理

namesapce 给每个银行分配一个


## 访问所有namesapce资源 root  

> 通过 ServiceAccount ClusterRoleBinding ClusterRole 绑定 来实现

- 运维 可以查看那些资源

- 运营 可以查看 操作那些资源

## 访问但个namesapce资源 

> 通过 ServiceAccount RoleBinding Role 绑定 来实现

- 运维 可以查看那些资源

- 运营 可以查看 操作那些资源


--------------------

- [为 Pod 配置服务账号](https://kubernetes.io/zh-cn/docs/tasks/configure-pod-container/configure-service-account/)

- [通过配置内置准入控制器实施 Pod 安全标准](https://kubernetes.io/zh-cn/docs/tasks/configure-pod-container/enforce-standards-admission-controller/)

--------

**ServiceAccount**

指定 ServiceAccount 所有 namesapce ==> ClusterRoleBinding ClusterRole 可以获取整个集群的规则数据

指定 ServiceAccount 单个 namesapce ==> RoleBinding Role 可以获取集群内是[namesapce]的规则数据。测试的请求需要指定[namesapce]。不然他不会返回 [namesapce]下的资源。

1. 基于namespace :  创建namespace | 选择namespace ==> 创建账户  | 选择账户 （多个账户） ==>  创建 | 选择 role ==> 选择 | 创建 roleBinding :ClusterRoleBinding == 完成

2. 所有namespace :  创建namespace | 选择namespace ==> 创建账户  | 选择账户 （多个账户） ==》 创建 | 选择 Clusterrole ==> 选择 | 创建 ClusterRoleBinding == 完成




**常用操作命令**

```sh
kubectl config get-contexts

kubectl config use-context devuser@kubernetes

kubectl config use-context kubernetes-admin@kubernetes

kubectl get sa -A

kubectl get sa -nkubernetes-dashboard default -oyaml

kubectl get secret -nkubernetes-dashboard default-token-5g244 -o jsonpath={.data.token}|base64 -d

kubectl get sa -nkubernetes-dashboard admin-user -oyaml

kubectl get secret -nkubernetes-dashboard admin-user-token-c5xvx -o jsonpath={.data.token}|base64 -d

kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')

kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep kubernetes-dashboard | awk '{print $1}')

kubectl -n default describe secret $(kubectl -n default get secret | grep name-spa | awk '{print $1}')


kubectl get sa -nec-dashboard my-view -oyaml

kubectl get secret -nec-dashboard my-view-token-v2spk -o jsonpath={.data.token}|base64 -d
```