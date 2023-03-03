
```sh
# 如果没有令牌，可以通过在控制平面节点上运行以下命令来获取令牌：
kubeadm token list
# 默认情况下，令牌会在 24 小时后过期。如果要在当前令牌过期后将节点加入集群， 则可以通过在控制平面节点上运行以下命令来创建新令牌：
kubeadm token create
# 如果你没有 --discovery-token-ca-cert-hash 的值，则可以通过在控制平面节点上执行以下命令链来获取它：
openssl x509 -pubkey -in /etc/kubernetes/pki/ca.crt | openssl rsa -pubin -outform der 2>/dev/null | \
   openssl dgst -sha256 -hex | sed 's/^.* //'

openssl x509 -pubkey -in hello.txt | openssl rsa -pubin -outform der 2>h.cc | openssl dgst -sha256 -hex | sed 's/^.* //'
   
```
<!-- kubeadm token list

kubeadm token create

openssl x509 -pubkey -in /etc/kubernetes/pki/ca.crt | openssl rsa -pubin -outform der 2>/dev/null | \
   openssl dgst -sha256 -hex | sed 's/^.* //'

openssl x509 -pubkey -in hello.txt | openssl rsa -pubin -outform der 2>/dev/null | \
   openssl dgst -sha256 -hex | sed 's/^.* //' -->


1. 安装docker (版本检测)
2. k8s环境配置 
3. 安装k8s三件套 (版本检测)
4. 运行加入命令 