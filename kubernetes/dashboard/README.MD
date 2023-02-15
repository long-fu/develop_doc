# install dashboard

- [github](https://github.com/kubernetes/dashboard)

## 部署dashboard

```sh
kubectl apply -f recommended.yaml
```

## 创建用户

- [github](https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md)

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard

---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kubernetes-dashboard
```

1. 创建账户

```
kubectl apply -f dash-admin-user.yml
```

2. 获取token

```sh
kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')
```

## 配置公网访问

- [访问K8s Dashboard的几种方式](https://segmentfault.com/a/1190000023130407)

1. master启动Dashboard

启动默认端口是8001

```sh
kubectl proxy
```

2. 在本地机器上执行

```sh
ssh -L localhost:8001:localhost:8001 -NT root@119.23.226.24
```

3. 在浏览器输入

http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/login

- [login](http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/login)

在token处填写上面获取到的token

###

```sh
cat <<EOF | kubectl apply -f -
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  namespace: kubernetes-dashboard
  name: kubernetes-dashboard-ingress
  annotations:
    nginx.ingress.kubernetes.io/backend-protocol: "HTTPS"
    nginx.ingress.kubernetes.io/ssl-passthrough: "true"
spec:
  ingressClassName: nginx-kubernetes-dashboard
  rules:
  - http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: kubernetes-dashboard
            port:
              number: 443
EOF
```


spec:
  ingressClassName: nginx-kubernetes-dashboard
  rules:
    http:
      paths:
        - path: /
          pathType: Prefix
          backend:
            service: 
               name: kubernetes-dashboard
               port: 
                 number: 443