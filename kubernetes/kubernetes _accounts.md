# Kubernetes 中的用户 


- [用户认证](https://kubernetes.io/zh-cn/docs/reference/access-authn-authz/authentication/#openid-connect-tokens)
- [使用 RBAC 鉴权](https://kubernetes.io/zh-cn/docs/reference/access-authn-authz/rbac/)
- [鉴权概述](https://kubernetes.io/zh-cn/docs/reference/access-authn-authz/authorization/)
- [准入控制器参考](https://kubernetes.io/zh-cn/docs/reference/access-authn-authz/admission-controllers/)
- [Kubernetes（k8s）权限管理RBAC详解](https://www.cnblogs.com/liugp/p/16438284.html)
- [浅聊Kubernetes的各种认证策略以及适用场景](https://blog.csdn.net/M2l0ZgSsVc7r69eFdTj/article/details/104112140)
- [彻底搞懂 Kubernetes 中的认证](https://cloudnative.to/blog/authentication-k8s/)
- [为 Kubernetes 搭建支持 OpenId Connect 的身份认证系统](https://github.com/labulakalia/ibm_bak/blob/main/ibm_articles/%E4%B8%BAKubernetes%E6%90%AD%E5%BB%BA%E6%94%AF%E6%8C%81OpenIdConnect%E7%9A%84%E8%BA%AB%E4%BB%BD%E8%AE%A4%E8%AF%81%E7%B3%BB%E7%BB%9F.md)

1. Authentication（认证）
2. Authorization（授权）
3. Admission Control（准入控制）

## 认证（Authentication）

- X509 客户证书 

- 静态令牌文件 

- 启动引导令牌 

- 服务账号令牌

- OpenID Connect（OIDC）令牌 

- Webhook 令牌身份认证

- 身份认证代理

> 三种客户端身份认证：

- HTTPS 证书认证：基于CA证书签名的数字证书认证
- HTTP Token认证：通过一个Token来识别用户
- HTTP Base认证：用户名+密码的方式认证

## 鉴权（Authentication）

- **Node** —— 一个专用鉴权模式，根据调度到 kubelet 上运行的 Pod 为 kubelet 授予权限。 要了解有关使用节点鉴权模式的更多信息，请参阅节点鉴权。
- **ABAC** —— 基于属性的访问控制（ABAC）定义了一种访问控制范型，通过使用将属性组合在一起的策略， 将访问权限授予用户。策略可以使用任何类型的属性（用户属性、资源属性、对象，环境属性等）。 要了解有关使用 ABAC 模式的更多信息，请参阅 ABAC 模式。
- **RBAC** —— 基于角色的访问控制（RBAC） 是一种基于企业内个人用户的角色来管理对计算机或网络资源的访问的方法。 在此上下文中，权限是单个用户执行特定任务的能力， 例如查看、创建或修改文件。要了解有关使用 RBAC 模式的更多信息，请参阅 RBAC 模式。
   - 被启用之后，RBAC（基于角色的访问控制）使用 rbac.authorization.k8s.io API 组来驱动鉴权决策，从而允许管理员通过 Kubernetes API 动态配置权限策略。
   - 要启用 RBAC，请使用 `--authorization-mode = RBAC` 启动 API 服务器。
- **Webhook** —— WebHook 是一个 HTTP 回调：发生某些事情时调用的 HTTP POST； 通过 HTTP POST 进行简单的事件通知。 实现 WebHook 的 Web 应用程序会在发生某些事情时将消息发布到 URL。 要了解有关使用 Webhook 模式的更多信息，请参阅 Webhook 模式。
检查 API 访问


## 准入控制（Admission Control）

Adminssion Control实际上是一个准入控制器插件列表，发送到API Server 的 请求都需要经过这个列表中的每个准入控制器插件的检查，检查不通过， 则拒绝请求。
现成的插件，比如psp,imagewebhook这些都是已经存在的插件，如果需要使用，可以启用该准入控制插件。 


--------------



1. 创建一个账户

2. 给账户分配访问权限

使用 ABAC 鉴权

基于属性的访问控制（Attribute-based access control - ABAC）定义了访问控制范例， 其中通过使用将属性组合在一起的策略来向用户授予访问权限。