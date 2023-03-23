# RBAC

## 登录

### 登录页面

- 登录: /api/account/login

## 个人中心

### 账号管理页面

- 查看 /api/account/get
- 修改 /api/account/update

### 修改个人密码页面

- 修改密码: /api/account/updatePassword

## sa

### 账户(sa)管理页面

- 列表sa: /api/account/list
- 创建sa
- 删除sa: /api/account/delete
- 更新sa
- 重置密码: /api/account/resetPassword

### 创建账户(sa)页面

- 有效sa: /api/account/isValid
- 创建sa: /api/account/create

### 更新账户sa页面

- 更新sa: /api/account/updateList

## ns

### 命名空间(ns)管理页面

- 列表ns: /api/namespace/getNamespaceList
- 删除ns: /api/namespace/deleteNamespace
  - 提示: 删除ns会删除ns下所有资源数据
- 创建ns

### 创建命名空间(ns)页面

- 创建ns: /api/namespace/getNamespaceList

## Role

### Role管理页面

- 列表Role: /api/rbac/listRole
- 删除Role: /api/rbac/deleteRole
  - 提示: 删除Role会导致账户访问权限有所变化，请注意检查RoleBinding资源是否存在关联。
- 创建Role
- 查看Role

### 创建Role页面

- 获取配置ClusterRole: /api/rbac/getRoleConfig
- 创建Role: /api/rbac/createRole

### Role详情页面

- 查看Role: /api/rbac/getRole

## ClusterRole

### ClusterRole管理页面

- 列表ClusterRole: /api/rbac/listClusterRole
- 删除ClusterRole: /api/rbac/deleteClusterRole
  - 提示: 删除ClusterRole会导致账户访问权限有所变化，请注意检查RoleBinding和ClusterRoleBinding资源是否存在关联。
- 创建ClusterRole
- 查看ClusterRole

### 创建ClusterRole页面

- 获取配置ClusterRole: /api/rbac/getRoleConfig
- 创建ClusterRole: /api/rbac/createClusterRole

### ClusterRole详情页面

- 查看ClusterRole: /api/rbac/getClusterRole

## RoleBinding

### RoleBinding管理页面

- 列表RoleBinding: /api/rbac/listRoleBinding
- 删除RoleBinding: /api/rbac/deleteRoleBinding
  - 提示: 删除RoleBinding会导致账户访问权限有所变化，请注意检查。
  - 逻辑: 会给相关联的账户，账户资料中`secret`，管理可以访问那些集群ns的字段`namespaces`删除当前RoleBinding的`namespace`表示**不可以访问此namespace**
- 创建RoleBinding
  - 逻辑: 会给相关联的账户，账户资料中`secret`，管理可以访问那些集群ns的字段`namespaces`添加当前RoleBinding的`namespace`表示**可以访问此namespace**
- 查看RoleBinding

### RoleBinding详情页面

- 查看RoleBinding: /api/rbac/getRoleBinding

### 创建RoleBinding页面

- 创建Role: /api/rbac/createRoleBinding

## ClusterRoleBinding

### ClusterRoleBinding管理页面

- 列表ClusterRoleBinding: /api/rbac/listClusterRoleBinding
- 删除ClusterRoleBinding: /api/rbac/deleteClusterRoleBinding
  - 提示: 删除ClusterRoleBinding会导致账户访问权限有所变化，请注意检查。
  - 逻辑: 会给相关联的账户`sa`，账户资料中`secret`，管理可以访问那些集群ns的字段`namespaces`删除一个`*`字符表示可以**不可以访问所有ns**。
- 创建ClusterRoleBinding
  - 逻辑: 会给相关联的账户`sa`，账户资料中`secret`，管理可以访问那些集群ns的字段`namespaces`添加一个`*`字符表示**可以访问所有ns**。
- 查看ClusterRoleBinding

### ClusterRoleBinding详情页面

- 查看ClusterRoleBinding: /api/rbac/getClusterRoleBinding

### 创建ClusterRoleBinding页面

- 创建Role: /api/rbac/createClusterRoleBinding