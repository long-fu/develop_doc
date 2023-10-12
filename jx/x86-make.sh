#!/bin/sh

BASE=`pwd`
OUTPUT_PATH=${BASE}/x86-install

make_dirs () {
    #为了方便管理，创建有关的目录
    cd ${BASE} && mkdir compressed x86-install source -p
}

tget () { #try wget
    filename=`basename $1`
    echo "Downloading [${filename}]..."
    if [ ! -f ${filename} ];then
        wget $1
    fi

    echo "[OK] Downloaded [${filename}] "
}

#下载地址,可自行下载存放在 compressed 目录中
download_package () {
    cd ${BASE}/compressed
    tget https://code.videolan.org/videolan/x264/-/archive/master/x264-master.tar.bz2
    tget https://ffmpeg.org/releases/ffmpeg-5.0.tar.gz
    tget http://download.videolan.org/videolan/x265/x265_3.2.tar.gz
}

tar_package () {
    cd ${BASE}/compressed
    ls * > /tmp/list.txt
    for TAR in `cat /tmp/list.txt`
    do
        tar -xf $TAR -C  ../source
    done
    rm -rf /tmp/list.txt
}

make_yasm(){
	cd ${BASE}/source/yasm*
	./configure 

	make -j8 && sudo make install
}

make_nasm(){
	cd ${BASE}/source/nasm*
	./configure 

	make -j8 && sudo make install
}

make_x264() {
    cd ${BASE}/source/x264*

	./configure --prefix=${OUTPUT_PATH}/ --enable-static --enable-pic

    make -j8 && make install
}

make_x265() {
    cd ${BASE}/source/x265*/build/linux
	# 编译安装
	cmake -G "Unix Makefiles" \
	-DCMAKE_INSTALL_PREFIX=${OUTPUT_PATH} \
	-DCMAKE_ASM_NASM_FLAGS=-w-macro-params-legacy \
	-DENABLE_SHARED:bool=off ../../source

	make -j8 && make install

	cd ${OUTPUT_PATH}/lib/pkgconfig
	# 修改x265.pc
    sed -i '9,9s/-lx265/-lx265 -lm -lstdc++ -ldl -lpthread/g' x265.pc
	sed -i '10,10s/-lstdc++/-lstdc++ -lm -lgcc_s -lgcc -lrt -ldl/g' x265.pc

}

make_sdl() {
	cd ${BASE}/source/SDL*
	# ./autogen.sh

	./configure
	
	make -j8 && sudo make install
}

make_ffmpeg() {
    export PKG_CONFIG_PATH=$PKG_CONFIG_PATH:${OUTPUT_PATH}/lib/pkgconfig/

    cd ${BASE}/source/ffmpeg*

	./configure \
	--prefix=${OUTPUT_PATH}/ffmpeg5.0 \
	--enable-gpl \
	--enable-pthreads \
	--enable-libx264 \
	--enable-libx265 \
	--enable-pic \
	--enable-shared \
	--enable-nonfree \
	--enable-postproc \
	--enable-ffplay

	make -j8 && make install
}

echo "Using ${BUILD_HOST}-gcc"
make_dirs
# download_package
tar_package

sudo apt install cmake -y
sudo apt install pkg-config
sudo apt-get install libx11-dev
sudo apt-get install xorg-dev

make_yasm
make_nasm
make_sdl
make_x264
make_x265
make_ffmpeg
