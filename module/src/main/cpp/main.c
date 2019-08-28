#include <stdio.h>
#include <jni.h>
#include <dlfcn.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <android/log.h>
#include <sys/system_properties.h>

#include "riru.h"
#include "log.h"
#include "utils.h"
#include "inject.h"

#define EXPORT __attribute__((visibility("default")))

#define DEX_PATH          "/system/framework/boot-qq-lite.jar"
#define INJECT_CLASS_PATH "com/github/kr328/qq/blocker/Injector"
#define INJECT_METHOD_NAME "inject"
#define TARGET_PACKAGE "com.tencent.mobileqq"

static char package_name[128];

static void load_package_name(JNIEnv *env, jstring jAppDataDir, jstring jPackageName) {
    if (jPackageName) {
        const char *packageName = (*env)->GetStringUTFChars(env, jPackageName, NULL);
        sprintf(package_name, "%s", packageName);
        (*env)->ReleaseStringUTFChars(env, jPackageName, packageName);
    } else if (jAppDataDir) {
        const char *appDataDir = (*env)->GetStringUTFChars(env, jAppDataDir, NULL);
        int user = 0;
        if (sscanf(appDataDir, "/data/%*[^/]/%d/%s", &user, package_name) != 2) {
            if (sscanf(appDataDir, "/data/%*[^/]/%s", package_name) != 1)
                package_name[0] = '\0';
        }
        (*env)->ReleaseStringUTFChars(env, jAppDataDir, appDataDir);
    } else {
        package_name[0] = 0;
    }
}

static bool is_app_need_hook() {
    return strncmp(package_name, TARGET_PACKAGE, 128) == 0;
}

EXPORT
void nativeForkAndSpecializePre(
        JNIEnv *env, jclass clazz, jint *_uid, jint *gid, jintArray *gids, jint *runtime_flags,
        jobjectArray *rlimits, jint *_mount_external, jstring *se_info, jstring *se_name,
        jintArray *fdsToClose, jintArray *fdsToIgnore, jboolean *is_child_zygote,
        jstring *instructionSet, jstring *appDataDir, jstring *packageName,
        jobjectArray *packagesForUID, jstring *sandboxId) {
    load_package_name(env, *appDataDir, *packageName);
}

EXPORT
int nativeForkAndSpecializePost(JNIEnv *env, jclass clazz, jint res) {
    if ( res == 0 && is_app_need_hook() )
        load_and_invoke_dex(env, DEX_PATH, INJECT_CLASS_PATH, INJECT_METHOD_NAME, "");
    return 0;
}

EXPORT
void specializeAppProcessPre(JNIEnv *env, jclass clazz, jint *_uid, jint *gid, jintArray *gids, jint *runtimeFlags,
        jobjectArray *rlimits, jint *mountExternal, jstring *seInfo, jstring *niceName,
        jboolean *startChildZygote, jstring *instructionSet, jstring *appDataDir,
        jstring *packageName, jobjectArray *packagesForUID, jstring *sandboxId) {
    load_package_name(env, *appDataDir, *packageName);
}

EXPORT
int specializeAppProcessPost(JNIEnv *env, jclass clazz, jint res) {
    if ( is_app_need_hook() )
        load_and_invoke_dex(env, DEX_PATH, INJECT_CLASS_PATH, INJECT_METHOD_NAME, "");
    return 0;
}

EXPORT
void onModuleLoaded() {
    riru_set_module_name("qq_view_blocker");
}
