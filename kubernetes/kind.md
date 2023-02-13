
## Kind 介绍

- NAT 网络地址转换

- Source NAT 替换数据包上的源 IP；在本页面中，这通常意味着替换为节点的 IP 地址

- Destination NAT 替换数据包上的目标 IP；在本页面中，这通常意味着替换为 Pod 的 IP 地址

- VIP 一个虚拟 IP 地址，例如分配给 Kubernetes 中每个 Service 的 IP 地址

- Kube-proxy 一个网络守护程序，在每个节点上协调 Service VIP 管理

### Kubernetes Service 总览

(Service)是一种抽象概念，它定义了 Pod 的逻辑集和访问 Pod 的协议。Service 使从属 Pod 之间的松耦合成为可能。 和其他 Kubernetes 对象一样, Service 用 YAML (更推荐) 或者 JSON 来定义. Service 下的一组 Pod 通常由 LabelSelector (请参阅下面的说明为什么你可能想要一个 spec 中不包含selector的服务)来标记。

尽管每个 Pod 都有一个唯一的 IP 地址，但是如果没有 Service ，这些 IP 不会暴露在集群外部。Service 允许你的应用程序接收流量。Service 也可以用在 ServiceSpec 标记type的方式暴露

- `ClusterIP` (默认) - 在集群的内部 IP 上公开 Service 。这种类型使得 Service 只能从集群内访问。

- `NodePort` - 使用 NAT 在集群中每个选定 Node 的相同端口上公开 Service 。使用<NodeIP>:<NodePort> 从集群外部访问 Service。是 `ClusterIP` 的超集。

- `LoadBalancer` - 在当前云中创建一个外部负载均衡器(如果支持的话)，并为 Service 分配一个固定的外部IP。是 NodePort 的超集。

- `ExternalName` - 通过返回带有该名称的 CNAME 记录，使用任意名称(由 spec 中的externalName指定)公开 Service。不使用代理。这种类型需要kube-dns的v1.7或更高版本。