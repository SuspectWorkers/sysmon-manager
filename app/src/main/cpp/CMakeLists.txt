# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.18.1)

project("smcore")

add_library(smcore
        SHARED
        jni.c
        ksu.c
        legacy.c
)

target_include_directories(smcore PRIVATE .)

find_library(log-lib log)

target_link_libraries(
        smcore
        ${log-lib}
)

find_package(cxx REQUIRED CONFIG)
link_libraries(cxx::cxx)

add_library(adbroot SHARED adbroot.cc)
target_link_libraries(adbroot ${log-lib})
