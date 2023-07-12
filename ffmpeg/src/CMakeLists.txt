cmake_minimum_required(VERSION 3.0.0)
project(ffmpeg_main VERSION 0.1.0 LANGUAGES CXX)

include(CTest)
enable_testing()

include_directories("/usr/include/x86_64-linux-gnu")

add_executable(ffmpeg_main main.cpp)

target_link_libraries(ffmpeg_main libavutil.so libavcodec.so libavformat.so libswscale.so )

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
include(CPack)
