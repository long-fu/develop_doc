FROM nvcr.io/nvidia/deepstream-l4t:6.2-base

RUN apt-get update && \
apt-get install -y gstreamer1.0-libav && \
apt-get install --reinstall -y gstreamer1.0-plugins-good gstreamer1.0-plugins-bad gstreamer1.0-plugins-ugly libavutil56 libavcodec58 libavformat58 libavfilter7 libx264-155 libde265-dev libde265-0 libx265-179 libvpx6 libmpeg2encpp-2.1-0 libmpeg2-4 libmpg123-0

COPY deepstream-6.2 /opt/nvidia/deepstream/deepstream-6.2

WORKDIR /opt/nvidia/deepstream/deepstream-6.2

RUN ./install.sh

ENTRYPOINT ["deepstream-app", "-c"]
CMD [ "./samples/configs/deepstream-app/source1_csi_dec_infer_resnet_int8.txt" ]
