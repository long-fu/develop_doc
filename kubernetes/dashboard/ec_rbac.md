# rbac

## 用 ServiceAccount 生成账户 --> 直接用token进行数据的访问




## 设置访问规则

- `cluster-admin`：超级用户权限，允许对任何资源执行任何操作。 在 ClusterRoleBinding 中使用时，可以完全控制集群和所有命名空间中的所有资源。 在 RoleBinding 中使用时，可以完全控制 RoleBinding 所在命名空间中的所有资源，包括命名空间自己。

- `admin` ：管理员权限，利用 RoleBinding 在某一命名空间内部授予。 在 RoleBinding 中使用时，允许针对命名空间内大部分资源的读写访问， 包括在命名空间内创建角色与角色绑定的能力。 但不允许对资源配额（resource quota）或者命名空间本身的写访问。

- `edit` ：允许对某一个命名空间内大部分对象的读写访问，但不允许查看或者修改角色或者角色绑定。

- `view` ：允许对某一个命名空间内大部分对象的只读访问。 不允许查看角色或者角色绑定。 由于可扩散性等原因，不允许查看 secret 资源。

### 资源参数社设置

- verbs` 可配置参数

"get", "list", "watch", "create", "update", "patch", "delete", "exec"

- `resources` 可配置参数

"services", "endpoints", "pods","secrets","configmaps","crontabs","deployments","jobs","nodes","rolebindings","clusterroles","daemonsets","replicasets","statefulsets","horizontalpodautoscalers","replicationcontrollers","cronjobs"

- `apiGroups` 可配置参数

"","apps", "autoscaling", "batch"

/apps/pod/{}/ns/{}

```yaml

kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  # "namespace" 被忽略，因为 ClusterRoles 不受名字空间限制
  name: my-view
rules:
  - apiGroups: [""]
    resources: ["namespaces","pods","nodes"] 
    verbs: ["get", "watch", "list"]

```

## 绑定定用户访问规则


---

1. 创建  ServiceAccount

2. 创建角色规则(Role | ClusterRole)

3. 绑定 多个 ServiceAccount 以及规则

1. 完全新建: 创建账户 | 选择多个账户 ===> 选择已有角色规则 | 创建角色规则(Role | ClusterRole) ===> 进行规则的绑定(RoleBinding | ClusterRoleBinding`只能绑定ClusterRole`)




