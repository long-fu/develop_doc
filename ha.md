3.2、HA三种工作方式：



（1）、主从方式 （非对称方式）



工作原理：主机工作，备机处于监控准备状况；当主机宕机时，备机接管主机的一切工作，待主机恢复正常后，按使用者的设定以自动或手动方式将服务切换到主机上运行，数据的一致性通过共享存储系统解决。



（2）、双机双工方式（互备互援）



工作原理：两台主机同时运行各自的服务工作且相互监测情况，当任一台主机宕机时，另一台主机立即接管它的一切工作，保证工作实时，应用服务系统的关键数据存放在共享存储系统中。



（3）、集群工作方式（多服务器互备方式）



工作原理：多台主机一起工作，各自运行一个或几个服务，各为服务定义一个或多个备用主机，当某个主机故障时，运行在其上的服务就可以被其它主机接管。



当一个人找不到出路的时候，最好的办法就是将当前能做好的事情做到极致，做到无人能及。


虚拟IP（VirtualIP/VIP）  http://blog.csdn.net/whycold/article/details/11898249

实现原理主要是靠TCP/IP的ARP协议。（虚拟IP漂移）
因为ip地址只是一个逻辑 地址，在以太网中MAC地址才是真正用来进行数据传输的物理地址，每台主机中都有一个ARP高速缓存，存储同一个网络内的IP地址与MAC地址的对应关 系，以太网中的主机发送数据时会先从这个缓存中查询目标IP对应的MAC地址，会向这个MAC地址发送数据。操作系统会自动维护这个缓存。这就是整个实现的关键。


三、Keepalived的工作原理
1、VRRP协议
keepalived是以VRRP协议为实现基础的，VRRP全称Virtual Router Redundancy Protocol，即虚拟路由冗余协议，可以认为是实现路由器高可用的协议。

VRRP是用来实现路由器冗余的协议。
VRRP协议是为了消除在静态缺省路由环境下路由器单点故障引起的网络失效而设计的主备模式的协议，使得发生故障而进行设计设备功能切换时可以不影响内外数据通信，不需要再修改内部网络的网络参数。
VRRP协议需要具有IP备份，优先路由选择，减少不必要的路由器通信等功能。
VRRP协议将两台或多台路由器设备虚拟成一个设备，对外提供虚拟路由器IP（一个或多个）。然而，在路由器组内部，如果实际拥有这个对外IP的路由器如果工作正常的话，就是master，或者是通过算法选举产生的，MASTER实现针对虚拟路由器IP的各种网络功能，如ARP请求，ICMP，以及数据的转发等，其他设备不具有该IP，状态是BACKUP。除了接收MASTER的VRRP状态通告信息外，不执行对外的网络功能，当主机失效时，BACKUP将接管原先MASTER的网络功能。
VRRP协议配置时，需要配置每个路由器的虚拟路由ID(VRID)和优先权值，使用VRID将路由器进行分组，具有相同VRID值的路由器为同一个组，VRID是一个0-255的整整数；同一个组中的路由器通过使用优先权值来选举MASTER。优先权大者为MASTER，优先权也是一个0-255的正整数。
对应到高可用的场景，实际上就是把路由器换成了服务器或者服务器上的应用：

通常情况下是将两台linux服务器组成一个热备组（master-backup），同一时间热备组内只有一台主服务器（master）提供服务，同时master会虚拟出一个共用IP地址（VIP），这个VIP只存在master上并对外提供服务。

如果keepalived检测到master宕机或服务故障，备服务器（backup）会自动接管VIP成为master，keepalived并将master从热备组移除，当master恢复后，会自动加入到热备组，默认再抢占成为master，起到故障转移功能。


负载均衡分类

现在我们知道，负载均衡就是一种计算机网络技术，用来在多个计算机（计算机集群）、网络连接、CPU、磁碟驱动器或其它资源中分配负载，以达到最佳化资源使用、最大化吞吐率、最小化响应时间、同时避免过载的目的。那么，这种计算机技术的实现方式有多种。大致可以分为以下几种，其中最常用的是四层和七层负载均衡：

