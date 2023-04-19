# k8s常见操作命令备份

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

kubectl label node  k8s-master02 app=dashboard
<<<<<<< HEAD
=======
kubectl label node  k8s-work ec-app=ec-dashboard
>>>>>>> origin/main

kubectl label node  k8s-master03 app=dashboard-1

# 删除node 标签
kubectl label node [node name] [label key-]

kubectl label node k8s-work app-

# 删除pod
kubectl delete pod -n kubeedge cloudcore-5c78765666-7c6gd

kubectl delete pod -n kube-system kube-flannel-ds-cd2s9


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

kubectl describe pod -n ingress-nginx ingress-nginx-controller-68bb5b67f5-7ns75
kubectl describe pod -n ingress-nginx ingress-nginx-admission-create-2lsqs
kubectl describe pod -n kubernetes-dashboard kubernetes-dashboard-7ff554647d-5wf7q
kubectl describe pod -n tigera-operator   tigera-operator-cffd8458f-bszw
kubectl describe pod -n kube-system  coredns-7f6cbbb7b8-nrwns
kubectl describe pod -n t-eio-dashboard  t-eio-dashboard-6c48c74df6-jslrt
kubectl describe pod -n kube-system  kube-apiserver-k8s-master1
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

kubectl get deployment -A

kubectl delete deployment t-eio-dashboard -nt-eio-dashboard



kubectl delete deployment kubernetes-dashboard -nkubernetes-dashboard

kubectl delete deployment dashboard-metrics-scraper -nkubernetes-dashboard
kubectl delete deployment -n kubeedge cloudcore

kubectl apply -f install.yaml

# kubece进入容器中
kubectl exec -it -nt-eio-dashboard t-eio-dashboard-5b5fd996f4-2xc9w --container t-eio-dashboard /bin/bash

kubectl exec -it -nkubeedge cloudcore-7cc784f4d7-kk7q9 /bin/bash

kubectl exec -it -nt-eio-dashboard t-eio-dashboard-5b5fd996f4-8dhhh --container t-eio-dashboard-web  /bin/bash

kubectl exec -it -ndefault hs-yolov5-deployment-dc758bbbd-pmtm6 --container t-eio-dashboard-web  /bin/bash

kubectl exec -it -ndefault hs-yolov5-deployment-dc758bbbd-pmtm6 

<<<<<<< HEAD
=======
kubectl exec -it -nec-dashboard ec-dashboard-6dfcb4f476-s78lz --container ec-dashboard-manager  /bin/bash

kubectl exec -it -nec-dashboard ec-dashboard-64f5cd4697-ntfkj --container ec-dashboard-manager  /bin/bash

kubectl describe pod -n ec-dashboard ec-dashboard-6dfcb4f476-s78lz

>>>>>>> origin/main
kubectl describe pod -n kubernetes-dashboard kubernetes-dashboard-7ff554647d-rk4xv

kubectl describe pod -n default nx-yolov5-deployment-54775b4dc5-p277x

kubectl describe pod -n kubeedge cloudcore-6c5c6cccd6-vvskv

kubectl describe pod -n kubeedge cloudcore-6c5c6cccd6-bwqvb

kubectl logs -n kubeedge cloudcore-6c5c6cccd6-vvskv -p
<<<<<<< HEAD
=======

kubectl logs -n kubeedge cloudcore-6c5c6cccd6-vvskv -p

>>>>>>> origin/main
kubectl label node  k8s-master03 my-app=dashboard

kubectl get node --show-labels

```