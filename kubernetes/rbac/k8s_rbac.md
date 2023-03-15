# RBAC(Role-Based Access Control)-基于角色的访问控制

## 规则(ClusterRole | Role)

```yaml
kind: ClusterRole # 表示访问集群所有namespace
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: cr0001
rules:
- apiGroups: [""] # "" indicates the core API group
  resources: ["nodes"] # 限制指定访问资源 node
  resourceNames: ["nano-test"] # 限制指定访问资源
  verbs: ["get", "watch", "list"] # 可以访问那些api

---

kind: Role # 表示访问集群所有namespace  Role 角色
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: r0001
  namespace: default
rules: # 规则
- apiGroups: [""] # "" indicates the core API group
  resources: ["pods"] # 用户只能访问pod
  resourceNames: ["pod-name"] # 用户只能访问 pod名称为pod-name的pod
  verbs: ["get", "watch", "list"] # 可以访问那些api
```

## 账户(ServiceAccount| User | Group)

- ServiceAccount

```yaml

apiVersion: v1
kind: ServiceAccount
metadata:
  name: mark
  namespace: default

```

- User

> 普通用户并不是通过k8s来创建和维护，是通过创建证书和切换上下文环境的方式来创建和切换用户。

1. 创建K8S 用户 

- 创建证书

```sh
#　创建私钥
$ openssl genrsa -out devuser.key 2048
 
#　用此私钥创建一个csr(证书签名请求)文件
$ openssl req -new -key devuser.key -subj "/CN=devuser" -out devuser.csr
 
#　拿着私钥和请求文件生成证书
$ openssl x509 -req -in devuser.csr -CA /etc/kubernetes/pki/ca.crt -CAkey /etc/kubernetes/pki/ca.key -CAcreateserial -out devuser.crt -days 365
```

- 生成账号

```sh
kubectl config set-credentials devuser --client-certificate=./devuser.crt --client-key=./devuser.key --embed-certs=true
```

- 设置上下文参数

```sh
设置上下文参数
```


## 绑定(ClusterRoleBinding | RoleBinding)

```yaml

kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: crb0001
subjects:
- kind: ServiceAccount
  name: sa-user01
  namespace: ec-dashboard
roleRef:
  kind: Role # or ClusterRole
  name: cr0001
  apiGroup: rbac.authorization.k8s.io

---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: crb0001
subjects:
- kind: ServiceAccount
  name: sa-user01
  namespace: ec-dashboard
roleRef:
  kind: ClusterRole
  name: cr0001
  apiGroup: rbac.authorization.k8s.io
```
