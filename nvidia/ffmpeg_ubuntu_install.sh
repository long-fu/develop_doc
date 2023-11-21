#!/bin/bash

sudo apt-get update -qq && sudo apt-get -y install \
  autoconf \
  automake \
  build-essential \
  cmake \
  git-core \
  libass-dev \
  libfreetype6-dev \
  libgnutls28-dev \
  libmp3lame-dev \
  libsdl2-dev \
  libtool \
  libva-dev \
  libvdpau-dev \
  libvorbis-dev \
  libxcb1-dev \
  libxcb-shm0-dev \
  libxcb-xfixes0-dev \
  meson \
  ninja-build \
  pkg-config \
  texinfo \
  wget \
  yasm \
  zlib1g-dev \
  libunistring-dev \
  libaom-dev \
  libdav1d-dev \
  nasm \
  libx264-dev \
  libx265-dev \
  libnuma-dev \
  libvpx-dev \
  libfdk-aac-dev \
  libopus-dev \
  libdav1d-dev

sudo apt-get -y install \
    openssh-server \
    screen \
    pkg-config \
    nasm \
    yasm \
    unzip \
    curl \
    axel \
    autoconf \
    automake \
    build-essential \
    cmake \
    htop \
    git-core \
    libass-dev \
    libgnutls28-dev \
    libsdl2-dev \
    libtool \
    libvdpau-dev \
    libssl-dev \
    dkms \
    libssh-dev \
    libxcb1-dev \
    libxcb-shm0-dev \
    libxcb-xfixes0-dev \
    libegl1-mesa \
    meson \
    ninja-build \
    texinfo \
    libfdk-aac-dev \
    libx264-dev \
    libopus-dev \
    libunistring-dev \
    libaom-dev \
    nvidia-cuda-toolkit

# mkdir ~/app
# cd ~/app

# #create working directory
# mkdir ~/app/ffmpeg_sources

#install CUDA SDK
#echo "Installing CUDA and the latest driver repositories from repositories"
#wget https://developer.download.nvidia.com/compute/cuda/repos/ubuntu2004/x86_64/cuda-ubuntu2004.pin
#sudo mv cuda-ubuntu2004.pin /etc/apt/preferences.d/cuda-repository-pin-600
#wget https://developer.download.nvidia.com/compute/cuda/11.4.2/local_installers/cuda-repo-ubuntu2004-11-4-local_11.4.2-470.57.02-1_amd64.deb
#sudo apt-key add /var/cuda-repo-ubuntu2004-11-4-local/7fa2af80.pub
#sudo dpkg -i cuda-repo-ubuntu2004-11-4-local_11.4.2-470.57.02-1_amd64.deb

#install NVIDIA  SDK

cd ~/app
git clone https://github.com/FFmpeg/nv-codec-headers.git -b master
cd nv-codec-headers
make -j$(nproc)
sudo make install
cd ~/app

#compile ffmpeg
echo "Compiling ffmpeg"
cd ~/app
git clone https://github.com/FFmpeg/FFmpeg -b master
# cd FFmpeg

./configure \
  --extra-cflags="-I/usr/local/cuda/include/" \
  --extra-ldflags="-L/usr/local/cuda/lib64/" \
  --extra-cflags=-I/usr/local/include/ \
  --extra-ldflags=-L/usr/local/include/ \
  --extra-libs="-lpthread -lm" \
  --ld="g++" \
  --enable-cuda-nvcc \
  --enable-cuvid \
  --enable-libnpp \
  --enable-gpl \
  --enable-libass \
  --enable-libfdk-aac \
  --enable-libfreetype \
  --enable-libaom \
  --enable-libopus \
  --enable-libx264 \
  --enable-libx265 \
  --enable-nonfree \
  --enable-nvenc \
  --enable-gnutls \
  --enable-libmp3lame \
  --enable-libdav1d \
  --enable-libvorbis \
  --enable-libvpx \
	--enable-decoder=hevc \
	--enable-sdl \
  --enable-ffplay \
	--enable-shared \
	--enable-pic \
	--enable-pthreads \
	--enable-ffmpeg \
	--enable-swscale

make -j$(nproc)
sudo make install
make distclean
hash -r

echo "Complete!"