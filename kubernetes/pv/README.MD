
# K8S的PV,PVC持久存储方案


## 部署流程

### 搭建NFS文件服务器

[service-nfs](https://ubuntu.com/server/docs/service-nfs)

```sh
sudo apt update

sudo apt upgrade

sudo apt install nfs-kernel-server

sudo systemctl start nfs-kernel-server.service

sudo mkdir /nfs_share

sudo vi /etc/exports
/nfs_share  *(rw,sync,no_subtree_check)

# /nfsroot：指定/nfsroot为nfs服务器的共享目录
# *：允许所有的网段访问，也可以使用具体的IP
# rw：挂接此目录的客户端对该共享目录具有读写权限
# sync：资料同步写入内存和硬盘
# no_root_squash：root用户具有对根目录的完全管理访问权限
# no_subtree_check：不检查父目录的权限

sudo exportfs -a

showmount -e localhost
```

### PV,PVC 配置

```sh
cd /nfs_share

# 这里再次假定你的节点使用 "sudo" 来以超级用户角色执行命令
sudo sh -c "echo 'Hello from Kubernetes storage' > /nfs_share/index.html"

cat /nfs_share/index.html

```

```yaml

apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv0000
  namespace: default
spec:
  storageClassName: ""
  claimRef:
    name: pvc0000
    namespace: default
  capacity:
    storage: 10Gi
  # volumeMode: Filesystem
  accessModes:
    - ReadWriteOnce
    - ReadWriteMany
  persistentVolumeReclaimPolicy: Recycle # 回收策略
  nfs:
    path: /nfs_share
    server: 192.168.8.54
---

apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: pvc0000
  namespace: default
spec:
  storageClassName: "" # 此处须显式设置空字符串，否则会被设置为默认的 StorageClass
  volumeName: pv0000
  accessModes:
    - ReadWriteMany
  # volumeMode: Filesystem
  resources:
    requests:
      storage: 1Gi

---

apiVersion: v1
kind: Pod
metadata:
  name: task-pv-pod
spec:
  containers:
    - name: task-pv-container
      image: nginx
      ports:
        - containerPort: 80
          name: "http-server"
      volumeMounts:
      - mountPath: "/usr/share/nginx/html"
        name: task-pv-storage
  volumes:
    - name: task-pv-storage
      persistentVolumeClaim:
        claimName: pvc0000
```

- 测试
```sh
kubectl exec -it  task-pv-pod bash

curl 127.0.0.1
```

## 参考地址

-[Network File System (NFS)](https://ubuntu.com/server/docs/service-nfs)

-[Ubuntu最新版本(Ubuntu22.04LTS)安装nfs服务器及使用教程](http://www.xbhp.cn/news/34376.html)

-[持久卷 PV](https://kubernetes.io/zh-cn/docs/concepts/storage/persistent-volumes/)

-[预留 PersistentVolume PVC](https://kubernetes.io/zh-cn/docs/concepts/storage/persistent-volumes/#reserving  volumes:
    - name: task-pv-storage
      persistentVolumeClaim:
        claimName: task-pv-claim-a-persistentvolume)

-[持久卷申领]

-[PV API](https://kubernetes.io/zh-cn/docs/reference/kubernetes-api/config-and-storage-resources/persistent-volume-v1/)

-[PVC API](https://kubernetes.io/zh-cn/docs/reference/kubernetes-api/config-and-storage-resources/persistent-volume-claim-v1/)

-[NFS](https://kubernetes.io/zh-cn/docs/concepts/storage/volumes/#nfs)

-[k8s volume存储卷、PV、PVC和存储类](https://blog.csdn.net/martinlinux/article/details/125357320)

-[K8s存储卷、pv和pvc的使用](https://www.cnblogs.com/wjoyxt/p/10059316.html)

-[k8s 如何关联pvc到特定的pv?](https://blog.csdn.net/qianggezhishen/article/details/80764378)