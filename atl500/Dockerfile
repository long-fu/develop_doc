#操作系统及版本号，根据实际修改
FROM ubuntu:18.04

#设置离线推理引擎包参数
ARG NNRT_PKG

#设置环境变量
ARG ASCEND_BASE=/usr/local/Ascend

ENV LD_LIBRARY_PATH=\
$LD_LIBRARY_PATH:\
$ASCEND_BASE/nnrt/latest/lib64:\
/home/data/miniD/driver/lib64

ENV ASCEND_AICPU_PATH=\
$ASCEND_BASE/nnrt/latest
#设置进入启动后的容器的目录
WORKDIR /root
#拷贝离线推理引擎包
COPY $NNRT_PKG .

#安装离线推理引擎包
RUN umask 0022 && \
    groupadd -g gid HwHiAiUser && useradd -g HwHiAiUser -d /home/HwHiAiUser -m HwHiAiUser && usermod -u uid HwHiAiUser &&\
    chmod +x ${NNRT_PKG} &&\
    ./${NNRT_PKG} --quiet --install &&\
    rm ${NNRT_PKG}

#拷贝业务推理程序压缩包、安装脚本与运行脚本
ARG DIST_PKG
COPY $DIST_PKG .
COPY install.sh .
COPY run.sh /usr/local/bin/

#运行安装脚本
RUN chmod +x /usr/local/bin/run.sh && \
    sh install.sh && \
    rm $DIST_PKG && \
    rm install.sh

#容器启动时默认执行的程序
CMD run.sh