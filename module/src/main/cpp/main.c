#include <stdio.h>
#include <jni.h>
#include <dlfcn.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include <android/log.h>
#include <sys/stat.h>
#include <sys/system_properties.h>

#include "log.h"
#include "inject.h"

#define EXPORT __attribute__((visibility("default"))) __attribute__((used))

#define DEX_PATH          "/system/framework/boot-qq-simplify.dex"
#define INJECT_CLASS_PATH "com/github/kr328/qq/simplify/Injector"
#define INJECT_METHOD_NAME "inject"
#define TARGET_PACKAGE "com.tencent.mobileqq"

static void do_inject(JNIEnv *env) {
    load_and_invoke_dex(env, DEX_PATH, INJECT_CLASS_PATH, INJECT_METHOD_NAME, "");
}

EXPORT int shouldSkipUid(int uid) {
    int user = uid / 100000;
    char buffer[PATH_MAX];

    sprintf(buffer, "/data/user/%d/" TARGET_PACKAGE, user);

    struct stat s;

    if ( stat(buffer, &s) < 0 ) return true;

    return s.st_uid != uid;
}

EXPORT int nativeForkAndSpecializePost(JNIEnv *env, jclass clazz, jint res) {
    LOGI("Native inject app process");

    do_inject(env);

    return 0;
}

EXPORT
int specializeAppProcessPost(
        JNIEnv *env, jclass clazz) {
    LOGI("Native inject app process");

    do_inject(env);

    return 0;
}
