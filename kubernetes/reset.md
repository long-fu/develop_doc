```sh
# 使用适当的凭证与控制平面节点通信，运行：
kubectl drain <node name> --delete-emptydir-data --force --ignore-daemonsets
# 在删除节点之前，请重置 kubeadm 安装的状态：
kubeadm reset
# 重置过程不会重置或清除 iptables 规则或 IPVS 表。如果你希望重置 iptables，则必须手动进行：
iptables -F && iptables -t nat -F && iptables -t mangle -F && iptables -X
# 如果要重置 IPVS 表，则必须运行以下命令：
ipvsadm -C
# 现在删除节点：
kubectl delete node <节点名称>
```


