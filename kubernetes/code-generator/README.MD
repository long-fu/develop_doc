https://github.com/kubernetes/sample-controller

https://github.com/kubernetes/code-generator

使用此一例子的时候



需要 在 home 目录下面 创建k8s.io目录 然后把sample-controller代码移动到~/k8s.io/目录下才可以成功运行

脚本是在 生成的目录 是～/k8s.io
生成的模块也是k8s.io

脚本顺利执行 还需要 替换下面两个模块 在go.mod下面

k8s.io/apimachinery v0.0.0-20230508165628-e7958c5fe270
k8s.io/code-generator v0.0.0-20230509180209-89a7a4d0e489

必须要初始化git仓库