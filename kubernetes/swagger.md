1. 如何部署 swagger-ui#
打开两个 ssh 连接到你的 K8S 集群中。

进入第一个窗口，执行如下命令建立一个反向代理


kubectl proxy --port=8080
进入第二个窗口，执行如下命令建立获取 k8s的 api 文档信息，输出到一个 k8s-swagger.json 文件中


curl localhost:8080/openapi/v2 > k8s-swagger.json
获取到后，第一个窗口就可以关闭了。

然后任选一个窗口，执行如下命令运行一个容器


docker run \
    --rm \
    -d \
    -p 801:8080 \
    -e SWAGGER_JSON=/k8s-swagger.json \
    -v $(pwd)/k8s-swagger.json:/k8s-swagger.json \
    swaggerapi/swagger-ui

2. 使用 swagger-ui#
此时，你在浏览器上输入 http://ip 就可以看到一个经过可视化的 api 文档界面，其中包括安装在集群上的所有自定义资源的模型和路径！