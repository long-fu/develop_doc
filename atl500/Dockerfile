FROM ubuntu:18.04

RUN apt-get update 
#\
# && apt-get install python3.6 && \ apt-get install python3-pip
# python3.6 -m pip install --upgrade pip --user -i https://mirrors.huaweicloud.com/repository/pypi/simple && \
# python3.6 -m pip install Cython numpy tornado==5.1.0 protobuf --user -i https://mirrors.huaweicloud.com/repository/pypi/simple && \
# apt-get install python3-opencv && \
# apt-get install -y libavformat-dev libavcodec-dev libavdevice-dev libavutil-dev libswscale-dev libavresample-dev && \
# python3.6 -m pip install --upgrade pip && \
# python3.6 -m pip install Cython && \
# apt-get install pkg-config libxcb-shm0-dev libxcb-xfixes0-dev && \
# python3.6 -m pip install av==6.2.0 && \
# apt-get install libtiff5-dev libjpeg8-dev zlib1g-dev libfreetype6-dev liblcms2-dev libwebp-dev tcl8.6-dev tk8.6-dev python-tk && \
# python3.6 -m pip install numpy && \
# python3.6 -m pip install Pillow

ARG NNRT_PKG

ARG ASCEND_BASE=/usr/local/Ascend

ENV LD_LIBRARY_PATH=\
$LD_LIBRARY_PATH:\
$ASCEND_BASE/nnrt/latest/lib64:\
/home/data/miniD/driver/lib64

ENV ASCEND_AICPU_PATH=\
$ASCEND_BASE/nnrt/latest

WORKDIR /root

COPY $NNRT_PKG .


RUN umask 0022 && \
    groupadd -g 1001 HwHiAiUser && useradd -g HwHiAiUser -d /home/HwHiAiUser -m HwHiAiUser && usermod -u 1001 HwHiAiUser &&\
    chmod +x ${NNRT_PKG} &&\
    ./${NNRT_PKG} --quiet --install &&\
    rm ${NNRT_PKG}
