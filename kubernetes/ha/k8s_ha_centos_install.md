```sh


sudo yum install -y yum-utils

sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo


sudo yum install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

systemctl enable docker.service --now



systemctl start docker

cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
setenforce 0
yum install -y kubelet-1.22.17 kubeadm-1.22.17 kubectl-1.22.17 --disableexcludes=kubernetes

systemctl enable kubelet && systemctl start kubelet





kubeadm init \
--control-plane-endpoint=k8s-vip:8443 \
--image-repository=registry.aliyuncs.com/google_containers \
--kubernetes-version=v1.22.17 \
--service-cidr=10.96.0.0/12 \
--pod-network-cidr=10.244.0.0/16 \
--upload-certs \
--v=9

Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

Alternatively, if you are the root user, you can run:

  export KUBECONFIG=/etc/kubernetes/admin.conf

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

You can now join any number of the control-plane node running the following command on each as root:

  kubeadm join k8s-vip:8443 --token v32aej.d4lnhkc6ntdqth16 \
        --discovery-token-ca-cert-hash sha256:79edd468431aedea02d2790c2b1deda162e05a4637e00b9a02c65734d00f360b \
        --control-plane --certificate-key db5fff0301a42c133a18191cf3b491d02e8c0bfe1294df825dd40607eb7ff34a

Please note that the certificate-key gives access to cluster sensitive data, keep it secret!
As a safeguard, uploaded-certs will be deleted in two hours; If necessary, you can use
"kubeadm init phase upload-certs --upload-certs" to reload certs afterward.

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join k8s-vip:8443 --token v32aej.d4lnhkc6ntdqth16 \
        --discovery-token-ca-cert-hash sha256:79edd468431aedea02d2790c2b1deda162e05a4637e00b9a02c65734d00f360b 


#----------------------------------------------------------------

kubectl get all -n kubeedge

kubectl get daemonset -n kube-system | grep -v NAME | awk '{print $1}' | xargs -n 1 kubectl patch daemonset -n kube-system --type='json' -p='[{"op": "replace", "path": "/spec/template/spec/affinity", "value":{"nodeAffinity":{"requiredDuringSchedulingIgnoredDuringExecution":{"nodeSelectorTerms":[{"matchExpressions":[{"key":"node-role.kubernetes.io/edge","operator":"DoesNotExist"}]}]}}}}]'


kubectl patch deploy metrics-server -n kube-system --type='json' -p='[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value":"--kubelet-insecure-tls"}]'

kubectl logs redis-7d7574899b-bk9xm

kubectl delete pod -n kubeedge cloudcore-6c5c6cccd6-82z2z

kubectl describe node kubeedge

kubectl describe pod -n kubeedge cloudcore-6c5c6cccd6-jbr89

kubectl taint nodes --all node-role.kubernetes.io/master-


kubectl describe pod -n kubeedge cloudcore-6c5c6cccd6-jbr89

kubectl describe pod -n t-eio-dashboard t-eio-dashboard-d96dddc88-n6cnd

kubectl delete namespace kubeedge

kubectl label nodes k8s-worker01 app=cloudcore


keadm init --advertise-address=39.108.49.13 --set iptablesManager.mode="external" --profile version=v1.12.1

kubectl delete clusterRole cloudcore

kubectl get ClusterRoleBinding

kubectl delete clusterRole cloudcore

kubectl delete ClusterRoleBinding cloudcore

kubectl get deploy -nkubeedge

kubectl edit deploy -nkubeedge cloudcore

kubectl edit deploy -nkubeedge cloudcore

kubectl get pods -A -owide

kubectl get nodes -owide

kubectl edit svc -n kubeedge cloudcore 

kubectl get svc -n kubeedge cloudcore

kubectl get cm -n kubeedge cloudcore


kubectl get cm -n kubeedge cloudcore -o yaml

keadm gettoken

kubectl delete node nano-desktop 

keadm join --cloudcore-ipport=39.108.49.13:10000 --token=b30c983a49c5cb08428cfaf4d579ecdaba82637e62d5b023cc6d0c97bb3a07cc.eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NzYxMDg4NTB9.TPRlD9r6bGVAIm1dHUMhj-vGYwy89YIS5DmPmcGnQvw --kubeedge-version=v1.12.1

kubectl delete -n default redis-7d7574899b-77w6f

kubectl delete pod -n default redis-7d7574899b-77w6f

kubectl logs redis-7d7574899b-h9lmw


```