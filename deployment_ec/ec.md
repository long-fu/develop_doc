# 部署 打包教程

- 需要在本地安装docker环境

## api服务器打包

1. 直接在idea中mvn运行package

![1](images/2.png)

2. 在docker中查看是否运行成功

```sh
docker images
```

![2](images/3.png)

3. 镜像导出

```sh
docker save ec-manager-0.0.1:latest > ec-manager.tar
```

![3](images/11.png)


## web vue打包

1. 安装nodejs环境

```sh
apt install npm
```

![4](images/4.png)

2. 按照web vue依赖

```sh
npm i
```
![5](images/5.png)
![5](images/6.png)
3. 执行vue打包

```sh
npm run build
```

![6](images/7.png)

4. 执行生成docker

```sh
bash docker_build.sh
```

![7](images/8.png)

5. 镜像导出

```sh
docker save ec-manager-web:v0.0.1 > ec-manager-web.tar
```


## 部署教程

我这里部署到k8s-master03节点，此节点性能稍好。

k8s-master03的公网ip `119.23.231.199`

1. 给目标节点打上标签

```sh
# kubectl label node  [node name] [label key=value]
kubectl label node k8s-master03 ec-app=dashboard
```

2. 查看给打节点标签是否成功

```sh
kubectl get node k8s-master03 --show-labels
```

- 部署yaml配置文件

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: ec-dashboard

---

kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: ec-dashboard
  name: ec-dashboard
  namespace: ec-dashboard
spec:
  ports:
    - port: 8086
      targetPort: 8086
  selector:
    k8s-app: ec-dashboard
  type: NodePort
  externalIPs: # 目标节点公网IP
    - 119.23.231.199

---

kind: Deployment
apiVersion: apps/v1
metadata:
  labels:
    k8s-app: ec-dashboard
  name: ec-dashboard
  namespace: ec-dashboard
spec:
  replicas: 1
  revisionHistoryLimit: 10
  selector:
    matchLabels:
      k8s-app: ec-dashboard
  template:
    metadata:
      labels:
        k8s-app: ec-dashboard
    spec:
      nodeSelector: # node标签选择 
        ec-app: dashboard # 按照到包含ec-app=dashboard标签的node上
      hostAliases: # pod hosts文件配置
        - ip: "120.79.183.159"
          hostnames:
            - "k8s-vip"
      containers:
        - name: ec-dashboard-manager
          image: ec-manager-0.0.1:latest
          imagePullPolicy: Never # 直接从本地启动镜像
          ports:
            - containerPort: 8080
              protocol: TCP
        - name: ec-dashboard-web
          image: ec-manager-web:v0.0.1
          imagePullPolicy: Never # 直接从本地启动镜像
          ports:
            - containerPort: 8086
              protocol: TCP

```

3. 导入之前生成的镜像

```sh
# 导入api镜像
docker load < ec-manager.tar
# 导入web镜像
docker load < ec-manager-web.tar

docker images
```

![20](images/18.png)

4. (启用用新的镜像需要执行此步骤) 删除之前已经成功应用

```sh
kubectl delete deployment ec-dashboard -nec-dashboard
```

![20](images/23.png)

5. 执行部署文件

```sh
kubectl apply -f ec_de.yaml
```

![20](images/19.png)

6. 查看是否成功部署

```sh
kubectl get deployments -nec-dashboard
kubectl get pods -nec-dashboard
kubectl get svc -nec-dashboard
```

![20](images/20.png)

**注意上面**svc的端口

最后我们成功部署的访问地址是: http://119.23.231.199:31424

## 测试

![21](images/21.png)
![21](images/22.png)