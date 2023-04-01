1. 运行ec_de.yaml
2. 给集群 clusterRole 角色 admin cluster-admin edit view 几个角色规则添加 标签 ec-dashboard.io/rbac: rbac-defaults k8s-app: ec-dashboard

3. 预处理数据 创建好 ClusterRoleBinding config.yaml 绑定规则 admin-crb cluster-admin-crb edit-crb view-crb
4. 通过接口创建一个账户

{"kind":"Status","apiVersion":"v1","metadata":{},"status":"Failure","message":"Role.rbac.authorization.k8s.io \"cluster-admin\" is invalid: rules[1].nonResourceURLs: Invalid value: []string{\"*\"}: namespaced rules cannot apply to non-resource URLs","reason":"Invalid","details":{"name":"cluster-admin","group":"rbac.authorization.k8s.io","kind":"Role","causes":[{"reason":"FieldValueInvalid","message":"Invalid value: []string{\"*\"}: namespaced rules cannot apply to non-resource URLs","field":"rules[1].nonResourceURLs"}]},"code":422}
