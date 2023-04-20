# minio

MinIO 是一个基于 Apache License v2.0 开源协议的对象存储服务。它兼容亚马逊 S3 云存储服务接口，非常适合于存储大容量非结构化的数据，例如图片、视频、日志文件、备份数据和容器/虚拟机镜像等，而一个对象文件可以是任意大小，从 KB 到最大 TB 不等。

- 高性能

  - MinIO 是全球领先的对象存储先锋，在标准硬件上，读/写速度上高达 183GB/s 和171GB/s，已经成为 Hadoop HDFS 的替代品。
  - MinIO 用作云原生应用程序的主要存储，与传统对象存储相比，云原生应用程序需要更高的吞吐量和更低的延迟。而这些都是 MinIO 能够达成的性能指标。

- 可扩展性
  - MinIO 一直秉承着 “简单可扩展” 的理念，为对象存储带来了一个简单的缩放模型。借助在此基础之上，可以实现 MinIO 从单个群集到其他 MinIO 群集联合使用的情况，并且在需要时可以跨越多个不同的数据中心。

- 开放全部源代码 + 企业级支持
  - MinIO 基于 Apache V2 license 的 100% 开放源代码的产品。
  - 客户能够自动的、无限制、自由免费使用和集成 MinIO、自由的创新和创造、 自由的去修改、自由的再次发行新的版本和软件。其部署的多样性使软件变得更加坚固，这是专有软件无法提供的
- 混合云和多云
  - 亚马逊云的 S3 API 是在全球范围内达到共识的对象存储的协议
  - 当添加到数以百万计的私有云实例和广泛的边缘部署时，MinIO 是混合云的领导者。

- 简单而强大
  - 极简主义是 MinIO 的指导性设计原则。简单性减少了出错的机会，提高了正常运行时间，提供了可靠性，同时简单性又是性能的基础。
  - 只需下载一个二进制文件然后执行，即可在几分钟内安装和配置 MinIO。MinIO 升级是通过一个简单命令完成的，这个命令可以无中断的完成 MinIO 的升级，并且不需要停机即可完成升级操作。



## 基础概念


### 存储相关的概念

- 一个对象存储在一个 Set

- 一个集群划分为多个 Set

- 一个 Set 中的 Drive 尽可能分布在不同的节点上

- 一个 Set 包含的 Drive 数量是固定的，默认由系统根据集群规模自动计算


| 概念名称 |	对应含义解释 |
| ----- | ----- |
| Object | 存储的基本对象；比如文件、图片等等 |
| Bucket | 用于存储 Object 的逻辑空间；相互之间互相隔离；类似于系统中的顶层文件夹 |
|Drive |	即存储数据的磁盘；所有的对象数据都会存储在 Drive 里面 |
|Set | 即一组 Drive 的集合；分布式部署根据集群规模自动划分一个或多个 Set |

### 纠删码 - EC - Erasure Code

MinIO 使用 纠删码和校验和 机制来保证高可靠性，即便丢失一半数量(N/2)的硬盘，仍然可以恢复数据。纠删码是一种恢复丢失和损坏数据的数学算法，MinIO 采用 Reed-Solomon code 将对象拆分成 N/2 数据和 N/2 奇偶校验块。这就意味着如果是 12 块盘，一个对象会被分成 6 个数据块、6 个奇偶校验块，你可以丢失任意 6 块盘，仍可以从剩下的盘中的数据进行恢复。

纠删码的工作原理和 RAID 或者复制不同，像 RAID6 可以在损失两块盘的情况下不丢数据，而 MinIO 纠删码可以在丢失一半的盘的情况下，仍可以保证数据安全。而且 MinIO 纠删码是作用在对象级别，可以一次恢复一个对象，而 RAID 是作用在卷级别，数据恢复时间很长。MinIO 对每个对象单独编码，存储服务一经部署，通常情况下是不需要更换硬盘或者修复。MinIO 纠删码的设计目标是为了性能和尽可能的使用硬件加速。

### 对象的存储形式

