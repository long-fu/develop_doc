#!/bin/sh

BASE=`pwd`
BUILD_HOST=aarch64-none-linux-gnu
OUTPUT_PATH=${BASE}/aarch64-install

OTHER_LIB=${OUTPUT_PATH}/all_without_ffmpeg

set_env(){
	export CROSS_COMPILE=aarch64-none-linux-gnu-
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
	export PATH=$PATH:/usr/local/ARM-toolchain/gcc-arm-9.2-2019.12-x86_64-aarch64-none-linux-gnu/bin
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
    tget https://code.videolan.org/videolan/x264/-/archive/stable/x264-stable.tar.gz
    tget https://ffmpeg.org/releases/ffmpeg-5.0.tar.gz
    # tget http://download.videolan.org/videolan/x265/x265_3.2.tar.gz
	# tget http://www.libsdl.org/release/SDL2-2.0.22.tar.gz
	tget https://download.savannah.gnu.org/releases/freetype/freetype-2.13.2.tar.gz
	# tget https://download.savannah.gnu.org/releases/freetype/freetype-2.12.0.tar.gz
	# tget https://download.savannah.gnu.org/releases/freetype/freetype-2.10.0.tar.gz
	tget https://github.com/harfbuzz/harfbuzz/archive/refs/tags/8.2.0.tar.gz
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

make_sdl() {
	cd ${BASE}/source/SDL*

	./autogen.sh

	./configure \
	--prefix=${OUTPUT_PATH}/sdl \
	--host=${BUILD_HOST} \
	--disable-pulseaudio

	make -j$(nproc) && make install
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

	# ./configure 
	# # CC=aarch64-linux-gnu-gcc \
	# --host=aarch64-linux \
	# --prefix=${OUTPUT_PATH}/freetype \
	# --with-zlib=no \
	# --with-png=n

make_harfbuzz() {

	cd ${BASE}/source/harfbuzz*
	
	./configure \
	CC=aarch64-linux-gnu-gcc \
	--host=arm-linux \
	--prefix=${OUTPUT_PATH}/harfbuzz \
	--enable-shared \
	--enable-static

	make -j$(nproc) && make install
}

make_freetype() {

	cd ${BASE}/source/freetype*
	
	./configure \
	CC=aarch64-linux-gnu-gcc \
	--host=arm-linux \
	--prefix=${OUTPUT_PATH}/freetype \
	--enable-shared \
	--enable-static \
	--with-zlib=no \
	--with-harfbuzz=no \
	--enable-freetype-config \
	--with-png=n

	make -j$(nproc) && make install

	# harfbuzz 可以不需要
}

# 失败
# make_freetype() {
# 	cd ${BASE}/source/freetype*
# 	./configure 
# 	--host=${BUILD_HOST} \
# 	--prefix=${OUTPUT_PATH}/freetype \
# 	--with-zlib=no \
# 	--with-png=n
# }

make_x265() {
    cd ${BASE}/source/x265*/build
	mkdir aarch64-x265
	cd aarch64-x265
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

make_opencv() {
	cd ${BASE}/source/opencv*
	rm -rf build
	cd build
	cmake \
	-DCMAKE_INSTALL_PREFIX=../aarch_64_install \
	-DWITH_CUDA=OFF \
	-DENABLE_PRECOMPILED_HEADERS=OFF \
	-DCMAKE_TOOLCHAIN_FILE=../platforms/linux/aarch64-gnu.toolchain.cmake \
	-DCUDA_GENERATION=Kepler ..
	make
	make install
}

prepare_other_lib () {
    # 这一个是针对 ffmpeg 方便管理外部库使用的
    # 核心思想是把 所有的库都放到一起，再让 ffmpeg ld的时候在这里找（而不是添加多行） --extra-cflags="-I${X264_DIR}/include -I${xxx}/include" \
    cd ${BASE}/aarch64-install/
    rm ${OTHER_LIB} -rf
    ls > /tmp/list.txt
    mkdir ${OTHER_LIB} -p
    for sub_dir in `cat /tmp/list.txt`
    do
        cp ${sub_dir}/* ${OTHER_LIB} -r -v
    done
    rm -rf /tmp/list.txt

    # 把SDL2库放到和其他库同一目录下
	# echo "当前目录是" $(pwd)
	# cd ${OTHER_LIB}/include
	# cp -r SDL2/. ./
	# rm -rf SDL2
}


make_ffmpeg() {
    MYPKGCONFIG=${OTHER_LIB}/lib/pkgconfig/
    export PKG_CONFIG_PATH=${MYPKGCONFIG}:$PKG_CONFIG_PATH

    cd ${BASE}/source/ffmpeg*
	make clean
	./configure \
	--prefix=${OUTPUT_PATH}/ffmpeg5.0 \
	--extra-cflags=-I${OTHER_LIB}/include \
	--extra-ldflags=-L${OTHER_LIB}/lib \
	--enable-cross-compile \
	--cross-prefix=${BUILD_HOST}- \
	--arch=aarch64 \
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
	--enable-libx264 \
	--disable-stripping \
	--disable-doc \
	--disable-debug \
	--disable-iconv \
	--disable-armv5te \
	--disable-armv6 \
	--disable-armv6t2 \
	--pkg-config="pkg-config --static"

	cd ${BASE}/source/ffmpeg*
	make -j$(nproc) && make install
}

# make_ffmpeg_play() {
#     MYPKGCONFIG=${OTHER_LIB}/lib/pkgconfig/
#     export PKG_CONFIG_PATH=${MYPKGCONFIG}:$PKG_CONFIG_PATH

#     cd ${BASE}/source/ffmpeg*
# 	make clean
# 	./configure \
# 	--prefix=${OUTPUT_PATH}/ffmpeg5.0 \
# 	--extra-cflags=-I${OTHER_LIB}/include \
# 	--extra-ldflags=-L${OTHER_LIB}/lib \
# 	--enable-cross-compile \
# 	--cross-prefix=${BUILD_HOST}- \
# 	--arch=aarch64 \
# 	--host-os=linux \
# 	--target-os=linux \
# 	--cc=${BUILD_HOST}-gcc \
# 	--enable-shared \
# 	--enable-pic \
# 	--enable-gpl \
# 	--enable-nonfree \
# 	--enable-pthreads \
# 	--enable-ffmpeg \
# 	--enable-swscale  \
# 	--enable-libx264 \
# 	--enable-sdl \
#     --enable-ffplay \
# 	--disable-stripping \
# 	--disable-doc \
# 	--disable-debug \
# 	--disable-iconv \
# 	--disable-armv5te \
# 	--disable-armv6 \
# 	--disable-armv6t2 \
# 	--pkg-config="pkg-config --static"

# #修改config.mak,使能FFPLAY
# 	cd ${BASE}/source/ffmpeg*/ffbuild
# 	sed -i 's/!CONFIG_FFPLAY=yes/CONFIG_FFPLAY=yes/g' config.mak

# 	cd ${BASE}/source/ffmpeg*
# 	make -j$(nproc) && make install
# }

echo "Using ${BUILD_HOST}-gcc"
make_dirs
download_package
tar_package
set_env

# make_sdl
# make_x264
# make_x265
# prepare_other_lib
# make_ffmpeg

# 可以不用编译
# make_harfbuzz

# make_freetype

make_opencv

