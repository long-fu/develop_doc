# yolov5 推理配置

## 本地开发环境配置

> 本地环境是**非昇腾设备**

1. [安装依赖](https://www.hiascend.com/document/detail/zh/canncommercial/601/envdeployment/instg/instg_000023.html)。按照教程进行依赖的安装。

2. [安装开发套件包](https://www.hiascend.com/document/detail/zh/canncommercial/601/envdeployment/instg/instg_000027.html)。按照教程进行`cann toolkit`的安装。

3. [配置环境变量](https://www.hiascend.com/document/detail/zh/canncommercial/601/envdeployment/instg/instg_000028.html)。按照教程进行环境变量的配置。

4. [配置交叉编译环境](https://www.hiascend.com/document/detail/zh/canncommercial/601/envdeployment/instg/instg_000029.html)。按照教程进行配置交叉编译环境。

5. [c++环境准备和依赖安装](https://gitee.com/ascend/samples/blob/master/cplusplus/environment/separate_environmental_guidance_CN.md)。按照教程进行C++环境的配置。


## 容器推理环境

在华为镜像仓库拉取[infer-modelzoo](https://ascendhub.huawei.com/#/detail/infer-modelzoo)镜像，版本是22.0.0。

**容器启动命令**

```sh
docker run -it --net=host --privileged -u root \
--device=/dev/davinci0 \
--device=/dev/davinci_manager \
--device=/dev/devmm_svm \
--device=/dev/hisi_hdc \
-v /usr/local/dcmi:/usr/local/dcmi \
-v /var/log/npu:/var/log/npu \
-v /home/data/miniD/driver/driver:/usr/local/Ascend/driver \
-v /usr/slog:/usr/slog \
-v /usr/local/bin/npu-smi:/usr/local/bin/npu-smi:ro \
-v /home/data/miniD/driver/lib64:/usr/local/Ascend/driver/lib64:ro \
-v /home/data/miniD/driver/tools/:/usr/local/Ascend/driver/tools/ \
-v /home/data/miniD/driver/add-ons/:/usr/local/Ascend/add-ons/ \
-v /data:/data \
-v /home/haoshuai/modelzoo_yolov5:/home/data/modelzoo_yolov5 \
-w /home/data/modelzoo_yolov5 \
ascendhub.huawei.com/public-ascendhub/infer-modelzoo:22.0.0 \
/bin/bash

docker run -it --net=host --privileged -u root \
--device=/dev/davinci0 \
--device=/dev/davinci_manager \
--device=/dev/devmm_svm \
--device=/dev/hisi_hdc \
-v /usr/local/dcmi:/usr/local/dcmi \
-v /var/log/npu:/var/log/npu \
-v /home/data/miniD/driver/driver:/usr/local/Ascend/driver \
-v /usr/slog:/usr/slog \
-v /usr/local/bin/npu-smi:/usr/local/bin/npu-smi:ro \
-v /home/data/miniD/driver/lib64:/usr/local/Ascend/driver/lib64:ro \
-v /home/data/miniD/driver/tools/:/usr/local/Ascend/driver/tools/ \
-v /home/data/miniD/driver/add-ons/:/usr/local/Ascend/add-ons/ \
-v /data:/data \
-v /home/zhonghang/core:/home/data/core \
-w /home/data/core \
ascendhub.huawei.com/public-ascendhub/infer-modelzoo:22.0.0 \
/bin/bash

docker run -it --net=host --privileged -u root \
--device=/dev/davinci0 \
--device=/dev/davinci_manager \
--device=/dev/devmm_svm \
--device=/dev/hisi_hdc \
-v /usr/local/dcmi:/usr/local/dcmi \
-v /var/log/npu:/var/log/npu \
-v /home/data/miniD/driver/driver:/usr/local/Ascend/driver \
-v /usr/slog:/usr/slog \
-v /usr/local/bin/npu-smi:/usr/local/bin/npu-smi:ro \
-v /home/data/miniD/driver/lib64:/usr/local/Ascend/driver/lib64:ro \
-v /home/data/miniD/driver/tools/:/usr/local/Ascend/driver/tools/ \
-v /home/data/miniD/driver/add-ons/:/usr/local/Ascend/add-ons/ \
-v /data:/data \
-v /home/zhonghang/core:/home/data/core \
-w /home/data/core \
infer-modelzoo:v22.0.2 \
/bin/bash

# 以安装用户在开发环境任意目录下执行以下命令，打开.bashrc文件。
vi ~/.bashrc  
# 在文件最后一行后面添加如下内容。CPU_ARCH环境变量请根据运行环境cpu架构填写，如export CPU_ARCH=aarch64
export CPU_ARCH=aarch64
# THIRDPART_PATH需要按照运行环境安装路径设置，如运行环境为arm，指定安装路径为Ascend-arm，则需要设置为export THIRDPART_PATH=${HOME}/Ascend-arm/thirdpart/${CPU_ARCH}
export THIRDPART_PATH=${HOME}/Ascend/thirdpart/${CPU_ARCH}  #代码编译时链接第三方库
# CANN软件安装后文件存储路径，最后一级目录请根据运行环境设置，运行环境为arm，这里填arm64-linux；运行环境为x86，则这里填x86_64-linux，以下以arm环境为例
export INSTALL_DIR=${HOME}/Ascend/ascend-toolkit/latest/arm64-linux

```

挂载本地代码仓库 `-v /home/haoshuai/modelzoo_yolov5:/home/data/modelzoo_yolov5 \` 命令

**容器中需要配置命令**

参照[昇腾AI设备安装开发环境，同时将此环境作为运行环境的samples相关依赖安装 ](https://gitee.com/ascend/samples/tree/master/cplusplus/environment) 教程安装依赖

阅读[YOLOV3_coco_detection_4_thread](https://gitee.com/ascend/samples/tree/master/cplusplus/level2_simple_inference/n_performance/1_multi_process_thread/YOLOV3_coco_detection_4_thread) `README_CN.md`文件

```sh

# 进入官方代码例子
cd samples/cplusplus/level2_simple_inference/n_performance/1_multi_process_thread/YOLOV3_coco_detection_4_thread

cd scripts

# 编译程序
bash sample_build.sh

# 运行程序
bash sample_run.sh

```

**yolov5的运行方式和3的一样**

----

**参考链接**

- [昇腾工具仓库](https://gitee.com/ascend/tools/tree/master)

- [快速部署](https://www.hiascend.com/document/detail/zh/quick-installation/22.0.0/quickinstg/500_Pro_3000/quickinstg_500_Pro_3000_0001.html)

- [CANN商用版6.0.1](https://www.hiascend.com/document/detail/zh/canncommercial/601/overview/index.html)

- [CANN样例仓介绍](https://gitee.com/ascend/samples/tree/master)

- [Yolov5_for_Pytorch 例子](https://gitee.com/ascend/modelzoo-GPL/tree/master/built-in/ACL_Pytorch/Yolov5_for_Pytorch)

- [API Samples](https://www.hiascend.com/zh/marketplace/mindx-sdk/case-studies/a0bfc98e-eed1-4493-aa8c-7463a1e6bb7e)

- [昇腾镜像仓库](https://ascendhub.huawei.com/#/index)

- [Atlas 500应用](https://support.huaweicloud.com/adevg-atlas500app/atlas500development_01_0001.html)

- [Atlas 500 制作容器镜像](https://support.huawei.com/enterprise/zh/doc/EDOC1100133176/7d1c2891)
