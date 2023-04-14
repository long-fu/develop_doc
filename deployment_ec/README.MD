1. 运行ec_de.yaml
2. 给集群 clusterRole 角色 admin cluster-admin edit view 几个角色规则添加 标签 ec-dashboard.io/rbac: rbac-defaults k8s-app: ec-dashboard

3. 预处理数据 创建好 ClusterRoleBinding config.yaml 绑定规则 admin-crb cluster-admin-crb edit-crb view-crb
4. 通过接口创建一个账户

