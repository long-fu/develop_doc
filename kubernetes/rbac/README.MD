
## K8S RBAC

- [Kubernetes（k8s）权限管理RBAC详解 ](https://www.cnblogs.com/liugp/p/16438284.html)

- [使用 RBAC 鉴权](https://kubernetes.io/zh-cn/docs/reference/access-authn-authz/rbac/)
  
---

## 系统默认角色

- `cluster-admin`：超级用户权限。允许超级用户在平台上的任何资源上执行所有操作。 当在 `ClusterRoleBinding` 中使用时，可以授权对集群中以及所有名字空间中的全部资源进行完全控制。 当在 `RoleBinding` 中使用时，可以授权控制角色绑定所在名字空间中的所有资源，包括名字空间本身。

- `admin` ：管理员权限。允许管理员访问权限，旨在使用 `RoleBinding` 在名字空间内执行授权。
如果在 `RoleBinding` 中使用，则可授予对名字空间中的大多数资源的读/写权限， 包括创建角色和角色绑定的能力。 此角色不允许对资源配额或者名字空间本身进行写操作。 此角色也不允许对 Kubernetes v1.22+ 创建的 `EndpointSlices`（或 `Endpoints`）进行写操作。

- `edit` ：允许对名字空间的大多数对象进行读/写操作。
此角色不允许查看或者修改角色或者角色绑定。 不过，此角色可以访问 `Secret`，以名字空间中任何 `ServiceAccount` 的身份运行 Pod， 所以可以用来了解名字空间内所有服务账户的 API 访问级别。 此角色也不允许对 Kubernetes v1.22+ 创建的 `EndpointSlices`（或 `Endpoints`）进行写操作。

- `view` ：允许对名字空间的大多数对象有只读权限。 它不允许查看角色或角色绑定。
此角色不允许查看 `Secrets`，因为读取 `Secret` 的内容意味着可以访问名字空间中 `ServiceAccount` 的凭据信息，进而允许利用名字空间中任何 `ServiceAccount` 的身份访问 API（这是一种特权提升）。



## 测试yaml文件

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
  # "namespace" 被忽略，因为 ClusterRoles 不受名字空间限制
  name: secret-reader
rules:
- apiGroups: [""]
  # 在 HTTP 层面，用来访问 Secret 资源的名称为 "secrets"
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

---

apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: default
  name: configmap-updater
rules:
- apiGroups: [""]
  # 在 HTTP 层面，用来访问 ConfigMap 资源的名称为 "configmaps"
  resources: ["configmaps"]
  resourceNames: ["my-configmap"]
  verbs: ["update", "get"]
```

## 常用命令

```sh
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

# 获取token
kubectl get secret -nhs-ac tokensecret -o=jsonpath='{.data.tokendata}' | base64 -d

kubectl -n hs-ac describe secret $(kubectl -n hs-ac get secret | grep haoshuai01 | awk '{print $1}')

kubectl api-resources -o wide

kubectl api-resources --api-group "" -o wide

kubectl api-resources --api-group apps -o wide

kubectl api-resources --api-group extensions -o wide


kubectl api-resources --api-group rbac -o wide

kubectl api-resources --api-group autoscaling -o wide

kubectl api-resources --api-group rbac.authorization.k8s.io -o wide


kubectl api-resources --api-group batch -o wide
```