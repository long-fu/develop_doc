1. 宿主机docker配置

http:  在`/etc/docker/daemon.json` 增加字段配置`insecure-registries:['119.23.231.199']`。

https: 联系系统管理员获取ca证书存放在 `/etc/docker/certs.d/119.23.231.199/ca.crt`。

2. 登录

```sh
docker login 119.23.231.199
login: #输入账号
password: #输入密码
```

3. 推送镜像

```sh
# 打标签
docker tag [local image]:[tag] 119.23.231.199/[project]/[repositorie]:[tag]

# 推送镜像
docker push 119.23.231.199/[project]/[repositorie]:[tag]

# 拉取镜像
docker pull 119.23.231.199/[project]/[repositorie]:[tag]
```

----

https://www.jianshu.com/p/e7dc4da97386

https://docs.docker.com/docker-hub/api/latest/

https://docs.docker.com/registry/spec/auth/jwt/

https://docs.docker.com/registry/spec/api/#pushing-an-image

https://119.23.231.199/api/v2.0/projects/test/repositories/ec-manager/artifacts?with_tag=false&with_scan_overview=true&with_label=true&with_accessory=false&q=labels%3D(1)&page_size=15&page=1