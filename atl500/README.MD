所有文档 https://support.huawei.com/enterprise/zh/ascend-computing/a500-3000-pid-250702836

Atlas 500 智能小站 用户指南 (型号 3000) 18 https://support.huawei.com/enterprise/zh/doc/EDOC1100133176/6d1ed110

[linux] SFTP文件传输基本命令 https://blog.csdn.net/Trance95/article/details/128735668

# Atlas 500 智能小站(型号 3000)

- [官方文档](https://support.huawei.com/enterprise/zh/doc/EDOC1100133176/426cffd9)

- [所有手册](https://support.huawei.com/enterprise/zh/ascend-computing/a500-3000-pid-250702836)

## 开发配置

1. 外网访问配置

2. 文件传输配置

开放/tmp目录进行文件传输：

ssh远程登录a500后，在IES:/ -> 界面，首先输入 sftp enable，终端返回 set enable success，即可开放atlas500的 /tmp 目录，进行远程文件传输。

```sh
IES:/->sftp enable
enable success
```

3. root方式直接登录配置



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

HwHiAiUser

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

# 开发环境配置

source /home/haoshuai/Ascend/ascend-toolkit/set_env.sh

export LD_LIBRARY_PATH=/home/haoshuai/Ascend/ascend-toolkit/latest/x86_64-linux/devlib/:$LD_LIBRARY_PATH


groupadd HwHiAiUser
useradd -g HwHiAiUser -d /home/HwHiAiUser -m HwHiAiUser -s /bin/bash
passwd HwHiAiUser

# 自己制作的

docker run -it --rm --net=host --privileged \
--device=/dev/davinci0 \
--device=/dev/davinci_manager \
--device=/dev/devmm_svm \
--device=/dev/hisi_hdc \
-v /usr/local/bin/npu-smi:/usr/local/bin/npu-smi:ro \
-v /home/data/miniD/driver/lib64:/home/data/miniD/driver/lib64:ro \
-v /etc/ascend_install.info:/etc/ascend_install.info \
-v /home/data/miniD/driver/version.info:/home/data/miniD/driver/version.info \
-v /home/haoshuai/image-build/samples-v0.9.0:/home/data/samples-v0.9.0 \
-w /home/data/samples-v0.9.0 \
haoshuai:v0.0.2 /bin/bash

# 官方的

需要改造挂载路径

docker run -it --rm --net=host --privileged -u root \
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

python3 om_val.py --tag 6.0 --model=yolov5s_bs4.om --nms_mode nms_script --batch_size=4


LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/var/davinci/driver/lib64:/var/davinci/driver/lib64/common:/var/davinci/driver/lib64/driver
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/fwkacllib/lib64:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/atc/lib64
export PYTHONPATH=$PYTHONPATH:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/fwkacllib/python/site-packages:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/toolkit/python/site-packages:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/atc/python/site-packages:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/pyACL/python/site-packages/acl
export PATH=$PATH:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/fwkacllib/ccec_compiler/bin:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/fwkacllib/bin:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/atc/bin
export ASCEND_AICPU_PATH=/home/HwHiAiUser/Ascend/ascend-toolkit/latest
export ASCEND_OPP_PATH=/home/HwHiAiUser/Ascend/ascend-toolkit/latest/opp
export TOOLCHAIN_HOME=/home/HwHiAiUser/Ascend/ascend-toolkit/latest/toolkit

export PYTHONPATH=$PYTHONPATH:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/fwkacllib/python/site-packages:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/toolkit/python/site-packages:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/atc/python/site-packages:/home/HwHiAiUser/Ascend/ascend-toolkit/latest/pyACL/python/site-packages/acl

python3.6 /home/data/samples-v0.9.0/python/level2_simple_inference/2_object_detection/YOLOV4_coco_detection_car_video/src/yolov4.py

--

/opt/middleware/MindXOM/bin/mount_white_path