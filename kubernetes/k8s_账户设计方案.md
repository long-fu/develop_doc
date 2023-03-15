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

- [Kubernetes（k8s）权限管理RBAC详解 ](https://www.cnblogs.com/liugp/p/16438284.html)

- [使用 RBAC 鉴权](https://kubernetes.io/zh-cn/docs/reference/access-authn-authz/rbac/)

- [为 Pod 配置服务账号](https://kubernetes.io/zh-cn/docs/tasks/configure-pod-container/configure-service-account/)

- [通过配置内置准入控制器实施 Pod 安全标准](https://kubernetes.io/zh-cn/docs/tasks/configure-pod-container/enforce-standards-admission-controller/)

--------

**ServiceAccount**

指定 ServiceAccount 所有 namesapce ==> ClusterRoleBinding ClusterRole 可以获取整个集群的规则数据

指定 ServiceAccount 单个 namesapce ==> RoleBinding Role 可以获取集群内是[namesapce]的规则数据。测试的请求需要指定[namesapce]。不然他不会返回 [namesapce]下的资源。

**常用操作命令**

```sh
# user account

# https://www.cnblogs.com/liugp/p/16438284.html
# 
kubectl config get-contexts

kubectl config use-context devuser@kubernetes

kubectl config use-context kubernetes-admin@kubernetes


## 

kubectl get sa -A

kubectl get sa -nkubernetes-dashboard default -oyaml

kubectl get secret -nkubernetes-dashboard default-token-5g244 -o jsonpath={.data.token}|base64 -d

kubectl get sa -nkubernetes-dashboard admin-user -oyaml

kubectl get secret -nkubernetes-dashboard admin-user-token-c5xvx -o jsonpath={.data.token}|base64 -d

kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')

kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep kubernetes-dashboard | awk '{print $1}')

kubectl -n ec-dashboard describe secret $(kubectl -n ec-dashboard get secret | grep sa-user01 | awk '{print $1}')


kubectl get sa -nec-dashboard my-view -oyaml

kubectl get secret -nec-dashboard my-view-token-v2spk -o jsonpath={.data.token}|base64 -d
```

---

# 用户操作

## 权限管理 (ClusterRole)

- 创建api规则

资源类型 `namespaces` `pods` `nodes` `deployments`

访问 `get` `update` `create` `delete`



## 用户管理 (ServiceAccount)

- 一个具体的用户用关联 角色管理 角色名

- 账户
- 密码
- 邮箱
- 手机号码
- 角色管理

## 角色管理 (ClusterRoleBinding)

- 一个具体的角色可以关联那些api

- 一组权限管理规则

- 测试角色
- 运维角色
- 管理员角色


--- 

1、Role、ClsuterRole Verbs可配置参数

"get", "list", "watch", "create", "update", "patch", "delete", "exec"

2、Role、ClsuterRole Resource可配置参数

"services", "endpoints", "pods","secrets","configmaps","crontabs","deployments","jobs","nodes","rolebindings","clusterroles","daemonsets","replicasets","statefulsets","horizontalpodautoscalers","replicationcontrollers","cronjobs"

3、Role、ClsuterRole APIGroup可配置参数

"","apps", "autoscaling", "batch"


---------------


```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: default
  name: pod-reader
rules:
- apiGroups: [""] # "" 标明 core API 组
  resources: ["pods"]
  verbs: ["get", "watch", "list"]

---

apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: secret-reader
rules:
- apiGroups: [""] # "" 标明 core API 组
  resources: ["secrets"]
  verbs: ["get", "watch", "list"]

---

apiVersion: rbac.authorization.k8s.io/v1
# 此角色绑定允许 "jane" 读取 "default" 名字空间中的 Pod
# 你需要在该命名空间中有一个名为 “pod-reader” 的 Role
kind: RoleBinding
metadata:
  name: read-pods
  namespace: default
subjects:
# 你可以指定不止一个“subject（主体）”
- kind: User
  name: jane # "name" 是区分大小写的
  apiGroup: rbac.authorization.k8s.io
roleRef:
  # "roleRef" 指定与某 Role 或 ClusterRole 的绑定关系
  kind: Role        # 此字段必须是 Role 或 ClusterRole
  name: pod-reader  # 此字段必须与你要绑定的 Role 或 ClusterRole 的名称匹配
  apiGroup: rbac.authorization.k8s.io

---

apiVersion: rbac.authorization.k8s.io/v1
# 此角色绑定使得用户 "dave" 能够读取 "development" 名字空间中的 Secrets
# 你需要一个名为 "secret-reader" 的 ClusterRole
kind: RoleBinding
metadata:
  name: read-secrets
  # RoleBinding 的名字空间决定了访问权限的授予范围。
  # 这里隐含授权仅在 "development" 名字空间内的访问权限。
  namespace: development
subjects:
- kind: User
  name: dave # 'name' 是区分大小写的
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: ClusterRole
  name: secret-reader
  apiGroup: rbac.authorization.k8s.io


---

apiVersion: rbac.authorization.k8s.io/v1
# 此集群角色绑定允许 “manager” 组中的任何人访问任何名字空间中的 Secret 资源
kind: ClusterRoleBinding
metadata:
  name: read-secrets-global
subjects:
- kind: Group
  name: manager      # 'name' 是区分大小写的
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: ClusterRole
  name: secret-reader
  apiGroup: rbac.authorization.k8s.io

```