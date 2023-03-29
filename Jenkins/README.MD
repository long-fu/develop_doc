
[Jenkins + Github + Nginx 自动化部署 Vue 项目](https://xie.infoq.cn/article/80e1ca90bdc8181374534ac85)

[Jenkins + Docker + Github 实现自动化部署 Maven 项目](https://xie.infoq.cn/article/c2c106a472a0d83a7e92a6ecc)


```sh
docker search jenkins

docker pull jenkins/jenkins:lts

mkdir -p /Users/jenkins/workspace


docker run -uroot -d --restart=always -p 9001:8080 \
 -v /Users/jenkins/workspace/:/var/jenkins_home/workspace \
 -v /var/run/docker.sock:/var/run/docker.sock \
 --name jenkins jenkins/jenkins:lts

docker exec -it -uroot jenkins bash

cat /var/jenkins_home/secrets/initialAdminPassword

ssh-keygen -t rsa -C "root"
```