二层负载均衡

负载均衡服务器对外依然提供一个VIP（虚IP），集群中不同的机器采用相同IP地址，但机器的MAC地址不一样。当负载均衡服务器接受到请求之后，通过改写报文的目标MAC地址的方式将请求转发到目标机器实现负载均衡。

三层负载均衡

和二层负载均衡类似，负载均衡服务器对外依然提供一个VIP（虚IP），但集群中不同的机器采用不同的IP地址。当负载均衡服务器接受到请求之后，根据不同的负载均衡算法，通过IP将请求转发至不同的真实服务器。

四层负载均衡

四层负载均衡工作在OSI模型的传输层，由于在传输层，只有TCP/UDP协议，这两种协议中除了包含源IP、目标IP以外，还包含源端口号及目的端口号。四层负载均衡服务器在接受到客户端请求后，以后通过修改数据包的地址信息（IP+端口号）将流量转发到应用服务器。

七层负载均衡

七层负载均衡工作在OSI模型的应用层，应用层协议较多，常用http、radius、DNS等。七层负载就可以基于这些协议来负载。这些应用层协议中会包含很多有意义的内容。比如同一个Web服务器的负载均衡，除了根据IP加端口进行负载外，还可根据七层的URL、浏览器类别、语言来决定是否要进行负载均衡。

图：四层和七层负载均衡 对于一般的应用来说，有了Nginx就够了。Nginx可以用于七层负载均衡。但是对于一些大的网站，一般会采用DNS+四层负载+七层负载的方式进行多层次负载均衡。

常用负载均衡工具

硬件负载均衡性能优越，功能全面，但价格昂贵，一般适合初期或者土豪级公司长期使用。因此软件负载均衡在互联网领域大量使用。常用的软件负载均衡软件有Nginx、LVS、HaProxy等。


kubeadm init \
--control-plane-endpoint=k8s-master03:6443 \
--image-repository=registry.aliyuncs.com/google_containers \
--kubernetes-version=v1.22.17 \
--service-cidr=10.96.0.0/16 \
--pod-network-cidr=192.244.0.0/16 \
--upload-certs

kubeadm init \
--control-plane-endpoint=k8s-vip:6443 \
--image-repository=registry.aliyuncs.com/google_containers \
--kubernetes-version=v1.22.17 \
--service-cidr=10.96.0.0/16 \
--pod-network-cidr=192.244.0.0/16 \
--upload-certs

kubeadm join 172.18.94.132:6443 --token u8ao9o.2riq3kax7anom8s4 --discovery-token-ca-cert-hash sha256:bf68cedf88809b5949e22d9f82e85fe6ced9645dba631501d94ec7ea989fc1eb --control-plane --certificate-key 16356eb35ccd9e619c660b6d3f90fb4a7db791b8a927727923ad33d737b0ebf3


o start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

Alternatively, if you are the root user, you can run:

  export KUBECONFIG=/etc/kubernetes/admin.conf

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

You can now join any number of the control-plane node running the following command on each as root:

  kubeadm join k8s-master03:6443 --token u8ao9o.2riq3kax7anom8s4 \
	--discovery-token-ca-cert-hash sha256:bf68cedf88809b5949e22d9f82e85fe6ced9645dba631501d94ec7ea989fc1eb \
	--control-plane --certificate-key 16356eb35ccd9e619c660b6d3f90fb4a7db791b8a927727923ad33d737b0ebf3

Please note that the certificate-key gives access to cluster sensitive data, keep it secret!
As a safeguard, uploaded-certs will be deleted in two hours; If necessary, you can use
"kubeadm init phase upload-certs --upload-certs" to reload certs afterward.

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join k8s-master03:6443 --token u8ao9o.2riq3kax7anom8s4 \
	--discovery-token-ca-cert-hash sha256:bf68cedf88809b5949e22d9f82e85fe6ced9645dba631501d94ec7ea989fc1eb 

----------------------
172.18.94.132

