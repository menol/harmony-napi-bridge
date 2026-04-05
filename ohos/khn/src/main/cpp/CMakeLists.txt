# the minimum version of CMake.
cmake_minimum_required(VERSION 3.5.0)
project(khn)

set(NATIVERENDER_ROOT_PATH ${CMAKE_CURRENT_SOURCE_DIR})

if(DEFINED PACKAGE_FIND_FILE)
    include(${PACKAGE_FIND_FILE})
endif()

get_filename_component(PROJECT_MAIN_DIR "${CMAKE_CURRENT_SOURCE_DIR}" DIRECTORY)
get_filename_component(PROJECT_SRC_DIR "${PROJECT_MAIN_DIR}" DIRECTORY)
# 获取项目根目录
get_filename_component(PROJECT_DIR "${PROJECT_SRC_DIR}" DIRECTORY)

include_directories(${NATIVERENDER_ROOT_PATH}
                    ${NATIVERENDER_ROOT_PATH}/include)

# 纯净版，不包含任何外部生成的 C++ 文件，只编译入口 C++
add_library(khn SHARED napi_init.cpp)
target_include_directories(khn PUBLIC ${PROJECT_DIR}/libs/arm64-v8a/include)
target_link_libraries(khn PUBLIC libace_napi.z.so)

# 链接 Kotlin Native 生成的 libsample_plugin.so
target_link_libraries(khn PUBLIC ${PROJECT_DIR}/libs/arm64-v8a/libsample_plugin.so)
