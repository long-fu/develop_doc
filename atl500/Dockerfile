FROM ubuntu:18.04

ARG NNRT_PKG

ARG ASCEND_BASE=/usr/local/Ascend

ENV LD_LIBRARY_PATH=\
$LD_LIBRARY_PATH:\
$ASCEND_BASE/nnrt/latest/lib64:\
/home/data/miniD/driver/lib64

ENV ASCEND_AICPU_PATH=\
$ASCEND_BASE/nnrt/latest

COPY $NNRT_PKG .

RUN umask 0022 && \
    groupadd -g 1001 HwHiAiUser && useradd -g HwHiAiUser -d /home/HwHiAiUser -m HwHiAiUser && usermod -u 1001 HwHiAiUser &&\
    chmod +x ${NNRT_PKG} &&\
    ./${NNRT_PKG} --quiet --install &&\
    rm ${NNRT_PKG}

RUN chmod 777 /tmp

RUN apt-get update

COPY install.sh .
RUN sh install.sh && \
    rm install.sh
