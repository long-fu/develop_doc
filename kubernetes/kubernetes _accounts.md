# Kubernetes 中的用户 

使用 Node 鉴权

使用 ABAC 鉴权

Secret

ServiceAccount


使用 RBAC 鉴权

Role

Role 总是用来在某个名字空间内设置访问权限； 在你创建 Role 时，你必须指定该 Role 所属的名字空间。

ClusterRole

ClusterRole 则是一个集群作用域的资源。

这两种资源的名字不同（Role 和 ClusterRole） 是因为 Kubernetes 对象要么是`名字空间`作用域的，要么是`集群`作用域的

RoleBinding

ClusterRoleBinding


https://kubernetes.io/zh-cn/docs/concepts/security/controlling-access/


https://www.cnblogs.com/liugp/p/16438284.html

- 服务账号
- [普通用户](https://kubernetes.io/zh-cn/docs/reference/access-authn-authz/certificate-signing-requests/#normal-user)

## 普通用户

角色访问控制（RBAC）子系统会确定用户是否有权针对某资源执行特定的操作。


## 服务账号

> 它们被绑定到特定的`名字空间`， 或者由 API 服务器自动创建，或者通过 API 调用创建。服务账号与一组以 `Secret` 保存的凭据相关，这些凭据会被挂载到 Pod 中，从而允许集群内的进程访问 Kubernetes API。

## 身份认证策略

### X509 客户证书

https://kubernetes.io/zh-cn/docs/tasks/administer-cluster/certificates/

### 静态令牌文件

### 在请求中放入持有者令牌

### 启动引导令牌

https://kubernetes.io/zh-cn/docs/reference/access-authn-authz/bootstrap-tokens/

### 服务账号令牌

Service Account




## API 请求

- 普通用户相关联
- 某服务账号相关联
- (匿名请求)[https://kubernetes.io/zh-cn/docs/reference/access-authn-authz/authentication/#anonymous-requests]


## 鉴权模块

- Node —— 一个专用鉴权模式，根据调度到 kubelet 上运行的 Pod 为 kubelet 授予权限。 要了解有关使用节点鉴权模式的更多信息，请参阅节点鉴权。
- ABAC —— 基于属性的访问控制（ABAC）定义了一种访问控制范型，通过使用将属性组合在一起的策略， 将访问权限授予用户。策略可以使用任何类型的属性（用户属性、资源属性、对象，环境属性等）。 要了解有关使用 ABAC 模式的更多信息，请参阅 ABAC 模式。
- RBAC —— 基于角色的访问控制（RBAC） 是一种基于企业内个人用户的角色来管理对计算机或网络资源的访问的方法。 在此上下文中，权限是单个用户执行特定任务的能力， 例如查看、创建或修改文件。要了解有关使用 RBAC 模式的更多信息，请参阅 RBAC 模式。
   - 被启用之后，RBAC（基于角色的访问控制）使用 rbac.authorization.k8s.io API 组来驱动鉴权决策，从而允许管理员通过 Kubernetes API 动态配置权限策略。
   - 要启用 RBAC，请使用 `--authorization-mode = RBAC` 启动 API 服务器。
- Webhook —— WebHook 是一个 HTTP 回调：发生某些事情时调用的 HTTP POST； 通过 HTTP POST 进行简单的事件通知。 实现 WebHook 的 Web 应用程序会在发生某些事情时将消息发布到 URL。 要了解有关使用 Webhook 模式的更多信息，请参阅 Webhook 模式。
检查 API 访问