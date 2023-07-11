
# harbor(v2.7.1) https 离线安装教程

## 机器配置

### 硬件配置

> 下表列出了部署 Harbor 的最低和推荐硬件配置。


| 资源 | 最低限度 | 推荐 |
|-----|-----|-----|
| CPU | 2 CPU |4 CPU |
| Mem | 4 GB | 8 GB |
|Disk | 40 GB | 160 GB |

### 软件

> 下表列出了目标主机上必须安装的软件版本。

| 软件 | 版本 | 描述 |
|-----|-----|-----|
Docker引擎 | 版本 17.06.0-ce+ 或更高版本 | 有关安装说明，请参阅 Docker 引擎文档。
码头工人组成 | docker-compose (v1.18.0+) 或 docker-compose v2 (docker-compose-plugin) | 有关安装说明，请参阅 Docker Compose 文档。
打开SSL | 最新的优先 | 用于为Harbor生成证书和密钥

### 网络端口

| 端口 | 协议 | 描述 |
|-----|-----|-----|
443 | HTTPS |Harbor 门户和核心 API 在此端口上接受 HTTPS 请求。您可以在配置文件中更改此端口。
4443 | HTTPS |连接到 Harbor 的 Docker Content Trust 服务。只有在启用 Notary 时才需要。您可以在配置文件中更改此端口。
80 | HTTP | Harbor 门户和核心 API 在此端口上接受 HTTP 请求。您可以在配置文件中更改此端口。

## 安装过程

```sh

hostnamectl set-hostname Harbor

tar xzvf harbor-offline-installer-version.tgz

# 1 生成证书颁发机构证书
# 1.1 生成 CA 证书私钥。
openssl genrsa -out ca.key 4096

# 1.2生成 CA 证书。
openssl req -x509 -new -nodes -sha512 -days 3650 \
 -subj "/C=CN/ST=Shenzhen/L=Shenzhen/O=ec/OU=Personal/CN=hub.ec.com" \
 -key ca.key \
 -out ca.crt

# 2 生成服务器证书

# 2.1 生成私钥。
openssl genrsa -out hub.ec.com.key 4096

# 2.2 生成证书签名请求 (CSR)。
openssl req -sha512 -new \
    -subj "/C=CN/ST=Shenzhen/L=Shenzhen/O=ec/OU=Personal/CN=hub.ec.com" \
    -key hub.ec.com.key \
    -out hub.ec.com.csr

# 2.3 生成 x509 v3 扩展文件。
cat > v3.ext <<-EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names

[alt_names]
DNS.1=hub.ec.com
DNS.2=hub.ec
DNS.3=hostname
EOF

# 2.4 使用该v3.ext文件为您的 Harbor 主机生成证书。
openssl x509 -req -sha512 -days 3650 \
    -extfile v3.ext \
    -CA ca.crt -CAkey ca.key -CAcreateserial \
    -in hub.ec.com.csr \
    -out hub.ec.com.crt

# 3 向 Harbor 和 Docker 提供证书
# 3.1 将服务器证书和密钥复制到 Harbor 主机上的 certificates 文件夹中
mkdir -p /data/certs
cp hub.ec.com.crt /data/cert/
cp hub.ec.com.key /data/cert/

# 3.2 转换yourdomain.com.crt为yourdomain.com.cert, 供 Docker 使用。
openssl x509 -inform PEM -in hub.ec.com.crt -out hub.ec.com.cert

# 3.3 将服务器证书、密钥和 CA 文件复制到 Harbor 主机上的 Docker 证书文件夹中。您必须先创建适当的文件夹。
cp hub.ec.com.cert /etc/docker/certs.d/hub.ec.com/
cp hub.ec.com.key /etc/docker/certs.d/hub.ec.com/
cp ca.crt /etc/docker/certs.d/hub.ec.com/

# 3.4 重新启动 Docker 引擎。

systemctl restart docker

# 4 配置 yml文件

cp harbor.yml.tmpl harbor.yml

vim harbor.yml

# ------
.....
hostname: hub.ec.com

# http related config
http:
  # port for http, default is 80. If https enabled, this port will redirect to https port
  port: 80

# https related config
https:
  # https port for harbor, default is 443
  port: 443
  # The path of cert and key files for nginx
  certificate: /data/cert/hub.ec.com.crt
  private_key: /data/cert/hub.ec.com.key
.....
# ------

systemctl restart docker

./prepare

./install.sh
...
...
...
Creating network "harbor_harbor" with the default driver
Creating harbor-log ... done
Creating harbor-db     ... done
Creating harbor-portal ... done
Creating registry      ... done
Creating redis         ... done
Creating registryctl   ... done
Creating harbor-core   ... done
Creating harbor-jobservice ... done
Creating nginx             ... done
✔ ----Harbor has been installed and started successfully.----

docker-compose stop  # 停止
docker-compose start  # 启动(第一次需要使用 up -d)
docker-compose down -v # 停止并删除容器（慎用）
docker-compose up -d # 创建并启动


```

## 测试

浏览器打开地址

https://119.23.231.199/

默认账号密码：`admin` `Harbor12345`

`Pass01:).`

创建一个私有的仓库 test

```sh
## 测试
docker tag ec-manager-0.0.1:latest hub.ec.com/test/ec-manager:v0.0.1

docker images

docker login hub.ec.com

docker push hub.ec.com/test/ec-manager:v0.0.1
```
<<<<<<< HEAD:Harbor/README.MD
=======
docker tag ec-manager-0.0.1:latest 119.23.231.199:80/test/ec-manager:v0.0.2

docker tag hub.docker.haoshuai.com/test/ec-manager:v0.0.3 119.23.231.199/test/ec-manager:v0.0.3

docker tag 119.23.231.199:80/test/kubeedge-web-app:v2.6 119.23.231.199/test/kubeedge-web-app:v2.6

docker tag ec-manager-web:v0.0.1 119.23.231.199/test/ec-manager-web:v0.0.4

docker tag ec-manager-0.0.1:latest 119.23.231.199/test/ec-manager:v0.0.4.1

docker tag 119.23.231.199:80/test/ec-manager:v0.0.2 119.23.231.199/test/ec-manager-web:v0.0.2

docker images

cp certs/domain.crt /usr/local/share/ca-certificates/myregistrydomain.com.crt
update-ca-certificates

docker -D login hub.ec.com


docker -D login 119.23.231.199:80 -u admin -p Harbor12345

docker push 119.23.231.199:80/test/ec-manager-web:v0.0.2

docker push hub.ec.com/test/ec-manager-web:v0.0.2
```
>>>>>>> origin/main:harbor/README.MD

## 登陆失败 要拷贝ca证书 也就是开始的验证证书 拷贝到响应的目录

```sh

scp harborhost:/harbor/cert/path/ca.crt /usr/local/share/ca-certificate/ca.crt
sudo update-ca-certificates
sudo systemctl restart docker.service


https://blog.csdn.net/nklinsirui/article/details/87952312
```

**参考链接**

- [github](https://github.com/goharbor/harbor)
- [官方文档](https://goharbor.io/docs/2.7.0/install-config/installation-prereqs/)
- [参考教程](https://www.cnblogs.com/leffss/p/15621165.html)
