
[Jenkins + Github + Nginx 自动化部署 Vue 项目](https://xie.infoq.cn/article/80e1ca90bdc8181374534ac85)

[Jenkins + Docker + Github 实现自动化部署 Maven 项目](https://xie.infoq.cn/article/c2c106a472a0d83a7e92a6ecc)


```sh
docker search jenkins

docker pull jenkins/jenkins:lts

mkdir -p /Users/jenkins/workspace


sudo docker run -uroot -d --restart=always -p 9001:8080 \
 -v /Users/jenkins/workspace/:/var/jenkins_home/workspace \
 -v /var/run/docker.sock:/var/run/docker.sock \
 --name jenkins jenkins/jenkins:lts

sudo docker run -uroot -d --restart=always -p 9001:8080 \
 -v /home/jenkins/workspace/:/var/jenkins_home/workspace \
 -v /var/run/docker.sock:/var/run/docker.sock \
 --name jenkins jenkins/jenkins:lts

docker exec -it -uroot jenkins bash

/var/jenkins_home/workspace/mvn
cat /var/jenkins_home/secrets/initialAdminPassword

ssh-keygen -t rsa -C "root"
```

sudo sed -i 's/^#\?PermitRootLogin.*/PermitRootLogin yes/g' /etc/ssh/sshd_config

sudo sed -i 's/^#\?PasswordAuthentication.*/PasswordAuthentication yes/g' /etc/ssh/sshd_config

,/ueE.9*_MdPg4G

,/ueE.9*_MdPg4G

https://docs.oracle.com/en/java/javase/index.html
1. docker 运行 Jenkins 之心的mvn jdk路径是容器里面的路径，如果用自动下载 对网络要求非常高，自动下载的jdk一般用不了。直接手动安装，下载下来后进行解压，然后配置路径/