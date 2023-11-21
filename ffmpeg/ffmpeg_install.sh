
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
  zlib1g-dev

sudo apt-get install build-essential yasm cmake libtool libc6 libc6-dev unzip wget libnuma1 libnuma-dev

./configure \
 --extra-libs="-lpthread -lm" \
 --ld="g++" \
 --enable-nonfree \
 --enable-cuda-nvcc \
 --enable-libnpp \
 --extra-cflags=-I/usr/local/cuda/include \
 --extra-ldflags=-L/usr/local/cuda/lib64 \
 --disable-static \
 --enable-shared \
  --enable-shared --enable-pic --enable-static \
  --enable-gpl \
  --enable-pthreads \
  --enable-shared \
  --enable-gnutls \
  --enable-libaom \
  --enable-libass \
  --enable-libfdk-aac \
  --enable-libfreetype \
  --enable-libmp3lame \
  --enable-libopus \
  --enable-libdav1d \
  --enable-libvorbis \
  --enable-libvpx \
  --enable-libx264 \
  --enable-libx265 \
  --enable-nonfree \
  --enable-ffplay
