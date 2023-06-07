所有文档 https://support.huawei.com/enterprise/zh/ascend-computing/a500-3000-pid-250702836

Atlas 500 智能小站 用户指南 (型号 3000) 18 https://support.huawei.com/enterprise/zh/doc/EDOC1100133176/6d1ed110

[linux] SFTP文件传输基本命令 https://blog.csdn.net/Trance95/article/details/128735668


基础配置 

https://blog.csdn.net/A_anonymous_user/article/details/127648286

https://blog.csdn.net/weixin_39433369/article/details/119869735

https://blog.csdn.net/hh1357102/article/details/130169513
---

web:
默认 admin : Huawei12#$
修改 admin00 : Longfu12#$

// 先登录web后会直接修改ssh
默认 admin : Huawei12#$

ssh
admin00@192.168.2.111
Longfu12#$



develop 命令
root 
默认 Huawei@SYS3
修改 Longfu12#$

/opt/middleware/AtlasEdge/edge_work_dir/edge_core/config/podconfig.json

#vi /opt/middleware/AtlasEdge/edge_work_dir/edge_core/config/podconfig.json

runAsRoot": false,

/opt/middleware/MindXOM/software/lib/Linux/config/ibma_edge_service.ini

# 手动增加网关 
route add default gw 192.168.2.1

# 查看网络配置
route