yum install -y httpd
systemctl start httpd
systemctl status httpd

curl 127.0.0.1:80

kubeadm reset -f 
rm -rf /etc/cni/net.d
rm -rf $HOME/.kube/config

#

kubeadm init \
--control-plane-endpoint k8s-vip:6443 \
--image-repository=registry.aliyuncs.com/google_containers \
--kubernetes-version=v1.22.17 \
--service-cidr=10.96.0.0/12 \
--pod-network-cidr=10.244.0.0/16 \
--upload-certs

netstat -lnp|grep 6443

netstat -lnp|grep 80

kill -9 28533

kubeadm join k8s-vip:6443 --token sabcy1.eettzh3pzl48q6q8 \
--discovery-token-ca-cert-hash sha256:ed6a6db4141176bd615f045efa86e11b3bb3a1c327facaa2ffe0a3d0a86d83d4 \
--control-plane --certificate-key 68a1165a3bcdc9e7a0b5c170d16cd5fa6903c43365043733e51f7e6e4e210e26

##
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

  kubeadm join k8s-master01:6443 --token uk9pr6.d3fzjw3d3opdi0ji \
	--discovery-token-ca-cert-hash sha256:cf06063467085615525014267f3b5de6a6f5f2483bdf2a6ab3469713a840170c \
	--control-plane --certificate-key 9099f57363bfedc3015410911b6f0ddd4c95e4caefe0aff0b42ea2ad736c0309

Please note that the certificate-key gives access to cluster sensitive data, keep it secret!
As a safeguard, uploaded-certs will be deleted in two hours; If necessary, you can use
"kubeadm init phase upload-certs --upload-certs" to reload certs afterward.

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join k8s-master01:6443 --token uk9pr6.d3fzjw3d3opdi0ji \
	--discovery-token-ca-cert-hash sha256:cf06063467085615525014267f3b5de6a6f5f2483bdf2a6ab3469713a840170c


#


kubeadm join k8s-master01:6443 --token uk9pr6.d3fzjw3d3opdi0ji \
 --discovery-token-ca-cert-hash sha256:cf06063467085615525014267f3b5de6a6f5f2483bdf2a6ab3469713a840170c \
 --control-plane --certificate-key 9099f57363bfedc3015410911b6f0ddd4c95e4caefe0aff0b42ea2ad736c0309

wget https://k8s-vip:6443/api/v1/namespaces/kube-public/configmaps/cluster-info?timeout=10s

wget --no-check-certificate  https://k8s-vip:6443/api/v1/namespaces/kube-public/configmaps/cluster-info?timeout=10s

[root@k8s-master01 ~]# wget https://k8s-master01:6443/api/v1/namespaces/kube-public/configmaps/cluster-info?timeout=10s
--2023-02-06 18:52:09--  https://k8s-master01:6443/api/v1/namespaces/kube-public/configmaps/cluster-info?timeout=10s
正在解析主机 k8s-master01 (k8s-master01)... 172.18.94.128
正在连接 k8s-master01 (k8s-master01)|172.18.94.128|:6443... 已连接。
错误: 无法验证 k8s-master01 的由 “/CN=kubernetes” 颁发的证书:
  无法本地校验颁发者的权限。
要以不安全的方式连接至 k8s-master01，使用“--no-check-certificate”。
[root@k8s-master01 ~]# wget --no-check-certificate https://k8s-master01:6443/api/v1/namespaces/kube-public/configmaps/cluster-info?timeout=10s
--2023-02-06 18:52:26--  https://k8s-master01:6443/api/v1/namespaces/kube-public/configmaps/cluster-info?timeout=10s
正在解析主机 k8s-master01 (k8s-master01)... 172.18.94.128
正在连接 k8s-master01 (k8s-master01)|172.18.94.128|:6443... 已连接。
警告: 无法验证 k8s-master01 的由 “/CN=kubernetes” 颁发的证书:
  无法本地校验颁发者的权限。
已发出 HTTP 请求，正在等待回应... 200 OK