文件对象上传到 MinIO 上面，会在对应的磁盘当中，以 Bucket 名称为目录，文件名称为下一级目录，文件名下是 part.1 和 xl.meta，前者是编码数据块及检验块，后者是元数据文件。

![](../images/640.png)

## 安装脚本记录

```sh

[req]
distinguished_name = req_distinguished_name
x509_extensions = v3_req
prompt = no

[req_distinguished_name]
C = US
ST = VA
L = Somewhere
O = MyOrg
OU = MyOU
CN = MyServerName

[v3_req]
subjectAltName = @alt_names

[alt_names]
IP.1 = 127.0.0.1

openssl req -x509 -nodes -days 2730 -newkey rsa:2048 -keyout private.key -out public.crt -config openssl.conf



docker pull quay.io/minio/minio

mkdir -p /{data1,data2,data3,data4}

vim /etc/default/minio


# MINIO_ROOT_USER and MINIO_ROOT_PASSWORD sets the root account for the MinIO server.
# This user has unrestricted permissions to perform S3 and administrative API operations on any resource in the deployment.
# Omit to use the default values 'minioadmin:minioadmin'.
# MinIO recommends setting non-default values as a best practice, regardless of environment.

MINIO_ROOT_USER=admin
MINIO_ROOT_PASSWORD=Pass01:)!!

# MINIO_VOLUMES sets the storage volumes or paths to use for the MinIO server.
# The specified path uses MinIO expansion notation to denote a sequential series of drives between 1 and 4, inclusive.
# All drives or paths included in the expanded drive list must exist *and* be empty or freshly formatted for MinIO to start successfully.

MINIO_VOLUMES="/data-{1...4}"

# MINIO_SERVER_URL sets the hostname of the local machine for use with the MinIO Server.
# MinIO assumes your network control plane can correctly resolve this hostname to the local machine.

# Uncomment the following line and replace the value with the correct hostname for the local machine.

#MINIO_SERVER_URL="http://minio.example.net"


docker run -dt                                  \
  -p 9000:9000 -p 9090:9090                     \
  -v /data1:/data-1                              \
  -v /data2:/data-2                              \
  -v /data3:/data-3                              \
  -v /data4:/data-4                              \
  -v /etc/default/minio:/etc/config.env         \
  -e "MINIO_CONFIG_ENV_FILE=/etc/config.env"    \
  --name "minio_local"                          \
  minio/minio server --console-address ":9090"

docker logs minio_local
```

## 测试

```python
# 安装pip包
pip3 install minio

# file_uploader.py
from minio import Minio
from minio.error import S3Error


def main():
    # Create a client with the MinIO server playground, its access key
    # and secret key.
    client = Minio(
        "play.min.io",
        access_key="uS3k1G3yxa7NXTvh",
        secret_key="F4X1wp0JG7dz6V6kBWo14n7n8mzogLol",
    )

    # Make 'asiatrip' bucket if not exist.
    found = client.bucket_exists("asiatrip")
    if not found:
        client.make_bucket("asiatrip")
    else:
        print("Bucket 'asiatrip' already exists")

    # Upload '/home/user/Photos/asiaphotos.zip' as object name
    # 'asiaphotos-2015.zip' to bucket 'asiatrip'.
    client.fput_object(
        "asiatrip", "asiaphotos-2015.zip", "/home/user/Photos/asiaphotos.zip",
    )
    print(
        "'/home/user/Photos/asiaphotos.zip' is successfully uploaded as "
        "object 'asiaphotos-2015.zip' to bucket 'asiatrip'."
    )


if __name__ == "__main__":
    try:
        main()
    except S3Error as exc:
        print("error occurred.", exc)
```

## 参考链接

- [docker运行教程](https://min.io/docs/minio/container/index.html)
- [minio官网](https://min.io/)
- [github](https://github.com/minio/minio)
- [云原生分布式文件存储 MinIO 保姆级中文教程](https://www.cnblogs.com/hahaha111122222/p/15981402.html)
- [分布式文件存储系统 Minio 实战](https://xie.infoq.cn/article/66ffc331f851f5873a3e1b2d3)