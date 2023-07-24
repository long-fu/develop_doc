sudo apt-get install libopencv-dev

sudo apt-get install build-essential yasm cmake libtool libc6 libc6-dev unzip wget libnuma1 libnuma-dev

./configure --enable-shared --disable-asm
make -j$(nproc)
sudo make install
sudo cp /usr/local/lib/libx264.so.164 /lib

./configure --enable-shared --enable-pic --enable-static \
    --disable-x86asm --enable-libx264 \
    --enable-gpl --enable-nonfree \
    --extra-cflags=-I/usr/local/cuda-12.1/include \
    --extra-ldflags=-L/usr/local/cuda-12.1/lib64 \
    --enable-cuda-nvcc --enable-libnpp
    
# -extra-cflags=-I/usr/local/cuda/include --extra-ldflags=-L/usr/local/cuda/lib64
make -j$(nproc)
sudo make install

