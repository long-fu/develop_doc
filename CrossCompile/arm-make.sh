#!/bin/sh

BASE=`pwd`
BUILD_HOST=arm-linux-gnueabihf
OUTPUT_PATH=${BASE}/arm-install

OTHER_LIB=${OUTPUT_PATH}/all_without_ffmpeg

set_env(){
	export CROSS_COMPILE=arm-linux-gnueabihf-
	AS=${CROSS_COMPILE}as
	AR=${CROSS_COMPILE}ar
	NM=${CROSS_COMPILE}nm
	CC=${CROSS_COMPILE}gcc
	GG=${CROSS_COMPILE}g++
	CXX=${CROSS_COMPILE}c++
	LD=${CROSS_COMPILE}ld
	RANLIB=${CROSS_COMPILE}ranlib
	STRIP=${CROSS_COMPILE}strip
	export AS AR NM CC GG LD RANLIB STRIP
	# export PATH=$PATH:/opt/pkg/petalinux/2018.3/tools/linux-i386/gcc-arm-linux-gnueabi/bin
}

make_dirs () {
    #为了方便管理，创建有关的目录
    cd ${BASE} && mkdir compressed arm-install source -p
}

tget () { #try wget
    filename=`basename $1`
    echo "Downloading [${filename}]..."
    if [ ! -f ${filename} ];then
        wget $1
    fi

    echo "[OK] Downloaded [${filename}] "
}

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

make_x264() {
    cd ${BASE}/source/x264*

    ./configure \
    --prefix=${OUTPUT_PATH}/x264 \
    --host=${BUILD_HOST} \
    --enable-shared \
    --enable-pic \
    --disable-asm

    make -j$(nproc) && make install
}

make_x265() {
    cd ${BASE}/source/x265*/build
	mkdir arm-x265
	cd arm-x265
    # 获取 工具链所在位置
	GCC_FULL_PATH=`whereis ${BUILD_HOST}-gcc | awk -F: '{ print $2 }' | awk '{print $1}'` # 防止多个结果
    CROSS_PATH=`dirname ${GCC_FULL_PATH}`
	touch crosscompile.cmake
	#echo "set(CROSS_COMPILE_ARM 1)" > crosscompile.cmake
	echo "set(CMAKE_SYSTEM_NAME Linux)" > crosscompile.cmake
	echo "set(CMAKE_SYSTEM_PROCESSOR arm)" >> crosscompile.cmake
	echo "" >> crosscompile.cmake
	echo "# specify the cross compiler" >> crosscompile.cmake
	echo "set(CMAKE_C_COMPILER ${CROSS_PATH}/${BUILD_HOST}-gcc)" >> crosscompile.cmake
	echo "set(CMAKE_CXX_COMPILER ${CROSS_PATH}/${BUILD_HOST}-g++)" >> crosscompile.cmake
	echo "set(CMAKE_SHARED_LINKER_FLAGS \"-ldl \${CMAKE_SHARED_LINKER_FLAGS}\")" >> crosscompile.cmake
	echo "" >> crosscompile.cmake
	echo "# specify the target environment" >> crosscompile.cmake
	echo "SET(CMAKE_FIND_ROOT_PATH  ${CROSS_PATH})" >> crosscompile.cmake
	# 编译安装
	cmake -DCMAKE_TOOLCHAIN_FILE=crosscompile.cmake -G "Unix Makefiles" \
	-DCMAKE_C_FLAGS="-fPIC ${CMAKE_C_FLAGS}" -DCMAKE_CXX_FLAGS="-fPIC ${CMAKE_CXX_FLAGS}" \
	-DCMAKE_SHARED_LINKER_FLAGS="-ldl ${CMAKE_SHARED_LINKER_FLAGS}"  \
	-DCMAKE_INSTALL_PREFIX=${OUTPUT_PATH}/x265 \
	../../source && make -j$(nproc)
	make install
}

prepare_other_lib () {
    # 这一个是针对 ffmpeg 方便管理外部库使用的
    # 核心思想是把 所有的库都放到一起，再让 ffmpeg ld的时候在这里找（而不是添加多行） --extra-cflags="-I${X264_DIR}/include -I${xxx}/include" \
    cd ${BASE}/arm-install/
    rm ${OTHER_LIB} -rf
    ls > /tmp/list.txt
    mkdir ${OTHER_LIB} -p
    for sub_dir in `cat /tmp/list.txt`
    do
        cp ${sub_dir}/* ${OTHER_LIB} -r -v
    done
    rm -rf /tmp/list.txt
}

make_ffmpeg() {
    MYPKGCONFIG=${BASE}/arm-install/x265/lib/pkgconfig/
    export PKG_CONFIG_PATH=${MYPKGCONFIG}:$PKG_CONFIG_PATH
    cd ${BASE}/source/ffmpeg*
	
	./configure \
	--prefix=${OUTPUT_PATH}/ffmpeg \
	--enable-cross-compile \
	--cross-prefix=${BUILD_HOST}- \
	--arch=arm \
	--host-os=linux \
	--target-os=linux \
	--cc=${BUILD_HOST}-gcc \
	--enable-shared \
	--enable-pic \
	--enable-gpl \
	--enable-nonfree \
	--enable-pthreads \
	--enable-ffmpeg \
	--enable-swscale  \
	# --enable-libx264 \
	# --enable-libx265 \
	# --enable-decoder=hevc \
    --enable-ffplay \
	--disable-stripping \
	--disable-doc  --disable-debug --disable-iconv --disable-armv5te --disable-armv6 --disable-armv6t2 \
	--pkg-config="pkg-config --static" \
	--extra-cflags=-I${OTHER_LIB}/include \
	--extra-ldflags=-L${OTHER_LIB}/lib

	make -j$(nproc) && make install
}

echo "Using ${BUILD_HOST}-gcc"
make_dirs
download_package
tar_package
# set_env
# make_x264
make_x265
# prepare_other_lib
# make_ffmpeg
