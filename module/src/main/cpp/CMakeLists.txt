cmake_minimum_required(VERSION 3.0)

set(CMAKE_C_FLAGS "-ffixed-x18 ${CMAKE_C_FLAGS}")
set(CMAKE_CXX_FLAGS "-ffixed-x18 ${CMAKE_CXX_FLAGS}")
set(CMAKE_EXE_LINKER_FLAGS "-ffixed-x18 ${CMAKE_EXE_LINKER_FLAGS}")
set(CMAKE_LIBRARY_LINKER_FLAGS "-ffixed-x18 ${CMAKE_LIBRARY_LINKER_FLAGS}")
set(CMAKE_MODULE_LINKER_FLAGS "-ffixed-x18 ${CMAKE_MODULE_LINKER_FLAGS}")

add_subdirectory(libriru_utils)

add_library(riru_qq_lite SHARED main.c inject.c utils.c)
target_link_libraries(riru_qq_lite riru log)

