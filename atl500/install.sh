#!/bin/bash
apt-get update
apt-get install -y wget
apt-get install -y vi
apt-get install -y vim
apt-get install -y curl
apt-get install -y python3.6 
apt-get install -y python3-pip 
apt-get install -y apt-utils
apt-get install -y python3-opencv
apt-get install -y ffmpeg
apt-get install -y libavformat-dev libavcodec-dev libavdevice-dev libavutil-dev libswscale-dev libavresample-dev
apt-get install -y pkg-config libxcb-shm0-dev libxcb-xfixes0-dev
apt-get install -y libtiff5-dev libjpeg8-dev zlib1g-dev libfreetype6-dev liblcms2-dev libwebp-dev tcl8.6-dev tk8.6-dev python-tk
python3.6 -m pip install --upgrade pip
python3.6 -m pip install av==6.2.0
python3.6 -m pip install Cython 
python3.6 -m pip install numpy 
python3.6 -m pip install tornado==5.1.0 
python3.6 -m pip install protobuf 
python3.6 -m pip install Pillow 
