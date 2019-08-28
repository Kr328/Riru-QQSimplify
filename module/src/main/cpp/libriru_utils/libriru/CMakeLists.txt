cmake_minimum_required(VERSION 3.0)

add_library(riru STATIC riru.c)
target_include_directories(riru PUBLIC ./)